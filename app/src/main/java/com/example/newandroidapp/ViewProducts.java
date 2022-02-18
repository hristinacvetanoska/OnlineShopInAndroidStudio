package com.example.newandroidapp;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewProducts extends AppCompatActivity{
    ArrayList<String> lines=new ArrayList<>();
    ListView lista;
    Spinner spin;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String productName, productDescription, productPrice, Admin, data, time, status,image, fullNameOfUser, emailofUser, city, address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);


        lista=findViewById(R.id.listaAktivnosti);
        spin = findViewById(R.id.FilterByStatus);
        String result = spin.getSelectedItem().toString();
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lines.clear();
                String result = spin.getSelectedItem().toString();
                String admin = getIntent().getStringExtra("admin");
                FirebaseDatabase.getInstance()
                        .getReference("Products").orderByChild("admin").equalTo(admin).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot:snapshot.getChildren()){
                            String status = postSnapshot.child("Status").getValue().toString();
                            if(result.equals("Orders received")){
                                if(status.equals("Order received")){
                                    lines.add(postSnapshot.child("pname").getValue().toString());

                                }

                             }
                            if(result.equals("Available")){
                                if(status.equals("Available")){
                                    lines.add(postSnapshot.child("pname").getValue().toString());

                                }

                            }
                            if(result.equals("New Orders")){
                                if(status.equals("wants to buy it")){
                                    lines.add(postSnapshot.child("pname").getValue().toString());

                                }

                            }
                            if(result.equals("Shipped")){
                                if(status.equals("Shipped")){
                                    lines.add(postSnapshot.child("pname").getValue().toString());
                                }

                            }


                        }
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(ViewProducts.this,android.R.layout.simple_list_item_1,lines);
                        lista.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick (AdapterView < ? > parent, View view, int index, long id){
                String imeProizvod = lines.get(index);
                Intent intent = new Intent(ViewProducts.this,ProductDetails.class);
                FirebaseDatabase.getInstance()
                        .getReference("Products").orderByChild("pname").equalTo(imeProizvod).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            productName = postSnapshot.child("pname").getValue().toString();
                            productDescription = postSnapshot.child("description").getValue().toString();
                            productPrice = postSnapshot.child("price").getValue().toString();
                            data = postSnapshot.child("date").getValue().toString();
                            time = postSnapshot.child("time").getValue().toString();
                            Admin = postSnapshot.child("admin").getValue().toString();
                            status = postSnapshot.child("Status").getValue().toString();
                            image = postSnapshot.child("image").getValue().toString();
                            fullNameOfUser = postSnapshot.child("FullNameUser").getValue().toString();
                            emailofUser = postSnapshot.child("EmailOfUser").getValue().toString();
                            city = postSnapshot.child("CityOfUser").getValue().toString();
                            address = postSnapshot.child("AddressOfUser").getValue().toString();
                        }
                        intent.putExtra("Title",productName);
                        intent.putExtra("description",productDescription);
                        intent.putExtra("Date",data);
                        intent.putExtra("price",productPrice);
                        intent.putExtra("time",time);
                        intent.putExtra("Admin",Admin);
                        intent.putExtra("Status", status);
                        intent.putExtra("imageUrl", image);
                        intent.putExtra("FullNameUser",fullNameOfUser);
                        intent.putExtra("EmailOfUser",emailofUser);
                        intent.putExtra("city", city);
                        intent.putExtra("address", address);

                        //intent.putExtra("phoneNumberUser",phoneNumberOfUser);

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