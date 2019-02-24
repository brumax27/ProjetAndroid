package com.projet.android.jankenpon.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projet.android.jankenpon.R;

import java.lang.reflect.Array;

public class SymbolsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public SymbolsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().findViewById(R.id.rockSym).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlpha(v, R.id.paperSym, R.id.scissorsSym);
                ((OnFragmentInteractionListener) getActivity()).onFragmentInteraction("rock");
            }
        });
        getView().findViewById(R.id.scissorsSym).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlpha(v, R.id.paperSym, R.id.rockSym);
                ((OnFragmentInteractionListener) getActivity()).onFragmentInteraction("scissors");
            }
        });
        getView().findViewById(R.id.paperSym).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlpha(v, R.id.rockSym, R.id.scissorsSym);
                ((OnFragmentInteractionListener) getActivity()).onFragmentInteraction("paper");
            }
        });
    }

    private void setAlpha(View v, int symbol1, int symbol2) {
        v.setAlpha(1f);
        getView().findViewById(symbol1).setAlpha(0.5f);
        getView().findViewById(symbol2).setAlpha(0.5f);
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
