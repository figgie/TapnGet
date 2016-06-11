package com.justbyte.tapnget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends AppCompatActivity {

    Button login;
    EditText username;
    EditText password;
    TextView signUp;
    String uname,pwd;
    ViewGroup viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);

        login      = (Button)findViewById(R.id.button);
        username   = (EditText)findViewById(R.id.login_id);
        password   = (EditText)findViewById(R.id.login_password);
        signUp     = (TextView)findViewById(R.id.login_singUp);

        TextInputLayout textInputLayout = (TextInputLayout)findViewById(R.id.login_tilPassword);


        signUp.setText(Html.fromHtml(getString(R.string.sign_up_text)));
        textInputLayout.setTypeface(Typeface.DEFAULT);


        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (!(networkInfo!=null && networkInfo.isConnected())) {
            signUp.setEnabled(false);
            login.setEnabled(false);

            Snackbar.make(viewGroup,"No Internet Access. Please try again",Snackbar.LENGTH_SHORT).show();
        }

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(v);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });

    }

    public void register (View view){
        Intent i = new Intent(this,registerTest.class);
        startActivity(i);
    }

    public void login(View view){
        uname = username.getText().toString();
        pwd = password.getText().toString();

        if(uname.isEmpty() || pwd.isEmpty()){
            Snackbar.make(viewGroup, "Please enter credentials", Snackbar.LENGTH_SHORT).show();
        }
        else {
            String method = "Login";

            BackgroundTask backgroundtask = new BackgroundTask(this);
            backgroundtask.execute(method, uname, pwd);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
