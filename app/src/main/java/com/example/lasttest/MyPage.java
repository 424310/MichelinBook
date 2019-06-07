package com.example.lasttest;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

public class MyPage extends AppCompatActivity {

    private ImageView imageView;

    private String UserId, name, url;

    private FirebaseStorage storage;
    private FirebaseAuth mAuth;
    private StorageReference storageRef, mstorageRef;
    private Button image_insert_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        imageView = (ImageView) findViewById(R.id.ivProfile);

        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getDisplayName();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        mstorageRef = storageRef.child(UserId);

        mstorageRef.child("profile"+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                url = uri.toString();
                Glide.with(getApplicationContext()).load(url).into(imageView);
            }
        });

        image_insert_button = (Button) findViewById(R.id.image_insert_button);
        image_insert_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPage.this, MyPage_DB_Insert.class);
                startActivity(intent);
            }
        });
    }
}
