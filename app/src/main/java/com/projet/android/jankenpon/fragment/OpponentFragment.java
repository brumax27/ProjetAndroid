package com.projet.android.jankenpon.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.projet.android.jankenpon.R;

import java.util.HashMap;

public class OpponentFragment extends Fragment {
    String mPlayedSymbol;
    String mResult;

    public OpponentFragment() {
        // Required empty public constructor
    }

    static public OpponentFragment newInstance(String playedSymbol, String result) {
        OpponentFragment f = new OpponentFragment();
        Bundle args = new Bundle();
        args.putString("playedSymbol", playedSymbol);
        args.putString("result", result);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_opponent, container, false);
        TextView tv = v.findViewById(R.id.resultTurn);
        tv.setText(mResult);
        ImageView iv = v.findViewById(R.id.opponentSymbol);
        iv.setImageResource(symbols().get(mPlayedSymbol));
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mPlayedSymbol = args.getString("playedSymbol", mPlayedSymbol);
            mResult = args.getString("result", mResult);
        }
    }

    private HashMap<String, Integer> symbols() {
        HashMap<String, Integer> symbols = new HashMap<>();
        symbols.put("rock", R.drawable.rock);
        symbols.put("paper", R.drawable.paper);
        symbols.put("scissors", R.drawable.scissors);

        return symbols;
    }
}
