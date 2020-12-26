package com.example.musicstats.artists;

public class ArtistItem
{
    private String name;
    private int cover;
    private String uri;

    public ArtistItem(String name, String uri)
    {
        this.name = name;
        this.uri = uri;
    }

    public int getCover()
    {
        return this.cover;
    }

    public String getName()
    {
        return this.name;
    }

    public String getUri()
    {
        return this.uri;
    }
}
