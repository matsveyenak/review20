package com.example.musicstats.artists;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicstats.R;
import com.example.musicstats.awards.AwardsActivity;
import com.example.musicstats.charts.ChartItem;
import com.example.musicstats.charts.ChartItemAdapter;
import com.example.musicstats.charts.ChartsActivity;
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

public class ArtistItemActivity extends Activity
{
    TextView artistName, artistBio;
    ImageView artistImage;
    RecyclerView rvTopTracks;
    ArrayList<ChartItem> items = new ArrayList<>();
    String name;
    String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artistitem_grid_layout);
        artistName = findViewById(R.id.artistItemName);
        artistBio = findViewById(R.id.artistBiography);
        artistImage = findViewById(R.id.artistItemImage);
        rvTopTracks = findViewById(R.id.rvTopTracks);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.nav_artists);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.nav_favourite:
                        Intent a = new Intent(ArtistItemActivity.this, FavouriteActivity.class);
                        startActivity(a);
                        return true;
                    case R.id.nav_charts:
                        Intent b = new Intent(ArtistItemActivity.this, ChartsActivity.class);
                        startActivity(b);
                        return true;
                    case R.id.nav_artists:
                        Intent c = new Intent(ArtistItemActivity.this, ArtistsActivity.class);
                        startActivity(c);
                        return true;
                    case R.id.nav_awards:
                        Intent d = new Intent(ArtistItemActivity.this, AwardsActivity.class);
                        startActivity(d);
                        return true;
                }
                return true;
            }
        });

        Bundle arguments = getIntent().getExtras();

        if (arguments != null)
            name = arguments.get("name").toString();

        SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();

        Cursor k = db.rawQuery("SELECT * FROM artists WHERE name = '" + name + "'", null);
        k.moveToFirst();
        int img = k.getInt(2);
        uri = k.getString(3);
        String bio = k.getString(4);
        k.close();

        artistImage.setImageResource(img);
        artistName.setText(name);
        artistBio.setText(bio);

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
                    if (id == 4)
                        break;
                }

                ChartItemAdapter chartItemAdapter = new ChartItemAdapter(ctx, items);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(ctx, 1, GridLayoutManager.VERTICAL, false);
                rvTopTracks.setLayoutManager(gridLayoutManager);
                rvTopTracks.setAdapter(chartItemAdapter);
            }

            @Override
            public void failure(SpotifyError spotifyError)
            {
                Log.d("Playlist failure", spotifyError.toString());
            }
        };

        MainActivity.spotify.getPlaylistTracks(MainActivity.token, uri, callback);
    }

}