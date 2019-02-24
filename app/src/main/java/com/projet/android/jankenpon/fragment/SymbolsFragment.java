package com.projet.android.jankenpon.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projet.android.jankenpon.R;

import java.util.HashMap;
import java.util.Map;

public class SymbolsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public SymbolsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        for(final Map.Entry<String, Integer> entry : symbols().entrySet()) {
            getView().findViewById(entry.getValue()).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateChoosenSymbol(entry.getKey());
                    ((OnFragmentInteractionListener) getActivity()).onFragmentInteraction(entry.getKey());
                }
            });
        }
    }

    public void updateChoosenSymbol(String playedSymbol) {
        for(Map.Entry<String, Integer> entry : symbols().entrySet()) {
            String symbol = entry.getKey();
            int id = entry.getValue();
            getView().findViewById(id).setAlpha(symbol == playedSymbol ? 1f : 0.5f);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_symbols, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateTimer(int secondsLeft) {
        TextView timer = getView().findViewById(R.id.secondsLeft);
        timer.setText(Integer.toString(secondsLeft));
    }

    public HashMap<String, Integer> symbols() {
        HashMap<String, Integer> symbols = new HashMap<>();
        symbols.put("rock", R.id.rockSym);
        symbols.put("paper", R.id.paperSym);
        symbols.put("scissors", R.id.scissorsSym);

        return symbols;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String symbol);
    }
}
