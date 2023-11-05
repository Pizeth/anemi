package com.piseth.anemi.firebase.repo;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.piseth.anemi.utils.model.User;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUserRepo {
    private final CollectionReference userRef;
    private final StorageReference storageReference;

//    private User user;

    public FirebaseUserRepo() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        userRef = firebaseFirestore.collection("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("profileImages");
    }

    public void addNewUser(Uri uri, User user, String id) {
        StorageReference profileImageRef = storageReference.child(user.getUsername());
        profileImageRef.putFile(uri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.isComplete()) {
                    profileImageRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                        user.setAvatar(uri1.toString());
                        setUser(id, user);
                    });
                }
            }
        });
    }

    public void updateUser(Uri uri, User user, String id, boolean isUpdateEmail, boolean isUpdatePassword, String current_password) {
        StorageReference profileImageRef = storageReference.child(user.getUsername() + System.currentTimeMillis());
        if (uri != null) {
            Log.d("SUCCESS", "Change profile picture to " + uri);
            profileImageRef.putFile(uri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.isComplete()) {
                        profileImageRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                            user.setAvatar(uri1.toString());
                            setUser(id, user);
                        });
                    }
                }
            });
        } else {
            setUser(id, user);
        }
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null && currentUser.getUid().equals(id)) {
            updateAuthenticateUser(user.getEmail(), user.getPassword(), isUpdateEmail, isUpdatePassword, current_password);
        }
    }

    private void setUser(String id, User user) {
        userRef.document(id).set(user, SetOptions.merge()).addOnCompleteListener(task12 -> {
            if (task12.isComplete()) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(user.getUsername())
                        .setPhotoUri(Uri.parse(user.getAvatar()))
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

    private void updateAuthenticateUser(String email, String password, boolean isUpdateEmail, boolean isUpdatePassword, String current_password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Get auth credentials from the user for re-authentication.
        AuthCredential credential;
        if (user != null) {
            credential = EmailAuthProvider.getCredential(user.getEmail(), current_password);

            // Prompt the user to re-provide their sign-in credentials
            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if(isUpdateEmail) {
                        user.updateEmail(email).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.d("Update Credential", "Email updated");
                            } else {
                                Log.d("Update Credential", "Error email not updated");
                            }
                        });
                    }
                    if(isUpdatePassword) {
                        user.updatePassword(password).addOnCompleteListener(task12 -> {
                            if (task12.isSuccessful()) {
                                Log.d("Update Credential", "Password updated");
                            } else {
                                Log.d("Update Credential", "Error password not updated");
                            }
                        });
                    }
                } else {
                    Log.d("Update Credential", "Error auth failed");
                }
            });
        }
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
        return userRef.whereEqualTo("isDeleted", 0).orderBy("userRoleId", Query.Direction.DESCENDING).orderBy("username", Query.Direction.ASCENDING);
    }
}
