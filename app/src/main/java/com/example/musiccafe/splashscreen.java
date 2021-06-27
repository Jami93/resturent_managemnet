package com.example.musiccafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class splashscreen extends AppCompatActivity {
    private ProgressBar progressBar;
    int progress;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);
        progressBar=findViewById(R.id.progress_barId);
        firebaseAuth= FirebaseAuth.getInstance();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
                startWork();
            }
        });
        thread.start();

    }
    public void doWork() {
        for (progress = 20; progress <= 100; progress += 20) {
            try {
                Thread.sleep(1000);
                progressBar.setProgress(progress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
    public void startWork(){

        FirebaseUser currentUSer= firebaseAuth.getCurrentUser();


        if(currentUSer!=null) {

            Intent intent = new Intent(splashscreen.this, StartActivity.class);
            startActivity(intent);
            finish();
       }
       else{

            Intent intent = new Intent(splashscreen.this, MainActivity.class);
            startActivity(intent);
            finish();
       }
    }


}