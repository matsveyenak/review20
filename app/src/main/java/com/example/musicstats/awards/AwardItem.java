package com.example.musicstats.awards;

public class AwardItem
{
    private String nomination;
    private String winner;
    private String nominee;
    private boolean expanded;


    public AwardItem(String nomination, String winner, String nominee)
    {
        this.nomination = nomination;
        this.winner = winner;
        this.nominee = nominee;
        this.expanded = false;
    }

    public String getNomination()
    {
        return this.nomination;
    }

    public String getNominee()
    {
        return this.nominee;
    }

    public String getWinner()
    {
        return winner;
    }

    public boolean isExpanded()
    {
        return  expanded;
    }

    public void setExpanded(boolean expanded)
    {
        this.expanded = expanded;
    }
}
