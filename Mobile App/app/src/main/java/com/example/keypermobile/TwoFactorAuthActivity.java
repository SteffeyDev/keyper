package com.example.keypermobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.keypermobile.utils.NetworkUtils;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import okhttp3.OkHttpClient;

public class TwoFactorAuthActivity extends AppCompatActivity {

    String totpUri;
    private Button verifyButton, openButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.totpUri = getIntent().getStringExtra("totpUri");

        final TwoFactorAuthActivity twoFactorAuthActivity = this;

        if (this.totpUri != null) {
            setContentView(R.layout.activity_two_factor_auth_new);

            openButton = (Button) findViewById(R.id.open);

            openButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(totpUri));
                        startActivity(intent);
                    } catch (Exception e) {
                        (new AlertDialog.Builder(twoFactorAuthActivity))
                                .setTitle("No authenticator app installed")
                                .setMessage("Do you want to install one now?")
                                .setPositiveButton("Download Google Authenticator", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2"));
                                        startActivity(intent);
                                    }
                                })
                                .setCancelable(true)
                                .show();
                    }
                }
            });
        } else {
            setContentView(R.layout.activity_two_factor_auth);
        }

        verifyButton = (Button) findViewById(R.id.verify);

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = ((EditText)findViewById(R.id.token)).getText().toString();

                NetworkUtils.injectCookies(AndroidNetworking.post("http://192.168.1.182:5000/api/token")
                        .addUrlEncodeFormBodyParameter("token", token)
                        .setContentType("application/x-www-form-urlencoded"), twoFactorAuthActivity)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                launchHomeActivity();
                            }

                            @Override
                            public void onError(ANError anError) {
                                (new AlertDialog.Builder(twoFactorAuthActivity)).setTitle("Code Incorrect").setMessage("Please try again").show();
                            }
                        });
            }
        });
    }

    private void launchHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
