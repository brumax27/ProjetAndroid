package com.projet.android.jankenpon;

public class Score {
    private String playerVictories;
    private String opponentVictories;
    private String opponent;

    public Score(char playerVictories, char opponentVictories, String opponent) {
        this.playerVictories = String.valueOf(playerVictories);
        this.opponentVictories = String.valueOf(opponentVictories);
        this.opponent = opponent;
    }

    public String getPlayerVictories() {
        return playerVictories;
    }

    public String getOpponentVictories() {
        return opponentVictories;
    }

    public String getOpponent() {
        return opponent;
    }

    public String toString() {
        return getStatus() + " contre " + this.opponent;
    }

    public String getStatus() {
        return Integer.parseInt(playerVictories) > Integer.parseInt(opponentVictories) ? "Victoire" : "Defaite";
    }

    public String toStream() {
        return playerVictories + " " + opponentVictories + " " + opponent;
    }
}
