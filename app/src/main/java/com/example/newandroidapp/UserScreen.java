package com.example.newandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class UserScreen extends AppCompatActivity {
    private ArrayList<String> lines = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private ArrayList<String> imageList = new ArrayList<>();
    private ListView lista;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private Button logout, prijaveniZadaci;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String celoIme, description, title, rastojanie, rejting, lat, lon, email, price, imageUrl;


    RecyclerView mRecyclerView;
    myAdapter mAdapter;

    public UserScreen() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);

        logout = findViewById(R.id.logout);
        prijaveniZadaci = findViewById(R.id.cartItems);
        String email = getIntent().getStringExtra("email");

        prijaveniZadaci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserScreen.this, CartItems.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });

        FirebaseDatabase.getInstance()
                .getReference("Products")
                .orderByChild("Status").equalTo("Available").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String imageUrl = postSnapshot.child("image").getValue().toString();
                    titleList.add(postSnapshot.child("pname").getValue().toString());
                    imageList.add(imageUrl);
                }

                mRecyclerView = (RecyclerView) findViewById(R.id.list);


                mRecyclerView.setLayoutManager(new LinearLayoutManager(UserScreen.this));

                mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                mAdapter = new myAdapter(titleList, imageList,email,R.layout.my_row, UserScreen.this);

                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(UserScreen.this,MainActivity.class));
            }
        });

    }


}