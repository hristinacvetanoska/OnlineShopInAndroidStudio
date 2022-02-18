package com.example.newandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    Spinner spin;
    private EditText address;
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
        address = findViewById(R.id.address);
        spin = findViewById(R.id.spinnerCity);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("image");
        String description = intent.getStringExtra("description");
        String titleP = intent.getStringExtra("Title");
        String rejting = intent.getStringExtra("AdminRating");
        String priceProduct = intent.getStringExtra("Price");
        String fullName = intent.getStringExtra("admin");

        titleProduct.setText(titleP);
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
                spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                String city = spin.getSelectedItem().toString();
                String addressUser=address.getText().toString();
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
                        map.put("CityOfUser",city);
                        map.put("AddressOfUser", addressUser);
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
