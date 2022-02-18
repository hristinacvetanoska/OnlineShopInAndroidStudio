package com.example.newandroidapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {

    private List<String> myList;
    private ArrayList<String> photoList;
    private int rowLayout;
    private Context mContext;
    private String celoIme, description, title, rastojanie, rejting, lat, lon, email, price, imageUrl;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView myName;
        public ImageView Pic;

        public ViewHolder(View itemView) {
            super(itemView);
            myName = (TextView) itemView.findViewById(R.id.Name);
            Pic = (ImageView) itemView.findViewById(R.id.picture);
        }
    }

    public myAdapter(List<String> myList, ArrayList<String> photoList, String email, int rowLayout, Context context) {
        this.myList = myList;
        this.photoList = photoList;
        this.email = email;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        String entry = myList.get(i);
        viewHolder.myName.setText(entry);
        viewHolder.myName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] niza = myList.get(i).split(" - ");
                String productName = niza[0];
                Intent intent = new Intent(mContext,UserProductDetails.class);
                FirebaseDatabase.getInstance()
                        .getReference("Products").orderByChild("pname").equalTo(productName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {


                            celoIme = postSnapshot.child("admin").getValue().toString();
                            description = postSnapshot.child("description").getValue().toString();
                            title = postSnapshot.child("pname").getValue().toString();
                            rejting = postSnapshot.child("RateOfAdmin").getValue().toString();
                            price = postSnapshot.child("price").getValue().toString();
                            imageUrl = postSnapshot.child("image").getValue().toString();
                        }
                        intent.putExtra("Title",title);
                        intent.putExtra("admin",celoIme);
                        intent.putExtra("description",description);
                        intent.putExtra("email", email);
                        intent.putExtra("AdminRating",rejting);
                        intent.putExtra("Price", price);
                        intent.putExtra("image", imageUrl);

                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        Picasso.get().load(photoList.get(i)).into(viewHolder.Pic);
    }

    @Override
    public int getItemCount() {
        return myList == null ? 0 : myList.size();
    }

}
