package com.piseth.anemi.firebase.viewmodel;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.piseth.anemi.firebase.repo.FirebasePageRepo;
import com.piseth.anemi.firebase.repo.FirebaseUserRepo;
import com.piseth.anemi.room.viewmodel.PageRoomViewModel;
import com.piseth.anemi.room.viewmodel.UserRoomViewModel;
import com.piseth.anemi.utils.model.User;

public class FirebaseUserViewModel extends ViewModel {
    private FirebaseUserRepo firebaseUserRepo;

    public FirebaseUserViewModel() {
        firebaseUserRepo = new FirebaseUserRepo();
    }

    public void uploadPageToFirebase(Uri uri, UserRoomViewModel userRoomViewModel, User user, String id) {
        firebaseUserRepo.addNewUser(uri, userRoomViewModel, user, id);
    }
//
//    public void getPagesFromFirebase(PageRoomViewModel pageRoomViewModel) {
//        firebasePageRepo.getPages(pageRoomViewModel);
//    }
}
