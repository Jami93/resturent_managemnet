package com.example.musiccafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class detailsActivity extends AppCompatActivity {
private ImageView img;
private  TextView t1,t2;
private  String url;
private  int value=0,p=0;
private  int ordernumber=0;
Button button;
private EditText editText;
private String ed;
private  FirebaseFirestore firebaseFirestore;
private FirebaseAuth firebaseAuth;
DatabaseReference databaseReference,databaseReference2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        img = findViewById(R.id.holderImageView);
        t1 = findViewById(R.id.holderTextView1);
        t2 = findViewById(R.id.holderTextView2);
        button= findViewById(R.id.addToCartButton);
        editText= findViewById(R.id.editTextNumberDecimal);
        ed= editText.getText().toString();
        firebaseAuth= FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("orders");
        databaseReference2=FirebaseDatabase.getInstance().getReference("showorder");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   String foodName=t1.getText().toString();
                   String foodPrice=t2.getText().toString();
                   String quantity= editText.getText().toString();

                if(!TextUtils.isEmpty(foodName)&&!TextUtils.isEmpty(foodPrice)&&!TextUtils.isEmpty(quantity)){
                    Order order =new Order(foodName,foodPrice,quantity);
                    String uid=firebaseAuth.getCurrentUser().getUid();
                    String key= databaseReference.push().getKey();
                    databaseReference.child(uid).child(key).setValue(order);
                    Toast.makeText(detailsActivity.this,"Order confirmed",Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(detailsActivity.this,recyclerView.class);
                             startActivity(intent);
                }
                String str = String.valueOf(System.currentTimeMillis());
                if(!TextUtils.isEmpty(foodName)&&!TextUtils.isEmpty(foodPrice)&&!TextUtils.isEmpty(quantity)){
                    Upload2 upload2= new Upload2( str,foodName,foodPrice,quantity);
                    String key1=databaseReference2.push().getKey();
                    databaseReference2.child(key1).setValue(upload2);

                }
            }
        });
        Glide.with(detailsActivity.this).load(getIntent().getStringExtra("image")).into(img);
        t1.setText(getIntent().getStringExtra("fname"));
        t2.setText(getIntent().getStringExtra("fprize"));




    }
}