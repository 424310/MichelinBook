package com.example.lasttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CategoryView extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    public ImageView imageView;
    private TextView textView1, textView2, textView3;
    public String Name, Address, Number, url;
    public Button Update, Delete, menu_insert, menu_open, menu_close;

    public static String key = "key";
    public CategoryView() {

    }
    public String getCategoryView() {
        return key;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);

        Intent intent = getIntent();

        Name = intent.getStringExtra("Name");
        Address = intent.getStringExtra("Address");
        Number = intent.getStringExtra("Number");
        url = intent.getStringExtra("Image");
        key = intent.getStringExtra("key");

        imageView = (ImageView) findViewById(R.id.imageView);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);

        textView1.setText(Name);
        textView2.setText(Address);
        textView3.setText(Number);
        Glide.with(this).load(url).into(imageView);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_menus);
        new menu_FirebaseDatabaseHelper().readMenus(new menu_FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Menu> menus, List<String> keys) {
                new menu_RecyclerView_Config().setConfig(mRecyclerView, CategoryView.this, menus, keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });

        Update = (Button) findViewById(R.id.Update);
        Delete = (Button) findViewById(R.id.Delete);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent =  new Intent(CategoryView.this, CategoryUpdate.class);

                intent.putExtra("key", key);
                intent.putExtra("Name", textView1.getText().toString());
                intent.putExtra("Address",  textView2.getText().toString());
                intent.putExtra("Number", textView3.getText().toString());
                intent.putExtra("Image", url);

                startActivity(intent);
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent =  new Intent(CategoryView.this, CategoryDelete.class);

                intent.putExtra("key", key);
                intent.putExtra("Name", textView1.getText().toString());
                intent.putExtra("Address",  textView2.getText().toString());
                intent.putExtra("Number", textView3.getText().toString());
                intent.putExtra("Image", url);

                startActivity(intent);
            }
        });

        menu_insert = (Button) findViewById(R.id.menu_insert);
        menu_open = (Button) findViewById(R.id.menu_open);
        menu_close = (Button) findViewById(R.id.menu_close);

        menu_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryView.this, Menu_DB_Insert.class);
                startActivity(intent);
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.menu_open) {
                    //menu_open를 클릭하게 되면, mRecyclerView는 VISIBLE로 set함으로써 보이게 됨
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else if (v.getId() == R.id.menu_close) {
                    //menu_close를 클릭하게 되면, mRecyclerView는 GONE으로 set함으로써 안 보이게 됨(공간도 차지X)
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
        };
        menu_open.setOnClickListener(listener);
        menu_close.setOnClickListener(listener);
    }
}