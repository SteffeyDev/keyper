package com.example.keypermobile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.keypermobile.models.Site;
import com.example.keypermobile.utils.EncryptionUtils;
import com.example.keypermobile.utils.NetworkUtils;
import com.example.keypermobile.utils.PasswordAdapter;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements PasswordAdapter.OnPasswordClickListener{
    Toolbar toolbar;
    RecyclerView recyclerView;
    PasswordAdapter adapter;

    DrawerLayout drawerLayout;

    NavigationView navigationView;

    ImageButton imageButtonOpenDrawer;

    Menu navigationViewMenu;

    List<Site> passwordList;

    FloatingActionButton floatingActionButtonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_home);

        AndroidNetworking.initialize(getApplicationContext());
        getSites();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        floatingActionButtonAdd = findViewById(R.id.floatingActionButtonAdd);
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addPasswordIntent = new Intent(getApplicationContext(), EditPasswordActivity.class);
                addPasswordIntent.putExtra("Activity Title", getResources().getString(R.string.title_activity_add_password));
                startActivity(addPasswordIntent);
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // highlight selected item
                menuItem.setChecked(true);

                if (menuItem.getItemId() == R.id.item_password_generator)
                    startActivity(new Intent(getApplicationContext(), PasswordGeneratorActivity.class));
                else if (menuItem.getItemId() == R.id.item_tags)
                    // Might change, unsure where to send on tags
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                else if (menuItem.getItemId() == R.id.item_log_out)
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                // User is in home, so no need to check item_home

                //close drawer when item is selected
                drawerLayout.closeDrawers();

                navigationView.setCheckedItem(R.id.menu_none);

                return true;
            }
        });

        navigationViewMenu = navigationView.getMenu();

        imageButtonOpenDrawer = findViewById(R.id.imageButtonOpenDrawer);
        imageButtonOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        passwordList = new ArrayList<Site>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        passwordList.add(
//                new Password(1,"Google Account","www.google.com", R.drawable.logo_google)
//        );
//        passwordList.add(
//                new Password(2,"NOT Google Account","www.notgoogle.com", R.drawable.logo_google)
//        );
//        passwordList.add(
//                new Password(3,"Goggles Account","www.goggles.com", R.drawable.logo_google)
//        );
//        passwordList.add(
//                new Password(4,"A Account","www.alabama.gov", R.drawable.logo_google)
//        );
//        passwordList.add(
//                new Password(5,"B Account","www.bet.com", R.drawable.logo_google)
//        );
//        passwordList.add(
//                new Password(6,"C Account","www.ComedyCentral.com", R.drawable.logo_google)
//        );
//        passwordList.add(
//                new Password(7,"Desmos","www.Desmos.com", R.drawable.logo_google)
//        );
        adapter = new PasswordAdapter(this, passwordList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPasswordClick ( int position){
        passwordList.get(position);
        Intent editPasswordIntent = new Intent(getApplicationContext(), EditPasswordActivity.class);
        editPasswordIntent.putExtra("Title", passwordList.get(position).getTitle());
        editPasswordIntent.putExtra("Website", passwordList.get(position).getUrl());
        editPasswordIntent.putExtra("Activity Title", getResources().getString(R.string.title_activity_edit_password));
        startActivity(editPasswordIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg = " ";
        switch(item.getItemId())
        {
            case R.id.setting:
                msg = "Setting";
                break;
            case R.id.logout:
                msg = "Logout";
                break;
        }
//        Toast.makeText(this, msg + " Checked", Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getSites();
    }

    public void getSites()
    {
        NetworkUtils.injectCookies(AndroidNetworking.get("http://192.168.1.182:5000/api/sites"), getApplicationContext())
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            try
                            {
                                JSONObject object = jsonArray.getJSONObject(i);
                                passwordList.add(EncryptionUtils.DecryptSite(Hex.decodeHex(object.getString("content").toCharArray()),
                                        object.getString("id"), getApplicationContext()));
                            }
                            catch (Exception e)
                            {
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(loginIntent);
                    }
                });
    }

}


