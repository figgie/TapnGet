package com.justbyte.tapnget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class UpdatePassword extends AppCompatActivity {

    EditText update_password;
    EditText confirm_update_password;
    String changed_password, updatepwd_url = "http://learnapk.netai.net/update_password.php";
    Context ctx;
    String userCollegeID, line="", response= null;
    String upd,cupd;
    View v;
    boolean check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        ctx = getApplicationContext();
        Log.e("V","V");

        update_password = (EditText)findViewById(R.id.new_passsword);
        confirm_update_password = (EditText)findViewById(R.id.comfirm_new_password);

        Button button = (Button)findViewById(R.id.new_password_button);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upd = update_password.getText().toString();
                cupd = confirm_update_password.getText().toString();

                if (!upd.equals(cupd)) {
                    Toast.makeText(ctx, "Passwords Dont Match", Toast.LENGTH_LONG).show();
                } else {
                    changed_password = cupd;
                    userCollegeID = getData(getString(R.string.myPrefCollegeID));

                    new UPDATEPASSWORD().execute();

                }
            }
        });
    }


    public void saveData(String dataTitle, String dataValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.myPref), Context.MODE_APPEND);
        SharedPreferences.Editor edit = sharedPreferences.edit();

        edit.putString(dataTitle, dataValue);
        edit.apply();
    }


    public String getData(String dataTitle) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.myPref), Context.MODE_APPEND);
        return sharedPreferences.getString(dataTitle, "");
    }


    class UPDATEPASSWORD extends AsyncTask<String, Void, String> {

         Context c;

         @Override
         protected void onPreExecute() {
             super.onPreExecute();
         }

         @Override
         protected String doInBackground(String... params) {

             try {
                 URL url = new URL(updatepwd_url);
                 HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                 httpURLConnection.setRequestMethod("POST");
                 httpURLConnection.setDoOutput(true);
                 httpURLConnection.setDoInput(true);
                 OutputStream outputStream = httpURLConnection.getOutputStream();
                 BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                 String data = URLEncoder.encode("user_user_name", "UTF-8") + "=" + URLEncoder.encode(userCollegeID, "UTF-8") + "&" +
                         URLEncoder.encode("user_user_password", "UTF-8") + "=" + URLEncoder.encode(changed_password, "UTF-8");

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
             if (s.equals("nullUPDATED<!-- Hosting24 Analytics Code --><script type=\"text/javascript\" src=\"http://stats.hosting24.com/count.php\"></script><!-- End Of Analytics Code -->")){
                 check = true;
                 Snackbar.make(v,"Password Updated!",Snackbar.LENGTH_SHORT);
                 saveData(getString(R.string.myPrefPassword), changed_password);
             }
             else if(s.equals("nullCould not Update<!-- Hosting24 Analytics Code --><script type=\"text/javascript\" src=\"http://stats.hosting24.com/count.php\"></script><!-- End Of Analytics Code -->")){
                 check= false;
                 Toast.makeText(ctx, "Could not update.Try again!", Toast.LENGTH_LONG).show();
             }
         }
     }

}
