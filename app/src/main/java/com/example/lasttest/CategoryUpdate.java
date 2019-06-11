package com.example.lasttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class CategoryUpdate extends AppCompatActivity {

    private EditText editName, editAddress, editNumber;
    private Button UpdateBtn, CancelBtn;
    private String key, Name,Address,Number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_update);

        key = getIntent().getStringExtra("key");
        Name = getIntent().getStringExtra("Name");
        Address = getIntent().getStringExtra("Address");
        Number = getIntent().getStringExtra("Number");

        editName = (EditText) findViewById(R.id.edit_name);
        editAddress = (EditText) findViewById(R.id.edit_address);
        editNumber = (EditText) findViewById(R.id.edit_number);

        UpdateBtn = (Button) findViewById(R.id.UpdateBtn);
        CancelBtn = (Button) findViewById(R.id.CancelBtn);

        editName.setText(Name);
        editAddress.setText(Address);
        editNumber.setText(Number);

        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category category = new Category();
                category.setName(editName.getText().toString());
                category.setAddress(editAddress.getText().toString());
                category.setNumber(editNumber.getText().toString());

                new FirebaseDatabaseHelper().updateCategories(key, category, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Category> categories, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {
                        Toast.makeText(CategoryUpdate.this, "Category record has been updated successfully", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void DataIsDeleted() {

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

