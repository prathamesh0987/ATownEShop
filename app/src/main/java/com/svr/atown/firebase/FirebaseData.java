package com.svr.atown.firebase;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseData {
    public static FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    public static DatabaseReference getUserList() {
        return FirebaseDatabase.getInstance().getReference().child("userList");
    }

    public static StorageReference getStorage() {
        FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
        return firebaseStorage.getReference();
    }

    public static DatabaseReference getProductReference() {
        return FirebaseDatabase.getInstance().getReference().child("product");
    }

    public static DatabaseReference getOrderReference() {
        return FirebaseDatabase.getInstance().getReference().child("order");
    }

    public static DatabaseReference getAdvertisementReference() {
        return FirebaseDatabase.getInstance().getReference().child("advertisement");
    }

    public static StorageReference getAdvStorage() {
        return FirebaseData.getStorage().child("advertisement");
    }
}
