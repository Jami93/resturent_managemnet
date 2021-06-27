package com.example.musiccafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
   public Button btn;
   private EditText ed1;
   String str;
   private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        btn= findViewById(R.id.fgpassBUtton);
        ed1=findViewById(R.id.fpassEditTxt);
        firebaseAuth= FirebaseAuth.getInstance();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               str= ed1.getText().toString();
               if(str.isEmpty()){
                   btn.setEnabled(false);
                   btn.setTextColor(Color.argb(50, 255, 255, 255));

               }
               else{
                   forgetPassword();
               }
            }
        });



    }

    private void forgetPassword() {
        firebaseAuth.sendPasswordResetEmail(str).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ForgetPassword.this,"Check your email",Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(ForgetPassword.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                Toast.makeText(ForgetPassword.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }
}