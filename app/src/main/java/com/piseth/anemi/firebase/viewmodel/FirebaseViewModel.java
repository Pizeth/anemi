package com.piseth.anemi.firebase.viewmodel;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.piseth.anemi.firebase.repo.FirebasePageRepo;
import com.piseth.anemi.room.viewmodel.PageRoomViewModel;

public class FirebaseViewModel extends ViewModel {
    private FirebasePageRepo firebasePageRepo;

    public FirebaseViewModel() {
        firebasePageRepo = new FirebasePageRepo();
    }

    public void uploadPageToFirebase(Uri uri, PageRoomViewModel pageRoomViewModel) {
        firebasePageRepo.uploadPage(uri, pageRoomViewModel);
    }

    public void getPagesFromFirebase(PageRoomViewModel pageRoomViewModel) {
        firebasePageRepo.getPages(pageRoomViewModel);
    }
}
