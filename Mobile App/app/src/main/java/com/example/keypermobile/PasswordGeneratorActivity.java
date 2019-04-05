package com.example.keypermobile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.SeekBar;

public class PasswordGeneratorActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView textViewGenerated;
    TextView textViewLength;
    Switch switchNumbers;
    Switch switchLetters;
    Switch switchSpecialCharacters;
    SeekBar seekBarLength;


    // Event Handlers
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_generator);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewGenerated = findViewById(R.id.textViewGenerated);
        textViewLength = findViewById(R.id.textViewLength);

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
    }
}
