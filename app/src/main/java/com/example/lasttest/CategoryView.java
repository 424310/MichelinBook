package com.example.lasttest;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class CategoryView extends AppCompatActivity {

    private RecyclerView mRecyclerView, pRecyclerView;

    public ImageView imageView, toolbar_img, Delete, menu_insert, Update, menu_open;
    private TextView textView1, textView2, textView3;
    public String Name, Address, Number, url;
    public FloatingActionButton post_insert;

    private boolean isMenuOpen = false;


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

        toolbar_img = (ImageView) findViewById(R.id.toolbar_btn_back);
        toolbar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Update = (ImageView) findViewById(R.id.Update);
        Delete = (ImageView) findViewById(R.id.Delete);
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

        menu_insert = (ImageView) findViewById(R.id.menu_insert);
        menu_open = (ImageView) findViewById(R.id.menu_open);


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
                    if (isMenuOpen) {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        menu_open.setImageResource(R.drawable.ic_menu_close);
                        isMenuOpen = false;
                    } else {
                        mRecyclerView.setVisibility(View.GONE);
                        menu_open.setImageResource(R.drawable.ic_menu_open);
                        isMenuOpen = true;
                    }
                }
            }
        };
        menu_open.setOnClickListener(listener);

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

        post_insert = (FloatingActionButton) findViewById(R.id.post_insert);
        post_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryView.this, Post_DB_Insert.class);
                startActivity(intent);
            }
        });

        pRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_posts);
        new post_FirebaseDatabaseHelper().readPosts(new post_FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Post> posts, List<String> keys) {
                new post_RecyclerView_Config().setConfig(pRecyclerView, CategoryView.this, posts, keys);
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

    }
}