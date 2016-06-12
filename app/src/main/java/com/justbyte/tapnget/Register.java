package com.justbyte.tapnget;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    EditText username,college_id,number,password;
    String   uname,clgid,num,pass;
    Button   register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username   = (EditText)findViewById(R.id.register_username);
        college_id = (EditText)findViewById(R.id.register_college_id);
        number     = (EditText)findViewById(R.id.register_number);
        password   = (EditText)findViewById(R.id.register_password);
        register = (Button)findViewById(R.id.register_button);

        TextInputLayout textInputLayout = (TextInputLayout)findViewById(R.id.register_tilPassword);
        textInputLayout.setTypeface(Typeface.DEFAULT);



        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        if(ab!=null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/paname.ttf");
        TextView welcome = (TextView)findViewById(R.id.register_welcomeText);
        welcome.setTypeface(myTypeface);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(username.getText().toString().trim().isEmpty() ||
                        college_id.getText().toString().trim().isEmpty() ||
                        number.getText().toString().trim().isEmpty() ||
                        password.getText().toString().trim().isEmpty())){

                    onClickRegisterButton(v);
                }
            }
        });


    }

    public void onClickRegisterButton(View view){

        uname = username.getText().toString();
        clgid = college_id.getText().toString() ;
        num   = number.getText().toString();
        pass  = password.getText().toString();

        String method="Register";

        if(clgid.toLowerCase().contains("@mnit.ac.in")){
            clgid = clgid.toLowerCase().replace("@mnit.ac.in","");
            clgid = clgid.toUpperCase();
        }

        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method,uname,clgid,num,pass);
        finish();
    }
}
