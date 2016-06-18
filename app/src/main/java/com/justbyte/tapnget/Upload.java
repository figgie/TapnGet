package com.justbyte.tapnget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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

                if (!FAB_Status) {
                    //Display FAB menu
                    expandFAB();
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    hideFAB();
                    FAB_Status = false;
                }
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

            try {
                Log.w("IV", uri.getPath());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String encodeFileToBase64 = null;
        String path = uri.getPath();
        try {
            encodeFileToBase64 = encodeFileToBase64Binary(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new upload(encodeFileToBase64,path);

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
    class upload extends AsyncTask<String,Void,String>{

       String encodeFileToBase64,path;
        public upload(String encodeFileToBase64, String path) {

            this.encodeFileToBase64=encodeFileToBase64;
            this.path=path;
        }

        @Override
        protected String doInBackground(String... params) {
            String upload_url = "http://www.tapnget.co.in/upload.php";
            String line = "", response = null;
            String userCollegeID = getData(getString(R.string.myPrefCollegeID));
            try {
                URL url = new URL(upload_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("charset", "utf-8");
                httpURLConnection.setUseCaches (false);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
               JSONObject jsonObject = new JSONObject();
                jsonObject.put("imageString", encodeFileToBase64);
                jsonObject.put("imageName", path);
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

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


    private String encodeFileToBase64Binary(Uri uri)throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis;
        try {
            fis = new FileInputStream(new File(uri.getPath()));
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
            return encodedString;
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
