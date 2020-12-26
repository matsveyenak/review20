package com.example.musicstats.charts;

import java.util.List;

public class ChartItem
{
    private int id;
    private List<String> artists;
    private String name;
    private int cover;
    private String uri;

    public ChartItem(int id, List<String> artists, String name, String uri)
    {
        this.id = id;
        this.artists = artists;
        this.name = name;
        this.uri = uri;
    }

    public int getID()
    {
        return this.id;
    }

    public int getCover()
    {
        return this.cover;
    }

    public List<String> getArtists()
    {
        return this.artists;
    }

    public String getName()
    {
        return this.name;
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
