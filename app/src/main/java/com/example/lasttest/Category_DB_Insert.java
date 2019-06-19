package com.example.lasttest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Category_DB_Insert extends AppCompatActivity {


    //이미지뷰!!(시작)
    private Bitmap bitmap;
    private ImageView imageView, toolbar_img, address_Btn;
    private StorageReference storageRef, mStorageRef;
    private String url;
    //이미지뷰!!(끝)

    // DB에 저장시킬 데이터를 입력받는 EditText
    private EditText editName, editAddress, editNumber;
    String addressString;

    // 데이터를 저장 버튼
    private Button inputBtn;


    // DB 관련 변수
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private String UserId;
    private String AddressString, name, number;
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
        address_Btn = (ImageView) findViewById(R.id.address_Btn);

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
                intent.putExtra("name", editName.getText().toString());
                intent.putExtra("number", editNumber.getText().toString());
                finish();
                startActivity(intent);
            }
        });

        AddressString = getIntent().getStringExtra("addressString");
        name = getIntent().getStringExtra("name");
        number = getIntent().getStringExtra("number");

        editName.setText(name);
        editAddress.setText(AddressString);
        editNumber.setText(number);

        //이미지뷰!!(시작)
        storageRef = FirebaseStorage.getInstance().getReference();

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        toolbar_img = (ImageView) findViewById(R.id.toolbar_btn_back);
        toolbar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });
        //이미지뷰!!(끝)

        // 버튼 리스너 정의
        // 클릭 시 EditText의 내용이 DB에 저장, imageView 이미지가 Storage에 저장
        inputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addCategory(), uploadImage() 코드는 하단에 있음

                //이미지뷰!!!(시작)
                uploadImage(editName.getText().toString().trim(), editAddress.getText().toString().trim(), editNumber.getText().toString().trim());
                //이미지뷰!!!(끝)

                finish();
                Intent intent = new Intent(Category_DB_Insert.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    public void addCategory(String name, String address, String number, String image) {
        Category categories = new Category(name, address, number, image);
        myRef.child(UserId).child("Category").child(name).setValue(categories);
    }

    //이미지뷰!!(시작)
    public void uploadImage(final String name, final String address, final String number) {
        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getUid();
        StorageReference mountainsRef = storageRef.child(UserId).child("Category").child(name + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                mStorageRef = storageRef.child(UserId).child("Category");
                mStorageRef.child(name + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        url = uri.toString();
                        Glide.with(getApplicationContext()).load(url).into(imageView);
                        //Glide.with(this).load("http://www.selphone.co.kr/homepage/img/team/3.jpg").into(imageView);
                        addCategory(name.trim(), address.trim(), number.trim(), url.trim());
                    }
                });
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri image = data.getData();
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
            imageView.setImageBitmap(bitmap);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    //이미지뷰!!(끝)
}