package com.example.newandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartItems extends AppCompatActivity {
    private ArrayList<String> lines = new ArrayList<>();
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private ListView lista;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String emailAdmin, phoneNumberAdmin, title, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_items);
        lista = findViewById(R.id.listaAktivni);
        String email = getIntent().getStringExtra("email");


        FirebaseDatabase.getInstance()
                .getReference("Products").orderByChild("Status").equalTo("Shipped").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        lines.add(postSnapshot.child("pname").getValue().toString());

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        CartItems.this, android.R.layout.simple_list_item_1, lines);
                lista.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] niza = lines.get(i).split(" - ");
                String tipUsluga = niza[0];
                Intent intent = new Intent(CartItems.this, FeedbackForAdmin.class);
                FirebaseDatabase.getInstance()
                        .getReference("Products").orderByChild("pname").equalTo(tipUsluga).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            emailAdmin = postSnapshot.child("email").getValue().toString();
                            phoneNumberAdmin = postSnapshot.child("PhoneNumber").getValue().toString();
                            title = postSnapshot.child("pname").getValue().toString();
                        }
                        intent.putExtra("title",title);
                        intent.putExtra("email",emailAdmin);
                        intent.putExtra("phoneNumber",phoneNumberAdmin);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}