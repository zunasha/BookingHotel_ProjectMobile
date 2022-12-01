package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private CardView cvMenu, cvTransaksi, cvRiwayat, cvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        cvMenu = findViewById(R.id.id_menu);
        cvTransaksi = findViewById(R.id.id_transaksi);
        cvRiwayat = findViewById(R.id.id_riwayat);
        cvLogout = findViewById(R.id.id_logout);

        cvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(move);
            }
        });

        cvTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(MainActivity.this, TransactionActivity.class);
                startActivity(move);
            }
        });

        cvRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(move);
            }
        });

        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(move);
                Toast.makeText(getApplicationContext(), "Logout berhasil", Toast.LENGTH_SHORT).show();
            }
        });
    }
}