package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TransactionListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        getSupportActionBar().hide();
    }
}