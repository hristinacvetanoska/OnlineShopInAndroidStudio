package com.example.newandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProductDetails extends AppCompatActivity {
    private Button prifati, otkazi, oceniUser;
    private TextView tipUslugaDesno, opisUslugaDesno, datumDesno, timeRight, priceRight, statusDesno, userRight,addresUserRight , emailUserDesno;
    private TextView userLeft, addresUserLeft,emailUserLeft;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Intent intent = getIntent();
        String nameOfProduct = intent.getStringExtra("Title");
        String date = intent.getStringExtra("Date");
        String description = intent.getStringExtra("description");
        String price = intent.getStringExtra("price");
        String time = intent.getStringExtra("time");
        String status = intent.getStringExtra("Status");
        String fullNameUser = intent.getStringExtra("FullNameUser");
        String emailofUser = intent.getStringExtra("EmailOfUser");
        String imageUrl = intent.getStringExtra("imageUrl");
        String city = intent.getStringExtra("city");
        String address = intent.getStringExtra("address");

        prifati = findViewById(R.id.prifati);
        otkazi = findViewById(R.id.otkazi);
        tipUslugaDesno = findViewById(R.id.productRight);
        opisUslugaDesno = findViewById(R.id.descriptionProductRight);
        datumDesno = findViewById(R.id.dateRight);
        timeRight = findViewById(R.id.timeProductRight);
        priceRight = findViewById(R.id.priceProductRight);
        statusDesno = findViewById(R.id.statusRight);
        userRight = findViewById(R.id.userRight);
        emailUserDesno = findViewById(R.id.emailUserRight);
        userLeft = findViewById(R.id.userLeft);
        emailUserLeft = findViewById(R.id.emailUserLeft);
        imageView = findViewById(R.id.imageViewProduct);
        addresUserLeft = findViewById(R.id.addressUserLeft);
        addresUserRight = findViewById(R.id.addressUserRight);


        tipUslugaDesno.setText(nameOfProduct);
        opisUslugaDesno.setText(description);
        datumDesno.setText(date);
        timeRight.setText(time);
        priceRight.setText(price);
        statusDesno.setText(status);
        userRight.setText(fullNameUser);
        emailUserDesno.setText(emailofUser);
        addresUserRight.setText(address+" "+city);
        Picasso.get().load(imageUrl).into(imageView);

        if(status.equals("Available")){
            prifati.setVisibility(View.GONE);
            otkazi.setVisibility(View.GONE);
            userRight.setVisibility(View.GONE);
            emailUserDesno.setVisibility(View.GONE);
            userLeft.setVisibility(View.GONE);
            emailUserLeft.setVisibility(View.GONE);
            addresUserLeft.setVisibility(View.GONE);
            addresUserRight.setVisibility(View.GONE);
        }
        else if(status.equals("Shipped")){
            prifati.setVisibility(View.GONE);
            otkazi.setVisibility(View.GONE);
        }else if(status.equals("Order received")){

            prifati.setVisibility(View.GONE);
            otkazi.setVisibility(View.GONE);
        }

        HashMap<String, Object> map = new HashMap<>();
        prifati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusDesno.setText("Shipped");
                prifati.setVisibility(View.GONE);
                otkazi.setVisibility(View.GONE);
                FirebaseDatabase.getInstance()
                        .getReference("Products").orderByChild("pname").equalTo(nameOfProduct).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap: snapshot.getChildren()){
                            map.put("Status","Shipped");
                            FirebaseDatabase.getInstance()
                                    .getReference("Products").child(snap.getKey()).updateChildren(map);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        otkazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusDesno.setText("Available");
                prifati.setVisibility(View.GONE);
                otkazi.setVisibility(View.GONE);
                userRight.setVisibility(View.GONE);
                addresUserRight.setVisibility(View.GONE);
                emailUserDesno.setVisibility(View.GONE);
                userLeft.setVisibility(View.GONE);
                emailUserLeft.setVisibility(View.GONE);
                addresUserLeft.setVisibility(View.GONE);
                addresUserRight.setVisibility(View.GONE);
                FirebaseDatabase.getInstance()
                        .getReference("Products").orderByChild("pname").equalTo(nameOfProduct).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap: snapshot.getChildren()){
                            map.put("Status","Available");
                            map.put("EmailOfUser","");
                            map.put("fullNameUser","");
                            FirebaseDatabase.getInstance().getReference("Products").child(snap.getKey()).updateChildren(map);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}