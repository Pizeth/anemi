package com.piseth.anemi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManageHandler extends SQLiteOpenHelper {

    // Table User
    public static final String TABLE_USER = "USER";
    // Table columns
    public static final String _ID = "_id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String PHONE = "phone";
    public static final String PHOTO = "photo";
    public static final String DELETED = "deleted";

    // Table UserRole
    public static final String TABLE_USER_ROLE = "USER_ROLE";
    // Table columns
    public static final String ROLE_ID = "role_id";
    public static final String USER_ROLE= "user_role";

    // Table Book
    public static final String TABLE_BOOK = "BOOK";
    // Table columns
    public static final String BOOK_ID = "book_id";
    public static final String BOOK_NAME = "book_name";
    public static final String DESCRIPTION = "description";
    public static final String AUTHOR = "author";
    public static final String COVER = "cover";
    public static final int ADMIN_ID = 1;

    // Database Information
    static final String DB_NAME = "ANEMI.DB";

    // database version
    static final int DB_VERSION = 1;

    private Context context;
    private SQLiteDatabase currentDB;

    private static final String CREATE_TABLE_USER = "create table " + TABLE_USER + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERNAME + " TEXT NOT NULL, " + PASSWORD
            + " TEXT NOT NULL, " + ROLE_ID + " INTEGER, " +  PHONE + " TEXT, " + PHOTO + " BLOB," + DELETED + " INTEGER DEFAULT 0);";

    private static final String CREATE_TABLE_USER_ROLE = "create table " + TABLE_USER_ROLE + "(" + ROLE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_ROLE + " TEXT NOT NULL, " + DELETED + " INTEGER DEFAULT 0);";

    private static final String CREATE_TABLE_BOOK = "create table " + TABLE_BOOK + "(" + BOOK_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BOOK_NAME + " TEXT NOT NULL, " + DESCRIPTION + " TEXT, " + AUTHOR
            + " TEXT, " + COVER + " BLOB, "+ DELETED + " INTEGER DEFAULT 0);";

    public DatabaseManageHandler(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
//        initializeUserAdmin();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(CREATE_TABLE_USER_ROLE);
        sqLiteDatabase.execSQL(CREATE_TABLE_BOOK);
        currentDB = sqLiteDatabase;
        initializeUserAdmin();
        initializeRole();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_ROLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
        onCreate(sqLiteDatabase);
    }

    // Getting single user
    User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        int user_id, role_id;
        String username, password, phone;
        Bitmap photo;
        Cursor cursor = db.query(TABLE_USER, new String[] { _ID,
                        USERNAME, PASSWORD, ROLE_ID, PHONE, PHOTO }, _ID + "=?",
                        new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            user_id = Integer.parseInt(cursor.getString(0));
            username = cursor.getString(1);
            password = cursor.getString(2);
            role_id = Integer.parseInt(cursor.getString(3));
            phone = cursor.getString(4);
            photo = AnemiUtils.getBitmapFromBytesArray(cursor.getBlob(5));
            cursor.close();
            // return user
            return new User(user_id, username, password, role_id, phone, photo);
        } else return null;
    }

    User getUser(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        int user_id, role_id;
        String username, password, phone;
        Bitmap photo;
        Cursor cursor = db.query(TABLE_USER, new String[] { _ID,
                        USERNAME, PASSWORD, ROLE_ID, PHONE, PHOTO }, USERNAME + "=?",
                new String[] { userName }, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            user_id = Integer.parseInt(cursor.getString(0));
            username = cursor.getString(1);
            password = cursor.getString(2);
            role_id = Integer.parseInt(cursor.getString(3));
            phone = cursor.getString(4);
            photo = AnemiUtils.getBitmapFromBytesArray(cursor.getBlob(5));
            cursor.close();
            // return user
            return new User(user_id, username, password, role_id, phone, photo);
        } else return null;
    }

    // Getting All Users
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_USER + " WHERE " + DELETED + " = 0";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User(Integer.parseInt(cursor.getString(0)),
                                cursor.getString(1), cursor.getString(2),
                                Integer.parseInt(cursor.getString(3)), cursor.getString(4),
                                AnemiUtils.getBitmapFromBytesArray(cursor.getBlob(5)));
                // Adding user to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return user list
        return userList;
    }

    public void initializeUserAdmin() {
        User admin = new User(ADMIN_ID, "admin", "123", 3, "0123456789", BitmapFactory.decodeResource(context.getResources(), R.mipmap.exia));
        addUser(admin);
    }

    public void initializeRole() {
        List<UserRole> roles = new ArrayList<>();
        roles.add( new UserRole(1, "Member"));
        roles.add( new UserRole(2, "Editor"));
        roles.add( new UserRole(3, "Admin"));
        for (UserRole r : roles) {
            addUserRole(r);
        }
    }

    public long addUser(User user) {
        if(currentDB == null) {
            currentDB = this.getWritableDatabase();
        }
//        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(USERNAME, user.getUsername());
        value.put(PASSWORD, user.getPassword());
        value.put(ROLE_ID, user.getUserRoleId());
        value.put(PHONE, user.getPhone());
        value.put(PHOTO, AnemiUtils.getBitmapAsByteArray(user.getPhoto()));
        return currentDB.insert(TABLE_USER, null, value);
//        return (result == -1) ? false : true;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(USERNAME, user.getUsername());
        value.put(PASSWORD, user.getPassword());
        value.put(ROLE_ID, user.getUserRoleId());
        value.put(PHONE, user.getPhone());
        value.put(PHOTO, AnemiUtils.getBitmapAsByteArray(user.getPhoto()));
        return db.update(TABLE_USER, value, _ID + " = " + user.getId(), null);
    }

    public int deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DELETED, 1);
        return db.update(TABLE_USER, values, _ID + " = " + id, null);
    }

    UserRole getUserRole(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER_ROLE, new String[] { ROLE_ID,
                        USER_ROLE }, ROLE_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();

        UserRole userRole = new UserRole(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
        // return contact
        return userRole;
    }

    String getRole(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER_ROLE, new String[] {USER_ROLE }, ROLE_ID + "=?",
                        new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        return cursor.getString(0);
    }

    public void addUserRole(UserRole userRole) {
        if(currentDB == null) {
            currentDB = this.getWritableDatabase();
        }
//        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(USER_ROLE, userRole.getUserRole());
        currentDB.insert(TABLE_USER_ROLE, null, value);
    }

    public int updateUserRole(UserRole userRole) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(USER_ROLE, userRole.getUserRole());
        return db.update(TABLE_USER_ROLE, value, ROLE_ID + " = " + userRole.getUserRoleId(), null);
    }

    public int deleteUserRole(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DELETED, 1);
        return db.update(TABLE_USER_ROLE, values, ROLE_ID + " = " + id, null);
    }

    // Getting single book
    Book getBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BOOK, new String[] { BOOK_ID,
                        BOOK_NAME, DESCRIPTION, AUTHOR, COVER }, BOOK_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();

        Book book = new Book(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), AnemiUtils.getBitmapFromBytesArray(cursor.getBlob(4)));
        // return contact
        return book;
    }

    // Getting All Books
    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_BOOK + " WHERE " + DELETED + " = 0";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book(Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), AnemiUtils.getBitmapFromBytesArray(cursor.getBlob(4)));
                // Adding user to list
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return user list
        return bookList;
    }

    public long addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(BOOK_NAME, book.getBookName());
        value.put(DESCRIPTION, book.getDescription());
        value.put(AUTHOR, book.getAuthor());
        value.put(COVER, AnemiUtils.getBitmapAsByteArray(book.getCover()));
        return db.insert(TABLE_BOOK, null, value);
    }

    public int updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(BOOK_NAME, book.getBookName());
        value.put(DESCRIPTION, book.getDescription());
        value.put(AUTHOR, book.getAuthor());
        value.put(COVER, AnemiUtils.getBitmapAsByteArray(book.getCover()));
        return db.update(TABLE_BOOK, value, BOOK_ID + " = " + book.getBookId(), null);
    }

    public int deleteBook(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DELETED, 1);
        return db.update(TABLE_BOOK, values, BOOK_ID + " = " + id, null);
    }

//    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//        return outputStream.toByteArray();
//    }
//
//    public static Bitmap getBitmapFromBytesArray(byte[] image) {
//        return BitmapFactory.decodeByteArray(image, 0, image.length);
//    }
}
