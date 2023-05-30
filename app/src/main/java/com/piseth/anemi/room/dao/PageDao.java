package com.piseth.anemi.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Query;

import com.piseth.anemi.utils.model.Page;

import java.util.List;

public interface PageDao {
    @Insert
    void insertPage(Page page);
    @Query("SELECT * FROM book_detail")
    LiveData<List<Page>> getAllPages();
}
