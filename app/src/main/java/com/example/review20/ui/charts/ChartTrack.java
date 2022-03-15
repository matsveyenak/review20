package com.example.review20.ui.charts;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;

public class ChartTrack
{
    private int id;
    private List<String> artists;
    private String title;
    private String uri;
    private String cover;

    public ChartTrack(int id, List<String> artists, String title, String uri, String cover)
    {
        this.id = id;
        this.artists = artists;
        this.title = title;
        this.uri = uri;
        this.cover = cover;
    }

    public int getID()
    {
        return this.id;
    }

    public String getCover()
    {
        return this.cover;
    }

    public List<String> getArtists()
    {
        return this.artists;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getUri()
    {
        return this.uri;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}
