package com.example.musiccafe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

  public class Myadapter2 extends FirebaseRecyclerAdapter <Model2,Myadapter2.myviewholder>{

    public Myadapter2(@NonNull FirebaseRecyclerOptions<Model2> options) {
        super(options);

    }

      @Override
      protected void onBindViewHolder(@NonNull  Myadapter2.myviewholder holder, final int position, @NonNull  Model2 model2) {
        holder.t1.setText(model2.getOrderNumber());
        holder.t2.setText(model2.getFoodName());
        holder.t3.setText(model2.getFoodPrice());
        holder.t4.setText(model2.getQuantity());
        holder.t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(holder.t1.getContext());
                builder.setTitle("Delete Order");
                builder.setMessage("Delete...?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       FirebaseDatabase.getInstance().getReference().child("showorder")
                               .child(getRef(position).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            builder.show();
            }
        });

      }


      @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow2,parent,false);
        return new myviewholder(view);
    }



      class myviewholder extends RecyclerView.ViewHolder
    {
        TextView t1,t2,t3,t4,t5;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            t1=itemView.findViewById(R.id.showOrderTextView1);
            t2=itemView.findViewById(R.id.showOrderTextView2);
            t3=itemView.findViewById(R.id.showOrderTextView3);
            t4=itemView.findViewById(R.id.showOrderTextView4);
            t5= itemView.findViewById(R.id.showOrderTextView5);

        }
    }
}
