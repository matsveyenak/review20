package com.example.review20.ui.artists;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.review20.MainActivity;
import com.example.review20.R;
import com.example.review20.ui.charts.ChartItem;
import com.example.review20.ui.charts.ChartListAdapter;
import com.example.review20.ui.home.HomeItem;
import com.example.review20.ui.home.HomeListAdapter;
import com.google.android.material.transition.Hold;
import com.google.android.material.transition.MaterialContainerTransform;

import java.util.ArrayList;

public class ArtistsFragment  extends Fragment
{
    private RecyclerView rvArtistsTop, rvArtists;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_artists, container, false);

        if (MainActivity.playerViewModel.click > 0 )
        {
            MainActivity.playerCard.card.setVisibility(View.VISIBLE);
        }

        MainActivity.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        MainActivity.tvToolbar.setText("artists");

        rvArtistsTop = root.findViewById(R.id.rv_artists_top);
        rvArtists = root.findViewById(R.id.rv_artists);

        return root;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        ArrayList<ArtistItem> artistsTop = new ArrayList<>();
        ArrayList<ArtistItem> artists = new ArrayList<>();

        SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
        Cursor c = db.query("artists", null, null, null, null, null, null);

        int t = 0;
        if (c.moveToFirst())
        {
            int nameColIndex = c.getColumnIndex("name");
            int imgColIndex = c.getColumnIndex("img");

            do
            {
                String title = c.getString(nameColIndex);
                String img = c.getString(imgColIndex);

                artists.add(new ArtistItem(title, img));
            }
            while (c.moveToNext());
        }
        c.close();

        for (int i = 0; i < 3; i++)
        {
            artistsTop.add(artists.get(0));
            artists.remove(0);
        }

        rvArtistsTop.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        ArtistTopListAdapter listAdapter = new ArtistTopListAdapter(getActivity(), artistsTop, this);//, Navigation.findNavController(getView()));
        rvArtistsTop.setAdapter(listAdapter);

        rvArtists.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        ArtistListAdapter adapter = new ArtistListAdapter(getActivity(), artists, this);//, Navigation.findNavController(getView()));
        rvArtists.setAdapter(adapter);
    }

    public void createTransition(Fragment fragment, View view)
    {
        MaterialContainerTransform transform = new MaterialContainerTransform();
        transform.setFadeMode(MaterialContainerTransform.FADE_MODE_CROSS);
        transform.setContainerColor(getResources().getColor(R.color.white));

        Hold hold = new Hold();
        hold.addTarget(R.id.artists_fragment);
        hold.setDuration(transform.getDuration());
        setExitTransition(hold);

        fragment.setSharedElementEnterTransition(transform);

        getParentFragmentManager()
                .beginTransaction()
                .addSharedElement(view, ViewCompat.getTransitionName(view))
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

}
