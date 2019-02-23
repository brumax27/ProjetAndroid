package com.projet.android.jankenpon.entity;

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

    public void clone(User user) {
        this.pseudo = user.getPseudo();
        this.rockHits = user.getRockHits();
        this.paperHits = user.getPaperHits();
        this.scissorsHits = user.getScissorsHits();
        this.victories = user.getVictories();
        this.defeats = user.getDefeats();
    }

    public User(String pseudo) {
        this.pseudo = pseudo;
    }

    public User(String pseudo, int rockHits, int paperHits, int scissorsHits, int victories, int defeats) {
        this.pseudo = pseudo;
        this.rockHits = 0;
        this.paperHits = 0;
        this.scissorsHits = 0;
        this.victories = 0;
        this.defeats = 0;
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

    public User addRockHits(int rockHits) {
        this.rockHits += rockHits;
        return this;
    }

    public User addPaperHits(int paperHits) {
        this.paperHits += paperHits;
        return this;
    }

    public User addScissorsHits(int scissorsHits) {
        this.scissorsHits += scissorsHits;
        return this;
    }

    public User addVictories(int victories) {
        this.victories += victories;
        return this;
    }

    public User addDefeats(int defeats) {
        this.defeats += defeats;
        return this;
    }


    public void setRockHits(int rockHits) {
        this.rockHits = rockHits;
    }

    public void setPaperHits(int paperHits) {
        this.paperHits = paperHits;
    }

    public void setScissorsHits(int scissorsHits) {
        this.scissorsHits = scissorsHits;
    }

    public void setVictories(int victories) {
        this.victories = victories;
    }

    public void setDefeats(int defeats) {
        this.defeats = defeats;
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
