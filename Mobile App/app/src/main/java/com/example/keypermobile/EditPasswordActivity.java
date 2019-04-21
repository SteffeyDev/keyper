package com.example.keypermobile;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.net.Uri;
import android.widget.Toast;
import com.google.gson.Gson;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditPasswordActivity extends AppCompatActivity implements IPasswordGenerator {

    Toolbar toolbar;

    TextView textViewTitle;
    TextView textViewWebsite;
    TextView textViewUsername;
    TextView textViewPassword;
    TextView textViewTags;
    TextView textViewNotes;
    TextView textViewToolBarText;

    EditText editTextTitle;
    EditText editTextEmail;
    EditText editTextWebsite;
    EditText editTextUsername;
    EditText editTextCreateTags;
    EditText editTextNotes;

    TextInputLayout editTextPasswordLayout;
    TextInputEditText editTextPasswordField;

    Button buttonSignIn;
    Button buttonDeletePassword;

    ImageButton imageButtonClose;
    ImageButton imageButtonSavePassword;
    ImageButton imageButtonGeneratePassword;
    ImageButton imageButtonCopy;
    ImageButton imageButtonAddTag;

    User user = new User();

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        textViewToolBarText = findViewById(R.id.toolbar_text);
        getIntent().getExtras().getString("Activity Title");
            textViewToolBarText.setText(getIntent().getExtras().getString("Activity Title"));

        linearLayout = findViewById(R.id.linearLayoutId);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewWebsite = findViewById(R.id.textViewWebsite);
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewPassword = findViewById(R.id.textViewPassword);
        textViewTags = findViewById(R.id.textViewTags);
        textViewNotes = findViewById(R.id.textViewNotes);

        editTextPasswordLayout = findViewById(R.id.editTextPassword);

        // hide keyboard when clicked off editTextTitle
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextTitle.setText(getIntent().getExtras().getString("Title"));
        editTextTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        // hide keyboard when clicked off editTextWebsite
        editTextWebsite = findViewById(R.id.editTextWebsite);
        editTextWebsite.setText(getIntent().getExtras().getString("Website"));
        editTextWebsite.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                if (!isValidUrl(editTextWebsite.getText().toString()))
                    editTextWebsite.setError("Please enter a valid URL.");
                }
            }
        });

        // hide keyboard when clicked off editTextEmail
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                if (!isValidEmail(editTextEmail.getText().toString()))
                    editTextEmail.setError("Please enter a valid email address.");
                }
            }
        });

        // hide keyboard when clicked off editTextUsername
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        // hide keyboard when clicked off editTextPassword
        editTextPasswordField = findViewById(R.id.editTextPasswordField);
        editTextPasswordField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        // hide keyboard when clicked off editTextCreateTag
        editTextCreateTags = findViewById(R.id.editTextCreateTag);
        editTextCreateTags.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        // hide keyboard when clicked off editTextNotes
        editTextNotes = findViewById(R.id.editTextNotes);
        editTextNotes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

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
               Gson gson = new Gson();
               // call gson.toJson() to serialize to object before making post request

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
                editTextPasswordField.setText(genPassword);
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
                // user.sites.get(currentSite).tags.add(editTextCreateTags);
                if (!editTextCreateTags.getText().toString().isEmpty()) {
                    final Button buttonTag = new Button(linearLayout.getContext());
                    buttonTag.setText(editTextCreateTags.getText().toString());

                    // Set background and text color
                    int bgColor = getRandomColor();
                    int textColor = getContrastColor(bgColor);
                    buttonTag.setBackgroundColor(bgColor);
                    buttonTag.setTextColor(textColor);

                    // Add to button to the linear layout
                    linearLayout.addView(buttonTag);

                    // TODO: Fix to work for current user/site, get currentSite through intent from home page
                    // user.sites.get(currentSite).tags.add(buttonTag.getText());

                    // Remove tag on click
                    buttonTag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            linearLayout.removeView(buttonTag);
                            // TODO: Fix to work for current user/site, get currentSite through intent from home page
                            // user.sites.get(currentSite).tags.add(buttonTag.getText());
                        }
                    });
                }
                else
                    Toast.makeText(getApplicationContext(), "Enter text to add a tag", Toast.LENGTH_SHORT).show();
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
        ClipData clipData = ClipData.newPlainText("Password", editTextPasswordField.getText());
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(getApplicationContext(), "Password Copied", Toast.LENGTH_SHORT).show();
    }

    // Hide the keyboard, used when user clicks away from editing text
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(EditPasswordActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // Used to generate random colors for tag buttons
    public int getRandomColor(){
        SecureRandom random = new SecureRandom();
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    // used to determine text color for tag buttons
    public static int getContrastColor(int colorIntValue) {
        int red = Color.red(colorIntValue);
        int green = Color.green(colorIntValue);
        int blue = Color.blue(colorIntValue);
        double lum = (((0.299 * red) + ((0.587 * green) + (0.114 * blue))));
        return lum > 186 ? 0xFF000000 : 0xFFFFFFFF;
    }

    // Used to determine if a valid email has been entered in the email field
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    // Used to determine if a valid URL has been entered in the website field
    private boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }
}
