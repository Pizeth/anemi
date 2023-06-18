package com.piseth.anemi.firebase.viewmodel;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.Query;
import com.piseth.anemi.firebase.repo.FirebaseUserRepo;
import com.piseth.anemi.utils.model.User;

import java.util.List;

public class FirebaseUserViewModel extends ViewModel {
    private FirebaseUserRepo firebaseUserRepo;

    public FirebaseUserViewModel() {
        firebaseUserRepo = new FirebaseUserRepo();
    }

    public void addNewUser(Uri uri, User user, String id) {
        firebaseUserRepo.addNewUser(uri, user, id);
    }

    public void updateUser(Uri uri, User user, String id, boolean isUpdateEmail, boolean isUpdatePassword, String current_password) {
        firebaseUserRepo.updateUser(uri, user, id, isUpdateEmail, isUpdatePassword, current_password);
    }

    public void deleteUser(String id) {
        firebaseUserRepo.deleteUser(id);
    }

    public List<User> getAllUsers() {
        return firebaseUserRepo.getAllUsers();
    }

//    public User getUser(String id) {
//        return firebaseUserRepo.getUser(id);
//    }

    public Query getAllUsersQuery() {
        return firebaseUserRepo.getAllUsersQuery();
    }
}
