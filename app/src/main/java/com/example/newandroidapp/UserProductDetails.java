package com.example.newandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UserProductDetails extends AppCompatActivity {
    private TextView titleProduct, description, descriptionRight, adminFullName, rejtingDesno, price;
    private Button addToShoppingCart;
    private String fullnameOfUser, ratingUser, phoneNumberUser, emailUser;
    private ImageView imageView;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product_details);
        addToShoppingCart = findViewById(R.id.addToCart);
        titleProduct = findViewById(R.id.productNameUserRight);
        descriptionRight = findViewById(R.id.descriptionProductRight1);
        price = findViewById(R.id.priceRight);
        imageView = findViewById(R.id.imageViewProduct);
        adminFullName = findViewById(R.id.adminFullNameRight);
        rejtingDesno = findViewById(R.id.ratingUserRight);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("image");
        //String datum = intent.getStringExtra("Datum");
        String description = intent.getStringExtra("description");
        String titleP = intent.getStringExtra("Title");
        String rejting = intent.getStringExtra("AdminRating");
        String priceProduct = intent.getStringExtra("Price");
        String fullName = intent.getStringExtra("admin");

        titleProduct.setText(titleP);
        //datumDesno.setText(datum);
        descriptionRight.setText(description);
        price.setText(priceProduct);
        rejtingDesno.setText(rejting);
        adminFullName.setText(fullName);
        Picasso.get().load(imageUrl).into(imageView);

        HashMap<String, Object> map = new HashMap<>();
        addToShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToShoppingCart.setVisibility(View.GONE);
                emailUser = getIntent().getStringExtra("email");
                FirebaseDatabase.getInstance()
                        .getReference("Users").orderByChild("email").equalTo(emailUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot datas : snapshot.getChildren()) {
                            fullnameOfUser = datas.child("fullName").getValue().toString();
                            ratingUser = datas.child("rating").getValue().toString();
                            phoneNumberUser = datas.child("phoneNumber").getValue().toString();
                        }
                        map.put("EmailOfUser", emailUser);
                        map.put("FullNameUser", fullnameOfUser);
                        map.put("PhoneNumberUser:", phoneNumberUser);
                        map.put("RatingOfUser", ratingUser);
                        map.put("Status", "wants to buy it");
                        FirebaseDatabase.getInstance()
                                .getReference("Products").orderByChild("pname").equalTo(titleP).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Products").child(postSnapshot.getKey()).updateChildren(map);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}