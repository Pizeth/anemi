package com.piseth.anemi.firebase.repo;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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

    public void uploadPage(Uri uri, Page page, Context context) {
        StorageReference pageImgRef = storageReference.child(String.valueOf(System.currentTimeMillis()));
        pageImgRef.putFile(uri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.isComplete()) {
                    pageImgRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                        page.setImageURL(uri1.toString());
                        firebaseFirestore.collection("Pages").add(page).addOnCompleteListener(task1 -> {
                            if (task1.isComplete()) {
                                Log.d("SUCCESS", "Page image has been added to " + uri);
                                Toast.makeText(context, "Page added successful!", Toast.LENGTH_LONG).show();
                            }
                        });
                    });
                }
            }
        });
    }

    public void upDatePage(Uri uri, Page page, String id, Context context) {
        StorageReference pageImgRef = storageReference.child(String.valueOf(System.currentTimeMillis()));
        if (uri != null) {
            Log.d("SUCCESS", "Change page image to " + uri);
            pageImgRef.putFile(uri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.isComplete()) {
                        pageImgRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                            page.setImageURL(uri1.toString());
                            setPage(id, page, context);
                        });
                    }
                }
            });
        } else {
            setPage(id, page, context);
        }
    }

    public void deletePage(String id) {
        pageRef.document(id).delete()
                .addOnSuccessListener(aVoid -> Log.d("SUCCESS", "Page successfully deleted!"))
                .addOnFailureListener(e -> Log.w("FAILED", "Error deleting Page", e));
    }

    private void setPage(String id, Page page, Context context) {
        pageRef.document(id).set(page, SetOptions.merge()).addOnCompleteListener(task12 -> {
            if (task12.isComplete()) {
                Log.d("SUCCESS", "THE ID IS " + id);
                Toast.makeText(context, "Page updated successful!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public Query getAllPageFromBookQuery(String book_id) {
        return pageRef.whereEqualTo("book_id", book_id).orderBy("pageNumber");
    }

    public void getPages(PageRoomViewModel pageRoomViewModel) {
        firebaseFirestore.collection("Pages").addSnapshotListener((value, error) -> {
            if(value != null) {
                for (DocumentChange doc : value.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        Page page = doc.getDocument().toObject(Page.class);
                        pageRoomViewModel.insertPage(page);
                    }
                }
            }
        });
    }
}
