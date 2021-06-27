package com.example.musiccafe;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;


public class Myadapter extends FirebaseRecyclerAdapter<model,Myadapter.myviewholder>
{
    Context context;
    public Myadapter(@NonNull FirebaseRecyclerOptions<model> options,Context context) {
        super(options);
        this.context= context;
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull model model)
    {
        Glide.with(holder.img.getContext()).load(model.getImage()).into(holder.img);
        holder.t1.setText(model.getName());
        holder.t2.setText(model.getPrice());
       // Toast.makeText(context,model.getImage(),Toast.LENGTH_SHORT).show();

        holder.t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.t1.getContext(),
                        detailsActivity.class);
                intent.putExtra("fname",model.getName());
                intent.putExtra("fprize",model.getPrice());
                intent.putExtra("image",model.getImage());


                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.t1.getContext().startActivity(intent);


            }
        });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView t1,t2;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            img=itemView.findViewById(R.id.rImageView);
            t1=itemView.findViewById(R.id.rtextView);
            t2=itemView.findViewById(R.id.rtextView2);
        }
    }

}
