package com.example.odooonline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void onPartnerClick(View v){
        Log.d("MENU", "partner button clicked");
    }
    public void onSaleOrderClick(View v){
        Log.d("MENU", "sale order button clicked");
    }
}