package com.example.healthbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.profile);
        View hView =  navigationView.getHeaderView(0);
        ((TextView) hView.findViewById(R.id.textView)).setText(Globals.displayName);
        ((TextView) hView.findViewById(R.id.textView2)).setText(Globals.email);
        Glide.with(ProfileActivity.this).load(Globals.photoUrl).error(R.drawable.logo).into((ImageView) hView.findViewById(R.id.imageView));
        Glide.with(ProfileActivity.this).load(Globals.photoUrl).error(R.drawable.logo).into((ImageView) findViewById(R.id.imageView));
        navigationView.setNavigationItemSelectedListener(this);

        ArrayList<String> labels = new ArrayList<>();
        ArrayList<String> data = new ArrayList<>();
        HashMap<String,String> map = new HashMap<>();
        int c;
        FileInputStream fis;
        String temp="";
        String[] info;
        try {
            fis = new FileInputStream(new File(getFilesDir().toString()+"/profile.txt"));
            while( (c = fis.read()) != -1){
                if(c == 10) {
                    info = temp.split("!!");
                    map.put(info[0],info[1]);
                    temp = "";
                }
                else
                    temp = String.format(Locale.getDefault(),"%s%s",temp,Character.toString((char)c));
            }
            labels.add("Email ID:");
            labels.add("Name:");
            labels.add("Contact Number:");
            labels.add("Address:");
            labels.add("Date Of Birth:");
            labels.add("Gender:");
            labels.add("Height:");
            labels.add("Weight:");
            labels.add("Blood Group:");
            labels.add("Any Disorder(s):");
            data.add(map.get("name"));
            data.add(map.get("email"));
            data.add(map.get("phone"));
            data.add(map.get("address"));
            data.add(map.get("dateOfBirth"));
            data.add(map.get("gender"));
            data.add(map.get("height"));
            data.add(map.get("weight"));
            data.add(map.get("bloodGroup"));
            data.add(map.get("disorder"));
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.profile);
            recyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));
            ProfileAdapter adapter = new ProfileAdapter(labels,data,ProfileActivity.this);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ProfileActivity.this, "Some Problem Occurred", Toast.LENGTH_LONG).show();
        }
    }

    boolean isUserClickedBackButton = false;
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if (!isUserClickedBackButton) {
            Toast.makeText(ProfileActivity.this, "Press Back Again to Exit", Toast.LENGTH_LONG).show();
            isUserClickedBackButton = true;
        }  else {
            finishAffinity();
            ProfileActivity.class.getDeclaredMethods();
            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    isUserClickedBackButton = false;
                }
            }.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch(item.getItemId()) {
            case R.id.health:
                startActivity(new Intent(ProfileActivity.this,HealthRecordActivity.class));
                break;
            case R.id.chatbot:
                startActivity(new Intent(ProfileActivity.this,MedicalChatbotActivity.class));
                break;
            case R.id.chatbot_new:
                startActivity(new Intent(ProfileActivity.this,NewMedicalChatbotActivity.class));
                break;
            case R.id.profile:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
