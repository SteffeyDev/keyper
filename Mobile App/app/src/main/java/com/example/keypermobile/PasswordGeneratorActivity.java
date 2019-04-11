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

public class PasswordGeneratorActivity extends AppCompatActivity {

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

            }
        });

        switchLetters = findViewById(R.id.switchLetters);
        switchLetters.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        switchSpecialCharacters = findViewById(R.id.switchSpecialCharacters);
        switchSpecialCharacters.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        seekBarLength = findViewById(R.id.seekBarLength);
        seekBarLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        imageButtonCopyGenerated = findViewById(R.id.imageButtonCopyGenerated);
        imageButtonCopyGenerated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Generated Password", textViewGeneratedPassword.getText());
                clipboardManager.setPrimaryClip(clipData);
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
}
