package com.justbyte.tapnget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.*;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Upload extends Fragment {

    View view;
    Button b;
    FloatingActionButton fabDevice, fabScan;
    private boolean FAB_Status = false;

    Animation show_fab_1;
    Animation hide_fab_1;
    Animation show_fab_2;
    Animation hide_fab_2;

    public Upload(){

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (this.isVisible()) {
            FAB_Status = false;
            if (!isVisibleToUser) {
                hideFAB();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload, container, false);

        b = (Button)view.findViewById(R.id.b);
        fabDevice = (FloatingActionButton)view.findViewById(R.id.upload_fab_device);
        fabScan = (FloatingActionButton)view.findViewById(R.id.upload_fab_scan);

        show_fab_1 = AnimationUtils.loadAnimation(getContext(), R.anim.b1_show);
        hide_fab_1 = AnimationUtils.loadAnimation(getContext(), R.anim.b1_hide);
        show_fab_2 = AnimationUtils.loadAnimation(getContext(), R.anim.b2_show);
        hide_fab_2 = AnimationUtils.loadAnimation(getContext(), R.anim.b2_hide);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            expandFAB();
            }
        });

        fabDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Document"),1);
            }
        });

        fabScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Scan", Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = null;
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            String uriString = uri.toString();
            File myFile= null;
            String encode = "";
            String displayName = "";

            try {
                InputStream is = getActivity().getContentResolver().openInputStream(uri);
                byte[] bytes = IOUtils.toByteArray(is);
                encode = Base64.encodeToString(bytes,Base64.DEFAULT);
                Log.e("e",encode);

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try{
                    cursor = getActivity().getContentResolver().query(uri,null,null,null,null);
                    if(cursor != null && cursor.moveToFirst()){
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                }finally {
                    cursor.close();
                }

            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
            }

            upload up = new upload(this);
            up.execute(encode,displayName);
            //new upload(encode, displayName);
            //Name of the file -> 'displayName' .... Encoded string is 'encode'
        }
    }
    class upload extends AsyncTask<String,Void,String>{

        String displayName,encode;

        Upload upload;
        public upload(Upload upload) {
            this.upload= upload;
        }


        @Override
        protected String doInBackground(String... params) {
            encode = params[0];
            displayName = params[1];
            String upload_url = "http://www.tapnget.co.in/upload.php";
            String line = "", response = null;
            String userCollegeID = getData(getString(R.string.myPrefCollegeID));
            try {
                URL url = new URL(upload_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data = URLEncoder.encode("college_id","UTF-8")+"="+URLEncoder.encode(userCollegeID,"UTF-8")+"&"+
                        URLEncoder.encode("displayname","UTF-8")+"="+URLEncoder.encode(displayName,"UTF-8")+"&"+
                        URLEncoder.encode("encodestring","UTF-8")+"="+URLEncoder.encode(encode,"UTF-8");

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
            Log.e("IV",response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

/**** HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
 httpURLConnection.setRequestMethod("POST");
 httpURLConnection.setDoOutput(true);
 httpURLConnection.setDoInput(true);
 httpURLConnection.setRequestProperty("Content-Type", "application/json");
 httpURLConnection.setRequestProperty("Accept", "application/json");
 httpURLConnection.setRequestProperty("charset", "utf-8");
 httpURLConnection.setUseCaches (false);
 OutputStream outputStream = httpURLConnection.getOutputStream();
 BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
 JSONObject jsonObject = new JSONObject();
 jsonObject.put("imageString", encode);
 jsonObject.put("imageName",displayName);
 jsonObject.put("college_id",userCollegeID);
 String data = jsonObject.toString();
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
****/
 // private String encodeFileToBase64Binary(File file)throws IOException {
        //int size = (int)file.length();


        /*String path = uri.getPath();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis;
        try {
            fis = ;
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] bbytes = baos.toByteArray();
            byte[] encoded = Base64.encode(bbytes,Base64.DEFAULT);
            String encodedString = new String(encoded);
            return encodedString;*/
   // }


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

    private void expandFAB() {

        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) fabDevice.getLayoutParams();
        layoutParams1.bottomMargin += (int) (fabDevice.getWidth() * 1.5);
        fabDevice.setLayoutParams(layoutParams1);
        fabDevice.startAnimation(show_fab_1);
        fabDevice.setClickable(true);

        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fabScan.getLayoutParams();
        layoutParams2.topMargin += (int) (fabScan.getWidth() * 1.5);
        fabScan.setLayoutParams(layoutParams2);
        fabScan.startAnimation(show_fab_2);
        fabScan.setClickable(true);

    }

    private void hideFAB() {

        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) fabDevice.getLayoutParams();
        layoutParams1.bottomMargin -= (int) (fabDevice.getWidth() * 1.5);
        fabDevice.setLayoutParams(layoutParams1);
        fabDevice.startAnimation(hide_fab_1);
        fabDevice.setClickable(false);

        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fabScan.getLayoutParams();
        layoutParams2.topMargin -= (int) (fabScan.getWidth() * 1.5);
        fabScan.setLayoutParams(layoutParams2);
        fabScan.startAnimation(hide_fab_2);
        fabScan.setClickable(false);


    }

}
