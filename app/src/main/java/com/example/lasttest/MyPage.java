package com.example.lasttest;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

public class MyPage extends AppCompatActivity {

    private ImageView imageView, toolbar_img;

    private String UserId, name, url;

    private FirebaseStorage storage;
    private FirebaseAuth mAuth;
    private StorageReference storageRef, mstorageRef;
    private Button image_insert_button;
    private TextView nameTextView;
    private  TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        imageView = (ImageView) findViewById(R.id.ivProfile);
        toolbar_img = (ImageView) findViewById(R.id.toolbar_btn_back);

        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getDisplayName();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        mstorageRef = storageRef.child(UserId);

        mAuth = FirebaseAuth.getInstance();

        nameTextView = (TextView) findViewById(R.id.header_name_textView);
        emailTextView = (TextView) findViewById(R.id.header_email_textView);

        nameTextView.setText(mAuth.getCurrentUser().getDisplayName());
        emailTextView.setText(mAuth.getCurrentUser().getEmail());

        mstorageRef.child("profile"+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                url = uri.toString();
                Glide.with(getApplicationContext()).load(url).into(imageView);
            }
        });

        toolbar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(MyPage.this, HomeActivity.class);
                startActivity(intent);
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
