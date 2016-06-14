package com.justbyte.tapnget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Settings extends Fragment {

    View view;

    EditText userName, phone, email;
    Button save, updatePassword;


    String newUserName;
    String newPhone;
    String newPasswordString;

    String userPassword;
    String userCollegeID;


    public Settings() {

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

        userName = (EditText) view.findViewById(R.id.settings_userName);
        phone = (EditText) view.findViewById(R.id.settings_phone);
        email = (EditText) view.findViewById(R.id.settings_email);
        save = (Button) view.findViewById(R.id.settings_save);
        updatePassword = (Button) view.findViewById(R.id.settings_password);

        userName.setText(getData(getString(R.string.myPrefUserName)));
        phone.setText(getData(getString(R.string.myPrefNumber)));
        email.setText(getData(getString(R.string.myPrefCollegeID)) + "@mnit.ac.in");

        userPassword = getData(getString(R.string.myPrefPassword));
        userCollegeID = getData(getString(R.string.myPrefCollegeID));

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

                newUserName = userName.getText().toString();
                newPhone    = phone.getText().toString();

                UPDATEDATA updatedata =  new UPDATEDATA();
                updatedata.execute();

                save.setEnabled(false);
                save.setBackgroundColor(Color.GRAY);

            }
        });

        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View view = v;

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater1 = getActivity().getLayoutInflater();

                final View dialogView = inflater1.inflate(R.layout.password_dialog, null);

                builder.setView(dialogView);
                builder.setTitle("Update Password");
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText dialogCurrPassword = (EditText) dialogView.findViewById(R.id.update_currPassword);
                        EditText dialogNewPassword = (EditText) dialogView.findViewById(R.id.update_newPassword);
                        EditText dialogCNewPassword = (EditText) dialogView.findViewById(R.id.update_cNewPassword);

                        if (dialogNewPassword.getText().toString().equals(dialogCNewPassword.getText().toString())) {
                            if (dialogCurrPassword.getText().toString().equals(getData(getString(R.string.myPrefPassword)))) {
                                newPasswordString = dialogNewPassword.getText().toString();
                                new UPDATEPASSWORD().execute();
                            } else {
                                Snackbar.make(view, "Incorrect password entered. Try again!", Snackbar.LENGTH_LONG).show();
                            }
                        } else {
                            Snackbar.make(view, "Passwords don't match. Try again!", Snackbar.LENGTH_LONG).show();

                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return view;
    }


    private void saveData(String dataTitle, String dataValue) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.myPref), Context.MODE_APPEND);
        SharedPreferences.Editor edit = sharedPreferences.edit();

        edit.putString(dataTitle, dataValue);
        edit.apply();
    }

    private String getData(String dataTitle) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.myPref), Context.MODE_APPEND);
        return sharedPreferences.getString(dataTitle, "");
    }


    private void updateData(boolean check) {

        if (check) {
            saveData(getString(R.string.myPrefUserName), userName.getText().toString());
            saveData(getString(R.string.myPrefNumber), phone.getText().toString());

            Snackbar.make(view, "Details have been updated!", Snackbar.LENGTH_LONG).show();

        } else{
            userName.setText(getData(getString(R.string.myPrefUserName)));
            phone.setText(getData(getString(R.string.myPrefNumber)));

            Snackbar.make(view, "Unable to update details. Please try later!", Snackbar.LENGTH_LONG).show();
        }

    }

    private void updatePassword(boolean check){

        if(check){
            saveData(getString(R.string.myPrefPassword), newPasswordString);

            Snackbar.make(view, "Password has been updated!", Snackbar.LENGTH_LONG).show();
        } else{
            Snackbar.make(view, "Unable to update password. Please try later!", Snackbar.LENGTH_LONG).show();
        }

    }


    class UPDATEDATA extends AsyncTask<String, Void, String> {

        String line="", response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String updateData_url = "http://www.tapnget.co.in/update_data.php";

            try {
                   URL url = new URL(updateData_url);
                   HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                   httpURLConnection.setRequestMethod("POST");
                   httpURLConnection.setDoOutput(true);
                   httpURLConnection.setDoInput(true);
                   OutputStream outputStream = httpURLConnection.getOutputStream();
                   BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                   String data = URLEncoder.encode("user_user_name","UTF-8")+"="+URLEncoder.encode(userCollegeID,"UTF-8")+"&"+
                           URLEncoder.encode("user_user_password","UTF-8")+"="+URLEncoder.encode(userPassword,"UTF-8")+"&"+
                           URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(newUserName,"UTF-8")+"&"+
                           URLEncoder.encode("user_number","UTF-8")+"="+URLEncoder.encode(newPhone,"UTF-8");

                   bufferedWriter.write(data);
                   bufferedWriter.flush();
                   bufferedWriter.close();
                   outputStream.close();

                   InputStream IS = httpURLConnection.getInputStream();
                   BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS,"iso-8859-1"));
                   while ((line = bufferedReader.readLine())!=null){
                       response+=line;
                   }

                   bufferedReader.close();
                   IS.close();
                   httpURLConnection.disconnect();

               } catch (MalformedURLException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }

            return response;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            Boolean check = false;

            if (s.equals("nullUPDATED\t")){
                check = true;
            }
            else if(s.equals("nullCould not Update\t")){
                check= false;
            }

            updateData(check);
        }
    }

    class UPDATEPASSWORD extends AsyncTask<String, Void, String> {

        String line="", response = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String updatepwd_url = "http://www.tapnget.co.in/update_password.php";

            try {
                URL url = new URL(updatepwd_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data = URLEncoder.encode("user_user_name", "UTF-8") + "=" + URLEncoder.encode(userCollegeID, "UTF-8") + "&" +
                        URLEncoder.encode("user_user_password", "UTF-8") + "=" + URLEncoder.encode(newPasswordString, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }

                bufferedReader.close();
                IS.close();
                httpURLConnection.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {

            boolean check = false;

            if (s.equals("nullUPDATED")){
                check = true;
            }
            else if(s.equals("nullCould not Update")){
                check= false;
            }

            updatePassword(check);
        }
    }

}