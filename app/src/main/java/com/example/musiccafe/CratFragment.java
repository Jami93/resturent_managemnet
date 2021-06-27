package com.example.musiccafe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CratFragment extends Fragment {
    private TextView tx1,tx2,tx3,tx4;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    List<Order>list=new ArrayList<>();
    String value="";
    int k=0;
    String  q="";
    String j="";
    String p="";
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_frag_layout, container, false);
        tx1= view.findViewById(R.id.carTextView1);
        tx2= view.findViewById(R.id.carTextView2);
        tx3= view.findViewById(R.id.carTextView3);
        tx4= view.findViewById(R.id.carTextView4);
        firebaseAuth= FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("orders");
        String uid=firebaseAuth.getCurrentUser().getUid();
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Order order=dataSnapshot.getValue(Order.class);
                    list.add(order);
                }
                for (int i=0; i<list.size();i++){
                    Order order2=list.get(i);
                    value+=order2.getFoodName()+"\n";
                    k+=Integer.parseInt(order2.getFoodPrice())*Integer.parseInt(order2.getQuantity());
                    j+=order2.getFoodPrice()+"\n";
                    q+=order2.getQuantity()+"\n";
                }
                p=String.valueOf(k)+" taka";
                tx1.setText(value);
                tx2.setText(j);
                tx3.setText(q);
                tx4.setText(p);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return view;
    }
}
