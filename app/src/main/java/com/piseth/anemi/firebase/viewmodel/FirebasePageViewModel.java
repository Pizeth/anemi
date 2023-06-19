package com.piseth.anemi.firebase.viewmodel;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.Query;
import com.piseth.anemi.firebase.repo.FirebasePageRepo;
import com.piseth.anemi.room.viewmodel.PageRoomViewModel;
import com.piseth.anemi.utils.model.Page;

public class FirebasePageViewModel extends ViewModel {
    private FirebasePageRepo firebasePageRepo;

    public FirebasePageViewModel() {
        firebasePageRepo = new FirebasePageRepo();
    }

    public void uploadPageToFirebase(Uri uri, Page page, Context context) {
        firebasePageRepo.uploadPage(uri, page, context);
    }

    public Query getAllPageFromBookQuery(String book_id) {
        return firebasePageRepo.getAllPageFromBookQuery(book_id);
    }

    public void upDatePage(Uri uri, Page page, String id, Context context) {
        firebasePageRepo.upDatePage(uri, page, id, context);
    }

    public void deletePage(String id) {
        firebasePageRepo.deletePage(id);
    }

    public void getPagesFromFirebase(PageRoomViewModel pageRoomViewModel) {
        firebasePageRepo.getPages(pageRoomViewModel);
    }
}
