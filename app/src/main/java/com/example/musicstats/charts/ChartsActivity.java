package com.example.musicstats.charts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicstats.R;
import com.example.musicstats.artists.ArtistsActivity;
import com.example.musicstats.awards.AwardsActivity;
import com.example.musicstats.favourite.FavouriteActivity;
import com.example.musicstats.launch.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ChartsActivity extends Activity implements OnMapReadyCallback
{
    RecyclerView chartList;
    MapView mapView;
    Switch switchMap;
    Context ctx;
    ArrayList<Item> countries = new ArrayList<>();
    List<Double> latitudes = new ArrayList<>();
    List<Double> longitudes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        chartList = findViewById(R.id.rvCharts);
        mapView = findViewById(R.id.mapView);
        switchMap = findViewById(R.id.switchMap);
        ctx = ChartsActivity.this;

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
                        Intent a = new Intent(ChartsActivity.this, FavouriteActivity.class);
                        startActivity(a);
                        return true;
                    case R.id.nav_charts:
                        Intent b = new Intent(ChartsActivity.this, ChartsActivity.class);
                        startActivity(b);
                        return true;
                    case R.id.nav_artists:
                        Intent c = new Intent(ChartsActivity.this, ArtistsActivity.class);
                        startActivity(c);
                        return true;
                    case R.id.nav_awards:
                        Intent d = new Intent(ChartsActivity.this, AwardsActivity.class);
                        startActivity(d);
                        return true;
                }
                return false;
            }
        });


        switchMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    chartList.setVisibility(View.GONE);
                    switchMap.setText("Map");
                    mapView.onCreate(savedInstanceState);
                    mapView.getMapAsync(ChartsActivity.this::onMapReady);
                    mapView.setVisibility(View.VISIBLE);
                }
                else
                {
                    mapView.setVisibility(View.GONE);
                    chartList.setVisibility(View.VISIBLE);
                    switchMap.setText("List");
                }
            }
        });


        SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
        Cursor c = db.query("charts", null, null, null, null, null, null);
        if (c.moveToFirst())
        {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("title");
            int imgColIndex = c.getColumnIndex("img");
            int uriColIndex = c.getColumnIndex("uri");
            int latColIndex = c.getColumnIndex("latitude");
            int lonColIndex = c.getColumnIndex("longitude");
            do
            {
                int id = c.getInt(idColIndex);
                String title = c.getString(nameColIndex);
                int img = c.getInt(imgColIndex);
                String uri = c.getString(uriColIndex);
                double lat = c.getDouble(latColIndex);
                double lon = c.getDouble(lonColIndex);

                countries.add(new Item(id, title, img, uri));
                latitudes.add(lat);
                longitudes.add(lon);
            }
            while (c.moveToNext());
        }
        c.close();

        switchMap.setChecked(true);
        ChartsAdapter chartsAdapter = new ChartsAdapter(this, countries);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        chartList.setLayoutManager(gridLayoutManager);
        chartList.setAdapter(chartsAdapter);
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        for (int i = 0; i < latitudes.size(); i++)
        {
            LatLng coordinates = new LatLng(latitudes.get(i), longitudes.get(i));
            map.addMarker(new MarkerOptions().position(coordinates).title(countries.get(i).getTitle()));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 2));
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                for (int i = 0; i < countries.size(); i++)
                {
                    if (marker.getTitle().equals(countries.get(i).getTitle()))
                    {
                        Intent intent = new Intent(ctx, ChartItemActivity.class);
                        intent.putExtra("uri", countries.get(i).getUri());
                        ctx.startActivity(intent);
                    }
                }
                return true;
            }
        });
        mapView.onResume();
    }
}
