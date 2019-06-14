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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.io.IOException;
import java.util.List;

public class CategoryUpdate extends AppCompatActivity {

    //이미지뷰!!(시작)
    private Bitmap bitmap;
    private ImageView imageView;
    private StorageReference storageRef, mStorageRef;
    private String url;
    //이미지뷰!!(끝)

    // DB 관련 변수
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private String UserId;

    // 사용자 정보 가져오려고
    private FirebaseAuth mAuth;

    private EditText editName, editAddress, editNumber;
    private Button UpdateBtn, CancelBtn;
    private String key, Name,Address,Number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_update);

        // DB 관련 변수 초기화
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //해당 User 값 받아오기
        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getDisplayName();

        key = getIntent().getStringExtra("key");
        Name = getIntent().getStringExtra("Name");
        Address = getIntent().getStringExtra("Address");
        Number = getIntent().getStringExtra("Number");
        url = getIntent().getStringExtra("Image");

        editName = (EditText) findViewById(R.id.edit_name);
        editAddress = (EditText) findViewById(R.id.edit_address);
        editNumber = (EditText) findViewById(R.id.edit_number);

        imageView = (ImageView) findViewById(R.id.imageView);

        UpdateBtn = (Button) findViewById(R.id.UpdateBtn);
        CancelBtn = (Button) findViewById(R.id.CancelBtn);

        editName.setText(Name);
        editAddress.setText(Address);
        editNumber.setText(Number);
        Glide.with(this).load(url).into(imageView);


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

        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Category category = new Category();
                category.setName(editName.getText().toString());
                category.setAddress(editAddress.getText().toString());
                category.setNumber(editNumber.getText().toString());
                category.setImage(url);
                */
                uploadImage(editName.getText().toString().trim(), editAddress.getText().toString().trim(), editNumber.getText().toString().trim());

            }
        });

        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); return;
            }
        });
    }

    public void addCategory(final String name, final String address, final String number, final String image){
        Category categories = new Category(name, address, number, image);
        myRef.child(UserId).child("Category").child(name).setValue(categories);
        new FirebaseDatabaseHelper().updateCategories(key, categories, new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Category> categories, List<String> keys) {

            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {
                Toast.makeText(CategoryUpdate.this, "Category record has been updated successfully", Toast.LENGTH_LONG).show();
                finish();
                Intent intent =  new Intent(CategoryUpdate.this, CategoryView.class);

                intent.putExtra("key", key);
                intent.putExtra("Name", name);
                intent.putExtra("Address", address);
                intent.putExtra("Number",number);
                intent.putExtra("Image", image);

                startActivity(intent);
            }

            @Override
            public void DataIsDeleted() {

            }
        });
    }

    //이미지뷰!!(시작)
    public void uploadImage(final String name, final String address, final String number){
        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getUid();
        StorageReference mountainsRef = storageRef.child(UserId).child("Category").child(name+".jpg");

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
                mStorageRef.child(name+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

