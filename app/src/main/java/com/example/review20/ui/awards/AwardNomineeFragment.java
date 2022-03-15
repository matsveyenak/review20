package com.example.review20.ui.awards;

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
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.review20.MainActivity;
import com.example.review20.R;
import com.example.review20.ui.charts.ChartTrack;
import com.example.review20.ui.charts.ChartTrackAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import retrofit.client.Response;

public class AwardNomineeFragment extends Fragment
{
    TextView tvWinner;
    String name, winner, others;
    RecyclerView rvNominee;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_award_nominees, container, false);

        if (MainActivity.playerViewModel.click > 0 )
        {
            MainActivity.playerCard.card.setVisibility(View.VISIBLE);
        }

        Bundle bundle = getArguments();
        if (bundle != null)
        {
            name = bundle.get("BUNDLE_NOMINATION").toString();
            winner = bundle.get("BUNDLE_WINNER").toString();
            others = bundle.get("BUNDLE_NOMINEE").toString();
        }

        MainActivity.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        MainActivity.tvToolbar.setText(name);

        tvWinner = root.findViewById(R.id.tv_winner);
        rvNominee = root.findViewById(R.id.rv_nominee);

        tvWinner.setText(winner);

        ArrayList<String> nominees = new ArrayList<>(Arrays.asList(others.split("\n")));

        rvNominee.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        AwardNomineeAdapter adapter = new AwardNomineeAdapter(getActivity(), nominees);
        rvNominee.setAdapter(adapter);

        return root;
    }
}
