package com.example.musicstats.launch;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.musicstats.R;
import com.example.musicstats.artists.ArtistsActivity;
import com.example.musicstats.awards.AwardsActivity;
import com.example.musicstats.charts.ChartsActivity;
import com.example.musicstats.database.DBHelper;
import com.example.musicstats.favourite.FavouriteActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.text.ParseException;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class MainActivity extends AppCompatActivity
{

    // Идентификатор уведомления
    private static final int NOTIFY_ID = 101;

    // Идентификатор канала
    private static String CHANNEL_ID = "Cat channel";

    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "ede51dedc98f442a9cb822a1995565a0";
    private static final String REDIRECT_URI = "com.example.musicstats://callback";

    public static SpotifyAppRemote mSpotifyAppRemote;
    public static String token = "";
    public static SpotifyService spotify;
    public static DBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        authenticateSpotify();

        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.nav_favourite:
                        Intent a = new Intent(MainActivity.this, FavouriteActivity.class);
                        startActivity(a);
                        return true;
                    case R.id.nav_charts:
                        Intent b = new Intent(MainActivity.this, ChartsActivity.class);
                        startActivity(b);
                        return true;

                    case R.id.nav_artists:
                        Intent c = new Intent(MainActivity.this, ArtistsActivity.class);
                        startActivity(c);
                        return true;

                    case R.id.nav_awards:
                        Intent d = new Intent(MainActivity.this, AwardsActivity.class);
                        startActivity(d);
                        return true;

                }
                return false;
            }
        });

        dbHelper = new DBHelper(this);

        Intent deleteIntent = new Intent(this.getApplicationContext(), MainActivity.class);
        deleteIntent.setAction("ru.startandroid.notifications.action_delete");
        PendingIntent deletePendingIntent = PendingIntent.getService(this, 0, deleteIntent, 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "uid1")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Title")
                        .setContentText("Notification text")
                        .addAction(android.R.drawable.ic_delete, "Delete", deletePendingIntent)
                        .addAction(android.R.drawable.ic_delete, "Delete", deletePendingIntent);

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        notificationManager.notify(0, notification);

    }

    public void onClick(View v) throws ParseException
    {
        switch (v.getId())
        {
            case R.id.btnMainCharts:
                Intent a = new Intent(MainActivity.this, ChartsActivity.class);
                startActivity(a);
                break;
            case R.id.btnMainArtists:
                Intent b = new Intent(MainActivity.this, ArtistsActivity.class);
                startActivity(b);
                break;
            case R.id.btnMainAwards:
                Intent c = new Intent(MainActivity.this, AwardsActivity.class);
                startActivity(c);
                break;
        }
    }

    void authenticateSpotify()
    {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener()
                {
                    public void onConnected(SpotifyAppRemote spotifyAppRemote)
                    {
                        mSpotifyAppRemote = spotifyAppRemote;
                    }

                    public void onFailure(Throwable throwable)
                    {
                        Log.e("AddActivity", throwable.getMessage(), throwable);
                    }
                });

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SpotifyApi.SPOTIFY_WEB_API_ENDPOINT)
                .setRequestInterceptor(new RequestInterceptor()
                {
                    @Override
                    public void intercept(RequestFacade request)
                    {
                        request.addHeader("Authorization", "Bearer " + token);
                    }
                })
                .build();

        spotify = restAdapter.create(SpotifyService.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE)
        {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType())
            {
                case TOKEN:
                    token = response.getAccessToken();
                    Log.d("STARTING", "GOT AUTH TOKEN " + token);

                    db = dbHelper.getWritableDatabase();

                    ContentValues cv = new ContentValues();

                    db.beginTransaction();
                    try
                    {
                        db.execSQL("delete from user");
                        cv.clear();
                        cv.put("token", token);
                        db.insert("user", null, cv);

                        //dbHelper.fillCharts(db);
                        //dbHelper.fillAwards(db);
                        //dbHelper.fillArtists(db);
                        db.setTransactionSuccessful();

                    }
                    finally
                    {
                        db.endTransaction();
                    }

                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }



}