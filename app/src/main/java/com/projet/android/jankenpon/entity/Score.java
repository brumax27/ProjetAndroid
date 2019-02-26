package com.projet.android.jankenpon.entity;

import java.util.Objects;

public class Score {
    private int playerVictories;
    private int opponentVictories;
    private String opponent;

    public Score() {
        // Default constructor required for calls to DataSnapshot.getValue(Score.class)
    }

    public Score(char playerVictories, char opponentVictories, String opponent) {
        this.playerVictories = Character.getNumericValue(playerVictories);
        this.opponentVictories = Character.getNumericValue(opponentVictories);
        this.opponent = opponent;
    }

    public Score(String opponent) {
        this.opponent = opponent;
    }

    public int getPlayerVictories() {
        return playerVictories;
    }

    public int getOpponentVictories() {
        return opponentVictories;
    }

    public String getOpponent() {
        return opponent;
    }

    public void incrementVictories() {
        playerVictories++;
    }

    public void incrementDefeats() {
        opponentVictories++;
    }

    public String getMessage() {
        return getStatus() + " contre " + this.opponent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Score)) return false;
        Score score = (Score) o;
        return getPlayerVictories() == score.getPlayerVictories() &&
                getOpponentVictories() == score.getOpponentVictories() &&
                Objects.equals(getOpponent(), score.getOpponent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayerVictories(), getOpponentVictories(), getOpponent());
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(playerVictories).append(" - ").append(opponentVictories);
        sb.append("    ").append(getMessage());
        return sb.toString();
    }

    public String getStatus() {
        return playerVictories > opponentVictories ? "Victoire" : "Defaite";
    }

    public String toStream() {
        return playerVictories + " " + opponentVictories + " " + opponent;
    }
}
