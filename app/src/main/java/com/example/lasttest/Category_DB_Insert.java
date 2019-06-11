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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class Category_DB_Insert extends AppCompatActivity {

    // DB에 저장시킬 데이터를 입력받는 EditText
    private EditText editName, editAddress, editNumber;
    private TextView address;
    String addressString;

    // 데이터를 저장 버튼
    private Button inputBtn;
    private Button address_Btn;

    // DB 관련 변수
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private String UserId;

    // 사용자 정보 가져오려고
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category__db__insert);

        // 변수 초기화
        editName = (EditText) findViewById(R.id.edit_name);
        editAddress = (EditText) findViewById(R.id.edit_address);
        editNumber = (EditText) findViewById(R.id.edit_number);
        inputBtn = (Button) findViewById(R.id.inputBtn);
        address_Btn = (Button) findViewById(R.id.address_Btn);
        address = (TextView) findViewById(R.id.address) ;

        // DB 관련 변수 초기화
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //해당 User 값 받아오기
        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getUid();

        address_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Category_DB_Insert.this, Map.class);
                startActivity(intent);
            }
        });

        // 버튼 리스너 정의
        // 클릭 시 EditText의 내용이 DB에 저장
        inputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addCategory() 코드는 하단에 있음
                addCategory(editName.getText().toString().trim(), editAddress.getText().toString().trim(), editNumber.getText().toString().trim());
                finish();
                Intent intent = new Intent(Category_DB_Insert.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        
    }

    public void addCategory(String name, String address, String number){
        Category categories = new Category(name, address, number);
        myRef.child(UserId).child("Category").child(name).setValue(categories);
    }
}
