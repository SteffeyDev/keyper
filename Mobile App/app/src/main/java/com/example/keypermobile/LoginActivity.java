package com.example.keypermobile;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.keypermobile.utils.JsonIO;
import com.loopj.android.http.*;

import static java.sql.DriverManager.println;

public class LoginActivity extends AppCompatActivity {

    private Button signUpButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUpButton = (Button) findViewById(R.id.create_account);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSignUpActivity();
            }
        });

        loginButton = (Button) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener(){
              @Override
              public void onClick (View view) {
                  EditText username = (EditText)findViewById(R.id.username);
                  EditText password = (EditText)findViewById(R.id.password);
                  JsonIO.sendPostRequest("http://13.59.202.229:5000/api/login", );
                  launchHomeActivity();
              }
    });
    }

    private void launchHomeActivity(){
        Intent intent;
        intent = new Intent(this, JsonIO.class);
        System.out.println("BLUE BLUE");
        startActivity(intent);
    }




    private void launchSignUpActivity(){

        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}
