package com.example.keypermobile;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.keypermobile.utils.NetworkUtils;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;

import okhttp3.OkHttpClient;

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

                  try {
                      MessageDigest md = MessageDigest.getInstance("SHA-512");
                      final String hash = String.valueOf(Hex.encodeHex(md.digest(password.getBytes())));

                      NetworkUtils.injectCookies(AndroidNetworking.post("http://192.168.1.182:5000/api/login")
                              .addUrlEncodeFormBodyParameter("username", username)
                              .addUrlEncodeFormBodyParameter("password", hash.substring(0, 50))
                              .setContentType("application/x-www-form-urlencoded"), loginActivity)
                              .build()
                              .getAsJSONObject(new JSONObjectRequestListener() {
                                  @Override
                                  public void onResponse(JSONObject response) {
                                      SharedPreferences pref = getApplicationContext().getSharedPreferences("keyper", 0);
                                      SharedPreferences.Editor editor = pref.edit();
                                      editor.putString("key", hash.substring(64));
                                      editor.commit();
                                      launchTwoFactorActivity();
                                  }

                                  @Override
                                  public void onError(ANError anError) {
                                      Log.e("Keyper Networking Error", anError.getErrorDetail());
                                      (new AlertDialog.Builder(loginActivity)).setTitle("Incorrect username or password").setMessage("Please double checked you entered your credentials correctly").show();
                                  }
                              });
                  } catch (NoSuchAlgorithmException e) {

                  }
              }
        });
    }

    private void launchTwoFactorActivity(){
        Intent intent = new Intent(this, TwoFactorAuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
    }

    private void launchSignUpActivity(){
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
    }

}
