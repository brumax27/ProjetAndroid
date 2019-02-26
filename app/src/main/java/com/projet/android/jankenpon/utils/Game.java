package com.projet.android.jankenpon.utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.FirebaseDatabase;
import com.projet.android.jankenpon.entity.Score;
import com.projet.android.jankenpon.entity.User;
import com.projet.android.jankenpon.io.FirebaseScoreUtils;
import com.projet.android.jankenpon.io.FirebaseUserUtils;

import java.util.HashMap;
import java.util.Map;

public class Game {
    private User user;
    private Score score;
    private String id;

    // 0 = draw
    // 1 = victory
    // 2 = defeat
    private static final int[][] RULES = {
        //   r  p  s
            {0, 2, 1}, // rock
            {1, 0, 2}, // paper
            {2, 1, 0}  // scissors
    };
    private static final Map<String, Integer> SYMBOLS = new HashMap<String, Integer>() {
        {
            put("rock", 0);
            put("paper", 1);
            put("scissors", 2);
        }
    };
    private static final int VICTORY_CONDITION = 2;
    private static final String[] RESULT = { "Egalité", "Victoire", "Défaite" };
    private static final String TAG = "GAME_TAG";

    public Game(Context context, String opponent) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        id = account.getId();
        user = new User();
        new FirebaseUserUtils(FirebaseDatabase.getInstance()).getByPlayerId(id, user);
        score = new Score(opponent);
    }

    public String playTurn(String playedSymbol, String opponentSymbol) {
        Log.i(TAG, "played: " + playedSymbol + " opponent: " + opponentSymbol);

        incrementSymbol(playedSymbol);


        int result = RULES[symbolIndex(playedSymbol)][symbolIndex(opponentSymbol)];
        switch (result) {
            case 1:
                score.incrementVictories();
                break;
            case 2:
                score.incrementDefeats();
                break;
        }
        return RESULT[result];
    }

    public int symbolIndex(String symbol) {
        return SYMBOLS.get(symbol);
    }

    public boolean finished() {
        Log.i("DATA_TAG", score.toString());
        if (score.getPlayerVictories() == VICTORY_CONDITION
                || score.getOpponentVictories() == VICTORY_CONDITION) {
            return true;
        }
        return false;
    }

    private void incrementSymbol(String playedSymbol) {
        switch (playedSymbol) {
            case "rock":
                user.addRockHits(1);
                break;
            case "paper":
                user.addPaperHits(1);
                break;
            case "scissors":
                user.addScissorsHits(1);
                break;
        }
    }

    public void pushToFirebase() {
        if (win()) {
            user.addVictories(1);
        } else {
            user.addDefeats(1);
        }
        new FirebaseUserUtils(FirebaseDatabase.getInstance()).updateUser(id, user);
        new FirebaseScoreUtils(FirebaseDatabase.getInstance()).addScore(id, score);
    }

    private boolean win() {
        return score.getPlayerVictories() == 2;
    }
}
