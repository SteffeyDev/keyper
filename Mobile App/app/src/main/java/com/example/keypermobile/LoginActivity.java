package com.example.keypermobile;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import static java.sql.DriverManager.println;

public class LoginActivity extends AppCompatActivity {

    private Button signUpButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AndroidNetworking.initialize(getApplicationContext());

        signUpButton = (Button) findViewById(R.id.create_account);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSignUpActivity();
            }
        });

        loginButton = (Button) findViewById(R.id.login_button);

        final LoginActivity loginActivity = this;

        loginButton.setOnClickListener(new View.OnClickListener(){
              @Override
              public void onClick (View view) {
                  String username = ((EditText) findViewById(R.id.username)).getText().toString();
                  String password = ((EditText)findViewById(R.id.password)).getText().toString();
                  AndroidNetworking.post("http://13.59.202.229:5000/api/login")
                      .addUrlEncodeFormBodyParameter("username", username)
                      .addUrlEncodeFormBodyParameter("password", password)
                      .build()
                      .getAsString(new StringRequestListener() {
                          @Override
                          public void onResponse(String response) {
                              launchHomeActivity();
                          }

                          @Override
                          public void onError(ANError anError) {
                              (new AlertDialog.Builder(loginActivity)).setTitle("Incorrect username or password").setMessage("Please double checked you entered your credentials correctly").show();
                          }
                      });
              }
        });
    }

    private void launchHomeActivity(){
        Intent intent;
        intent = new Intent(this, HomeActivity.class);
        System.out.println("BLUE BLUE");
        startActivity(intent);
    }




    private void launchSignUpActivity(){

        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}
