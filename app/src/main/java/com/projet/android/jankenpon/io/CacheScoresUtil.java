package com.projet.android.jankenpon.io;

import android.content.Context;
import android.util.Log;

import com.projet.android.jankenpon.entity.Score;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class CacheScoresUtil {

    private CacheScoresUtil() {
    }

    public static boolean isCacheEmpty(Context context) {
        File cacheFile = new File(context.getCacheDir(), "scores.txt");
        if(cacheFile.exists()) {
            return false;
        }
        return true;
    }

    public static List<Score> readCache(Context context) {
        File cacheDir = context.getCacheDir();
        List<Score> scores = new ArrayList<>();
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
        logList("FROM CACHE", scores);
        return scores;
    }

    public void resetCache(Context context) {
        deleteCache(context);
        writeCache(context, new ArrayList<Score>());
    }

    public static void deleteCache(Context context) {
        File cacheFile = new File(context.getCacheDir(), "scores.txt");
        cacheFile.delete();
    }

    public static void writeCache(Context context, List<Score> scores) {
        File cacheDir = context.getCacheDir();
        try {
            FileOutputStream fos = new FileOutputStream(cacheDir.getPath() + "/scores.txt");
            writeDataToFOS(fos,scores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeDataToFOS(FileOutputStream fos, List<Score> scores) throws IOException {
        for(Score score : scores) {
            fos.write(score.toStream().getBytes());
            fos.write('\n');
        }
        fos.flush();
    }

    public static void logList(String msg, List<Score> scores) {
        Log.i("SCORE_TAG", msg);
        for (Score score : scores) {
            Log.i("SCORE_TAG", score.toString());
        }
    }
}
