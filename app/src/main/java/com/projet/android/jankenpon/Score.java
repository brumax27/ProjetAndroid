package com.projet.android.jankenpon;

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

    public Score(int playerVictories, int opponentVictories, String opponent) {
        this.playerVictories = playerVictories;
        this.opponentVictories = opponentVictories;
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

    public String getMessage() {
        return getStatus() + " contre " + this.opponent;
    }

    public String toString() {
        return playerVictories + " - " + opponentVictories + " " + getMessage();
    }

    public String getStatus() {
        return playerVictories > opponentVictories ? "Victoire" : "Defaite";
    }

    public String toStream() {
        return playerVictories + " " + opponentVictories + " " + opponent;
    }
}
