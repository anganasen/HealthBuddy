package com.example.healthbuddy;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class NewMedicalChatbotActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medical_chatbot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.chatbot_new);
        View hView =  navigationView.getHeaderView(0);
        ((TextView) hView.findViewById(R.id.textView)).setText(Globals.displayName);
        ((TextView) hView.findViewById(R.id.textView2)).setText(Globals.email);
        Glide.with(NewMedicalChatbotActivity.this).load(Globals.photoUrl).error(R.drawable.logo).into((ImageView) hView.findViewById(R.id.imageView));
        navigationView.setNavigationItemSelectedListener(this);

        final ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        final Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://healthywealthyalways.herokuapp.com/"));

        ((TextView) findViewById(R.id.appId)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboardManager.setPrimaryClip(ClipData.newPlainText("APP_ID",getResources().getString(R.string.app_id)));
                Toast.makeText(NewMedicalChatbotActivity.this, "App ID Copied", Toast.LENGTH_LONG).show();
            }
        });
        ((TextView) findViewById(R.id.appKey)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboardManager.setPrimaryClip(ClipData.newPlainText("APP_KEY",getResources().getString(R.string.app_key)));
                Toast.makeText(NewMedicalChatbotActivity.this, "App Key Copied", Toast.LENGTH_LONG).show();
            }
        });

        ((Button) findViewById(R.id.open)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent.createChooser(i,"Open with..."));
            }
        });
    }

    boolean isUserClickedBackButton = false;
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if (!isUserClickedBackButton) {
            Toast.makeText(NewMedicalChatbotActivity.this, "Press Back Again to Exit", Toast.LENGTH_LONG).show();
            isUserClickedBackButton = true;
        }  else {
            finishAffinity();
            NewMedicalChatbotActivity.class.getDeclaredMethods();
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
            startActivity(new Intent(NewMedicalChatbotActivity.this, SettingsActivity.class));
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
                startActivity(new Intent(NewMedicalChatbotActivity.this,HealthRecordActivity.class));
                break;
            case R.id.chatbot:
                startActivity(new Intent(NewMedicalChatbotActivity.this,MedicalChatbotActivity.class));
                break;
            case R.id.chatbot_new:
                break;
            case R.id.profile:
                startActivity(new Intent(NewMedicalChatbotActivity.this,ProfileActivity.class));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
