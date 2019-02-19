package com.projet.android.jankenpon.io;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projet.android.jankenpon.entity.Score;

import java.util.ArrayList;
import java.util.List;

public final class FirebaseScoreUtils {

    private FirebaseDatabase database;

    public List<Score> getByPlayerId(String playerId) {
        final List<Score> scores = new ArrayList<>();
        database.getReference("matches").child(playerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot scoreSnapshot : dataSnapshot.getChildren()) {
                    Score score = scoreSnapshot.getValue(Score.class);
                    scores.add(score);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DATABASE_TAG", "loadPost:onCancelled", databaseError.toException());
            }
        });
        return scores;
    }

    public void addScore(final Score score){
        database.getReference("matches").child(String.valueOf(score.getPlayerVictories())).setValue(score);
    }
}
