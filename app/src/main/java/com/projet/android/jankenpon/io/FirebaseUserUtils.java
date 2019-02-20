package com.projet.android.jankenpon.io;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projet.android.jankenpon.entity.User;

import java.util.ArrayList;
import java.util.List;

public final class FirebaseUserUtils {

    private FirebaseDatabase database;

    public FirebaseUserUtils() {
    }

    public FirebaseUserUtils(FirebaseDatabase database) {
        this.database = database;
    }

    public void getByPlayerId(String playerId, final User user) {
        final List<User> users = new ArrayList<>();
        database.getReference("users").child(playerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userSnap = dataSnapshot.getValue(User.class);
                user.clone(userSnap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DATABASE_TAG", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    public void updateUser(String googleAccountId, final User user) {
        final List<User> users = new ArrayList<>();
        database.getReference("users").child(googleAccountId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userSnap = dataSnapshot.getValue(User.class);
                user.setVictories(userSnap.getVictories());
                user.setDefeats(userSnap.getDefeats());

                user.addPaperHits(userSnap.getPaperHits())
                    .addRockHits(userSnap.getRockHits())
                    .addScissorsHits(userSnap.getScissorsHits());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DATABASE_TAG", "loadPost:onCancelled", databaseError.toException());
            }
        });
        database.getReference("users").child(googleAccountId).setValue(user);
    }


    public void addUser(final User user, String googleAccountId){
        database.getReference("users").child(googleAccountId).setValue(user);
    }
}
