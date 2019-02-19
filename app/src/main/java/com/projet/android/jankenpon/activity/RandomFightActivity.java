package com.projet.android.jankenpon.activity;

import android.content.Intent;
import android.os.Handler;
import com.projet.android.jankenpon.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class RandomFightActivity extends AppCompatActivity {

        ImageView iv_player, iv_enemy;
        TextView tv_points;
        ProgressBar progressBar;

        Handler handler;
        Runnable runnable;

        Random r;

        private final static int STATE_ROCK = 1;
        private final static int STATE_PAPER = 2;
        private final static int STATE_SCISSORS = 3;

        int playerState = STATE_PAPER;
        int enemyState = STATE_ROCK;

        int currentTime = 4000;
        int startTime = 4000;

        int currentPoints = 0;

        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_random_fight);

            iv_player = findViewById(R.id.player);
            iv_enemy = findViewById(R.id.enemy);
            tv_points = findViewById(R.id.points);
            progressBar = findViewById(R.id.progressBar);

            findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(RandomFightActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            progressBar.setMax(startTime);
            progressBar.setProgress(startTime);

            tv_points.setText("Points: " + currentPoints);

            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    currentTime = currentTime - 100;
                    progressBar.setProgress(currentTime);

                    r = new Random();
                    enemyState = r.nextInt(3) + 1;

                    if (currentTime > 0) {
                        handler.postDelayed(runnable, 100);
                    } else {
                        iv_player.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setPlayerImage(setPlayerPosition(playerState));
                            }
                        });

                        setEnemyImage(enemyState);

                        if ((playerState == STATE_ROCK && enemyState == STATE_SCISSORS) ||
                            (playerState == STATE_PAPER && enemyState == STATE_ROCK) ||
                            (playerState == STATE_SCISSORS && enemyState == STATE_PAPER))
                            {
                            currentPoints = currentPoints + 1;
                            tv_points.setText("Points :" + currentPoints);

                            startTime = startTime - 100;
                            if (startTime < 1000) {
                                startTime = 2000;
                            }
                            progressBar.setMax(startTime);
                            currentTime = startTime;
                            progressBar.setProgress(currentTime);

                            handler.postDelayed(runnable, 100);
                        } else {
                            iv_player.setEnabled(false);
                            Toast.makeText(RandomFightActivity.this, "Game Over!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            };
            handler.postDelayed(runnable, 100);
        }

        private void setEnemyImage (int state)
        {
            switch (state) {
                case STATE_ROCK:
                    iv_enemy.setImageResource(R.drawable.rock);
                    enemyState = STATE_ROCK;
                    break;
                case STATE_PAPER:
                    iv_enemy.setImageResource(R.drawable.paper);
                    enemyState = STATE_PAPER;
                    break;
                case STATE_SCISSORS:
                    iv_enemy.setImageResource(R.drawable.scissors);
                    enemyState = STATE_SCISSORS;
                    break;
            }
        }

        private void setRotation (final ImageView image, final int drawable ){
            RotateAnimation rotateAnimation = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(100);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    image.setImageResource(drawable);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            image.startAnimation(rotateAnimation);
        }

        private int setPlayerPosition (int position){
            position = position + 1;
            if (position == 4) {
                position = 1;
            }
            return position;
        }

        private void setPlayerImage (int state){
            switch (state) {
                case STATE_ROCK:
                    setRotation(iv_player, R.drawable.rock);
                    playerState = STATE_ROCK;
                    break;
                case STATE_PAPER:
                    setRotation(iv_player, R.drawable.paper);
                    playerState = STATE_PAPER;
                    break;
                case STATE_SCISSORS:
                    setRotation(iv_player, R.drawable.scissors);
                    playerState = STATE_SCISSORS;
                    break;
            }
        }
    }


