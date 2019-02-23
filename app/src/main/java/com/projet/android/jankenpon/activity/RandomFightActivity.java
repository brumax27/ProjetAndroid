package com.projet.android.jankenpon.activity;

import android.content.Intent;
import com.projet.android.jankenpon.R;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class RandomFightActivity extends AppCompatActivity {
    Button b_rock, b_paper, b_scissors;
    TextView tv_scores;
    ImageView iv_PcChoice, iv_playerChoice, b_back, b_replay;
    int playerScore, pcScore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_fight);

        b_rock = findViewById(R.id.b_rock);
        b_paper = findViewById(R.id.b_paper);
        b_scissors = findViewById(R.id.b_scissors);
        b_back = findViewById(R.id.b_back);
        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RandomFightActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        b_replay = findViewById(R.id.b_replay);
        b_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b_rock.setEnabled(true);
                b_paper.setEnabled(true);
                b_scissors.setEnabled(true);
                tv_scores.setText("Computer : 0  Player : 0");
                playerScore = 0;
                pcScore = 0;
            }
        });

        iv_PcChoice = findViewById(R.id.iv_pc);
        iv_playerChoice = findViewById(R.id.ic_player);

        tv_scores = findViewById(R.id.tv_scores);
        tv_scores.setText("Computer : 0  Player : 0");

        b_rock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_playerChoice.setImageResource(R.drawable.rock);
                play_turn ("rock");
                tv_scores.setText("Computer : " + Integer.toString(pcScore) + "  Player : " + Integer.toString(playerScore));
            }
        });

        b_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_playerChoice.setImageResource(R.drawable.paper);
                play_turn ("paper");
                tv_scores.setText("Computer : " + Integer.toString(pcScore) + "  Player : " + Integer.toString(playerScore));
            }
        });

        b_scissors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_playerChoice.setImageResource(R.drawable.scissors);
                play_turn ("scissors");
                tv_scores.setText("Computer : " + Integer.toString(pcScore) + "  Player : " + Integer.toString(playerScore));
            }
        });
    }

    public void play_turn(String player_choice)
    {
        String pc_choice = "";
        Random r = new Random();

        // choose 1,2ou3
        int pc_choice_number = r.nextInt(3)+1;
        if (pc_choice_number == 1) {
            pc_choice = "rock";
        }
        if (pc_choice_number == 2) {
            pc_choice = "paper";
        }
        if (pc_choice_number == 3) {
            pc_choice = "scissors";
        }

        //set the pc image based on his choice
        if (pc_choice == "rock") {
            iv_PcChoice.setImageResource(R.drawable.rock);
        } else
        if (pc_choice == "paper") {
            iv_PcChoice.setImageResource(R.drawable.paper);
        } else
        if (pc_choice == "scissors") {
            iv_PcChoice.setImageResource(R.drawable.scissors);
        }

        //pc player determine who win
        if (playerScore == 10 || pcScore == 10) {
            b_rock.setEnabled(false);
            b_paper.setEnabled(false);
            b_scissors.setEnabled(false);
            if (playerScore == 10) {
                Toast.makeText(RandomFightActivity.this, "YOU WIN !!!", Toast.LENGTH_SHORT).show();
            } else if (pcScore == 10) {
                Toast.makeText(RandomFightActivity.this, "YOU LOOSE !!!", Toast.LENGTH_SHORT).show();
            }
        } else if (pc_choice == player_choice) {
            Toast.makeText(RandomFightActivity.this, "Choose again please!", Toast.LENGTH_SHORT).show();
        } else if (player_choice == "rock" && pc_choice == "scissors") {
            playerScore++;
        } else if (player_choice == "rock" && pc_choice == "paper") {
            pcScore++;
        } else if (player_choice == "scissors" && pc_choice == "rock") {
            pcScore++;
        } else if (player_choice == "scissors" && pc_choice == "paper") {
            playerScore++;
        } else if (player_choice == "paper" && pc_choice == "rock") {
            playerScore++;
        } else if (player_choice == "paper" && pc_choice == "scissors") {
            pcScore++;
        }
    }
}


