package com.example.musiccafe;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 12;
    private static final int RESULT_OK = 5;
    private TextView ptextView,textView1,textView2,textView3,textView4;
    private Button btn1;

    private CircleImageView nImageview;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference,databaseReference2;
    String userId,foodname;
    String value="";
    List<Order> orderList=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_frag_layout, container, false);
        User user;
        nImageview= view.findViewById(R.id.profileImage);
        textView1= view.findViewById(R.id.profileTextviewId1);
        textView2= view.findViewById(R.id.profileTextview2);
        textView3= view.findViewById(R.id.profileTextView3);
        textView4=view.findViewById(R.id.profileTextview4);
        btn1= view.findViewById(R.id.profileButton);
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        userId= firebaseAuth.getCurrentUser().getUid();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),updateprofile.class);
                startActivity(intent);
            }
        });

        databaseReference= FirebaseDatabase.getInstance().getReference(userId);
        databaseReference2= FirebaseDatabase.getInstance().getReference("orders");
        //Toast.makeText(getContext(),userId,Toast.LENGTH_LONG).show();
        DocumentReference documentReference=firebaseFirestore.collection("USERS").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    textView1.setText(documentSnapshot.getString("email"));
                    textView2.setText(documentSnapshot.getString("address"));
                    textView3.setText(documentSnapshot.getString("fullName"));

                }
                else{

                    Toast.makeText(getContext(),"Error ",Toast.LENGTH_SHORT).show();
                }

            }
        });
        DocumentReference documentReference1=firebaseFirestore.collection("profilepic").document(userId);
        documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Glide.with(getContext()).load(documentSnapshot.getString("picture")).into(nImageview);
                }
                else{

                    Toast.makeText(getContext(),"Error ",Toast.LENGTH_SHORT).show();
                }

            }
        });

   databaseReference2.child(userId).addValueEventListener(new ValueEventListener() {
       @Override
       public void onDataChange(@NonNull  DataSnapshot snapshot) {
           orderList.clear();
           for(DataSnapshot dataSnapshot:snapshot.getChildren()){
              Order order=dataSnapshot.getValue(Order.class);
              orderList.add(order);
           }
           for (int i=0;i<orderList.size();i++){
               Order order2=orderList.get(i);
               value+=order2.getFoodName()+"\n";
           }
           textView4.setText(value);


       }

       @Override
       public void onCancelled(@NonNull DatabaseError error) {

       }
   });

//        ptextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ImagePicker.with((Activity) getContext())
//                        .crop()	    			//Crop image(Optional), Check Customization for more option
//                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
//                        .start();
//
//
//            }
//        });
        return view;
    }


}





