package com.example.review20.ui.player;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.review20.MainActivity;
import com.squareup.picasso.Picasso;

public class PlayerViewModel extends ViewModel
{
    public static int click = 0;
    private MutableLiveData<String> song;
    private MutableLiveData<String> uri;
    private MutableLiveData<String> artists;
    private MutableLiveData<Drawable> fav;
    private MutableLiveData<String> play;
    private MutableLiveData<String> repeat;
    private MutableLiveData<String> shuffle;
    private MutableLiveData<String> cover;

    public PlayerViewModel()
    {
        song = new MutableLiveData<>();
        uri = new MutableLiveData<>();
        artists = new MutableLiveData<>();
        fav = new MutableLiveData<>();
        play = new MutableLiveData<>();
        cover = new MutableLiveData<>();
        repeat = new MutableLiveData<>();
        shuffle = new MutableLiveData<>();
        repeat.setValue("all");
        shuffle.setValue("false");
    }

    public MutableLiveData<String> getSong()
    {
        return song;
    }

    public MutableLiveData<String> getUri()
    {
        return uri;
    }

    public MutableLiveData<String> getArtists() {
        return artists;
    }

    public MutableLiveData<Drawable> getFav() {
        return fav;
    }

    public MutableLiveData<String> getCover() {
        return cover;
    }

    public MutableLiveData<String> getPlay() {
        return play;
    }

    public MutableLiveData<String> getRepeat() {
        return repeat;
    }

    public MutableLiveData<String> getShuffle() {
        return shuffle;
    }

    public void setUri(String uri)
    {
        this.uri.setValue(uri);
        MainActivity.playerCard.uri = uri;
    }

    public void setSong(String song)
    {
        this.song.setValue(song);
        MainActivity.playerCard.tvSong.setText(song);
    }

    public void setArtists(String artists)
    {
        this.artists.setValue(artists);
        MainActivity.playerCard.tvArtists.setText(artists);
    }

    public void setFav(Drawable fav)
    {
        this.fav.setValue(fav);
        MainActivity.playerCard.btnFav.setBackground(fav);
    }

    public void setCover(String cover)
    {
        this.cover.setValue(cover);
        Picasso.get().load(cover).into(MainActivity.playerCard.ivCover);
    }

    public void setPlay(String play)
    {
        this.play.setValue(play);
    }

    public void setRepeat(String repeat)
    {
        this.repeat.setValue(repeat);
    }

    public void setShuffle(String shuffle)
    {
        this.shuffle.setValue(shuffle);
    }

}