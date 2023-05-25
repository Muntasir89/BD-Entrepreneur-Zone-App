package com.example.bdentepreneuerzone.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.bdentepreneuerzone.R;


public class BankAct extends AppCompatActivity {
    TextView BankTV, AddressTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        BankTV = findViewById(R.id.BankTV);
        AddressTV = findViewById(R.id.AddressTV);

        BankTV.setText(getIntent().getStringExtra("Bank"));
        AddressTV.setText(getIntent().getStringExtra("Address"));
    }
}