package com.example.musiccafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.PrimitiveIterator;

public class ownerLogin extends AppCompatActivity {
    private String emailPattern ="[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private Button ownerBtn;
    private EditText ownerEmail,ownerPassword;
    String email="luljami8@gmail.com";
    String pass= "42258900";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_login);
        ownerBtn= findViewById(R.id.oLButton);
        ownerEmail= findViewById(R.id.OwnerEmailAddress);
        ownerPassword= findViewById(R.id.OwnerPassword);
        ownerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailPassword();
            }
        });

        ownerEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ownerPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void checkEmailPassword() {
        if (ownerEmail.getText().toString().matches(emailPattern)) {
            if (ownerPassword.length() >= 8) {
                if(ownerEmail.getText().toString().matches(email)&&ownerPassword.getText().toString().matches(pass)){
                    Intent intent= new Intent(ownerLogin.this,OwnerActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(ownerLogin.this,"you are not a owner ",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void checkInput() {
        if (!TextUtils.isEmpty(ownerEmail.getText())) {
            if (!TextUtils.isEmpty(ownerPassword.getText())) {
                ownerBtn.setEnabled(true);
               ownerBtn.setTextColor(Color.rgb(255, 255, 255));
            } else {
                ownerBtn.setEnabled(false);
                ownerBtn.setTextColor(Color.argb(50, 255, 255, 255));
            }
        } else {
            ownerBtn.setEnabled(false);
            ownerBtn.setTextColor(Color.argb(50, 255, 255, 255));
        }

    }


      }







