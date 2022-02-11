package com.example.newandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FeedbackConfirm extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_confirm);

    }

    public void onClick(View v){
        if(v.getId() == R.id.continueShopping){
            startActivity(new Intent(this, UserScreen.class));
        }
    }

}