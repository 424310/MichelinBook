package com.example.lasttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class CategoryDelete extends AppCompatActivity {

    private Button DeleteBtn, CancelBtn;
    private String key, Name,Address,Number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_delete);

        key = getIntent().getStringExtra("key");
        Name = getIntent().getStringExtra("Name");
        Address = getIntent().getStringExtra("Address");
        Number = getIntent().getStringExtra("Number");

        DeleteBtn = (Button) findViewById(R.id.DeleteBtn);
        CancelBtn = (Button) findViewById(R.id.CancelBtn);

        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FirebaseDatabaseHelper().deleteCategories(key, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Category> categories, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        Toast.makeText(CategoryDelete.this, "Category record has been deleted successfully", Toast.LENGTH_LONG).show();
                        finish(); return;
                    }
                });
            }
        });

        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); return;
            }
        });
    }
}

