package com.piseth.anemi.firebase.repo;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.piseth.anemi.room.viewmodel.PageRoomViewModel;
import com.piseth.anemi.room.viewmodel.UserRoomViewModel;
import com.piseth.anemi.utils.model.Page;
import com.piseth.anemi.utils.model.User;

public class FirebaseUserRepo {
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;

    public FirebaseUserRepo() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("profileImages");
    }



    public void addNewUser(Uri uri, UserRoomViewModel userRoomViewModel, User user, String id) {
        StorageReference profileIamgeRef = storageReference.child(String.valueOf(System.currentTimeMillis()));
        profileIamgeRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.isComplete()) {
                        profileIamgeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                user.setPhoto(uri.toString());
//                                firebaseFirestore.collection("users")
                                firebaseFirestore.collection("Users").document(id).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isComplete()) {
                                            userRoomViewModel.insertUser(user);
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
    }

//    public void getPages(PageRoomViewModel pageRoomViewModel) {
//        firebaseFirestore.collection("pages").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                for(DocumentChange doc : value.getDocumentChanges()) {
//                    if(doc.getType() == DocumentChange.Type.ADDED) {
//                        Page page = doc.getDocument().toObject(Page.class);
//                        pageRoomViewModel.insertPage(page);
//                    }
//                }
//            }
//        });
//    }
}
