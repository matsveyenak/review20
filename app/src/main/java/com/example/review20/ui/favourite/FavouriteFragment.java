package com.example.review20.ui.favourite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.review20.MainActivity;
import com.example.review20.R;
import com.example.review20.ui.charts.ChartItem;
import com.example.review20.ui.charts.ChartTrack;
import com.example.review20.ui.charts.ChartTrackAdapter;
import com.google.android.material.transition.MaterialFadeThrough;
import com.google.android.material.transition.MaterialSharedAxis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.SnapshotId;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FavouriteFragment extends Fragment
{
    RecyclerView rvFavourite;
    TextView tvCount;
    ArrayList<ChartTrack> items;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_favourite, container, false);

        return root;
    }

    void onClick (View v)
    {

        final Map<String, Object> options = new HashMap<String, Object>();
        final List<String> trackUris = new ArrayList<>();
        for (ChartTrack track : items)
        {
            trackUris.add(track.getUri());
        }
        options.put("uris", trackUris);

        final Map<String, Object> queryParameters = new HashMap<String, Object>();

        SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
        Cursor k = db.rawQuery("SELECT u_id, playlist FROM user", null);
        k.moveToNext();
        final String[] value = {k.getString(0), k.getString(1)};

        if (value[1] == null)
        {
            final Map<String, Object> opt = new HashMap<String, Object>();
            options.put("name", "favourite of review20");
            options.put("public", String.valueOf(false));

            MainActivity.spotifyService.createPlaylist(value[0], opt, new Callback<Playlist>()
            {
                @Override
                public void success(Playlist playlist, Response response)
                {
                    value[1] = playlist.id;
                    ContentValues cv = new ContentValues();
                    cv.put("playlist", playlist.id);
                    db.update("user", cv, "token = ?", new String[]{MainActivity.token});

                    MainActivity.spotifyService.addTracksToPlaylist(value[0], value[1], queryParameters, options, new Callback<Pager<PlaylistTrack>>()
                    {
                        @Override
                        public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {}

                        @Override
                        public void failure(RetrofitError error) {}
                    });
                }

                @Override
                public void failure(RetrofitError error)
                {
                    Log.e("FavouriteFragment", error.getMessage());
                }
            });

        }

        MainActivity.spotifyService.addTracksToPlaylist(value[0], value[1], queryParameters, options, new Callback<Pager<PlaylistTrack>>()
        {
            @Override
            public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {}

            @Override
            public void failure(RetrofitError error) {}
        });
    }


    @Override
    public void onStart()
    {
        super.onStart();

        if (MainActivity.playerViewModel.click > 0 )
        {
            MainActivity.playerCard.card.setVisibility(View.VISIBLE);
        }
        Button buttonSave = getView().findViewById(R.id.btn_favourite_save);
        buttonSave.setOnClickListener(this::onClick);

        MainActivity.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        MainActivity.tvToolbar.setText("favourite");

        rvFavourite = getView().findViewById(R.id.rv_favourite);
        tvCount = getView().findViewById(R.id.tv_favourite_count);


        getInitialData();
    }

    public void getInitialData()
    {
        items = new ArrayList<>();

        SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
        db.beginTransaction();
        try
        {
            Cursor c = db.rawQuery("SELECT * FROM favourite", null);
            int k = 0;

            if (c.moveToFirst())
            {
                int artistsColIndex = c.getColumnIndex("artists");
                int nameColIndex = c.getColumnIndex("name");
                int imgColIndex = c.getColumnIndex("img");
                int uriColIndex = c.getColumnIndex("uri");
                do
                {
                    k++;

                    int id = k;
                    String artists = c.getString(artistsColIndex);
                    String name = c.getString(nameColIndex);
                    String img = c.getString(imgColIndex);
                    String uri = c.getString(uriColIndex);

                    items.add(new ChartTrack(id, new ArrayList<>(Arrays.asList(artists)), name, uri, img));
                }
                while (c.moveToNext());
            }

            tvCount.setText(k + " tracks");

            c.close();
            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }

        rvFavourite.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        ChartTrackAdapter adapter = new ChartTrackAdapter(getContext(), items, 1); //Navigation.findNavController(getView()));
        rvFavourite.setAdapter(adapter);

    }
}
