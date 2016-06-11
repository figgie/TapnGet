package com.justbyte.tapnget;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Balance extends Fragment {

    View view;

    public Balance(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_balance, container, false);

        TextView balance = (TextView)view.findViewById(R.id.balance_credits);
        balance.setText(getData(getString(R.string.myPrefCredit)));
        return view;
    }

    private String getData(String dataTitle){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.myPref), Context.MODE_APPEND);
        return sharedPreferences.getString(dataTitle,"");
    }
}
