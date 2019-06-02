package com.example.lasttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CategoryView extends AppCompatActivity {

    private TextView textView1, textView2, textView3;
    private String Name, Address, Number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);

        Intent intent = getIntent();

        Name = intent.getStringExtra("Name");
        Address = intent.getStringExtra("Address");
        Number = intent.getStringExtra("Number");

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);

        textView1.setText(Name);
        textView2.setText(Address);
        textView3.setText(Number);
    }
}