package com.piseth.anemi.room.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.piseth.anemi.room.dao.PageDao;
import com.piseth.anemi.room.dao.UserDao;
import com.piseth.anemi.utils.model.Page;

@Database(entities = {Page.class}, version = 1)
public abstract class AnemiDatabase extends RoomDatabase {
    private static AnemiDatabase mInstance;
    public abstract PageDao pageDao();
    public abstract UserDao uSerDao();
    public static synchronized AnemiDatabase getmInstance(Context context) {
        if (mInstance == null) {
            mInstance = Room.databaseBuilder(context.getApplicationContext(), AnemiDatabase.class, "AnemiDatabase").fallbackToDestructiveMigration().build();
        }
        return  mInstance;
    }
}
