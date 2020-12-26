package com.example.musicstats.launch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicstats.R;
import com.example.musicstats.artists.ArtistsActivity;
import com.example.musicstats.awards.AwardsActivity;
import com.example.musicstats.charts.ChartsActivity;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class AddActivity extends Activity
{
    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "ede51dedc98f442a9cb822a1995565a0";
    private static final String REDIRECT_URI = "com.example.musicstats://callback";
    public static SpotifyAppRemote mSpotifyAppRemote;
    Button btnPlay, btnNext;
    TextView tvPlaylist, tvSong;
    ImageView ivCover;
    public static String token = "";
    public static SpotifyService spotify;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        btnPlay = findViewById(R.id.btnPlay);
        btnNext = findViewById(R.id.btnNext);
        tvPlaylist = findViewById(R.id.tvPlaylist);
        tvSong = findViewById(R.id.tvSong);
        ivCover = findViewById(R.id.ivCover);

        authenticateSpotify();

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
                        mSpotifyAppRemote.getPlayerApi().subscribeToPlayerState().
                                setEventCallback(playerState ->
                                {
                                    if (playerState.track == null)
                                    {
                                        btnPlay.setText("play");
                                    }
                                    else
                                    {
                                        tvSong.setText(playerState.track.artist.name +
                                                " - " + playerState.track.name);

                                        tvSong.setText(playerState.track.artist.name +
                                                " - " + playerState.track.name);
                                        mSpotifyAppRemote.getImagesApi()
                                                .getImage(playerState.track.imageUri).setResultCallback
                                                (
                                                        bitmap ->
                                                        {
                                                            ivCover.setImageBitmap(bitmap);
                                                        }
                                                );
                                        if (playerState.isPaused)
                                        {
                                            btnPlay.setText("resume");
                                        }
                                        else
                                        {
                                            btnPlay.setText("pause");
                                        }
                                    }

                                });

                        Log.d("AddActivity", "Connected! Yay!");

                        connected();

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

    private void connected()
    {
        tvPlaylist.setText("Listening to the Radar Release");
    }

    public void onClickPlay(View v)
    {
        mSpotifyAppRemote.getPlayerApi().getPlayerState().setResultCallback(
                playerState ->
                {
                    if (playerState.track == null)
                    {
                        mSpotifyAppRemote.getPlayerApi().play("spotify:track:4MzXwWMhyBbmu6hOcLVD49");
                        btnPlay.setText("resume");
                    }
                    else if (playerState.isPaused)
                    {
                        mSpotifyAppRemote.getPlayerApi().resume();
                        btnPlay.setText("resume");
                    }
                    else
                    {
                        mSpotifyAppRemote.getPlayerApi().pause();
                        btnPlay.setText("pause");
                    }
                }
        );
    }

    public void onClickNext(View v)
    {
        mSpotifyAppRemote.getPlayerApi().skipNext();
    }

    public void onClickChart(View v)
    {
        Intent intent = new Intent(this, ChartsActivity.class);
        startActivity(intent);
    }

    public void onClickAward(View v)
    {
        Intent intent = new Intent(this, AwardsActivity.class);
        startActivity(intent);
    }

    public void onClickArtists(View v)
    {
        Intent intent = new Intent(this, ArtistsActivity.class);
        startActivity(intent);
    }

    void authenticateSpotify()
    {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

}
