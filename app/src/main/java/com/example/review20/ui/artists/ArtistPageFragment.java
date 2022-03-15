package com.example.review20.ui.artists;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.review20.MainActivity;
import com.example.review20.R;
import com.example.review20.ui.charts.ChartTrack;
import com.example.review20.ui.charts.ChartTrackAdapter;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import retrofit.client.Response;

public class ArtistPageFragment extends Fragment
{
    TextView tvName, tvBiography;
    ImageView ivImage;
    RecyclerView rvTopTracks;
    ArrayList<ChartTrack> items = new ArrayList<>();
    String name;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_artist_page, container, false);

        if (MainActivity.playerViewModel.click > 0 )
        {
            MainActivity.playerCard.card.setVisibility(View.VISIBLE);
        }

        View layoutContainer = root.findViewById(R.id.artist_page_fragment);

        Bundle bundle = getArguments();
        if (bundle != null)
        {
            name = bundle.get("BUNDLE_NAME").toString();
            ViewCompat.setTransitionName(layoutContainer, name);
        }

        MainActivity.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        MainActivity.tvToolbar.setText(name);

        tvName = root.findViewById(R.id.tv_artist_page_name);
        tvBiography = root.findViewById(R.id.tv_biography);
        ivImage = root.findViewById(R.id.iv_artist_page_image);
        rvTopTracks = root.findViewById(R.id.rv_top_tracks);

        return root;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Context ctx = getActivity();

        SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();

        Cursor k = db.rawQuery("SELECT * FROM artists WHERE name = '" + name + "'", null);
        k.moveToFirst();
        String img = k.getString(2);
        String uri = k.getString(3);
        String bio = k.getString(4);
        k.close();

        tvName.setText(name);
        tvBiography.setText(bio);

        Resources res = ctx.getResources();
        int resID = res.getIdentifier(img, "drawable", ctx.getPackageName());
        Drawable image = res.getDrawable(resID);
        ivImage.setImageDrawable(image);

        SpotifyCallback<Pager<PlaylistTrack>> callback = new SpotifyCallback<Pager<PlaylistTrack>>()
        {
            @Override
            public void success(Pager<PlaylistTrack> playlistTrackPager, Response response)
            {
                int id = 1;
                for (PlaylistTrack t : playlistTrackPager.items)
                {
                    String image = t.track.album.images.get(0).url;

                    ArrayList<String> artists = new ArrayList<>();
                    for (ArtistSimple art : t.track.artists)
                        artists.add(art.name);

                    items.add(new ChartTrack(id, artists, t.track.name, t.track.uri, image));
                    id++;

                    if (id == 4)
                        break;

                    ChartTrackAdapter chartItemAdapter = new ChartTrackAdapter(ctx, items, 2);//, Navigation.findNavController(getView()));
                    rvTopTracks.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
                    rvTopTracks.setAdapter(chartItemAdapter);
                }
            }

            @Override
            public void failure(SpotifyError spotifyError)
            {
                Log.e("ArtistPageFragment", spotifyError.getMessage());
            }
        };

        MainActivity.spotifyService.getPlaylistTracks(MainActivity.token, uri, callback);
    }
}
