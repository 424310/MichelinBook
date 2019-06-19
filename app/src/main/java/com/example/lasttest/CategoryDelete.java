package com.example.lasttest;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class CategoryDelete extends AppCompatActivity {

    //이미지 삭제용!!(시작)
    private StorageReference storageRef, mStorageRef, post_storageRef, post_mStorageRef;
    private String UserId;
    private FirebaseAuth mAuth;
    //이미지 삭제용!!(끝)

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

                    }
                });

                new menu_FirebaseDatabaseHelper().deleteAllMunu(key, new menu_FirebaseDatabaseHelper.DataStatus() {
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

                    }
                });

                new post_FirebaseDatabaseHelper().deleteAllPost(key, new post_FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Post> posts, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        finish(); return;
                    }
                });

                //카테고리 이미지 삭제용!! (시작)
                storageRef = FirebaseStorage.getInstance().getReference();
                mAuth = FirebaseAuth.getInstance();
                UserId = mAuth.getCurrentUser().getUid();
                mStorageRef = storageRef.child(UserId).child("Category").child(key + ".jpg");
                mStorageRef.delete();
                //카테고리 이미지 삭제용!! (끝)

                //게시글 이미지 삭제용!! (시작)
                post_storageRef = FirebaseStorage.getInstance().getReference();
                post_mStorageRef = post_storageRef.child(UserId).child("Post").child(key);
                post_mStorageRef.delete();
                //게시글 이미지 삭제용!! (끝)

                finish();

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

