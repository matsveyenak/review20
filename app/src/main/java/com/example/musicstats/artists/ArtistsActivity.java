package com.example.musicstats.artists;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicstats.R;
import com.example.musicstats.awards.AwardsActivity;
import com.example.musicstats.charts.ChartsActivity;
import com.example.musicstats.favourite.FavouriteActivity;
import com.example.musicstats.launch.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ArtistsActivity extends Activity
{
    RecyclerView artistList, artistListNext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists);
        artistList = findViewById(R.id.rvArtists);
        artistListNext = findViewById(R.id.rvArtistsNext);

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
                        Intent a = new Intent(ArtistsActivity.this, FavouriteActivity.class);
                        startActivity(a);
                        return true;
                    case R.id.nav_charts:
                        Intent b = new Intent(ArtistsActivity.this, ChartsActivity.class);
                        startActivity(b);
                        return true;
                    case R.id.nav_artists:
                        Intent c = new Intent(ArtistsActivity.this, ArtistsActivity.class);
                        startActivity(c);
                        return true;
                    case R.id.nav_awards:
                        Intent d = new Intent(ArtistsActivity.this, AwardsActivity.class);
                        startActivity(d);
                        return true;
                }
                return false;
            }
        });

        ArrayList<String> artists = new ArrayList<>();
        ArrayList<Integer> images = new ArrayList<>();
        ArrayList<String> artists3 = new ArrayList<>();
        ArrayList<Integer> images3 = new ArrayList<>();

        SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
        Cursor c = db.query("artists", null, null, null, null, null, null);

        int t = 0;
        if (c.moveToFirst())
        {
            int nameColIndex = c.getColumnIndex("name");
            int imgColIndex = c.getColumnIndex("img");

            do {
                String title = c.getString(nameColIndex);
                int img = c.getInt(imgColIndex);
                if (t < 3)
                {
                    artists3.add(title);
                    images3.add(img);
                }
                else
                {
                    artists.add(title);
                    images.add(img);
                }
                Log.d("database", title + " " + img + " " + t);
                t++;
            }
            while (c.moveToNext());
        }
        c.close();

        ArtistsAdapter artistsAdapter3 = new ArtistsAdapter(this, artists3, images3, 0);
        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        artistList.setLayoutManager(gridLayoutManager3);
        artistList.setAdapter(artistsAdapter3);

        ArtistsAdapter artistsAdapter = new ArtistsAdapter(this, artists, images, 1);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        artistListNext.setLayoutManager(gridLayoutManager);
        artistListNext.setAdapter(artistsAdapter);

    }
}
