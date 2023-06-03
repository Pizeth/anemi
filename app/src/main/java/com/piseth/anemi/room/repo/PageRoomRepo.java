package com.piseth.anemi.room.repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.piseth.anemi.room.dao.PageDao;
import com.piseth.anemi.room.database.AnemiDatabase;
import com.piseth.anemi.utils.model.Page;
import com.piseth.anemi.utils.model.User;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PageRoomRepo {
    private PageDao pageDao;
    private LiveData<List<Page>> pageListLiveData;
    private Executor executor = Executors.newSingleThreadExecutor();

    public LiveData<List<Page>> getPageListLiveData() {
        return pageListLiveData;
    }

    public PageRoomRepo(Application application) {
        AnemiDatabase anemiDatabase = AnemiDatabase.getmInstance(application);
        pageDao = anemiDatabase.pageDao();
        pageListLiveData = pageDao.getAllPages();
    }

//    public long insertPage(Page page) {
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                pageDao.insertPage(page);
//
//            }
//        });
//    }

    public long insertPage(Page page) {
        final long[] id = new long[1];
        executor.execute(() -> id[0] = pageDao.insertPage(page));
        return id[0];
    }
}
