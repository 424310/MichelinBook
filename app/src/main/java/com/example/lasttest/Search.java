package com.example.lasttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

public class Search extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    //recyclerView 검색용
    private EditText search;
    private ImageView toolbar_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar_img = (ImageView) findViewById(R.id.toolbar_btn_back);
        toolbar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Search.this, HomeActivity.class);
                startActivity(intent);
            }
        });


        //recyclerView 검색용
        search = (EditText) findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    search(s.toString());
                }
                else{
                    search("");
                }
            }
        });
    }

    //recyclerview 검색용
    private void search(String s) {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_search);
        new FirebaseDatabaseHelper().search(s, new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Category> categories, List<String> keys) {
                new RecyclerView_Config().setConfig(mRecyclerView, Search.this, categories, keys);
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

