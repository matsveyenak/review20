package com.example.musicstats.charts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicstats.R;
import com.example.musicstats.artists.ArtistsActivity;
import com.example.musicstats.awards.AwardsActivity;
import com.example.musicstats.favourite.FavouriteActivity;
import com.example.musicstats.launch.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import retrofit.client.Response;

public class ChartItemActivity extends Activity
{
    RecyclerView chartList;
    ArrayList<ChartItem> items = new ArrayList<>();
    String playlistUri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chartitem);
        chartList = findViewById(R.id.rvChartItem);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.nav_charts);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.nav_favourite:
                        Intent a = new Intent(ChartItemActivity.this, FavouriteActivity.class);
                        startActivity(a);
                        return true;
                    case R.id.nav_charts:
                        Intent b = new Intent(ChartItemActivity.this, ChartsActivity.class);
                        startActivity(b);
                        return true;
                    case R.id.nav_artists:
                        Intent c = new Intent(ChartItemActivity.this, ArtistsActivity.class);
                        startActivity(c);
                        return true;
                    case R.id.nav_awards:
                        Intent d = new Intent(ChartItemActivity.this, AwardsActivity.class);
                        startActivity(d);
                        return true;
                }
                return false;
            }
        });


        Bundle arguments = getIntent().getExtras();

        if(arguments != null)
        {
            playlistUri = arguments.get("uri").toString();
        }

        getInitialData(this);
    }

    public void getInitialData(Context ctx)
    {
        SpotifyCallback<Pager<PlaylistTrack>> callback = new SpotifyCallback<Pager<PlaylistTrack>>()
        {
            @Override
            public void success(Pager<PlaylistTrack> playlistTrackPager, Response response)
            {
                int id = 1;
                for (PlaylistTrack t : playlistTrackPager.items)
                {
                    ArrayList<String> artists = new ArrayList<>();
                    for (ArtistSimple art : t.track.artists)
                        artists.add(art.name);

                    Log.d("Playlist: ", t.track.name + " \n");
                    items.add(new ChartItem(id, artists, t.track.name, t.track.uri));
                    id++;
                }

                ChartItemAdapter chartItemAdapter = new ChartItemAdapter(ctx, items);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(ctx, 1, GridLayoutManager.VERTICAL, false);
                chartList.setLayoutManager(gridLayoutManager);
                chartList.setAdapter(chartItemAdapter);
            }

            @Override
            public void failure(SpotifyError spotifyError)
            {
                Log.d("Playlist failure", spotifyError.toString());
            }
        };

        MainActivity.spotify.getPlaylistTracks(MainActivity.token, playlistUri, callback);
    }

}