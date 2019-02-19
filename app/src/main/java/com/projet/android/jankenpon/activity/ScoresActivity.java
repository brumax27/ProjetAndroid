package com.projet.android.jankenpon.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.projet.android.jankenpon.R;
import com.projet.android.jankenpon.entity.Score;
import com.projet.android.jankenpon.io.CacheScoresUtil;
import com.projet.android.jankenpon.io.FirebaseScoreUtils;

import java.util.ArrayList;
import java.util.List;

//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class ScoresActivity extends AppCompatActivity {
    List<Score> scores = new ArrayList<>();
    private ListView listView;
    private ArrayAdapter<Score> arrayAdapter;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        listView = (ListView)findViewById(R.id.listScores);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1 , scores);
        if (CacheScoresUtil.isCacheEmpty(context)) {
            fetchScoreFromFirebase();
        } else {
            scores = CacheScoresUtil.readCache(context);
            listView.setAdapter(arrayAdapter);
            Toast.makeText(this, "Size Score : " + scores.size(), Toast.LENGTH_SHORT).show();
        }
    }

    public void fetchScoreFromFirebase() {
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        scores = new FirebaseScoreUtils().getByPlayerId("1");
        CacheScoresUtil.writeCache(context, scores);
        CacheScoresUtil.logList("FROM FIREBASE",scores);
        listView.setAdapter(arrayAdapter);
    }


}
