package com.example.healthbuddy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoadDataActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_data);

        progressDialog = new ProgressDialog(LoadDataActivity.this);
        progressDialog.setMessage("Fetching Profile, Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        SharedPreferences preferences = getSharedPreferences("HEALTH_BUDDY",MODE_PRIVATE);
        if(!preferences.getBoolean("REGISTERED",false)) {
            loadData();
            return;
        }
        startActivity(new Intent(LoadDataActivity.this, HealthRecordActivity.class));
    }

    private void loadData() {
        FirebaseDatabase.getInstance().getReference().child(Globals.email.split("@")[0].replaceAll("\\.","-")+"_registration").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.hide();
                try {
                    FileOutputStream fos = openFileOutput("profile.txt",MODE_PRIVATE);
                    String entry;
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        entry = String.format(Locale.getDefault(),"%s!!%s\n",Objects.requireNonNull(snapshot.getKey()), Objects.requireNonNull(snapshot.getValue(String.class)));
                        fos.write(entry.getBytes());
                    }
                    fos.close();
                    startActivity(new Intent(LoadDataActivity.this, HealthRecordActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
