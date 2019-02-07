package com.projet.android.jankenpon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScoresActivity extends AppCompatActivity {
    List<Score> scores = new ArrayList<>();
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        ListView listView = (ListView)findViewById(R.id.listScores);

        if (isCacheEmpty()) {
            fetchScoreFromFirebase();

        } else {
            // createFakeData();            readCache();
            // TODO : Send to adaptater
        }
        ArrayAdapter<Score> arrayAdapter
                = new ArrayAdapter<Score>(this, android.R.layout.simple_list_item_1 , scores);
        listView.setAdapter(arrayAdapter);
    }

    public void fetchScoreFromFirebase() {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        // TODO : Replace "1" by current player id
        mFirebaseDatabase = mFirebaseInstance.getReference("matches").child("1");

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot scoreSnapshot : dataSnapshot.getChildren()) {
                    Score score = scoreSnapshot.getValue(Score.class);
                    scores.add(score);
                }

                writeCache();
                logList("FROM FIREBASE");
                // TODO : Send to adaptater
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("DATABASE_TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mFirebaseDatabase.addValueEventListener(userListener);
    }

    public void createFakeData() {
        for (int i = 0; i < 5; i++) {
            scores.add(new Score('2', '1', "Bidule"));
            scores.add(new Score('0', '2', "Machin"));
        }
        writeCache();
    }

    public boolean isCacheEmpty() {
        File cacheFile = new File(getCacheDir(), "scores.txt");
        if(cacheFile.exists()) {
            return false;
        }
        return true;
    }

    public void readCache() {
        File cacheDir = getCacheDir();
        try {
            BufferedReader br = new BufferedReader(new FileReader(cacheDir.getPath() + "/scores.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                Score score = new Score(line.charAt(0), line.charAt(2), line.substring(4));
                scores.add(score);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        logList("FROM CACHE");
    }

    public void resetCache() {
        deleteCache();
        writeCache();
    }

    public void deleteCache() {
        File cacheFile = new File(getCacheDir(), "scores.txt");
        cacheFile.delete();
    }

    public void writeCache() {
        File cacheDir = getCacheDir();
        try {
            FileOutputStream fos = new FileOutputStream(cacheDir.getPath() + "/scores.txt");
            writeDataToFOS(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeDataToFOS(FileOutputStream fos) throws IOException {
        for(Score score : scores) {
            fos.write(score.toStream().getBytes());
            fos.write('\n');
        }
        fos.flush();
    }

    public void logList(String msg) {
        Log.i("SCORE_TAG", msg);
        for (Score score : scores) {
            Log.i("SCORE_TAG", score.toString());
        }
    }
}
