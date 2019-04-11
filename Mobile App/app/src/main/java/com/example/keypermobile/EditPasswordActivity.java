package com.example.keypermobile;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class EditPasswordActivity extends AppCompatActivity {

    Toolbar toolbar;

    TextView textViewTitle;
    TextView textViewWebsite;
    TextView textViewUsername;
    TextView textViewPassword;
    TextView textViewTags;
    TextView textViewNotes;

    EditText editTextTitle;
    EditText editTextWebsite;
    EditText editTextUsername;
    EditText editTextPassword;
    EditText editTextNotes;

    Button buttonSignIn;
    Button buttonDeletePassword;

    ImageButton imageButtonClose;
    ImageButton imageButtonSavePassword;
    ImageButton imageButtonGeneratePassword;
    ImageButton imageButtonCopy;
    ImageButton imageButtonAddTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewWebsite = findViewById(R.id.textViewWebsite);
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewPassword = findViewById(R.id.textViewPassword);
        textViewTags = findViewById(R.id.textViewTags);
        textViewNotes = findViewById(R.id.textViewNotes);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextWebsite = findViewById(R.id.editTextWebsite);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextNotes = findViewById(R.id.editTextNotes);

        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonDeletePassword = findViewById(R.id.buttonDeletePassword);
        buttonDeletePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageButtonClose = findViewById(R.id.imageButtonClose);
        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent closeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(closeIntent);
            }
        });

        imageButtonSavePassword = findViewById(R.id.imageButtonSavePassword);
        imageButtonSavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent savePasswordIntent = new Intent(getApplicationContext(), HomeActivity.class);

                // TODO: Add Tags to this intent
                savePasswordIntent.putExtra("title", editTextTitle.getText().toString());
                savePasswordIntent.putExtra("website", editTextWebsite.getText().toString());
                savePasswordIntent.putExtra("username", editTextUsername.getText().toString());
                savePasswordIntent.putExtra("password", editTextPassword.getText().toString());
                savePasswordIntent.putExtra("notes", editTextNotes.getText().toString());

                startActivity(savePasswordIntent);
            }
        });

        imageButtonGeneratePassword = findViewById(R.id.imageButtonGenerate);
        imageButtonGeneratePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageButtonCopy = findViewById(R.id.imageButtonCopy);
        imageButtonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Password", textViewPassword.getText());
                clipboardManager.setPrimaryClip(clipData);
            }
        });

        imageButtonAddTag = findViewById(R.id.imageButtonAddTag);
        imageButtonAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
