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

public class menu_FirebaseDatabaseHelper {
    private String UserId, key;
    public FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef, myRef;
    private List<Menu> menus = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Menu> menus, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }
    public menu_FirebaseDatabaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getUid();
        //카테고리 이름(key)은 어떻게 가져와야할까? 이렇게!
        CategoryView categoryView = new CategoryView();
        key = categoryView.getCategoryView();

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference(UserId);
        myRef = databaseRef.child("Menu").child(key);
    }

    public void readMenus(final DataStatus dataStatus){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                menus.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Menu menu = keyNode.getValue(Menu.class);
                    menus.add(menu);
                }
                dataStatus.DataIsLoaded(menus, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateMunu(String key, Menu menu, final DataStatus dataStatus){
        myRef.child(key).setValue(menu).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsUpdated();
            }
        });
    }

    public void deleteMunu(String key, final DataStatus dataStatus){
        myRef.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }

    public void deleteAllMunu(String key, final DataStatus dataStatus){
        databaseRef.child("Menu").child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }
}
