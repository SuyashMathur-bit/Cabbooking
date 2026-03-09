package com.example.cabbooking;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BookSucessfull extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_sucessfull);
        ImageView ivTick = findViewById(R.id.ivTick);
        TextView tvMessage = findViewById(R.id.tvMessage);


        tvMessage.setText("✅ BOOKING SUCCESSFUL");

    }
}