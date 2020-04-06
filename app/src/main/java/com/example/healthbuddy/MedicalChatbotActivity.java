package com.example.healthbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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

public class MedicalChatbotActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    ProgressBar progressDialog;
    EditText message;
    ImageView button;
    ScrollView scrollView;
    LinearLayout linearLayout;
    String m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_chatbot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.chatbot);
        View hView =  navigationView.getHeaderView(0);
        ((TextView) hView.findViewById(R.id.textView)).setText(Globals.displayName);
        ((TextView) hView.findViewById(R.id.textView2)).setText(Globals.email);
        Glide.with(MedicalChatbotActivity.this).load(Globals.photoUrl).error(R.drawable.logo).into((ImageView) hView.findViewById(R.id.imageView));
        navigationView.setNavigationItemSelectedListener(this);

        progressDialog = (ProgressBar) findViewById(R.id.typing);
        button = (ImageView) findViewById(R.id.send);
        linearLayout = (LinearLayout) findViewById(R.id.senderArea);
        scrollView = (ScrollView) findViewById(R.id.messageHolder);
        message = (EditText) findViewById(R.id.userMessage);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m =  message.getText().toString();
                if(!m.equals("")) {
                    message.setText("");
                    try {
                        new GetDataOld(progressDialog, MedicalChatbotActivity.this, linearLayout, scrollView, button, m).execute();
                    } catch (Exception e) {
                        Toast.makeText(MedicalChatbotActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    boolean isUserClickedBackButton = false;
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if (!isUserClickedBackButton) {
            Toast.makeText(MedicalChatbotActivity.this, "Press Back Again to Exit", Toast.LENGTH_LONG).show();
            isUserClickedBackButton = true;
        }  else {
            finishAffinity();
            MedicalChatbotActivity.class.getDeclaredMethods();
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
            startActivity(new Intent(MedicalChatbotActivity.this, SettingsActivity.class));
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
                startActivity(new Intent(MedicalChatbotActivity.this,HealthRecordActivity.class));
                break;
            case R.id.chatbot:
                break;
            case R.id.chatbot_new:
                startActivity(new Intent(MedicalChatbotActivity.this,NewMedicalChatbotActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(MedicalChatbotActivity.this,ProfileActivity.class));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
