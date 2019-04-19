package com.example.keypermobile;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.net.Uri;
import android.widget.Toast;
import java.security.SecureRandom;

public class EditPasswordActivity extends AppCompatActivity implements IPasswordGenerator {

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

    User user;

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
                // Take you to website url and copy password to clipboard
                CopyPassword();
                Uri webPage = Uri.parse("http://" + editTextWebsite.getText().toString());
                Intent webIntent = new Intent(Intent.ACTION_VIEW,webPage);

                startActivity(webIntent);
            }
        });

        buttonDeletePassword = findViewById(R.id.buttonDeletePassword);
        buttonDeletePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete this site object for this user, when JSON IO is finished

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

                // pass the saved data to the home page to update the cards
                savePasswordIntent.putExtra("title", editTextTitle.getText().toString());
                savePasswordIntent.putExtra("website", editTextWebsite.getText().toString());
                startActivity(savePasswordIntent);

                // send saved data back to the database once JsonIo is done
            }
        });

        imageButtonGeneratePassword = findViewById(R.id.imageButtonGenerate);
        imageButtonGeneratePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // use default values for password generator to gen password
                String genPassword = generatePassword(DEFAULT_PASSWORD_LENGTH, ALL_CHARACTERS);
                editTextPassword.setText(genPassword);
            }
        });

        imageButtonCopy = findViewById(R.id.imageButtonCopy);
        imageButtonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CopyPassword();
            }
        });

        imageButtonAddTag = findViewById(R.id.imageButtonAddTag);
        imageButtonAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add tag to current user site when JSON IO completed
            }
        });
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

    // Copy password to the clipboard and notify the user
    public void CopyPassword()
    {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Password", editTextPassword.getText());
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(getApplicationContext(), "Password Copied", Toast.LENGTH_SHORT).show();
    }
}
