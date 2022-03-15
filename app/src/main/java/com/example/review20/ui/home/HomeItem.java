package com.example.review20.ui.home;

public class HomeItem
{
    private String title;
    private String image;
    private String uri;

    public HomeItem(String title, String image)
    {
        this.title = title;
        this.image = image;
    }

    public HomeItem(String title, String image, String uri)
    {
        this.title = title;
        this.image = image;
        this.uri = uri;
    }

    public String getImage()
    {
        return this.image;
    }

    public String getUri() {
        return this.uri;
    }

    public String getTitle()
    {
        return this.title;
    }
}
