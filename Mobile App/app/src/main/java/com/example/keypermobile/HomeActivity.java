package com.example.keypermobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.keypermobile.models.Password;
import com.example.keypermobile.utils.PasswordAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    PasswordAdapter adapter;

    List<Password> passwordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Keyper");

        passwordList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
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
