package com.example.newandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Seller extends AppCompatActivity implements View.OnClickListener{
    TextView displayNameofSeller;
    Button addBtn, viewBtn, logOutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        displayNameofSeller = findViewById(R.id.sellerName);

        String name = getIntent().getStringExtra("fullName");
        displayNameofSeller.setText(name);


        addBtn = findViewById(R.id.addNewProduct);
        addBtn.setOnClickListener(this);


        viewBtn = findViewById(R.id.viewProducts);
        viewBtn.setOnClickListener(this);


        logOutBtn = findViewById(R.id.logOut);
        logOutBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.addNewProduct:
                //startActivity(new Intent(this, AddNewProduct.class));
                addProductActivity();
                break;
            case R.id.viewProducts:
                viewProductsActivity();
                //startActivity(new Intent(this, ViewProducts.class));
                break;
            case R.id.logOut:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    private void viewProductsActivity() {
        Intent intent = new Intent(getApplicationContext(), ViewProducts.class);
        intent.putExtra("admin", getIntent().getStringExtra("fullName"));
        intent.putExtra("phoneNumber", getIntent().getStringExtra("phoneNumber"));
        startActivity(intent);
    }

    private void addProductActivity() {
        Intent intent = new Intent(getApplicationContext(), AddNewProduct.class);
        intent.putExtra("admin", getIntent().getStringExtra("fullName"));
        intent.putExtra("phoneNumber", getIntent().getStringExtra("phoneNumber"));
        intent.putExtra("email", getIntent().getStringExtra("email"));
        intent.putExtra("rating", getIntent().getStringExtra("rating"));
        startActivity(intent);
    }
}