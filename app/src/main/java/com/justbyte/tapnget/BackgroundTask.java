package com.justbyte.tapnget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
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

public class BackgroundTask extends AsyncTask<String,Void,String> {

    Context ctx;
    BackgroundTask(Context ctx){
        this.ctx=ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        String reg_url= "http://www.learnapk.netai.net/register.php";
        String login_url= "http://www.learnapk.netai.net/login.php";
        String method = params[0];
        if(method.equals("Register")){

            String fname=params[1];
            String uname=params[2];
            String clgid=params[3];
            String num=params[4];
            String pass=params[5];

            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data = URLEncoder.encode("user_full_name","UTF-8")+"="+URLEncoder.encode(fname,"UTF-8")+"&"+
                        URLEncoder.encode("user_username","UTF-8")+"="+URLEncoder.encode(uname,"UTF-8")+"&"+
                        URLEncoder.encode("user_college_id","UTF-8")+"="+URLEncoder.encode(clgid,"UTF-8")+"&"+
                        URLEncoder.encode("user_number","UTF-8")+"="+URLEncoder.encode(num,"UTF-8")+"&"+
                        URLEncoder.encode("user_password","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS,"iso-8859-1"));
                String line;
                line = bufferedReader.readLine();
                IS.close();

                return line;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else if(method.equals("Login")){
            String uname = params[1];
            String pwd = params[2];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data = URLEncoder.encode("user_user_name","UTF-8")+"="+URLEncoder.encode(uname,"UTF-8")+"&"+
                        URLEncoder.encode("user_user_password","UTF-8")+"="+URLEncoder.encode(pwd,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS,"iso-8859-1"));
                String line;
                line = bufferedReader.readLine();
                bufferedReader.close();
                IS.close();
                httpURLConnection.disconnect();
                return line;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values)
    {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("<h3>Database Connection Success... </h3>Registration Success")){
            Toast.makeText(ctx,"Registration Success",Toast.LENGTH_LONG).show();
        }
        else if(result.equals("<h3>Database Connection Success... </h3>Login Failed... Try again!")){
            Toast.makeText(ctx,"Login Failed... Try again!",Toast.LENGTH_LONG).show();
        }

        else if(result.equals("<h3>Database Connection Success... </h3>Account Exists")){
            Toast.makeText(ctx,"Account Exists. Please try again!",Toast.LENGTH_LONG).show();
        }

        else {
            Toast.makeText(ctx,"Login Success...",Toast.LENGTH_LONG).show();
            Intent i = new Intent(ctx, MainActivity.class);
            ctx.startActivity(i);
        }
    }
}
