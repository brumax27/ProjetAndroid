package com.projet.android.jankenpon.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.github.silvestrpredko.dotprogressbar.DotProgressBarBuilder;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.projet.android.jankenpon.R;
import com.projet.android.jankenpon.fragment.OpponentFragment;
import com.projet.android.jankenpon.fragment.SymbolsFragment;
import com.projet.android.jankenpon.utils.Game;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class LoadingGameActivity extends AppCompatActivity implements SymbolsFragment.OnFragmentInteractionListener {

    private static final int RC_SELECT_PLAYERS = 9006;
    private static final int RC_INVITATION_INBOX = 9008;
    private static final String TAG = "GAME_TAG";
    private static final int MIN_PLAYERS = 2;

    private Activity thisActivity = this;
    private Room mRoom;
    boolean mPlaying = false;
    private RoomConfig mJoinedRoomConfig;
    private String mMyParticipantId;

    // RPS Game
    private String playedSymbol;
    private int secondsLeft;
    private String opponentId;
    private String opponentSymbol;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_game);

        new DotProgressBarBuilder(this)
                .setDotAmount(5)
                .setStartColor(Color.BLACK)
                .setAnimationDirection(DotProgressBar.LEFT_DIRECTION)
                .build();

        startQuickGame(0x0);
//        invitePlayers();
    }

    private void startQuickGame(long role) {
        // auto-match criteria to invite one random automatch opponent.
        // You can also specify more opponents (up to 3).
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(1, 1, role);

        // build the room config:
        RoomConfig roomConfig = RoomConfig.builder(mRoomUpdateCallback)
                .setOnMessageReceivedListener(mMessageReceivedHandler)
                .setRoomStatusUpdateCallback(mRoomStatusCallbackHandler)
                .setAutoMatchCriteria(autoMatchCriteria)
                .build();

        // prevent screen from sleeping during handshake
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Save the roomConfig so we can use it if we call leave().
        mJoinedRoomConfig = roomConfig;

        // create room:
        Games.getRealTimeMultiplayerClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .create(roomConfig);
    }

    private void invitePlayers() {
        // launch the player selection screen
        // minimum: 1 other player; maximum: 3 other players
        Games.getRealTimeMultiplayerClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .getSelectOpponentsIntent(1, 1, true)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_SELECT_PLAYERS);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SELECT_PLAYERS) {
            if (resultCode != Activity.RESULT_OK) {
                // Canceled or some other error.
                return;
            }

            // Get the invitee list.
            final ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

            // Get Automatch criteria.
            int minAutoPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
            int maxAutoPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);

            // Create the room configuration.
            RoomConfig.Builder roomBuilder = RoomConfig.builder(mRoomUpdateCallback)
                    .setOnMessageReceivedListener(mMessageReceivedHandler)
                    .setRoomStatusUpdateCallback(mRoomStatusCallbackHandler)
                    .addPlayersToInvite(invitees);
            if (minAutoPlayers > 0) {
                roomBuilder.setAutoMatchCriteria(
                        RoomConfig.createAutoMatchCriteria(minAutoPlayers, maxAutoPlayers, 0));
            }

            // Save the roomConfig so we can use it if we call leave().
            mJoinedRoomConfig = roomBuilder.build();
            Games.getRealTimeMultiplayerClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .create(mJoinedRoomConfig);
        }

        if (requestCode == RC_INVITATION_INBOX){
            if (resultCode != Activity.RESULT_OK) {
                // Canceled or some error.
                return;
            }
            Invitation invitation = data.getExtras().getParcelable(Multiplayer.EXTRA_INVITATION);
            if (invitation != null) {
                RoomConfig.Builder builder = RoomConfig.builder(mRoomUpdateCallback)
                        .setInvitationIdToAccept(invitation.getInvitationId());
                mJoinedRoomConfig = builder.build();
                Games.getRealTimeMultiplayerClient(thisActivity,GoogleSignIn.getLastSignedInAccount(this))
                        .join(mJoinedRoomConfig);
                // prevent screen from sleeping during handshake
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }

    }

    boolean shouldStartGame(Room room) {
        int connectedPlayers = 0;
        for (Participant p : room.getParticipants()) {
            if (p.isConnectedToRoom()) {
                ++connectedPlayers;
            }
        }
        return connectedPlayers >= MIN_PLAYERS;
    }

    boolean shouldCancelGame(Room room) {
        return false;
    }


    //HANDLE FOR MUTLIPLAYER

    private RoomUpdateCallback mRoomUpdateCallback = new RoomUpdateCallback() {
        @Override
        public void onRoomCreated(int code, @Nullable Room room) {
            // Update UI and internal state based on room updates.
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room " + room.getRoomId() + " created.");
            } else {
                Log.w(TAG, "Error creating room: " + code);
                // let screen go to sleep
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            }
        }

        @Override
        public void onJoinedRoom(int code, @Nullable Room room) {
            // Update UI and internal state based on room updates.
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room " + room.getRoomId() + " joined.");
            } else {
                Log.w(TAG, "Error joining room: " + code);
                // let screen go to sleep
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            }
        }

        @Override
        public void onLeftRoom(int code, @NonNull String roomId) {
            Log.d(TAG, "Left room" + roomId);
        }

        @Override
        public void onRoomConnected(int code, @Nullable Room room) {
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room " + room.getRoomId() + " connected.");
            } else {
                Log.w(TAG, "Error connecting to room: " + code);
                // let screen go to sleep
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                startGame();
            }
        }
    };

    private RoomStatusUpdateCallback mRoomStatusCallbackHandler = new RoomStatusUpdateCallback() {
        @Override
        public void onRoomConnecting(@Nullable Room room) {
            Log.i(TAG, "onRoomConnecting");
            // Update the UI status since we are in the process of connecting to a specific room.
        }

        @Override
        public void onRoomAutoMatching(@Nullable Room room) {
            Log.i(TAG, "onRoomAutoMatching");

            // Update the UI status since we are in the process of matching other players.
        }

        @Override
        public void onPeerInvitedToRoom(@Nullable Room room, @NonNull List<String> list) {
            Log.i(TAG, "onPeerInvitedToRoom");

            // Update the UI status since we are in the process of matching other players.
        }

        @Override
        public void onPeerDeclined(@Nullable Room room, @NonNull List<String> list) {
            Log.i(TAG, "onPeerDeclined");

            // Peer declined invitation, see if game should be canceled
            if (!mPlaying && shouldCancelGame(room)) {
                Games.getRealTimeMultiplayerClient(thisActivity,
                        GoogleSignIn.getLastSignedInAccount(thisActivity))
                        .leave(mJoinedRoomConfig, room.getRoomId());
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }

        @Override
        public void onPeerJoined(@Nullable Room room, @NonNull List<String> list) {
            Log.i(TAG, "onPeerJoined " + list.get(0));
            opponentId = list.get(0);

            // Update UI status indicating new players have joined!
        }

        @Override
        public void onPeerLeft(@Nullable Room room, @NonNull List<String> list) {
            Log.i(TAG, "onPeerLeft");

            // Peer left, see if game should be canceled.
            if (!mPlaying && shouldCancelGame(room)) {
                Games.getRealTimeMultiplayerClient(thisActivity,
                        GoogleSignIn.getLastSignedInAccount(thisActivity))
                        .leave(mJoinedRoomConfig, room.getRoomId());
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }

        @Override
        public void onConnectedToRoom(@Nullable Room room) {
            Log.i(TAG, "onConnectedToRoom");

            // Connected to room, record the room Id.
            mRoom = room;
            Games.getPlayersClient(thisActivity, GoogleSignIn.getLastSignedInAccount(thisActivity))
                    .getCurrentPlayerId().addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String playerId) {
                    mMyParticipantId = mRoom.getParticipantId(playerId);
                    Log.i(TAG, "onSuccess");

                }
            });
        }

        @Override
        public void onDisconnectedFromRoom(@Nullable Room room) {
            Log.i(TAG, "onDisconnectedFromRoom");

            // This usually happens due to a network error, leave the game.
            Games.getRealTimeMultiplayerClient(thisActivity, GoogleSignIn.getLastSignedInAccount(thisActivity))
                    .leave(mJoinedRoomConfig, room.getRoomId());
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            // show error message and return to main screen
            mRoom = null;
            mJoinedRoomConfig = null;
        }

        @Override
        public void onPeersConnected(@Nullable Room room, @NonNull List<String> list) {
            Log.i(TAG, "onPeersConnected: " + shouldStartGame(room));

            if (mPlaying) {
                // add new player to an ongoing game
            } else if (shouldStartGame(room)) {
                startGame();
            }
        }

        @Override
        public void onPeersDisconnected(@Nullable Room room, @NonNull List<String> list) {
            Log.i(TAG, "onPeersDisconnected");

            if (mPlaying) {
                // do game-specific handling of this -- remove player's avatar
                // from the screen, etc. If not enough players are left for
                // the game to go on, end the game and leave the room.
            } else if (shouldCancelGame(room)) {
                // cancel the game
                Games.getRealTimeMultiplayerClient(thisActivity,
                        GoogleSignIn.getLastSignedInAccount(thisActivity))
                        .leave(mJoinedRoomConfig, room.getRoomId());
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }

        @Override
        public void onP2PConnected(@NonNull String participantId) {
            Log.i(TAG, "onP2PConnected");

            // Update status due to new peer to peer connection.
        }

        @Override
        public void onP2PDisconnected(@NonNull String participantId) {
            Log.i(TAG, "onP2PDisconnected");

            // Update status due to  peer to peer connection being disconnected.
        }
    };

    HashSet<Integer> pendingMessageSet = new HashSet<>();

    synchronized void recordMessageToken(int tokenId) {
        pendingMessageSet.add(tokenId);
    }

    private void sendMessage(byte[] message) {
        Games.getRealTimeMultiplayerClient(this, GoogleSignIn.getLastSignedInAccount(this))
            .sendReliableMessage(message, mRoom.getRoomId(), opponentId,
                    handleMessageSentCallback).addOnCompleteListener(new OnCompleteListener<Integer>() {
                @Override
                public void onComplete(@NonNull Task<Integer> task) {
                    // Keep track of which messages are sent, if desired.
                    recordMessageToken(task.getResult());
                }
            });
    }

    private RealTimeMultiplayerClient.ReliableMessageSentCallback handleMessageSentCallback = new RealTimeMultiplayerClient.ReliableMessageSentCallback() {
        @Override
        public void onRealTimeMessageSent(int statusCode, int tokenId, String recipientId) {
            // handle the message being sent.
            synchronized (this) {
                pendingMessageSet.remove(tokenId);
            }
        }
    };

    private OnRealTimeMessageReceivedListener mMessageReceivedHandler = new OnRealTimeMessageReceivedListener() {
        @Override
        public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
            // Handle messages received here.
            byte[] message = realTimeMessage.getMessageData();
            try {
                opponentSymbol = new String(message, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // process message contents...
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "stop");
        Games.getRealTimeMultiplayerClient(thisActivity, GoogleSignIn.getLastSignedInAccount(this))
                .leave(mJoinedRoomConfig, mRoom.getRoomId());
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void startGame() {
        displayScreenGame();
        game = new Game();
        final Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                initTurn();
                playTurn(h);

                Log.i(TAG, "handler victories: " + game.victories + " defeats: " + game.defeats);
                if (game.finished()) {
                    Log.i(TAG, "RETURN");
                    return;
                }

                h.postDelayed(this, 10000);
            }
        });
    }

    private void playTurn(final Handler h) {
        h.postDelayed(
                new Runnable() {
                    public void run() {
                        if(secondsLeft <= 0) {
                            if (playedSymbol == null) {
                                playRandomSymbol();
                            }
                            displayEndOfTurn();
                            sendMessage(playedSymbol.getBytes());
                            h.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    String result = game.playTurn(playedSymbol, opponentSymbol);
                                    displayOpponentSymbol(result);
                                    Log.i(TAG, "Choosen symbol: " + playedSymbol);
                                }
                            }, 2000);
                            return;
                        }
                        gameTick();
                        h.postDelayed(this, 1000);
                    }
                },
                1000);
    }

    private void displayOpponentSymbol(String result) {
        OpponentFragment opponentFragment = OpponentFragment.newInstance(opponentSymbol, result);
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.opponentFragmentDestination, opponentFragment);
        fragmentTransaction.commit();
    }

    private void hideOppenentSymbol() {
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (allFragments != null) {
            for (Fragment fragment : allFragments) {
                if (fragment instanceof OpponentFragment) {
                    fragmentTransaction.remove(fragment);
                }
            }
        }
        fragmentTransaction.commit();
    }

    private void displayEndOfTurn() {
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        if (allFragments != null) {
            for (Fragment fragment : allFragments) {
                if (fragment instanceof SymbolsFragment) {
                    Log.i(TAG, "update symbols");
                    SymbolsFragment f1 = (SymbolsFragment) fragment;
                    f1.endOfTurn(playedSymbol);
                }
            }
        }
    }

    void gameTick() {
        Log.i(TAG, "tic: " + secondsLeft);
        if (secondsLeft > 0) {
            --secondsLeft;
        }

        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        if (allFragments != null) {
            for (Fragment fragment : allFragments) {
                if (fragment instanceof SymbolsFragment) {
                    SymbolsFragment f1 = (SymbolsFragment) fragment;
                    f1.updateTimer(secondsLeft);
                }
            }
        }
    }

    private void resetSymbol() {
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        if (allFragments != null) {
            for (Fragment fragment : allFragments) {
                if (fragment instanceof SymbolsFragment) {
                    SymbolsFragment f1 = (SymbolsFragment) fragment;
                    f1.reset();
                }
            }
        }
    }

    private void playRandomSymbol() {
        int random = new Random().nextInt(3);
        String[] symbols = { "rock", "paper", "scissors" };
        playedSymbol = symbols[random];
    }

    private void initTurn() {
        secondsLeft = 5;
        playedSymbol = null;
        resetSymbol();
        hideOppenentSymbol();
    }

    private void displayScreenGame() {
        hideLoadingScreen();
        displaySymbols();
    }

    private void hideLoadingScreen() {
        findViewById(R.id.dot_progress_bar).setVisibility(View.GONE);
        findViewById(R.id.loading_message).setVisibility(View.GONE);
    }

    private void displaySymbols() {
        SymbolsFragment symbolsFragment = new SymbolsFragment();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.symbolsFragmentDestination, symbolsFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(String symbol) {
        Log.i(TAG, "symbol: " + symbol);
        playedSymbol = symbol;
    }
}
