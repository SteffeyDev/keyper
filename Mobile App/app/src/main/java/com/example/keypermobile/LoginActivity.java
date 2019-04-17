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

import com.loopj.android.http.*;

public class LoginActivity extends AppCompatActivity {

    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUp = (Button) findViewById(R.id.create_account);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSignUpActivity();
            }
        });


    }

    private void launchSignUpActivity(){

        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}
