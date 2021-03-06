package com.example.keypermobile;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.net.Uri;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.keypermobile.models.Site;
import com.example.keypermobile.utils.EncryptionUtils;
import com.example.keypermobile.utils.NetworkUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

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

    LinearLayout linearLayout;

    Site site;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("Site")) {
                site = new Site(getIntent().getExtras().getString("Site"));
            } else {
                site = new Site();
            }
        } else {
            site = new Site();
        }

        textViewToolBarText = findViewById(R.id.toolbar_text);
        getIntent().getExtras().getString("Activity Title");
        textViewToolBarText.setText(getIntent().getExtras().getString("Activity Title"));

        final EditPasswordActivity editPasswordActivity = this;

        setupUI(findViewById(R.id.parent));


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
        editTextTitle.setText(site.getTitle());

        // hide keyboard when clicked off editTextWebsite
        editTextWebsite = findViewById(R.id.editTextWebsite);
        editTextWebsite.setText(site.getUrl());
        editTextWebsite.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!isValidUrl(editTextWebsite.getText().toString()))
                        editTextWebsite.setError("Please enter a valid URL.");
                }
            }
        });

        // hide keyboard when clicked off editTextEmail
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextEmail.setText(site.getEmail());
        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!isValidEmail(editTextEmail.getText().toString()))
                        editTextEmail.setError("Please enter a valid email address.");
                }
            }
        });

        // hide keyboard when clicked off editTextUsername
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextUsername.setText(site.getUsername());

        // hide keyboard when clicked off editTextPassword
        editTextPasswordField = findViewById(R.id.editTextPasswordField);
        editTextPasswordField.setText(site.getPassword());

        // hide keyboard when clicked off editTextCreateTag
        editTextCreateTags = findViewById(R.id.editTextCreateTag);

        // hide keyboard when clicked off editTextNotes
        editTextNotes = findViewById(R.id.editTextNotes);
        editTextNotes.setText(site.getNotes().toString());

        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Take you to website url and copy password to clipboard
                CopyPassword();
                String url = editTextWebsite.getText().toString();
                if (!url.startsWith("http"))
                    url = "https://" + url;
                Uri webPage = Uri.parse(url);
                Intent webIntent = new Intent(Intent.ACTION_VIEW,webPage);

                startActivity(webIntent);
            }
        });

        buttonDeletePassword = findViewById(R.id.buttonDeletePassword);
        buttonDeletePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user confirmation before deleting site
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete Item")
                        .setMessage("Are you sure want to delete this site? This action cannot be undone.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                site.setTitle("");
                                site.setUrl("");
                                site.setEmail("");
                                site.setPassword("");
                                site.setUsername("");
                                site.setNotes("");

                                for (String tag : site.tags)
                                    site.tags.remove(tag);

                                final Intent closePasswordIntent = new Intent();

                                NetworkUtils.injectCookies(AndroidNetworking.delete(NetworkUtils.getApiUrl(editPasswordActivity) + "site/" + site.getId()),
                                        editPasswordActivity)
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                closePasswordIntent.putExtra("id", site.getId());
                                                closePasswordIntent.putExtra("site", site.toJson());
                                                setResult(Activity.RESULT_OK, closePasswordIntent);
                                                Toast.makeText(getApplicationContext(), "Password Deleted", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                Log.e("Keyper Networking Error", anError.getErrorDetail());
                                                (new AlertDialog.Builder(editPasswordActivity)).setTitle("Deletion failed").setMessage("Password could not be deleted").show();
                                            }
                                        });
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });

        imageButtonClose = findViewById(R.id.imageButtonClose);
        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent closeIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, closeIntent);
                finish();
            }
        });

        imageButtonSavePassword = findViewById(R.id.imageButtonSavePassword);
        imageButtonSavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update site from text fields
                site.setTitle(editTextTitle.getText().toString());
                site.setUrl(editTextWebsite.getText().toString());
                site.setEmail(editTextEmail.getText().toString());
                site.setPassword(editTextPasswordField.getText().toString());
                site.setUsername(editTextUsername.getText().toString());
                site.setNotes(editTextNotes.getText().toString());

                final Intent savePasswordIntent = new Intent();

                NetworkUtils.injectCookies(AndroidNetworking.post(NetworkUtils.getApiUrl(editPasswordActivity) + "site/" + site.getId())
                        .addByteBody(EncryptionUtils.EncryptSite(site, editPasswordActivity)), editPasswordActivity)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                savePasswordIntent.putExtra("id", site.getId());
                                savePasswordIntent.putExtra("site", site.toJson());
                                setResult(Activity.RESULT_OK, savePasswordIntent);
                                Toast.makeText(getApplicationContext(), "Password Updated", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.e("Keyper Networking Error", anError.getErrorDetail());
                                (new AlertDialog.Builder(editPasswordActivity)).setTitle("Invalid Password entry").setMessage("Please double checked you entered valid information").show();
                            }
                        });
            }
        });

        imageButtonGeneratePassword = findViewById(R.id.imageButtonGenerate);
        imageButtonGeneratePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // use default values for password generator to gen password
                final String genPassword = generatePassword(DEFAULT_PASSWORD_LENGTH, ALL_CHARACTERS);

                if (site.getPassword().length() > 0) {
                    new AlertDialog.Builder(editPasswordActivity)
                            .setTitle("Overwrite password")
                            .setMessage("Do you really want to overwrite the current password with a new, random one?")
                            .setIcon(android.R.drawable.ic_dialog_alert)

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    site.setPassword("");
                                    editTextPasswordField.setText(genPassword);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                } else {
                    editTextPasswordField.setText(genPassword);
                }
            }
        });

        imageButtonCopy = findViewById(R.id.imageButtonCopy);
        imageButtonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CopyPassword();
            }
        });

        // Add current tags to the view
        for (final String tag : site.tags)
        {
            final Button buttonTag = new Button(linearLayout.getContext());
            buttonTag.setText(tag);

            // Set background and text color
            int bgColor = getRandomColor();
            int textColor = getContrastColor(bgColor);
            buttonTag.setBackgroundColor(bgColor);
            buttonTag.setTextColor(textColor);

            // Add to button to the linear layout
            linearLayout.addView(buttonTag);

            // Remove tag on click
            buttonTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get user confirmation before deleting tag
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Remove Tag")
                            .setMessage("Are you sure want to remove the tag '" + tag + "'?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    linearLayout.removeView(buttonTag);
                                    site.tags.remove(tag);
                                    Toast.makeText(editPasswordActivity, "Tag Removed", Toast.LENGTH_SHORT).show();
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            });
        }

        imageButtonAddTag = findViewById(R.id.imageButtonAddTag);
        imageButtonAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editTextCreateTags.getText().toString().isEmpty()) {
                    final String tag = editTextCreateTags.getText().toString();
                    site.tags.add(editTextCreateTags.getText().toString());
                    final Button buttonTag = new Button(linearLayout.getContext());
                    buttonTag.setText(editTextCreateTags.getText().toString());
                    editTextCreateTags.setText("");

                    // Set background and text color
                    int bgColor = getRandomColor();
                    int textColor = getContrastColor(bgColor);
                    buttonTag.setBackgroundColor(bgColor);
                    buttonTag.setTextColor(textColor);

                    // Add to button to the linear layout
                    linearLayout.addView(buttonTag);

                    // Remove tag on click
                    buttonTag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        // Get user confirmation before deleting tag
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Remove Tag")
                                .setMessage("Are you sure want to remove the tag '" + tag + "'?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        linearLayout.removeView(buttonTag);
                                        site.tags.remove(tag);
                                        Toast.makeText(editPasswordActivity, "Tag Removed", Toast.LENGTH_SHORT).show();
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();
                        }
                    });
                }
                else
                    Toast.makeText(editPasswordActivity, "Enter text to add a tag", Toast.LENGTH_SHORT).show();
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
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(EditPasswordActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
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
