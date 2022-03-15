package com.example.review20;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.review20.database.DBHelper;
import com.example.review20.ui.artists.ArtistsFragment;
import com.example.review20.ui.awards.AwardsFragment;
import com.example.review20.ui.charts.ChartsFragment;
import com.example.review20.ui.favourite.FavouriteFragment;
import com.example.review20.ui.home.HomeFragment;
import com.example.review20.ui.player.PlayerCard;
import com.example.review20.ui.player.PlayerFragment;
import com.example.review20.ui.player.PlayerViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.transition.MaterialArcMotion;
import com.google.android.material.transition.MaterialSharedAxis;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
{
    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "31c1ba0e566347828c3b7ae1ed06b84f";
    private static final String REDIRECT_URI = "com.example.review20://callback";

    public static PlayerViewModel playerViewModel;
    public static SpotifyAppRemote spotifyAppRemote;
    public static String token = "";
    public static SpotifyService spotifyService;
    public static DBHelper dbHelper;
    public static PlayerCard playerCard;
    public static Toolbar toolbar;
    public static TextView tvToolbar;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //this.deleteDatabase("reviewDB");
        dbHelper = new DBHelper(this);
        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        authenticateSpotify();

        setContentView(R.layout.activity_main);

        //navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        //NavigationUI.setupWithNavController(navView, navController);

        toolbar = findViewById(R.id.tb_main);
        tvToolbar = findViewById(R.id.tv_tb_main);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        navView.setOnNavigationItemSelectedListener
                (item ->
                {
                    switch (item.getItemId())
                    {
                        case R.id.navigation_home:
                            replaceFragment(new HomeFragment());
                            return true;
                        case R.id.navigation_charts:
                            replaceFragment(new ChartsFragment());
                            return true;
                        case R.id.navigation_artists:
                            replaceFragment(new ArtistsFragment());
                            return true;
                        case R.id.navigation_awards:
                            replaceFragment(new AwardsFragment());
                            return true;
                        case R.id.navigation_favourite:
                            replaceFragment(new FavouriteFragment());
                            return true;
                    }
                    return false;
                });

        playerCard = new PlayerCard(findViewById(android.R.id.content).getRootView(), this);//, navController);

        replaceFragment(new HomeFragment());
    }

    public void replaceFragment(Fragment fragment)
    {
        fragment.setEnterTransition(createTransition());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    private MaterialSharedAxis createTransition()
    {
        MaterialSharedAxis fadeThrough = new MaterialSharedAxis(MaterialSharedAxis.Z, true);
        fadeThrough.setDuration(700L);
        fadeThrough.setPathMotion(new MaterialArcMotion());

        fadeThrough.addTarget(R.id.home_fragment);
        fadeThrough.addTarget(R.id.charts_fragment);
        fadeThrough.addTarget(R.id.artists_fragment);
        fadeThrough.addTarget(R.id.awards_fragment);
        fadeThrough.addTarget(R.id.favourite_fragment);

        return fadeThrough;
    }

    @Override
    public void onBackPressed()
    {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count < 2)
        {
            super.onBackPressed();
            //additional code
        }
        else
        {
            getSupportFragmentManager().popBackStack();
        }

    }

    void authenticateSpotify()
    {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"streaming playlist-modify-public playlist-modify-private app-remote-control"});
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
                    public void onConnected(SpotifyAppRemote spotifyRemote)
                    {
                        spotifyAppRemote = spotifyRemote;

                        spotifyAppRemote.getPlayerApi()
                                .subscribeToPlayerState()
                                .setEventCallback(playerState ->
                                {
                                    if (!playerState.isPaused)
                                    {
                                        Track track = playerState.track;

                                        if (track != null)
                                        {
                                            playerViewModel.setUri(track.uri);
                                            playerViewModel.setSong(track.name);

                                            StringBuilder sb = new StringBuilder();
                                            int size = track.artists.size();

                                            for (int i = 0; i < size; i++)
                                            {
                                                if (i != size - 1)
                                                    sb.append(track.artists.get(i).name + ", ");
                                                else
                                                    sb.append(track.artists.get(i).name);
                                            }
                                            playerViewModel.setArtists(sb.toString());

                                            String imageUri = track.imageUri.raw.replace("spotify:image:", "");
                                            playerViewModel.setCover("https://i.scdn.co/image/" + imageUri);

                                            Resources res = MainActivity.this.getResources();
                                            int fullFavId = res.getIdentifier("ic_baseline_favorite_24",
                                                    "drawable", MainActivity.this.getPackageName());
                                            int emptyFavId = res.getIdentifier("ic_baseline_favorite_border_24",
                                                    "drawable", MainActivity.this.getPackageName());
                                            Drawable fullFavImage = res.getDrawable(fullFavId);
                                            Drawable emptyFavImage = res.getDrawable(emptyFavId);

                                            SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
                                            Cursor c = db.rawQuery("SELECT * FROM favourite WHERE uri = '" +
                                                    playerCard.uri + "'", null);

                                            if (c.getCount() > 0)
                                            {
                                                playerViewModel.setFav(fullFavImage);
                                            }
                                            else
                                            {
                                                playerViewModel.setFav(emptyFavImage);
                                            }
                                            c.close();

                                            playerViewModel.setPlay("pause");

                                            int pauseId = res.getIdentifier("ic_pause_button", "drawable",
                                                    MainActivity.this.getPackageName());
                                            Drawable pauseImage = res.getDrawable(pauseId);
                                            playerCard.btnPlay.setBackground(pauseImage);

                                            PlayerFragment.savePlayerState(MainActivity.playerViewModel.getRepeat().getValue(),
                                                    MainActivity.playerViewModel.getShuffle().getValue());
                                        }
                                    }
                                });
                    }

                    public void onFailure(Throwable throwable)
                    {
                        Log.e("MainActivity", throwable.getMessage(), throwable);
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

        spotifyService = restAdapter.create(SpotifyService.class);
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

                    db = dbHelper.getWritableDatabase();
                    spotifyService.getMe(new Callback<UserPrivate>()
                    {
                        @Override
                        public void success(UserPrivate userPrivate, Response response)
                        {
                            ContentValues cv = new ContentValues();
                            cv.clear();

                            Cursor k = db.rawQuery("SELECT * FROM user WHERE u_id = '" + userPrivate.id + "'", null);
                            if (k.getCount() > 0)
                            {
                                db.beginTransaction();
                                try
                                {
                                    cv.put("token", token);
                                    db.update("user", cv, "u_id = ?", new String[]{userPrivate.id});

                                    db.setTransactionSuccessful();
                                }
                                finally
                                {
                                    db.endTransaction();
                                }
                            }
                            else
                            {
                                cv.put("u_id", userPrivate.id);
                                cv.putNull("playlist");
                                cv.put("token", token);
                                db.insert("user", null, cv);
                            }
                            k.close();
                        }

                        @Override
                        public void failure(RetrofitError error)
                        {
                            Log.e("MainActivity", error.getMessage());
                        }
                    });

                    break;

                case ERROR:
                    Log.e("MainActivity", "Auth error");
                    break;

                default:
                    Log.e("MainActivity", "Problem occured with Spotify auth");
            }
        }
    }

}