package com.example.keypermobile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.androidnetworking.AndroidNetworking;
import com.example.keypermobile.models.Password;
import com.example.keypermobile.utils.PasswordAdapter;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class HomeActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    PasswordAdapter adapter;

    DrawerLayout drawerLayout;

    NavigationView navigationView;

    Menu navigationViewMenu;

    List<Password> passwordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AndroidNetworking.initialize(getApplicationContext());
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this));
        final OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Keyper");

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

        passwordList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        passwordList.add(
                new Password(1,"Google Account","www.google.com", R.drawable.logo_google)
        );
        passwordList.add(
                new Password(2,"NOT Google Account","www.notgoogle.com", R.drawable.logo_google)
        );
        passwordList.add(
                new Password(3,"Goggles Account","www.goggles.com", R.drawable.logo_google)
        );
        passwordList.add(
                new Password(4,"A Account","www.alabama.gov", R.drawable.logo_google)
        );
        passwordList.add(
                new Password(5,"B Account","www.bet.com", R.drawable.logo_google)
        );
        passwordList.add(
                new Password(6,"C Account","www.ComedyCentral.com", R.drawable.logo_google)
        );
        passwordList.add(
                new Password(7,"Desmos","www.Desmos.com", R.drawable.logo_google)
        );
        adapter = new PasswordAdapter(this, passwordList);
        recyclerView.setAdapter(adapter);
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
}
