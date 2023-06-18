package com.piseth.anemi.firebase.repo;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.piseth.anemi.room.viewmodel.PageRoomViewModel;
import com.piseth.anemi.utils.model.Page;

public class FirebasePageRepo {
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference pageRef;
    private StorageReference storageReference;

    public FirebasePageRepo() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        pageRef = firebaseFirestore.collection("Pages");
        storageReference = FirebaseStorage.getInstance().getReference().child("pageImages");
    }

    public void uploadPage(Uri uri, Page page, String id) {
        StorageReference pageRef = storageReference.child(String.valueOf(System.currentTimeMillis()));
        pageRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.isComplete()) {
                        pageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
//                                Page page = new Page();
                                page.setImageURL(uri.toString());
                                page.setBook_id(id);
                                firebaseFirestore.collection("Pages").add(page).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if(task.isComplete()) {
//                                            pageRoomViewModel.insertPage(page);
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

    public Query getAllPageFromBookQuery(String book_id) {
        return pageRef.whereEqualTo("book_id", book_id);
    }

    public void getPages(PageRoomViewModel pageRoomViewModel) {
        firebaseFirestore.collection("Pages").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentChange doc : value.getDocumentChanges()) {
                    if(doc.getType() == DocumentChange.Type.ADDED) {
                        Page page = doc.getDocument().toObject(Page.class);
                        pageRoomViewModel.insertPage(page);
                    }
                }
            }
        });
    }
}
