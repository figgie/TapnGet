package com.justbyte.tapnget;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


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
import java.util.ArrayList;
import java.util.List;

import com.gigamole.library.navigationtabstrip.NavigationTabStrip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private NavigationTabStrip nts;
    String mssguser,mssgpwd,username,college_id,number,password,credit;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nts = (NavigationTabStrip)findViewById(R.id.nts_center);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        nts.setViewPager(viewPager);

        Bundle data = getIntent().getExtras();

        mssguser = data.getString("1stmssg");
        mssgpwd = data.getString("2ndmssg");


        new GetUserData().execute();
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Upload(), "Upload");
        adapter.addFragment(new Balance(), "Balance");
        adapter.addFragment(new Settings(), "Settings");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
            // to display icons return null;
        }
    }

    class GetUserData extends AsyncTask<String,Void,String> {

        String json_url;
        String myJSON;

        JSONArray data = new JSONArray();

        @Override
        protected void onPreExecute() {
            json_url="http://www.tapnget.co.in/jsonretrieve.php";
        }

        @Override
        protected String doInBackground(String... params) {

            String line="", response = null;
            try {
                URL url = new URL(json_url);

                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data = URLEncoder.encode("user_user_name", "UTF-8")+"="+URLEncoder.encode(mssguser,"UTF-8")+"&"+
                        URLEncoder.encode("user_user_password","UTF-8")+"="+URLEncoder.encode(mssgpwd,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

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

            if(response!=null) {
                response = response.substring(4);
            }
            return response;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            myJSON = s;

            try{
                JSONObject jsonObj = new JSONObject(myJSON);
                data = jsonObj.getJSONArray("server_response");

                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonObject = data.getJSONObject(i);

                    username = jsonObject.getString("username");
                    college_id = jsonObject.getString("college_id");
                    number = jsonObject.getString("number");
                    password = jsonObject.getString("password");
                    credit = jsonObject.getString("balance");

                    saveData(getString(R.string.myPrefUserName),username);
                    saveData(getString(R.string.myPrefCollegeID),college_id);
                    saveData(getString(R.string.myPrefNumber),number);
                    saveData(getString(R.string.myPrefCredit), credit);
                    saveData(getString(R.string.myPrefPassword), password);

                    Log.e("N",number);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveData(String dataTitle, String dataValue){
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.myPref), Context.MODE_APPEND);
        SharedPreferences.Editor edit = sharedPreferences.edit();

        edit.putString(dataTitle, dataValue);
        edit.apply();
    }

    private String getData(String dataTitle){
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.myPref), Context.MODE_APPEND);
        return sharedPreferences.getString(dataTitle,"");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
