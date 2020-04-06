package com.example.healthbuddy;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HealthRecordActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    private DatabaseReference mDatabase;
    private ArrayList<Integer> pulses = new ArrayList<>();
    private ArrayList<Date> dates = new ArrayList<>();
    private ArrayList<Integer> temperatures = new ArrayList<>();
    private ArrayList<String> ecgs = new ArrayList<>();
    private ArrayList<Integer> steps = new ArrayList<>();
    private ArrayList<Integer> caloriesBurnts = new ArrayList<>();
    private ProgressDialog progressDialog;
    private HealthAdapter adapter;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager manager;
    private RecyclerView recyclerView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.health);
        View hView =  navigationView.getHeaderView(0);
        ((TextView) hView.findViewById(R.id.textView)).setText(Globals.displayName);
        ((TextView) hView.findViewById(R.id.textView2)).setText(Globals.email);
        Glide.with(HealthRecordActivity.this).load(Globals.photoUrl).error(R.drawable.logo).into((ImageView) hView.findViewById(R.id.imageView));
        navigationView.setNavigationItemSelectedListener(this);

        initiateViews();
        progressDialog.setMessage("Loading Health Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        mBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mBuilder = new NotificationCompat.Builder(HealthRecordActivity.this,Globals.channel.getId())
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Info")
                    .setContentText("Health Data Received !!!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        mDatabase.child(Globals.email.split("@")[0].replaceAll("\\.","-")).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                pulses.add(dataSnapshot.child("pulse").getValue(Integer.class));
                temperatures.add(dataSnapshot.child("temperature").getValue(Integer.class));
                ecgs.add(dataSnapshot.child("ecg").getValue(String.class));
                steps.add(dataSnapshot.child("steps").getValue(Integer.class));
                caloriesBurnts.add(dataSnapshot.child("caloriesBurnt").getValue(Integer.class));
                try {
                    dates.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dataSnapshot.child("timestamp").getValue(String.class)));
                } catch (ParseException e) {
                    dates.add(new Date());
                }
                mBuilder.build();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int pos = pulses.indexOf(dataSnapshot.child("pulse").getValue(Integer.class));
                try {
                    dates.set(pos,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dataSnapshot.child("timestamp").getValue(String.class)));
                    pulses.set(pos,dataSnapshot.child("pulse").getValue(Integer.class));
                    temperatures.set(pos,dataSnapshot.child("temperature").getValue(Integer.class));
                    steps.set(pos,dataSnapshot.child("steps").getValue(Integer.class));
                    ecgs.set(pos,dataSnapshot.child("ecg").getValue(String.class));
                    caloriesBurnts.set(pos,dataSnapshot.child("caloriesBurnt").getValue(Integer.class));
                } catch (ParseException e) {
                    Toast.makeText(HealthRecordActivity.this,"Sync Error !!! Close the App and Open Again",Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int pos = pulses.indexOf(dataSnapshot.child("pulse").getValue(Integer.class));
                try {
                    dates.remove(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dataSnapshot.child("timestamp").getValue(String.class)));
                    pulses.remove(pos);
                    temperatures.remove(pos);
                    steps.remove(pos);
                    ecgs.remove(pos);
                    caloriesBurnts.remove(pos);
                } catch (ParseException e) {
                    Toast.makeText(HealthRecordActivity.this,"Sync Error !!! Close the App and Open Again",Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.child(Globals.email.split("@")[0].replaceAll("\\.","-")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initiateViews() {
        Globals.context = HealthRecordActivity.this;
        progressDialog = new ProgressDialog(HealthRecordActivity.this);
        recyclerView = findViewById(R.id.pulseLogNew);
        recyclerView.setLayoutManager(new LinearLayoutManager(HealthRecordActivity.this));

        Globals.mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        adapter = new HealthAdapter(pulses,dates,temperatures,ecgs,steps,caloriesBurnts, HealthRecordActivity.this);
        recyclerView.setAdapter(adapter);
    }
    
    boolean isUserClickedBackButton = false;
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if (!isUserClickedBackButton) {
            Toast.makeText(HealthRecordActivity.this, "Press Back Again to Exit", Toast.LENGTH_LONG).show();
            isUserClickedBackButton = true;
        }  else {
            finishAffinity();
            HealthRecordActivity.class.getDeclaredMethods();
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
            startActivity(new Intent(HealthRecordActivity.this, SettingsActivity.class));
            return true;
        }
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch(item.getItemId()) {
            case R.id.health:
                break;
            case R.id.chatbot:
                startActivity(new Intent(HealthRecordActivity.this,MedicalChatbotActivity.class));
                break;
            case R.id.chatbot_new:
                startActivity(new Intent(HealthRecordActivity.this,NewMedicalChatbotActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(HealthRecordActivity.this,ProfileActivity.class));
                break;
        }
        
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
