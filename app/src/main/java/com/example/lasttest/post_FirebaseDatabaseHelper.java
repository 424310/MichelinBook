package com.example.lasttest;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class post_FirebaseDatabaseHelper {
    private String UserId, key;
    public FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef, myRef;
    private List<Post> posts = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Post> posts, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }
    public post_FirebaseDatabaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getUid();
        //카테고리 이름(key)은 어떻게 가져와야할까? 이렇게!
        CategoryView categoryView = new CategoryView();
        key = categoryView.getCategoryView();

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference(UserId);
        myRef = databaseRef.child("Post").child(key);
    }

    public void readPosts(final DataStatus dataStatus){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posts.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Post post = keyNode.getValue(Post.class);
                    posts.add(post);
                }
                dataStatus.DataIsLoaded(posts, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updatePost(String key, Post post, final DataStatus dataStatus){
        myRef.child(key).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsUpdated();
            }
        });
    }

    public void deletePost(String key, final DataStatus dataStatus){
        myRef.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }

    public void deleteAllPost(String key, final DataStatus dataStatus){
        databaseRef.child("Post").child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }
}
