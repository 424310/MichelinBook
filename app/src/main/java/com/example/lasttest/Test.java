package com.example.lasttest;

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

public class Test extends AppCompatActivity {

    // DB에 저장시킬 데이터를 입력받는 EditText
    private EditText editName, editAddress, editNumber;

    // 데이터를 저장, 수정 버튼
    private Button inputBtn, updateBtn;

    // DB 관련 변수
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private String UserId;

    // 사용자 정보(email) 가져오려고
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // 변수 초기화
        editName = (EditText) findViewById(R.id.edit_name);
        editAddress = (EditText) findViewById(R.id.edit_address);
        editNumber = (EditText) findViewById(R.id.edit_number);
        inputBtn = (Button) findViewById(R.id.inputBtn);
        updateBtn = (Button) findViewById(R.id.updateBtn);

        // DB 관련 변수 초기화
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Category");

        //해당 User 값 받아오기
        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getDisplayName();

        // 버튼 리스너 정의
        // 클릭 시 EditText의 내용이 DB에 저장
        inputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addCategory() 코드는 하단에 있음
                addCategory(editName.getText().toString().trim(), editAddress.getText().toString().trim(), editNumber.getText().toString().trim());
            }
        });

        // 클릭 시 수정된 내용이 DB에 저장
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updateCategory() 코드는 하단에 있음
                updateCategory(editName.getText().toString().trim(), editAddress.getText().toString().trim(), editNumber.getText().toString().trim());
            }
        });

    }

    public void addCategory(String name, String address, String number){
        Category categories = new Category(name, address, number);
        myRef.child("Users").child(UserId).child(name).setValue(categories);
    }

    public void updateCategory(String name, String address, String number){
        myRef.child("Users").child(UserId).child(name).child("name").setValue(name);
        myRef.child("Users").child(UserId).child(name).child("address").setValue(address);
        myRef.child("Users").child(UserId).child(name).child("number").setValue(number);
    }

}