package com.example.lasttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class CategoryListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_categories);
        new FirebaseDatabaseHelper().readCategories(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Category> categories, List<String> keys) {
                new RecyclerView_Config().setConfig(mRecyclerView, CategoryListActivity.this, categories, keys);
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

