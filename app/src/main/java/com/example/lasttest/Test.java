package com.example.lasttest;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Test extends AppCompatActivity {

    // DB에 저장시킬 데이터를 입력받는 EditText
    private EditText editName, editAddress, editNumber;

    // 데이터를 저장, 수정 버튼
    private Button inputBtn, updateBtn;

    // DB 데이터를 보여줄 리스트뷰, 어댑터
    private  ListView listView;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();

    // DB 관련 변수
    private DatabaseReference myRef;
    private FirebaseDatabase database;

    // 사용자 정보(이름) 가져오려고
    private String UserId;
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
        listView = (ListView) findViewById(R.id.listView);

        // DB 관련 변수 초기화
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Category");

        // 리스트뷰에 출력할 데이터 초기화
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,new ArrayList<String>());

        // 리스트뷰에 어댑터 붙여줌
        listView.setAdapter(adapter);

        //해당 User 이름 받아오기
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


        // 자신이 얻은 Reference(myRef)에 이벤트를 붙여줌
        /* addValueEventListener는 데이터를 읽어오거나 변화가 감지되었을 때 사용하는 메소드로,
           이 리스너를 통해서 콜백되는 함수가 onDataChange()이다.
           이 메소드에 담긴 snapshot으로 데이터를 읽어올 수가 있다 */
        // ValueEventListener는 경로 전체 내용에 대한 변경을 읽고 수신 대기
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            // onDataChange()에서 해줘야 할 일은 해당 레퍼런스("Category" 레퍼런스)의 스냅샷을 가져와 자식노드를 반복문을 통해 하나씩 꺼냄

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 리스트 초기화(데이터가 바뀔 때마다 같은 내용이 반복되어 저장되는 것을 방지)
                adapter.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String C_snap = snapshot.getValue().toString();
                    Array.add(C_snap);
                    adapter.add(C_snap);
                }
                // 어댑터에 리스트가 바뀌었다는 것을 알림
                adapter.notifyDataSetChanged();
                // 리스트뷰의 위치를 마지막으로 보내주기 위함
                listView.setSelection(adapter.getCount() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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