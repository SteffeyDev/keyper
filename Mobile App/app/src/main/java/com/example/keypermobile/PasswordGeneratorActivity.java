package com.example.keypermobile;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
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

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

public class PasswordGeneratorActivity extends AppCompatActivity implements IPasswordGenerator{

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

    int passwordLength = DEFAULT_PASSWORD_LENGTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_password_generator);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        textViewGenerated = findViewById(R.id.textViewGenerated);
        textViewLength = findViewById(R.id.textViewLength);
        textViewGeneratedPassword = findViewById(R.id.textViewGeneratedPassword);

        navigationView = findViewById(R.id.nav_view_password_generator);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // highlight selected item
                menuItem.setChecked(true);

                //close drawer when item is selected
                drawerLayout.closeDrawers();

                return true;
            }
        });

        navigationViewMenu = navigationView.getMenu();

        switchNumbers = findViewById(R.id.switchNumbers);
        switchNumbers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textViewGeneratedPassword.setText(generatePassword(passwordLength, getCharacterSet()));
            }
        });

        switchLetters = findViewById(R.id.switchLetters);
        switchLetters.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textViewGeneratedPassword.setText(generatePassword(passwordLength, getCharacterSet()));
            }
        });

        switchSpecialCharacters = findViewById(R.id.switchSpecialCharacters);
        switchSpecialCharacters.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textViewGeneratedPassword.setText(generatePassword(passwordLength, getCharacterSet()));
            }
        });

        seekBarLength = findViewById(R.id.seekBarLength);
        seekBarLength.setMax((SEEK_BAR_MAX - SEEK_BAR_MIN)/1);
        seekBarLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Change the value for the generated password length, update generated password
                passwordLength = SEEK_BAR_MAX + (progress * SEEK_BAR_STEP);
                textViewGeneratedPassword.setText(generatePassword(passwordLength, getCharacterSet()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // only gen new password when seekbar moves
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // only gen new password when seekbar moves
            }
        });

        imageButtonCopyGenerated = findViewById(R.id.imageButtonCopyGenerated);
        imageButtonCopyGenerated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Generated Password", textViewGeneratedPassword.getText());
                clipboardManager.setPrimaryClip(clipData);
                // Using toast to notify user the text has been copied
                Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT);
            }
        });

        imageButtonOpenDrawer = findViewById(R.id.imageButtonOpenDrawer);
        imageButtonOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
           }
        });
    }

    // Menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Generate password using the user inputs in this activity
    @Override
    public String generatePassword(int length, String characterSet)
    {
        // Finish method by checking switch cases and choosing accordingly
        SecureRandom random = new SecureRandom();
        String generatedPassword = "";

        for (int i = 0; i < length; i++)
        {
            int randomInt = random.nextInt(length);
            generatedPassword.concat(Character.toString(characterSet.charAt(randomInt)));
        }
        return generatedPassword;
    }

    // return the characterSet corresponding to the checked switches
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
        else
            return SPECIAL_CHARACTERS;
    }

}
