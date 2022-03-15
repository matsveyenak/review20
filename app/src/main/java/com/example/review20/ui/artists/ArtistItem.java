package com.example.review20.ui.artists;

public class ArtistItem
{
    private String name;
    private String image;

    public ArtistItem(String name, String image)
    {
        this.name = name;
        this.image = image;
    }

    public String getImage()
    {
        return this.image;
    }

    public String getName()
    {
        return this.name;
    }
}
