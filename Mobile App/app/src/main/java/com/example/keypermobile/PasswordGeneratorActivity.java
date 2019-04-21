package com.example.keypermobile;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.SeekBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.keypermobile.utils.NetworkUtils;

import org.json.JSONArray;

import java.security.SecureRandom;

public class PasswordGeneratorActivity extends AppCompatActivity implements IPasswordGenerator {

    DrawerLayout drawerLayout;

    NavigationView navigationView;

    Menu navigationViewMenu;

    Toolbar toolbar;

    TextView textViewGenerated;
    TextView textViewGeneratedPassword;
    TextView textViewLength;

    Switch switchNumbers;
    Switch switchLetters;
    Switch switchSpecialCharacters;

    SeekBar seekBarLength;

    ImageButton imageButtonCopyGenerated;
    ImageButton imageButtonOpenDrawer;
    ImageButton imageButtonRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_password_generator);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        textViewGenerated = findViewById(R.id.textViewGenerated);
        textViewLength = findViewById(R.id.textViewLength);
        textViewGeneratedPassword = findViewById(R.id.textViewGeneratedPassword);

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // highlight selected item
                menuItem.setChecked(true);

                if (menuItem.getItemId() == R.id.item_home)
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                else if (menuItem.getItemId() == R.id.item_tags)
                    // Might change, unsure where to send on tags
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                else if (menuItem.getItemId() == R.id.item_log_out) {
                    logout();
                }
                // User is in password generator, so no need to check item_password_generator

                //close drawer when item is selected
                drawerLayout.closeDrawers();

                navigationView.setCheckedItem(R.id.menu_none);

                return true;
            }
        });

        navigationViewMenu = navigationView.getMenu();

        switchNumbers = findViewById(R.id.switchNumbers);
        switchNumbers.setChecked(true);
        switchNumbers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        textViewGeneratedPassword.setText(generatePassword(seekBarLength.getProgress(), getCharacterSet()));
            }
        });

        switchLetters = findViewById(R.id.switchLetters);
        switchLetters.setChecked(true);
        switchLetters.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textViewGeneratedPassword.setText(generatePassword(seekBarLength.getProgress(), getCharacterSet()));
            }
        });

        switchSpecialCharacters = findViewById(R.id.switchSpecialCharacters);
        switchSpecialCharacters.setChecked(true);
        switchSpecialCharacters.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textViewGeneratedPassword.setText(generatePassword(seekBarLength.getProgress(), getCharacterSet()));
            }
        });

        // Seekbar current length textView
        final TextView textViewProgress = findViewById(R.id.progress);
        textViewProgress.setVisibility(View.GONE);

        seekBarLength = findViewById(R.id.seekBarLength);
        seekBarLength.setMax(SEEK_BAR_MAX);
        seekBarLength.setMin(SEEK_BAR_MIN);
        seekBarLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update generated password
                textViewGeneratedPassword.setText(generatePassword(seekBarLength.getProgress(), getCharacterSet()));
                // Getting the position to set the progress text
                int position = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                textViewProgress.setText("" + progress);
                textViewProgress.setX((seekBar.getX() + position + seekBar.getThumbOffset() / 2) - 40);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Set the current number to be visible when touched
                textViewProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Set the  current number to be invisible when released
                textViewProgress.setVisibility(View.GONE);
            }
        });

        imageButtonCopyGenerated = findViewById(R.id.imageButtonCopyGenerated);
        imageButtonCopyGenerated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CopyPassword();
            }
        });

        imageButtonOpenDrawer = findViewById(R.id.imageButtonOpenDrawer);
        imageButtonOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
           }
        });

        imageButtonRefresh = findViewById(R.id.imageButtonRefresh);
        imageButtonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Refresh the generated password
                textViewGeneratedPassword.setText(generatePassword(seekBarLength.getProgress(), getCharacterSet()));
            }
        });

        // Generate password
        textViewGeneratedPassword.setText(generatePassword(seekBarLength.getProgress(), getCharacterSet()));
    }

    // Generate password using the user inputs in this activity
    @Override
    public String generatePassword(int length, String characterSet)
    {
        SecureRandom random = new SecureRandom();
        StringBuilder generatedPassword = new StringBuilder(length);

        for (int i = 0; i < length; i++)
        {
            int randomIndex = random.nextInt(characterSet.length());
            generatedPassword.append(characterSet.charAt(randomIndex));
        }
        return generatedPassword.toString();
    }

    // Return the characterSet corresponding to the checked switches
    public String getCharacterSet()
    {
        if (switchLetters.isChecked() && switchNumbers.isChecked() && switchSpecialCharacters.isChecked())
            return ALL_CHARACTERS;
        else if (switchLetters.isChecked() && switchNumbers.isChecked())
            return NUMBERS_LETTERS;
        else if (switchLetters.isChecked() && switchSpecialCharacters.isChecked())
            return LETTERS_SPECIAL_CHARACTERS;
        else if (switchNumbers.isChecked() &&switchSpecialCharacters.isChecked())
            return NUMBERS_SPECIAL_CHARACTERS;
        else if (switchLetters.isChecked())
            return LETTERS;
        else if (switchNumbers.isChecked())
            return NUMBERS;
        else if (switchSpecialCharacters.isChecked())
            return SPECIAL_CHARACTERS;
        // No switches are checked
        else
        {
            switchLetters.setChecked(true);
            // notify user at least one character set must be enabled
            Toast.makeText(getApplicationContext(), "One character set must be enabled", Toast.LENGTH_SHORT).show();
            return LETTERS;
        }
    }

    // Copy password to the clipboard and notify the user
    public void CopyPassword()
    {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Generated Password", textViewGeneratedPassword.getText());
        clipboardManager.setPrimaryClip(clipData);
        // Using toast to notify user the text has been copied
        Toast.makeText(getApplicationContext(), "Password Copied", Toast.LENGTH_SHORT).show();
    }

    public void logout()
    {
        NetworkUtils.injectCookies(AndroidNetworking.get("http://192.168.1.182:5000/api/logout"), getApplicationContext())
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(loginIntent);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(loginIntent);
                    }
                });
    }
}
