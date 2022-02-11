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

       // lista = findViewById(R.id.productList);
        logout = findViewById(R.id.logout);
        prijaveniZadaci = findViewById(R.id.cartItems);
        String email = getIntent().getStringExtra("email");

        prijaveniZadaci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserScreen.this, CartItems.class);
                intent.putExtra("email",email);
                startActivity(intent);
                //startActivity(new Intent(UserScreen.this, CartItems.class));
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
                    //lines.add(postSnapshot.child("pname").getValue().toString());
                }
              /*  ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        UserScreen.this, android.R.layout.simple_list_item_1, lines);
                lista.setAdapter(adapter);*/
                mRecyclerView = (RecyclerView) findViewById(R.id.list);

                // оваа карактеристика може да се користи ако се знае дека промените
                // во содржината нема да ја сменат layout големината на RecyclerView
                // mRecyclerView.setHasFixedSize(true);

                // ќе користиме LinearLayoutManager
                mRecyclerView.setLayoutManager(new LinearLayoutManager(UserScreen.this));

                // и default animator (без анимации)
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                // сетирање на кориснички дефиниран адаптер myAdapter (посебна класа)
                mAdapter = new myAdapter(titleList, imageList,email,R.layout.my_row, UserScreen.this);

                //прикачување на адаптерот на RecyclerView
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


       /* lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] niza = lines.get(i).split(" - ");
                String productName = niza[0];
                Intent intent = new Intent(UserScreen.this,UserProductDetails.class);
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

                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });*/

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(UserScreen.this,MainActivity.class));
            }
        });

    }


}