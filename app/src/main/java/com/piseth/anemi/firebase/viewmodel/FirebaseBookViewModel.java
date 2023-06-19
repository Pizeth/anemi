package com.piseth.anemi.firebase.viewmodel;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.Query;
import com.piseth.anemi.firebase.repo.FirebaseBookRepo;
import com.piseth.anemi.utils.model.Book;

import java.util.List;

public class FirebaseBookViewModel extends ViewModel {
    private FirebaseBookRepo firebaseBookRepo;

    public FirebaseBookViewModel() {
        firebaseBookRepo = new FirebaseBookRepo();
    }

    public void addNewBook(Uri uri, Book book) {
        firebaseBookRepo.addNewBook(uri, book);
    }

    public void updateBook(Uri uri,  Book book, String id) {
        firebaseBookRepo.updateBook(uri, book, id);
    }

    public void deleteBook(String id) {
        firebaseBookRepo.deleteBook(id);
    }

    public List<Book> getAllBook() {
        return firebaseBookRepo.getAllBooks();
    }

    public Query getAllBooksQuery() {
        return firebaseBookRepo.getAllBooksQuery();
    }

    public Query getSearchBookQuery(String book_title) {
        return firebaseBookRepo.getSearchBookQuery(book_title);
    }
}
