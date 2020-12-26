package com.example.musicstats.charts;

public class Item
{
    private int id;
    private String title;
    private int img;
    private String uri;

    public Item(int id, String title, int img, String uri)
    {
        this.id = id;
        this.title = title;
        this.img = img;
        this.uri = uri;
    }

    public int getID()
    {
        return this.id;
    }

    public int getImg()
    {
        return this.img;
    }

    public String getTitle()
    {
        return title;
    }

    public String getUri()
    {
        return this.uri;
    }
}
