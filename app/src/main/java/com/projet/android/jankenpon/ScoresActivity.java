package com.projet.android.jankenpon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScoresActivity extends AppCompatActivity {
    List<Score> scores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        if (isCacheEmpty()) {
            fetchScoreFromFirebase();
        } else {
            // createFakeData();
            readCache();
        }

        // TODO: Use an adapter to display scores as a ListView
    }

    public boolean isCacheEmpty() {
        return false;
    }

    public void fetchScoreFromFirebase() {
        // TODO: Fetch scores from Firebase
    }

    public void createFakeData() {
        for (int i = 0; i < 5; i++) {
            scores.add(new Score('2', '1', "Bidule"));
            scores.add(new Score('0', '2', "Machin"));
        }
        writeCache();
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
        logCache();
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

    public void logCache() {
        for (Score score : scores) {
            Log.i("SCORE", score.toString());
        }
    }
}
