package com.piseth.anemi.room.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.piseth.anemi.room.dao.PageDao;
import com.piseth.anemi.room.dao.UserDao;
import com.piseth.anemi.utils.model.Page;
import com.piseth.anemi.utils.model.User;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Database(entities = {Page.class, User.class}, version = 1)
public abstract class AnemiDatabase extends RoomDatabase {
    private static AnemiDatabase mInstance;
    public abstract PageDao pageDao();
    public abstract UserDao userDao();
    private Executor executor = Executors.newSingleThreadExecutor();
    public static synchronized AnemiDatabase getmInstance(Context context) {
        if (mInstance == null) {
            mInstance = buildDatabase(context);
        }
        return  mInstance;
    }

    private static AnemiDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                        AnemiDatabase.class, "AnemiDatabase")
                .fallbackToDestructiveMigration()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                getmInstance(context).userDao().insertUser(new User(1, "admin", "123", 3, "0123456789", "123"));
                            }
                        });
                    }
                })
                .build();
    }

//    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//        }
//    };
//
//    public static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
//        private UserDao userDao;
//        private PopulateDbAsyncTask(AnemiDatabase db) {
//            userDao = db.userDao();
//        }
//        @Override
//        protected Void doInBackground(Void... voids) {
////            userDao.insertUser(User(ADMIN_ID, "admin", "123", 3, "0123456789", BitmapFactory.decodeResource(context.getResources(), R.mipmap.exia));
//            return null;
//        }
//    }
}
