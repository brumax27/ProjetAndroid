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

    public List<User> getByPlayerId(String playerId) {
        final List<User> users = new ArrayList<>();
        database.getReference("users").child(playerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot scoreSnapshot : dataSnapshot.getChildren()) {
                    User user = scoreSnapshot.getValue(User.class);
                    users.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DATABASE_TAG", "loadPost:onCancelled", databaseError.toException());
            }
        });
        return users;
    }

    public void addScore(final User user, String googleAccountId){
        database.getReference("users").child(googleAccountId).setValue(user);
    }
}
