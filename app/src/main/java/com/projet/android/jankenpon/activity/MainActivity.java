package com.projet.android.jankenpon.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projet.android.jankenpon.R;
import com.projet.android.jankenpon.entity.User;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE = 7645;
    private GoogleSignInClient client = null ;
    FirebaseDatabase mFirebaseInstance;
    DatabaseReference mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        findViewById(R.id.play).setVisibility(View.GONE);
        findViewById(R.id.results).setVisibility(View.GONE);

        getClients();

        findViewById(R.id.results).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScoresActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.quit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.practice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RandomFightActivity.class);
                startActivity(intent);
            }
        });
        
        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoadingGameActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = client.getSignInIntent();
                startActivityForResult(signInIntent, REQUEST_CODE);
            }
        });

        findViewById(R.id.sign_out_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
                        findViewById(R.id.sign_out_button).setVisibility(View.GONE);
                        findViewById(R.id.play).setVisibility(View.GONE);
                        findViewById(R.id.results).setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null){
            CreateNewAccount(account);
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
            findViewById(R.id.play).setVisibility(View.VISIBLE);
            findViewById(R.id.results).setVisibility(View.VISIBLE);
        }
    }

    private void CreateNewAccount(final GoogleSignInAccount account) {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users").child(account.getId());
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user == null) {
                    user = new User(account.getDisplayName());
                    mFirebaseDatabase.setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DATABASE_TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mFirebaseDatabase.addListenerForSingleValueEvent(userListener);
    }

    private void getClients(){
        GoogleSignInOptions gso  = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        this.client = GoogleSignIn.getClient(this, gso);
    }
}

