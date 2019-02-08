package com.projet.android.jankenpon;

public class User {
    private String pseudo;
    private int rockHits;
    private int paperHits;
    private int scissorsHits;
    private int victories;
    private int defeats;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String pseudo, int rockHits, int paperHits, int scissorsHits, int victories, int defeats) {
        this.pseudo = pseudo;
        this.rockHits = rockHits;
        this.paperHits = paperHits;
        this.scissorsHits = scissorsHits;
        this.victories = victories;
        this.defeats = defeats;
    }

    public String getPseudo() {
        return pseudo;
    }

    public int getRockHits() {
        return rockHits;
    }

    public int getPaperHits() {
        return paperHits;
    }

    public int getScissorsHits() {
        return scissorsHits;
    }

    public int getVictories() {
        return victories;
    }

    public int getDefeats() {
        return defeats;
    }

    public int totalBattles() {
        return victories + defeats;
    }

    public int totalHits() {
        return paperHits + rockHits + scissorsHits;
    }

    public double victoryRatio() {
        return Math.round(victories * 100.0 / totalBattles());
    }

    public double paperRatio() {
        return Math.round(paperHits * 100.0 / totalHits());
    }

    public double rockRatio() {
        return Math.round(rockHits * 100.0 / totalHits());
    }

    public double scissorsRatio() {
        return Math.round(scissorsHits * 100.0 / totalHits());
    }

    @Override
    public String toString() {
        return "User{" +
                "pseudo='" + pseudo + '\'' +
                ", rockHits=" + rockHits +
                ", paperHits=" + paperHits +
                ", scissorsHits=" + scissorsHits +
                ", victories=" + victories +
                ", defeats=" + defeats +
                '}';
    }
}
