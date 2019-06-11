package com.example.lasttest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class Menu_DB_Insert extends AppCompatActivity {

    private EditText edit_menuName, edit_menuPrice, edit_menuComment;
    private Button InsertBtn, CancelBtn;
    private String UserId, name;

    // DB 관련 변수
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__db__insert);

        edit_menuName = (EditText) findViewById(R.id.edit_menuName);
        edit_menuPrice = (EditText) findViewById(R.id.edit_menuPrice);
        edit_menuComment = (EditText) findViewById(R.id.edit_menuComment);
        InsertBtn = (Button) findViewById(R.id.InsertBtn);
        CancelBtn = (Button) findViewById(R.id.CancelBtn);

        //해당 User 값 받아오기
        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getDisplayName();

        //카테고리 이름 받아오기
        CategoryView categoryView = new CategoryView();
        name = categoryView.getCategoryView();

        // 파이어베이스 입력 경로
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        // 클릭 시 EditText의 내용이 DB에 저장
        InsertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMenu(edit_menuName.getText().toString().trim(), edit_menuPrice.getText().toString().trim(), edit_menuComment.getText().toString().trim());
                finish(); return;
            }
        });

        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); return;
            }
        });

    }

    public void addMenu(String menu_name, String menu_price, String menu_comment){
        Menu menus = new Menu(menu_name, menu_price,menu_comment);
        myRef.child(UserId).child("Menu").child(name).child(menu_name).setValue(menus);
    }
}
