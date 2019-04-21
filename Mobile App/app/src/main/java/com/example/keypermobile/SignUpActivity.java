package com.example.keypermobile;

import android.content.Intent;
import android.content.SharedPreferences;
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

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.OkHttpClient;

public class SignUpActivity extends AppCompatActivity {

    private Button mBtGoBack;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        AndroidNetworking.initialize(getApplicationContext());

        signUpButton = (Button) findViewById(R.id.sign_up);

        final SignUpActivity signUpActivity = this;

        signUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {
                String username = ((EditText) findViewById(R.id.username)).getText().toString();
                String email = ((EditText) findViewById(R.id.email)).getText().toString();
                String password = ((EditText)findViewById(R.id.password)).getText().toString();
                String confirm = ((EditText)findViewById(R.id.confirm)).getText().toString();

                if (password.length() == 0 || email.length() == 0 || username.length() == 0) {
                    (new AlertDialog.Builder(signUpActivity)).setTitle("Incomplete Fields").setMessage("Please enter an email, username, and strong password").show();
                    return;
                }

                if (!password.equals(confirm)) {
                    (new AlertDialog.Builder(signUpActivity)).setTitle("Passwords don't match").setMessage("Please double check you spelled your password correctly").show();
                    return;
                }

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (!email.matches(emailPattern)) {
                    (new AlertDialog.Builder(signUpActivity)).setTitle("Invalid email address").setMessage("Please enter a valid email address").show();
                    return;
                }

                try {
                    MessageDigest md = MessageDigest.getInstance("SHA-512");
                    final String hash = String.valueOf(Hex.encodeHex(md.digest(password.getBytes())));

                    NetworkUtils.injectCookies(AndroidNetworking.post("http://192.168.1.182:5000/api/register")
                            .addUrlEncodeFormBodyParameter("username", username)
                            .addUrlEncodeFormBodyParameter("email", email)
                            .addUrlEncodeFormBodyParameter("password", hash.substring(0, 50)), signUpActivity)
                            .setContentType("application/x-www-form-urlencoded")
                            .build()
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String response) {
                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("keyper", 0);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("key", hash.substring(64));
                                    editor.commit();
                                    launchTwoFactorActivity(response);
                                }

                                @Override
                                public void onError(ANError anError) {
                                    (new AlertDialog.Builder(signUpActivity)).setTitle("Issue Signing Up").setMessage("Username already taken").show();
                                }
                            });
                } catch (NoSuchAlgorithmException e) {}
            }
        });
    }

    private void launchTwoFactorActivity(String totpUri) {
        Intent intent = new Intent(this, TwoFactorAuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        intent.putExtra("totpUri", totpUri);
        startActivity(intent);
    }
}
