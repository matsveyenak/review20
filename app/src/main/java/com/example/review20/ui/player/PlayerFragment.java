package com.example.review20.ui.player;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.review20.MainActivity;
import com.example.review20.R;
import com.example.review20.ui.charts.ChartItem;
import com.example.review20.ui.charts.ChartTrack;
import com.example.review20.ui.charts.ChartTrackAdapter;
import com.google.android.material.card.MaterialCardView;
import com.spotify.protocol.types.Repeat;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.Image;

public class PlayerFragment extends Fragment
{
    public static TextView tvSong, tvArtists;
    public static ImageView ivCover;
    public static ImageButton btnFav, btnClose, btnShuffle,
            btnPrev, btnPlay, btnNext, btnRepeat;

    private Resources res;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_player, container, false);

        res = getResources();
        MainActivity.playerCard.card.setVisibility(View.GONE);

        btnFav = root.findViewById(R.id.btn_player_fragment_fav);
        btnShuffle = root.findViewById(R.id.btn_player_shuffle);
        btnPlay = root.findViewById(R.id.btn_player_fragment_play);
        btnPrev = root.findViewById(R.id.btn_player_prev);
        btnNext = root.findViewById(R.id.btn_player_next);
        btnRepeat = root.findViewById(R.id.btn_player_repeat);

        initPlayerState(MainActivity.playerViewModel.getRepeat().getValue(),
                MainActivity.playerViewModel.getShuffle().getValue());

        ivCover = root.findViewById(R.id.iv_fragment_player_image);
        tvArtists = root.findViewById(R.id.tv_fragment_player_artist);
        tvSong = root.findViewById(R.id.tv_fragment_player_title);

        MainActivity.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        MainActivity.playerViewModel.getSong().observe(getViewLifecycleOwner(), new Observer<String>()
        {
            @Override
            public void onChanged(String s)
            {
                tvSong.setText(s);
            }
        });
        MainActivity.playerViewModel.getArtists().observe(getViewLifecycleOwner(), new Observer<String>()
        {
            @Override
            public void onChanged(String s)
            {
                tvArtists.setText(s);
            }
        });
        MainActivity.playerViewModel.getCover().observe(getViewLifecycleOwner(), new Observer<String>()
        {
            @Override
            public void onChanged(String s)
            {
                Picasso.get().load(s).into(ivCover);
            }
        });
        MainActivity.playerViewModel.getFav().observe(getViewLifecycleOwner(), new Observer<Drawable>()
        {
            @Override
            public void onChanged(Drawable d)
            {
                btnFav.setBackground(d);
            }
        });
        MainActivity.playerViewModel.getPlay().observe(getViewLifecycleOwner(), new Observer<String>()
        {
            @Override
            public void onChanged(String s)
            {
                int pauseId = res.getIdentifier("ic_baseline_pause_circle_24", "drawable", getActivity().getPackageName());
                int playId = res.getIdentifier("ic_baseline_play_circle_24", "drawable", getActivity().getPackageName());
                Drawable pauseImage = res.getDrawable(pauseId);
                Drawable playImage = res.getDrawable(playId);

                if (s.equals("pause"))
                {
                    btnPlay.setBackground(pauseImage);
                }
                else if (s.equals("play"))
                {
                    btnPlay.setBackground(playImage);
                }
            }
        });

        btnFav.setOnClickListener(this::onFavClick);
        btnShuffle.setOnClickListener(this::onShuffleClick);
        btnPlay.setOnClickListener(this::onPlayClick);
        btnPrev.setOnClickListener(this::onPrevClick);
        btnNext.setOnClickListener(this::onNextClick);
        btnRepeat.setOnClickListener(this::onRepeatClick);

        return root;
    }

    static public void savePlayerState(String repeat, String shuffle)
    {
        switch (repeat)
        {
            case "one":
                MainActivity.spotifyAppRemote.getPlayerApi().setRepeat(Repeat.ONE);
                break;
            case "all":
                MainActivity.spotifyAppRemote.getPlayerApi().setRepeat(Repeat.ALL);
                break;
            case "off":
                MainActivity.spotifyAppRemote.getPlayerApi().setRepeat(Repeat.OFF);
                break;
        }

        switch (shuffle)
        {
            case "false":
                MainActivity.spotifyAppRemote.getPlayerApi().setShuffle(false);
                break;
            case "true":
                MainActivity.spotifyAppRemote.getPlayerApi().setShuffle(true);
                break;
        }
    }

    void initPlayerState(String repeat, String shuffle)
    {
        int repeatId = res.getIdentifier("replay", "drawable", getActivity().getPackageName());
        int repeatOneId = res.getIdentifier("replay_one", "drawable", getActivity().getPackageName());
        int repeatAllId = res.getIdentifier("replay_all", "drawable", getActivity().getPackageName());
        Drawable repeatImage = res.getDrawable(repeatId);
        Drawable repeatOneImage = res.getDrawable(repeatOneId);
        Drawable repeatAllImage = res.getDrawable(repeatAllId);

        switch (repeat)
        {
            case "one":
                btnRepeat.setBackground(repeatOneImage);
                MainActivity.spotifyAppRemote.getPlayerApi().setRepeat(Repeat.ONE);
                break;
            case "all":
                btnRepeat.setBackground(repeatAllImage);
                MainActivity.spotifyAppRemote.getPlayerApi().setRepeat(Repeat.ALL);
                break;
            case "off":
                btnRepeat.setBackground(repeatImage);
                MainActivity.spotifyAppRemote.getPlayerApi().setRepeat(Repeat.OFF);
                break;
        }

        int shuffleId = res.getIdentifier("ic_shuffle", "drawable", getActivity().getPackageName());
        int shuffleOffId = res.getIdentifier("ic_shuffle_off", "drawable", getActivity().getPackageName());
        Drawable shuffleImage = res.getDrawable(shuffleId);
        Drawable shuffleOffImage = res.getDrawable(shuffleOffId);

        switch (shuffle)
        {
            case "false":
                btnShuffle.setBackground(shuffleOffImage);
                MainActivity.spotifyAppRemote.getPlayerApi().setShuffle(false);
                break;
            case "true":
                btnShuffle.setBackground(shuffleImage);
                MainActivity.spotifyAppRemote.getPlayerApi().setShuffle(true);
                break;
        }

    }

    void onFavClick(View v)
    {
        Drawable drawable = btnFav.getBackground();

        int fullFavId = res.getIdentifier("ic_baseline_favorite_24", "drawable", getActivity().getPackageName());
        int emptyFavId = res.getIdentifier("ic_baseline_favorite_border_24", "drawable", getActivity().getPackageName());
        Drawable fullFavImage = res.getDrawable(fullFavId);
        Drawable emptyFavImage = res.getDrawable(emptyFavId);

        if (drawable.getConstantState().equals(fullFavImage.getConstantState()))
        {
            SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
            db.delete("favourite", "uri=?", new String[]{MainActivity.playerViewModel.getUri().getValue()});
            MainActivity.playerViewModel.setFav(emptyFavImage);
        }
        else
        {
            SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();

            db.beginTransaction();
            try
            {
                Cursor c = db.rawQuery("SELECT * FROM user WHERE token = '" + MainActivity.token + "'", null);
                c.moveToFirst();
                int id = c.getInt(0);
                c.close();

                cv.put("artists", MainActivity.playerViewModel.getArtists().getValue());
                cv.put("name", MainActivity.playerViewModel.getSong().getValue());
                cv.put("img", MainActivity.playerViewModel.getCover().getValue());
                cv.put("uri", MainActivity.playerViewModel.getUri().getValue());
                cv.put("user_id", id);
                db.insert("favourite", null, cv);

                db.setTransactionSuccessful();
            }
            finally
            {
                db.endTransaction();
            }

            MainActivity.playerViewModel.setFav(fullFavImage);
        }
    }

    void onShuffleClick(View v)
    {
        Drawable drawable = btnShuffle.getBackground();

        int shuffleId = res.getIdentifier("ic_shuffle", "drawable", getActivity().getPackageName());
        int shuffleOffId = res.getIdentifier("ic_shuffle_off", "drawable", getActivity().getPackageName());
        Drawable shuffleImage = res.getDrawable(shuffleId);
        Drawable shuffleOffImage = res.getDrawable(shuffleOffId);

        if (drawable.getConstantState().equals(shuffleImage.getConstantState()))
        {
            btnShuffle.setBackground(shuffleOffImage);
            MainActivity.spotifyAppRemote.getPlayerApi().setShuffle(false);
            MainActivity.playerViewModel.setShuffle("false");
        }
        else
        {
            btnShuffle.setBackground(shuffleImage);
            MainActivity.spotifyAppRemote.getPlayerApi().setShuffle(true);
            MainActivity.playerViewModel.setShuffle("true");
        }
    }

    void onPrevClick(View v)
    {
        MainActivity.spotifyAppRemote.getPlayerApi().skipPrevious();
    }

    void onPlayClick(View v)
    {
        Drawable drawable = btnPlay.getBackground();

        int pauseId = res.getIdentifier("ic_baseline_pause_circle_24", "drawable", getActivity().getPackageName());
        Drawable pauseImage = res.getDrawable(pauseId);

        if (drawable.getConstantState().equals(pauseImage.getConstantState()))
        {
            MainActivity.playerViewModel.setPlay("play");
            MainActivity.spotifyAppRemote.getPlayerApi().pause();

            int btnPlayId = res.getIdentifier("ic_play_button", "drawable", getActivity().getPackageName());
            Drawable btnPlayImage = res.getDrawable(btnPlayId);
            MainActivity.playerCard.btnPlay.setBackground(btnPlayImage);
        }
        else
        {
            MainActivity.playerViewModel.setPlay("pause");
            MainActivity.spotifyAppRemote.getPlayerApi().resume();

            int btnPauseId = res.getIdentifier("ic_pause_button", "drawable", getActivity().getPackageName());
            Drawable btnPauseImage = res.getDrawable(btnPauseId);
            MainActivity.playerCard.btnPlay.setBackground(btnPauseImage);
        }
    }

    void onNextClick(View v)
    {
        MainActivity.spotifyAppRemote.getPlayerApi().skipNext();
    }

    void onRepeatClick(View v)
    {
        Drawable drawable = btnRepeat.getBackground();

        int repeatId = res.getIdentifier("replay", "drawable", getActivity().getPackageName());
        int repeatOneId = res.getIdentifier("replay_one", "drawable", getActivity().getPackageName());
        int repeatAllId = res.getIdentifier("replay_all", "drawable", getActivity().getPackageName());
        Drawable repeatImage = res.getDrawable(repeatId);
        Drawable repeatOneImage = res.getDrawable(repeatOneId);
        Drawable repeatAllImage = res.getDrawable(repeatAllId);

        if (drawable.getConstantState().equals(repeatImage.getConstantState()))
        {
            btnRepeat.setBackground(repeatOneImage);
            MainActivity.spotifyAppRemote.getPlayerApi().setRepeat(Repeat.ONE);
            MainActivity.playerViewModel.setRepeat("one");
        }
        else if (drawable.getConstantState().equals(repeatOneImage.getConstantState()))
        {
            btnRepeat.setBackground(repeatAllImage);
            MainActivity.spotifyAppRemote.getPlayerApi().setRepeat(Repeat.ALL);
            MainActivity.playerViewModel.setRepeat("all");
        }
        else if (drawable.getConstantState().equals(repeatAllImage.getConstantState()))
        {
            btnRepeat.setBackground(repeatImage);
            MainActivity.spotifyAppRemote.getPlayerApi().setRepeat(Repeat.OFF);
            MainActivity.playerViewModel.setRepeat("off");
        }
    }
}
