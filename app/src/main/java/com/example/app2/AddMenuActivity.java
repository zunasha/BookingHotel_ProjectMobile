package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AddMenuActivity extends AppCompatActivity {

    private TextView tvTambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);
        getSupportActionBar().hide();

        tvTambah = findViewById(R.id.buttonTambah);

        tvTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(AddMenuActivity.this, MenuActivity.class);
                startActivity(move);
            }
        });
    }
}