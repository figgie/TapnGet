package com.justbyte.tapnget;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
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

public class BackgroundTask extends AsyncTask<String,Void,String> {

    Context ctx;
    BackgroundTask(Context ctx){
        this.ctx=ctx;
    }
    String uname,pwd;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        String line="",response=null;
        String reg_url= "http://www.tapnget.co.in/register.php";
        String login_url= "http://www.tapnget.co.in/login.php";
        String method = params[0];
        if(method.equals("Register")){

            uname=params[1];
            String clgid=params[2];
            String num=params[3];
            String pass=params[4];

            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data = URLEncoder.encode("user_username","UTF-8")+"="+URLEncoder.encode(uname,"UTF-8")+"&"+
                        URLEncoder.encode("user_college_id","UTF-8")+"="+URLEncoder.encode(clgid,"UTF-8")+"&"+
                        URLEncoder.encode("user_number","UTF-8")+"="+URLEncoder.encode(num,"UTF-8")+"&"+
                        URLEncoder.encode("user_password","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS,"iso-8859-1"));
                while ((line = bufferedReader.readLine())!=null){
                    response+=line;
                }

                IS.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else if(method.equals("Login")){
            uname = params[1];
            pwd = params[2];

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
    protected void onProgressUpdate(Void... values)
    {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("nullRegistration Success")){
            Toast.makeText(ctx,"Registration Success...",Toast.LENGTH_LONG).show();
        }
        else if(result.equals("nullLogin Failed... Try again!")){
            Toast.makeText(ctx,"Login Failed Try again!",Toast.LENGTH_LONG).show();

        }

        else if(result.equals("nullLogin Success...")){
            Toast.makeText(ctx,"Login Success...",Toast.LENGTH_LONG).show();
            Intent i = new Intent(ctx, MainActivity.class);
            i.putExtra("1stmssg",uname);
            i.putExtra("2ndmssg",pwd);
            ctx.startActivity(i);

        }
        else if(result.equals("nullRegistration Failed. User Exists"))
            Toast.makeText(ctx,"Registration Failed. User Exists.\nPlease Try Again!",Toast.LENGTH_SHORT).show();
    }
}
