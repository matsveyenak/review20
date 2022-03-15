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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.example.review20.MainActivity;
import com.example.review20.R;
import com.example.review20.ui.charts.ChartTrackFragment;
import com.google.android.material.card.MaterialCardView;

public class PlayerCard
{
    public static Context ctx;
    public String uri;
    public TextView tvSong, tvArtists;
    public ImageView ivCover;
    public ImageButton btnPlay, btnFav;
    public MaterialCardView card;
    //NavController navController;

    public PlayerCard(View root, Context ctx)//, NavController navController)
    {
        this.ctx = ctx;
        //this.navController = navController;

        card = root.findViewById(R.id.player_card);
        card.setVisibility(View.GONE);

        ivCover = root.findViewById(R.id.iv_player_card_image);
        tvArtists = root.findViewById(R.id.tv_player_card_artist);
        tvSong = root.findViewById(R.id.tv_player_card_title);
        btnPlay = root.findViewById(R.id.btn_player_card_play);
        btnFav = root.findViewById(R.id.btn_player_card_fav);

        card.setOnClickListener(this::onClick);
        btnFav.setOnClickListener(this::onClick);
        btnPlay.setOnClickListener(this::onClick);
    }

    void onClick (View v)
    {
        if (v.getId() == R.id.player_card)
        {
            MainActivity activity = (MainActivity) ctx;
            Fragment f = new PlayerFragment();
            activity.replaceFragment(f);
            //navController.navigate(R.id.action_player);
        }
        else if (v.getId() == R.id.btn_player_card_fav)
        {
            Drawable drawable = MainActivity.playerViewModel.getFav().getValue();

            Resources res = ctx.getResources();
            int fullFavId = res.getIdentifier("ic_baseline_favorite_24", "drawable", ctx.getPackageName());
            int emptyFavId = res.getIdentifier("ic_baseline_favorite_border_24", "drawable", ctx.getPackageName());
            Drawable fullFavImage = res.getDrawable(fullFavId);
            Drawable emptyFavImage = res.getDrawable(emptyFavId);

            if (drawable.getConstantState().equals(fullFavImage.getConstantState()))
            {
                SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
                db.delete("favourite", "uri=?", new String[]{uri});
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
        else if (v.getId() ==  R.id.btn_player_card_play)
        {
            Drawable drawable = btnPlay.getBackground();

            Resources res = ctx.getResources();
            int playId = res.getIdentifier("ic_play_button", "drawable", ctx.getPackageName());
            int pauseId = res.getIdentifier("ic_pause_button", "drawable", ctx.getPackageName());
            Drawable playImage = res.getDrawable(playId);
            Drawable pauseImage = res.getDrawable(pauseId);

            if (drawable.getConstantState().equals(pauseImage.getConstantState()))
            {
                MainActivity.playerViewModel.setPlay("play");
                MainActivity.spotifyAppRemote.getPlayerApi().pause();
                btnPlay.setBackground(playImage);
            }
            else
            {
                MainActivity.playerViewModel.setPlay("pause");
                MainActivity.spotifyAppRemote.getPlayerApi().resume();
                btnPlay.setBackground(pauseImage);
            }
        }
    }

}
