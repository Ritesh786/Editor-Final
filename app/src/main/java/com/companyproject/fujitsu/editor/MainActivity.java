package com.companyproject.fujitsu.editor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button mloginnbtn,mloginviaotp;
    EditText musernamme,mpassword;

    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new UserSessionManager(getApplicationContext());

        musernamme = (EditText) findViewById(R.id.login_username);
        mpassword = (EditText) findViewById(R.id.login_password);

        mloginnbtn = (Button) findViewById(R.id.btn_login);
        mloginviaotp =  (Button) findViewById(R.id.btn_viaotp);
        mloginviaotp.setOnClickListener(this);
        mloginnbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btn_viaotp){

            Intent registerintent = new Intent(getApplicationContext(), loginvotp.class);
            startActivity(registerintent);
            finish();

        }



    }



}
