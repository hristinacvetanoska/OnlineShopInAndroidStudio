package com.example.newandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button signIn;
    TextInputLayout emailUser, passwordUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView register = findViewById(R.id.register);
        register.setOnClickListener(this);
        emailUser = findViewById(R.id.email);

        passwordUser = findViewById(R.id.password);

        signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, Register.class));
                break;
            case R.id.signIn:
                loginUser();
                break;
        }
    }
    public void loginUser() {

        if (!validateFields()) {
            return;
        } else {

            final String userEmail = emailUser.getEditText().getText().toString().trim();
            final String userPassword = passwordUser.getEditText().getText().toString().trim();

            Query checkUser = FirebaseDatabase.getInstance()
                    .getReference("Users").orderByChild("password").equalTo(userPassword);

            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        String emailFromDB = snapshot.child(userPassword).child("email").getValue().toString();

                        if (userEmail.equals(emailFromDB)) {

                            emailUser.setError(null);
                            emailUser.setErrorEnabled(false);

                            String fullNameFromDB = snapshot.child(userPassword).child("fullName").getValue(String.class);
                            String passwordFromDB = snapshot.child(userPassword).child("password").getValue(String.class);
                            String phoneNumberFromDB = snapshot.child(userPassword).child("phoneNumber").getValue(String.class);
                            String roleFromDB = snapshot.child(userPassword).child("role").getValue(String.class);
                            String rating = snapshot.child(userPassword).child("rating").getValue().toString();

                            Toast.makeText(MainActivity.this, fullNameFromDB + "\n" + emailFromDB + "\n" + phoneNumberFromDB + "\n" + roleFromDB, Toast.LENGTH_SHORT).show();

                            if(roleFromDB.equals("Admin")){
                                Intent intent = new Intent(getApplicationContext(), Seller.class);

                                intent.putExtra("fullName", fullNameFromDB);
                                intent.putExtra("phoneNumber", phoneNumberFromDB);
                                intent.putExtra("email", emailFromDB);
                                intent.putExtra("password", passwordFromDB);
                                intent.putExtra("role", roleFromDB);
                                intent.putExtra("rating", rating);

                                startActivity(intent);
                            }
                            if(roleFromDB.equals("User")){
                                Intent intent = new Intent(getApplicationContext(), UserScreen.class);

                                intent.putExtra("fullName", fullNameFromDB);
                                intent.putExtra("phoneNumber", phoneNumberFromDB);
                                intent.putExtra("email", emailFromDB);
                                intent.putExtra("password", passwordFromDB);
                                intent.putExtra("role", roleFromDB);

                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Не постои таква улога!!!!", Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            Toast.makeText(MainActivity.this, "Не постои таков корисник!!!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Пасвордот не се совпаѓа!!!", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    private  boolean  validateFields(){
        String userEmail = emailUser.getEditText().getText().toString().trim();
        String userPassword = passwordUser.getEditText().getText().toString().trim();
        if(userEmail.isEmpty()){
            emailUser.setError("Мора да внесете емаил адреса!");
            return false;
        }
        if(userPassword.isEmpty()){
            passwordUser.setError("Мора да внесете пасворд!");
            return false;
        }
        else {
            passwordUser.setError(null);
            passwordUser.setErrorEnabled(false);

            emailUser.setError(null);
            emailUser.setErrorEnabled(false);
            return true;
        }
    }
}