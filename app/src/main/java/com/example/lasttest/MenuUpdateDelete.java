package com.example.lasttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class MenuUpdateDelete extends AppCompatActivity {

    private EditText menuName, menuPrice, menuComment;
    private Button menuUpdateBtn, menuDeleteBtn;
    private String key, Menu_name, Menu_price, Menu_comment;
    private ImageView toolbar_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_update_delete);

        key = getIntent().getStringExtra("key");
        Menu_name = getIntent().getStringExtra("Menu_name");
        Menu_price = getIntent().getStringExtra("Menu_price");
        Menu_comment = getIntent().getStringExtra("Menu_comment");

        menuName = (EditText) findViewById(R.id.edit_menuName);
        menuPrice = (EditText) findViewById(R.id.edit_menuPrice);
        menuComment = (EditText) findViewById(R.id.edit_menuComment);

        menuUpdateBtn = (Button) findViewById(R.id.menuUpdateBtn);
        menuDeleteBtn = (Button) findViewById(R.id.menuDeleteBtn);

        menuName.setText(Menu_name);
        menuPrice.setText(Menu_price);
        menuComment.setText(Menu_comment);

        toolbar_img = (ImageView) findViewById(R.id.toolbar_btn_back);
        toolbar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        menuUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Menu menu = new Menu();
                menu.setMenu_name(menuName.getText().toString());
                menu.setMenu_price(menuPrice.getText().toString());
                menu.setMenu_comment(menuComment.getText().toString());

                new menu_FirebaseDatabaseHelper().updateMunu(key, menu, new menu_FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Menu> menus, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {
                        //Toast.makeText(MenuUpdateDelete.this, "Menu record has been updated successfully", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
        });

        menuDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new menu_FirebaseDatabaseHelper().deleteMunu(key, new menu_FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Menu> menus, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        //Toast.makeText(MenuUpdateDelete.this, "Menu record has been deleted successfully", Toast.LENGTH_LONG).show();
                        finish(); return;
                    }
                });
            }
        });
    }
}
