package com.example.review20.ui.awards;

public class AwardNominationItem
{
    private String nomination;
    private String winner;
    private String nominee;

    public AwardNominationItem(String nomination, String winner, String nominee)
    {
        this.nomination = nomination;
        this.winner = winner;
        this.nominee = nominee;
    }

    public String getNomination() {
        return this.nomination;
    }

    public String getNominee() {
        return this.nominee;
    }

    public String getWinner() {
        return winner;
    }
}
