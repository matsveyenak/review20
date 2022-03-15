package com.example.review20.ui.charts;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.review20.MainActivity;
import com.example.review20.R;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import retrofit.client.Response;

public class ChartTrackFragment  extends Fragment
{
    RecyclerView rvChartTracks;
    TextView tvCount, tvChart;
    ImageView ivChart;
    ArrayList<ChartTrack> items = new ArrayList<>();
    static String playlistUri, chartName, chartImage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_chart_tracks, container, false);

        if (MainActivity.playerViewModel.click > 0 )
        {
            MainActivity.playerCard.card.setVisibility(View.VISIBLE);
        }

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle)
    {
        View layoutContainer = view.findViewById(R.id.fragment_chart_tracks_layout);

        bundle = getArguments();
        if (bundle != null)
        {
            playlistUri = bundle.getString("BUNDLE_URI");
            chartName = bundle.getString("BUNDLE_TITLE");
            chartImage = bundle.getString("BUNDLE_IMAGE");

            ViewCompat.setTransitionName(layoutContainer, chartName);
        }

        MainActivity.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        MainActivity.tvToolbar.setText(chartName);

        rvChartTracks = view.findViewById(R.id.rv_chart_tracks);
        tvChart = view.findViewById(R.id.tv_chart_name);
        tvCount = view.findViewById(R.id.tv_chart_count);
        ivChart = view.findViewById(R.id.iv_chart_track_image);

        getInitialData();


    }
    public void getInitialData()
    {
        tvChart.append("\n" + chartName);

        Resources res = getActivity().getResources();
        int resId = res.getIdentifier(chartImage, "drawable", getActivity().getPackageName());
        Drawable image = res.getDrawable(resId);
        ivChart.setImageDrawable(image);

        SpotifyCallback<Pager<PlaylistTrack>> callback = new SpotifyCallback<Pager<PlaylistTrack>>()
        {
            @Override
            public void success(Pager<PlaylistTrack> playlistTrackPager, Response response)
            {
                int id = 0;
                for (PlaylistTrack t : playlistTrackPager.items)
                {
                    String image = t.track.album.images.get(0).url;

                    if (id >= 50)
                        break;
                    else
                        id++;

                    ArrayList<String> artists = new ArrayList<>();
                    for (ArtistSimple art : t.track.artists)
                    {
                        artists.add(art.name);
                    }

                    items.add(new ChartTrack(id, artists, t.track.name, t.track.uri, image));

                }

                tvCount.setText(id + " tracks");

                rvChartTracks.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
                ChartTrackAdapter adapter = new ChartTrackAdapter(getActivity(), items, 0);//,  Navigation.findNavController(getView()));
                rvChartTracks.setAdapter(adapter);
            }

            @Override
            public void failure(SpotifyError spotifyError)
            {
                Log.e("ChartTrackFragment", spotifyError.getMessage());
            }
        };

        MainActivity.spotifyService.getPlaylistTracks(MainActivity.token, playlistUri, callback);
    }
}
