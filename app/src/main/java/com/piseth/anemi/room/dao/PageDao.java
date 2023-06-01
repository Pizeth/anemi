package com.piseth.anemi.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.piseth.anemi.utils.model.Page;

import java.util.List;

@Dao
public interface PageDao {
    @Insert
    long insertPage(Page page);
    @Update
    int updatePage(Page page);
    @Query("SELECT * FROM table_book_detail")
    LiveData<List<Page>> getAllPages();
}
