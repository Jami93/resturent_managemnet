package com.example.musiccafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
     Button logInButtonId,registerButtonId;

     TextView skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logInButtonId=findViewById(R.id.Loginbuttonid);
        registerButtonId= findViewById(R.id.registerButtonId);
        skip = findViewById(R.id.skipId);
        logInButtonId.setOnClickListener(this);
        registerButtonId.setOnClickListener(this);
        skip.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.registerButtonId){
            Intent intent=new Intent(MainActivity.this,signup.class);
            startActivity(intent);

        }
        if(v.getId()==R.id.Loginbuttonid){
            Intent intent= new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);

        }
        if(v.getId()==R.id.skipId){
            Intent intent= new Intent(MainActivity.this,ownerLogin.class);
            startActivity(intent);

        }


    }

    }
