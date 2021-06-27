package com.example.musiccafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ShowOrder extends AppCompatActivity {
    private RecyclerView recyclerView2;
    Myadapter2 myadapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Model2> options =
                new FirebaseRecyclerOptions.Builder<Model2>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("showorder"), Model2.class)
                        .build();
   myadapter2= new Myadapter2(options);
   recyclerView2.setAdapter(myadapter2);
    }
    @Override
    protected void onStart() {
        super.onStart();
        myadapter2.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myadapter2.stopListening();
    }

}