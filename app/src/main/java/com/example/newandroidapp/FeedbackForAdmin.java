package com.example.newandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FeedbackForAdmin extends AppCompatActivity {
    private EditText descriptionForAdmin;
    private RatingBar ratingForAdmin;
    private Button done;
    private TextView emailDesno, telefonDesno;
    private String emailAdmin, phoneNumberOfAdmin, title;
    private float sumaRejting,rejting;
    private Integer brojRejting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_for_admin);

        emailDesno = findViewById(R.id.emailAdminRight);
        telefonDesno = findViewById(R.id.phoneNumberAdminRight);
        descriptionForAdmin = findViewById(R.id.descriptionAdminText);
        ratingForAdmin = findViewById(R.id.ratingAdminText);
        done = findViewById(R.id.doneButton);

        HashMap<String, Object> mapNaracka = new HashMap<>();
        HashMap<String, Object> mapUser = new HashMap<>();
        Intent intent = getIntent();
        emailAdmin = intent.getStringExtra("email");
        phoneNumberOfAdmin = intent.getStringExtra("phoneNumber");
        title = intent.getStringExtra("title");

        emailDesno.setText(emailAdmin);
        telefonDesno.setText(phoneNumberOfAdmin);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance()
                        .getReference("Products").orderByChild("pname").equalTo(title).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap: snapshot.getChildren()){
                            mapNaracka.put("Status", "Order received");
                            mapNaracka.put("DescriptionForAdmin", descriptionForAdmin.getText().toString());
                            mapNaracka.put("RatingForAdmin", ratingForAdmin.getRating());
                            FirebaseDatabase.getInstance()
                                    .getReference("Products").child(snap.getKey()).updateChildren(mapNaracka);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                FirebaseDatabase.getInstance()
                        .getReference("Users").orderByChild("email").equalTo(emailAdmin).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap: snapshot.getChildren()){
                            sumaRejting = Integer.parseInt(snap.child("SumaRejting").getValue().toString());
                            brojRejting = Integer.parseInt(snap.child("BrojNaRejting").getValue().toString());
                        }
                        sumaRejting += ratingForAdmin.getRating();
                        brojRejting += 1;
                        rejting = sumaRejting/brojRejting;
                        mapUser.put("BrojNaRejting", brojRejting);
                        mapUser.put("SumaRejting", sumaRejting);
                        mapUser.put("rating", rejting);
                        for(DataSnapshot snap: snapshot.getChildren()){
                            FirebaseDatabase.getInstance()
                                    .getReference("Users").child(snap.getKey()).updateChildren(mapUser);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                startActivity(new Intent(FeedbackForAdmin.this,FeedbackConfirm.class));
            }
        });
    }
}