package com.justbyte.tapnget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
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
    Context ctx;
    boolean check;
    String newUserName;
    String newPhone;
    String userPassword;
    String userCollegeID;
    String update_url = "http://www.learnapk.netai.net/update_data.php";
    String line="", response = null;

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
        newUserName = userName.getText().toString();
        newPhone = phone.getText().toString();
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
                boolean isUpdate = updateData();

                if (isUpdate) {
                    saveData(getString(R.string.myPrefUserName), userName.getText().toString());
                    saveData(getString(R.string.myPrefNumber), phone.getText().toString());

                    Snackbar.make(view, "Updated!", Snackbar.LENGTH_SHORT).show();
                } else if (!isUpdate) {
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

                Intent i = new Intent(ctx,UpdatePassword.class);
                startActivity(i);
                //Snackbar snackbar = Snackbar.make(view, "Clicked", Snackbar.LENGTH_SHORT);
                //snackbar.show();
            }
        });

        return view;
    }


    private boolean updateData() {
        //Update the user's data on the database
        //call class instance
        String method = "update data";
        UPDATEDATA updatedata = new UPDATEDATA(ctx);
        updatedata.execute(method);
        //if not successful then return false
        return check;
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

    class UPDATEDATA extends AsyncTask<String, Void, String> {

        Context c;

        public UPDATEDATA(Context ctx) {
            this.c = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
           String method = params[0];
           if(method.equals("update data")){

               try {
                   URL url = new URL(update_url);
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
           }
            return response;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("nullUPDATED\t")){
             check = true;
            }
            else if(s.equals("nullCould not Update\t")){
                check= false;
            }
        }
    }
}