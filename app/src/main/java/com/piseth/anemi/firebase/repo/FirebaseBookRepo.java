package com.piseth.anemi.firebase.repo;

import android.net.Uri;
import android.util.Log;

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
import com.piseth.anemi.firebase.viewmodel.FirebasePageViewModel;
import com.piseth.anemi.room.viewmodel.UserRoomViewModel;
import com.piseth.anemi.utils.model.Book;
import com.piseth.anemi.utils.model.User;

import java.util.ArrayList;
import java.util.List;

public class FirebaseBookRepo {
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference bookRef;
    private StorageReference storageReference;

    public FirebaseBookRepo() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        bookRef = firebaseFirestore.collection("Books");
        storageReference = FirebaseStorage.getInstance().getReference().child("bookCover");
    }

    public void addNewBook(Uri uri, Book book /*, FirebasePageViewModel page*/) {
        StorageReference bookCoverRef = storageReference.child(book.getBookName());
        bookCoverRef.putFile(uri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.isComplete()) {
                    bookCoverRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                        book.setCover(uri1.toString());
                        bookRef.add(book).addOnCompleteListener(task12 -> {
                            if (task12.isComplete()) {
                                if(task12 != null) {
                                    Log.i("Added", "New Book : " + task12.getResult().getId() + " added");
                                }
//                                page.uploadPageToFirebase();
                            }
                        });
                    });
                }
            }
        });
    }

    public void updateBook(Uri uri, Book book, /*FirebasePageViewModel page,*/ String id) {
        StorageReference bookCoverRef = storageReference.child(book.getBookName());
        if (uri != null) {
            Log.d("SUCCESS", "Change book cover to " + uri);
            bookCoverRef.putFile(uri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.isComplete()) {
                        bookCoverRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                            book.setCover(uri1.toString());
                            setBook(id, book);
                        });
                    }
                }
            });
        } else {
            setBook(id, book);
        }
    }

    private void setBook(String id, Book book) {
        bookRef.document(id).set(book, SetOptions.merge()).addOnCompleteListener(task12 -> {
            if (task12.isComplete()) {
                Log.d("SUCCESS", "THE ID IS " + id);
            }
        });
    }

    public void deleteBook(String id) {
        bookRef.document(id).update("isDeleted", 1);
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        bookRef.whereEqualTo("isDeleted", 0).addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }
            if (value != null) {
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    Book book = documentSnapshot.toObject(Book.class);
                    books.add(book);
                }
            }
        });
        return books;
    }

    public Query getAllBooksQuery() {
        return bookRef.whereEqualTo("isDeleted", 0);
    }

    public Query getSearchBookQuery(String book_title) {
        return bookRef.whereEqualTo("book_title", book_title);
    }
}
