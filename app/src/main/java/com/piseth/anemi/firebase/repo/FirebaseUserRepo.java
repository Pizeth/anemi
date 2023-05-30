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
import com.piseth.anemi.utils.model.Page;

public class FirebaseUserRepo {
    private FirebaseFirestore firebaseFirestore;

    public FirebaseUserRepo() {
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void uploadPage(Uri uri, PageRoomViewModel pageRoomViewModel) {
//        StorageReference pageRef = storageReference.child(String.valueOf(System.currentTimeMillis()));
//        pageRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                if(task.isSuccessful()) {
//                    if(task.isComplete()) {
//                        pageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                Page page = new Page();
//                                page.setImageURL(uri.toString());
//                                firebaseFirestore.collection("pages").add(page).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<DocumentReference> task) {
//                                        if(task.isComplete()) {
//                                            pageRoomViewModel.insertPage(page);
//                                        }
//                                    }
//                                });
//                            }
//                        });
//                    }
//                }
//            }
//        });
    }

    public void getPages(PageRoomViewModel pageRoomViewModel) {
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
    }
}
