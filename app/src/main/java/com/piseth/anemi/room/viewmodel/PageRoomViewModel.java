package com.piseth.anemi.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.piseth.anemi.room.repo.PageRoomRepo;
import com.piseth.anemi.utils.model.Page;

import java.util.List;

public class PageRoomViewModel extends AndroidViewModel {
    private PageRoomRepo pageRoomRepo;

    public PageRoomViewModel(@NonNull Application application) {
        super(application);
        pageRoomRepo = new PageRoomRepo(application);
    }

    public void insertPage(Page page) {
        pageRoomRepo.insertPage(page);
    }

    public LiveData<List<Page>> getAllPagesLiveData() {
        return pageRoomRepo.getPageListLiveData();
    }
}
