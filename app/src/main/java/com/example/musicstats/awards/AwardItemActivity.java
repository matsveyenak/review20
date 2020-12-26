package com.example.musicstats.awards;

import android.app.Activity;
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
import com.example.musicstats.artists.ArtistsActivity;
import com.example.musicstats.charts.ChartsActivity;
import com.example.musicstats.favourite.FavouriteActivity;
import com.example.musicstats.launch.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class AwardItemActivity extends Activity
{
    String awardName;
    ImageView icon;
    TextView name;
    RecyclerView rvAwardList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.awarditem_grid_layout);
        icon = findViewById(R.id.awardItemIcon);
        name = findViewById(R.id.awardItemName);
        rvAwardList = findViewById(R.id.rvAwardList);


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
                        Intent a = new Intent(AwardItemActivity.this, FavouriteActivity.class);
                        startActivity(a);
                        return true;
                    case R.id.nav_charts:
                        Intent b = new Intent(AwardItemActivity.this, ChartsActivity.class);
                        startActivity(b);
                        return true;
                    case R.id.nav_artists:
                        Intent c = new Intent(AwardItemActivity.this, ArtistsActivity.class);
                        startActivity(c);
                        return true;
                    case R.id.nav_awards:
                        Intent d = new Intent(AwardItemActivity.this, AwardsActivity.class);
                        startActivity(d);
                        return true;
                }
                return false;
            }
        });


        Bundle arguments = getIntent().getExtras();

        if(arguments != null)
            awardName = arguments.get("name").toString();

        name.setText(awardName);

        List<AwardItem> items = new ArrayList<>();

        SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();

        Cursor k = db.rawQuery("SELECT * FROM awards WHERE name = '" + awardName + "'", null);
        k.moveToFirst();
        int id = k.getInt(0);
        int img = k.getInt(2);
        int amount = k.getInt(3);
        k.close();

        icon.setImageResource(img);

        int t = 0;
        Cursor c = db.query("awards", null, null, null, null, null, null);
        if (c.moveToPosition(id-1))
        {
            //int imgColIndex = c.getColumnIndex("img");
            do
            {
                String nomination = c.getString(4);
                String winner = c.getString(5) ;
                String nominee =  c.getString(6);
                Log.d("database", nomination + " " + nominee);
                items.add(new AwardItem(nomination, winner, nominee));
                t++;
            }
            while (c.moveToNext() && t != amount);
        }
        c.close();

        AwardsListAdapter awardsListAdapter = new AwardsListAdapter(items);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        rvAwardList.setLayoutManager(gridLayoutManager);
        rvAwardList.setAdapter(awardsListAdapter);

    }

}
