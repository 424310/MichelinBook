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
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static java.lang.Thread.sleep;

public class Post_DB_Insert extends AppCompatActivity {

    private Bitmap bitmap;
    private StorageReference storageRef, mStorageRef;

    private DatabaseReference myRef;
    private FirebaseDatabase database;

    private FirebaseAuth mAuth;
    private String UserId, name, url;

    private EditText edit_content;
    private ImageView imageView;
    private Button inputBtn;

    // 현재 시간 가져오기용
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post__db__insert);

        // 변수 초기화
        edit_content = (EditText) findViewById(R.id.edit_content);
        inputBtn = (Button) findViewById(R.id.inputBtn);

        // DB 관련 변수 초기화
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //해당 User 값 받아오기
        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getUid();

        //카테고리 이름 받아오기
        CategoryView categoryView = new CategoryView();
        name = categoryView.getCategoryView();

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
                //addPost(), uploadImage(), getTime() 코드는 하단에 있음

                //이미지뷰!!!(시작)
                uploadImage(getTime().trim(), edit_content.getText().toString().trim());
                //이미지뷰!!!(끝)

                finish(); return;
            }
        });

    }

    public void addPost(String date, String content, String image){
        Post posts = new Post(date, content, image);
        myRef.child(UserId).child("Post").child(name).child(date).setValue(posts);
    }

    //이미지뷰!!(시작)
    public void uploadImage(final String date, final String content){
        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getUid();
        StorageReference mountainsRef = storageRef.child(UserId).child("Post").child(name).child(date+".jpg");

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
                mStorageRef = storageRef.child(UserId).child("Post");
                mStorageRef.child(name).child(date+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        url = uri.toString();
                        Glide.with(getApplicationContext()).load(url).into(imageView);
                        //Glide.with(this).load("http://www.selphone.co.kr/homepage/img/team/3.jpg").into(imageView);
                        addPost(date.trim(), content.trim(), url.trim());
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

    //현재 시간 가져오기용
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

}
