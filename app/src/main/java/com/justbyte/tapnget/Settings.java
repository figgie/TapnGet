package com.justbyte.tapnget;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Settings extends Fragment {

    View view;
    EditText userName,phone,email;
    Button save,updatePassword;

    public Settings(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        userName       = (EditText)view.findViewById(R.id.settings_userName);
        phone          = (EditText)view.findViewById(R.id.settings_phone);
        email          = (EditText)view.findViewById(R.id.settings_email);
        save           = (Button)view.findViewById(R.id.settings_save);
        updatePassword = (Button)view.findViewById(R.id.settings_password);

        userName.setText(getData(getString(R.string.myPrefUserName)));
        phone.setText(getData(getString(R.string.myPrefNumber)));
        email.setText(getData(getString(R.string.myPrefCollegeID)) + "@mnit.ac.in");

        email.setFocusable(false);
        save.setEnabled(false);
        save.setBackgroundColor(Color.GRAY);

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                save.setEnabled(true);
                save.setBackgroundColor(getResources().getColor(R.color.accent));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                save.setEnabled(true);
                save.setBackgroundColor(getResources().getColor(R.color.accent));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdate = updateData();

                if (isUpdate) {
                    saveData(getString(R.string.myPrefUserName), userName.getText().toString());
                    saveData(getString(R.string.myPrefNumber), phone.getText().toString());

                    Snackbar.make(view, "Updated!", Snackbar.LENGTH_SHORT).show();
                }
                else if (!isUpdate) {
                    userName.setText(getString(R.string.myPrefUserName));
                    phone.setText(getString(R.string.myPrefNumber));

                    Snackbar.make(view, "Unable to save. Try again later!", Snackbar.LENGTH_SHORT).show();

                }
                save.setEnabled(false);
                save.setBackgroundColor(Color.GRAY);

            }
        });

        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(view,"Clicked",Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

        return view;
    }


    private boolean updateData(){
        //Update the user's data on the database
        String newUserName = userName.getText().toString();
        String newPhone    = userName.getText().toString();

        String userPassword = getData(getString(R.string.myPrefPassword));

        //if not successful then return false
        return true;
    }

    private void saveData(String dataTitle, String dataValue){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.myPref), Context.MODE_APPEND);
        SharedPreferences.Editor edit = sharedPreferences.edit();

        edit.putString(dataTitle, dataValue);
        edit.apply();
    }


    private String getData(String dataTitle){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.myPref), Context.MODE_APPEND);
        return sharedPreferences.getString(dataTitle,"");
    }
}
