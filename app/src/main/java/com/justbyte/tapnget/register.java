package com.justbyte.tapnget;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class register extends AppCompatActivity {

    EditText fullname,username,college_id,number,password;
    String   fname,uname,clgid,num,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullname   = (EditText)findViewById(R.id.register_fullname);
        username   = (EditText)findViewById(R.id.register_username);
        college_id = (EditText)findViewById(R.id.register_college_id);
        number     = (EditText)findViewById(R.id.register_number);
        password   = (EditText)findViewById(R.id.register_password);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


    }

    public void onclickregisterbutton(View view){

        fname = fullname.getText().toString();
        uname = username.getText().toString();
        clgid = college_id.getText().toString();
        num   = number.getText().toString();
        pass  = password.getText().toString();

        String method="Register";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method,fname,uname,clgid,num,pass);
        finish();
    }
}
