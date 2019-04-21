package com.example.keypermobile;

import android.app.SearchManager;
import android.content.Context;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
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

    List<Site> passwordList;

    FloatingActionButton floatingActionButtonAdd;

    TextView textViewLogout;

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

        textViewLogout = findViewById(R.id.item_log_out);
        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.item_password_generator)
                    startActivity(new Intent(getApplicationContext(), PasswordGeneratorActivity.class));

                //close drawer when item is selected
                drawerLayout.closeDrawers();

                return true;
            }
        });

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

        adapter = new PasswordAdapter(this, passwordList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPasswordClick ( int position){
        passwordList.get(position);
        Intent editPasswordIntent = new Intent(getApplicationContext(), EditPasswordActivity.class);
        editPasswordIntent.putExtra("Site", passwordList.get(position).toJson());
        editPasswordIntent.putExtra("Activity Title", getResources().getString(R.string.title_activity_edit_password));
        startActivity(editPasswordIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getSites();
    }

    public void getSites()
    {
        NetworkUtils.injectCookies(AndroidNetworking.get(NetworkUtils.getApiUrl(getApplicationContext()) + "sites"), getApplicationContext())
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        passwordList.clear();
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            try
                            {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Site decryptedSite = EncryptionUtils.DecryptSite(Hex.decodeHex(object.getString("content").toCharArray()),
                                        object.getString("id"), getApplicationContext());
                                if (decryptedSite != null)
                                    passwordList.add(decryptedSite);
                            }
                            catch (Exception e)
                            {
                            }
                        }
                        adapter.getFilter().filter("");
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivityForResult(loginIntent, 0);
                    }
                });
    }

    public void logout()
    {
        NetworkUtils.injectCookies(AndroidNetworking.get(NetworkUtils.getApiUrl(getApplicationContext()) + "logout"), getApplicationContext())
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivityForResult(loginIntent, 0);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivityForResult(loginIntent, 0);
                    }
                });
    }

}


