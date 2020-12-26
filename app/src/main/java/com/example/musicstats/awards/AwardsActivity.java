package com.example.musicstats.awards;

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
import com.example.musicstats.artists.ArtistsActivity;
import com.example.musicstats.charts.ChartsActivity;
import com.example.musicstats.favourite.FavouriteActivity;
import com.example.musicstats.launch.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class AwardsActivity extends Activity
{
    RecyclerView awardsList;
    ArrayList<String> awards = new ArrayList<>();
    ArrayList<Integer> icons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awards);
        awardsList = findViewById(R.id.rvAwards);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.nav_awards);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.nav_favourite:
                        Intent a = new Intent(AwardsActivity.this, FavouriteActivity.class);
                        startActivity(a);
                        return true;
                    case R.id.nav_charts:
                        Intent b = new Intent(AwardsActivity.this, ChartsActivity.class);
                        startActivity(b);
                        return true;
                    case R.id.nav_artists:
                        Intent c = new Intent(AwardsActivity.this, ArtistsActivity.class);
                        startActivity(c);
                        return true;
                    case R.id.nav_awards:
                        Intent d = new Intent(AwardsActivity.this, AwardsActivity.class);
                        startActivity(d);
                        return true;
                }
                return false;
            }
        });

        SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
        Cursor c = db.query("awards", null, null, null, null, null, null);

        int amount = 0;
        if (c.moveToPosition(amount))
        {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int imgColIndex = c.getColumnIndex("img");
            int amountColIndex = c.getColumnIndex("amount");

            do {
                int id = c.getInt(idColIndex);
                amount = c.getInt(amountColIndex) + id;
                String title = c.getString(nameColIndex);
                int img = c.getInt(imgColIndex);
                awards.add(title);
                icons.add(img);
                Log.d("database", title + " " + img + " " + amount);
            }
            while (c.moveToPosition(amount));
        }
        c.close();

        AwardsAdapter awardsAdapter = new AwardsAdapter(this, awards, icons, 1);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        awardsList.setLayoutManager(gridLayoutManager);
        awardsList.setAdapter(awardsAdapter);
    }
}
