package com.example.review20.ui.charts;

import java.util.List;

public class ChartItem
{
    private String title;
    private String image;
    private String uri;

    public ChartItem(String title, String image, String uri)
    {
        this.title = title;
        this.image = image;
        this.uri = uri;
    }

    public String getUri()
    {
        return uri;
    }

    public String getImage()
    {
        return this.image;
    }

    public String getTitle()
    {
        return this.title;
    }
}
