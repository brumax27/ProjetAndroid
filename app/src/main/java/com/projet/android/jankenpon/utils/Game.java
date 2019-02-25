package com.projet.android.jankenpon.utils;

import android.util.Log;

import java.util.HashMap;

public class Game {
    public int victories = 0;
    public int defeats = 0;
    public HashMap<String, Integer> playedSymbols;

    // 0 = draw
    // 1 = victory
    // 2 = defeat
    private static final int[][] RULES = {
        //   r  p  s
            {0, 2, 1}, // rock
            {1, 0, 2}, // paper
            {2, 1, 0}  // scissors
    };
    private static final HashMap<String, Integer> SYMBOLS = new HashMap<String, Integer>() {
        {
            put("rock", 0);
            put("paper", 1);
            put("scissors", 2);
        }
    };
    private static final int VICTORY_CONDITION = 2;
    private static final String[] RESULT = { "Egalité", "Victoire", "Défaite" };
    private static final String TAG = "GAME_TAG";

    public Game() {
        playedSymbols = new HashMap<>();
        playedSymbols.put("rock", 0);
        playedSymbols.put("paper", 0);
        playedSymbols.put("scissors", 0);
    }

    public String playTurn(String playedSymbol, String opponentSymbol) {
        Log.i(TAG, "played: " + playedSymbol + " opponent: " + opponentSymbol);
        playedSymbols.put(playedSymbol, playedSymbols.get(playedSymbol) + 1);
        int result = RULES[symbolIndex(playedSymbol)][symbolIndex(opponentSymbol)];
        switch (result) {
            case 1:
                victories++;
                break;
            case 2:
                defeats++;
                break;
        }
        Log.i(TAG, "Fragment victories: " + victories + " defeats: " + defeats);
        return RESULT[result];
    }

    public int symbolIndex(String symbol) {
        return SYMBOLS.get(symbol);
    }

    public boolean finished() {
        if (victories == VICTORY_CONDITION || defeats == VICTORY_CONDITION) {
            return true;
        }
        return false;
    }
}
