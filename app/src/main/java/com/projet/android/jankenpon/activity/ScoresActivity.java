package com.projet.android.jankenpon.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.FirebaseDatabase;
import com.projet.android.jankenpon.R;
import com.projet.android.jankenpon.entity.Score;
import com.projet.android.jankenpon.io.CacheScoresUtil;
import com.projet.android.jankenpon.io.FirebaseScoreUtils;

import java.util.ArrayList;
import java.util.List;

public class ScoresActivity extends AppCompatActivity {
    List<Score> scores = new ArrayList<>();
    private ListView listView;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        listView = findViewById(R.id.listScores);
        listView.setAdapter(new ArrayAdapter<Score>(this, android.R.layout.simple_list_item_1 , scores));

        if (CacheScoresUtil.isCacheEmpty(context)) {

            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            new FirebaseScoreUtils(FirebaseDatabase.getInstance()).getByPlayerId(account.getId(), scores);
            if (scores != null && !scores.isEmpty()) {
                CacheScoresUtil.writeCache(context, scores);
                CacheScoresUtil.logList("FROM FIREBASE", scores);
            }

        } else {

            scores = CacheScoresUtil.readCache(context);
            Toast.makeText(this, "Size Score : " + scores.size(), Toast.LENGTH_SHORT).show();

        }

    }
}
