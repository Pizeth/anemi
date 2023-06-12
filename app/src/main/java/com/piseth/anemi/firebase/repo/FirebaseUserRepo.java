package com.piseth.anemi.firebase.repo;

import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.piseth.anemi.room.viewmodel.UserRoomViewModel;
import com.piseth.anemi.utils.model.User;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUserRepo {
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference userRef;
    private StorageReference storageReference;

    private User user;

    public FirebaseUserRepo() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        userRef = firebaseFirestore.collection("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("profileImages");
    }


    public void addNewUser(Uri uri, UserRoomViewModel userRoomViewModel, User user, String id) {
        StorageReference profileImageRef = storageReference.child(user.getUsername() + System.currentTimeMillis());
        profileImageRef.putFile(uri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.isComplete()) {
                    profileImageRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                        user.setPhoto(uri1.toString());
                        userRef.document(id).set(user).addOnCompleteListener(task12 -> {
                            if (task12.isComplete()) {
                                userRoomViewModel.insertUser(user);
                                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(user.getUsername())
                                        .setPhotoUri(uri1)
                                        .build();
                                if (firebaseUser != null) {
                                    firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Log.d("ADD NEW USER", "User profile updated.");
                                        }
                                    });
                                }
                            }
                        });
                    });
                }
            }
        });
    }

    public void updateUser(Uri uri, UserRoomViewModel userRoomViewModel, User user, String id) {
        StorageReference profileImageRef = storageReference.child(user.getUsername() + System.currentTimeMillis());
        if (uri != null) {
            Log.d("SUCCESS", "Change profile picture to " + uri);
            profileImageRef.putFile(uri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.isComplete()) {
                        profileImageRef.getDownloadUrl().addOnSuccessListener(uri1 -> user.setPhoto(uri1.toString()));
                        Log.d("SUCCESS", "Image Uri " + user.getPhoto());
                    }
                }
            });
        }
        Log.d("SUCCESS", "THE ID IS " + id);

        userRef.document(id).set(user, SetOptions.merge()).addOnCompleteListener(task -> {
            if (task.isComplete()) {
                userRoomViewModel.updateUser(user);
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(user.getUsername())
                        .setPhotoUri(uri)
                        .build();
                if (firebaseUser != null) {
                    firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Log.d("UPDATE USER", "User profile updated.");
                        }
                    });
                }
            }
        });

    }

    public void deleteUser(String id) {
        userRef.document(id).update("isDeleted", 1);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRef.whereEqualTo("isDeleted", 0).addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }
            if (value != null) {
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    User user = documentSnapshot.toObject(User.class);
                    users.add(user);
                }
            }
        });
        return users;
    }

    public Query getAllUsersQuery() {
        return userRef.whereEqualTo("isDeleted", 0);
    }

    public User getUser(String id) {
        userRef.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user = document.toObject(User.class);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return  user;
    }
}
