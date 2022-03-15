package com.example.review20.ui.home;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.review20.MainActivity;
import com.example.review20.R;
import com.example.review20.ui.awards.AwardItem;
import com.example.review20.ui.awards.AwardListAdapter;
import com.example.review20.ui.player.PlayerCard;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.transition.Hold;
import com.google.android.material.transition.MaterialContainerTransform;
import com.google.android.material.transition.MaterialFadeThrough;
import com.google.android.material.transition.MaterialSharedAxis;

import java.util.ArrayList;

public class HomeFragment extends Fragment
{
    RecyclerView rvCategory, rvUserTop, rvPlaylist;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        if (MainActivity.playerViewModel.click > 0 )
        {
            MainActivity.playerCard.card.setVisibility(View.VISIBLE);
        }

        MainActivity.toolbar.setNavigationIcon(null);
        MainActivity.tvToolbar.setText("review'20");

        rvCategory = root.findViewById(R.id.rv_category);
        rvUserTop  = root.findViewById(R.id.rv_user_top);
        rvPlaylist = root.findViewById(R.id.rv_playlist);

        return root;
    }


    @Override
    public void onStart()
    {
        super.onStart();

        ArrayList<HomeItem> itemsCategory = new ArrayList<>();
        itemsCategory.add(new HomeItem("charts", "btn_charts"));
        itemsCategory.add(new HomeItem("artists", "btn_artists"));
        itemsCategory.add(new HomeItem("awards", "btn_awards"));

        rvCategory.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        HomeListAdapter listAdapter = new HomeListAdapter(getActivity(), itemsCategory, 0, this);//, Navigation.findNavController(getView()), 0);
        rvCategory.setAdapter(listAdapter);


        ArrayList<HomeItem> itemsUserTop = new ArrayList<>();
        itemsUserTop.add(new HomeItem("", "user_top_2020"));

        rvUserTop.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        listAdapter = new HomeListAdapter(getActivity(), itemsUserTop, 1, this);//, Navigation.findNavController(getView()), 1);
        rvUserTop.setAdapter(listAdapter);

        ArrayList<HomeItem> itemsPlaylist = new ArrayList<>();
        itemsPlaylist.add(new HomeItem("Pop", "pop", "37i9dQZF1DX5QPo288x03n"));
        itemsPlaylist.add(new HomeItem("K-Pop", "kpop", "37i9dQZF1DWVHNixKGCLqw"));
        itemsPlaylist.add(new HomeItem("Rock","rock", "37i9dQZF1DX2UyG74cWOyT"));
        itemsPlaylist.add(new HomeItem("Non-Binary+ Artists", "nonbinary", "37i9dQZF1DX0yAkDWJqoXB"));
        itemsPlaylist.add(new HomeItem("R&B", "rnb", "37i9dQZF1DWVYxTSK0RENg"));
        itemsPlaylist.add(new HomeItem("EDM","edm", "37i9dQZF1DWX4UM2EREq2R"));
        itemsPlaylist.add(new HomeItem("Country","country", "37i9dQZF1DWYvU2z6HruAo"));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPlaylist.setLayoutManager(gridLayoutManager);
        HomePlaylistAdapter playlistAdapter = new HomePlaylistAdapter(getActivity(), itemsPlaylist, this);//, Navigation.findNavController(getView()));
        rvPlaylist.setAdapter(playlistAdapter);
    }

    public void createTransition(Fragment fragment)
    {
        MaterialSharedAxis exitTransition = new MaterialSharedAxis(MaterialSharedAxis.X, true);
        exitTransition.addTarget(R.id.home_fragment);
        exitTransition.addTarget(R.id.charts_fragment);
        exitTransition.addTarget(R.id.artists_fragment);
        exitTransition.addTarget(R.id.awards_fragment);

        fragment.setEnterTransition(exitTransition);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void createPlaylistTransition(Fragment fragment, View view)
    {
        MaterialContainerTransform transform = new MaterialContainerTransform();
        transform.setFadeMode(MaterialContainerTransform.FADE_MODE_CROSS);
        transform.setContainerColor(getResources().getColor(R.color.white));

        Hold hold = new Hold();
        hold.addTarget(R.id.home_fragment);
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
