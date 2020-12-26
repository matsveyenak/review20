package com.example.musicstats.favourite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicstats.R;
import com.example.musicstats.artists.ArtistsActivity;
import com.example.musicstats.awards.AwardsActivity;
import com.example.musicstats.charts.ChartItem;
import com.example.musicstats.charts.ChartItemAdapter;
import com.example.musicstats.charts.ChartsActivity;
import com.example.musicstats.launch.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;

public class FavouriteActivity  extends Activity
{
    RecyclerView chartList;
    String playlistUri = "37i9dQZF1DWVHNixKGCLqw";
    Context ctx;
    ArrayList<ChartItem> items = new ArrayList<>();
    public static ChartItemAdapter chartItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        chartList = findViewById(R.id.rvFavourite);
        ctx = FavouriteActivity.this;

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.nav_favourite);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.nav_favourite:
                        Intent a = new Intent(FavouriteActivity.this, FavouriteActivity.class);
                        startActivity(a);
                        return true;
                    case R.id.nav_charts:
                        Intent b = new Intent(FavouriteActivity.this, ChartsActivity.class);
                        startActivity(b);
                        return true;
                    case R.id.nav_artists:
                        Intent c = new Intent(FavouriteActivity.this, ArtistsActivity.class);
                        startActivity(c);
                        return true;
                    case R.id.nav_awards:
                        Intent d = new Intent(FavouriteActivity.this, AwardsActivity.class);
                        startActivity(d);
                        return true;
                }
                return false;
            }
        });

        getInitialData(this);
    }
    public void getInitialData(Context ctx)
    {
        SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
        db.beginTransaction();
        try
        {
            Cursor c = db.rawQuery("SELECT * FROM favourite", null);
            int k = 1;
            if (c.moveToFirst())
            {
                int artistsColIndex = c.getColumnIndex("artists");
                int nameColIndex = c.getColumnIndex("name");
                int uriColIndex = c.getColumnIndex("uri");
                do
                {
                    int id = k;
                    String artists = c.getString(artistsColIndex);
                    String name = c.getString(nameColIndex);
                    String uri = c.getString(uriColIndex);

                    items.add(new ChartItem(id, new ArrayList<>(Arrays.asList(artists)), name, uri));
                    k++;
                }
                while (c.moveToNext());
            }
            c.close();
            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }

        chartItemAdapter = new ChartItemAdapter(ctx, items, 1);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ctx, 1, GridLayoutManager.VERTICAL, false);
        chartList.setLayoutManager(gridLayoutManager);
        chartList.setAdapter(chartItemAdapter);

    }
}
