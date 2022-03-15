package com.example.review20.ui.awards;

public class AwardItem
{
    private String title;
    private String image;

    public AwardItem(String title, String image)
    {
        this.title = title;
        this.image = image;
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
