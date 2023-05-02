package com.piseth.anemi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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
    public static final String PAGE = "page";

    // Database Information
    static final String DB_NAME = "EANEMI.DB";

    // database version
    static final int DB_VERSION = 1;

    private static final String CREATE_TABLE_USER = "create table " + TABLE_USER + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERNAME + " TEXT NOT NULL, " + PASSWORD
            + " TEXT NOT NULL, " + USER_ROLE + " INTEGER, " +  PHONE + " TEXT, " + PHONE + " BLOB," + DELETED + " INTEGER);";

    private static final String CREATE_TABLE_USER_ROLE = "create table " + TABLE_USER_ROLE + "(" + ROLE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_ROLE + " TEXT NOT NULL, " + DELETED + " INTEGER);";

    private static final String CREATE_TABLE_BOOK = "create table " + TABLE_BOOK + "(" + BOOK_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BOOK_NAME + " TEXT NOT NULL, " + DESCRIPTION + " TEXT, " + AUTHOR
            + " TEXT, " + PAGE + " INTEGER, "+ DELETED + " INTEGER);";

    public DatabaseManageHandler(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(CREATE_TABLE_USER_ROLE);
        sqLiteDatabase.execSQL(CREATE_TABLE_BOOK);
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

        Cursor cursor = db.query(TABLE_USER, new String[] { _ID,
                        USERNAME, PASSWORD, ROLE_ID, PHONE }, _ID + "=?",
                        new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();

        User user = new User(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2),
                        Integer.parseInt(cursor.getString(3)), cursor.getString(4), cursor.getInt(5));
        // return contact
        return user;
    }

    // Getting All Users
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User(Integer.parseInt(cursor.getString(0)),
                                cursor.getString(1), cursor.getString(2),
                                Integer.parseInt(cursor.getString(3)), cursor.getString(4), cursor.getInt(5));
                // Adding user to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return user list
        return userList;
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(USERNAME, user.getUsername());
        value.put(PASSWORD, user.getPassword());
        value.put(ROLE_ID, user.getUserRoleId());
        value.put(PHONE, user.getPhone());
        db.insert(TABLE_USER, null, value);
        db.close();
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(USERNAME, user.getUsername());
        value.put(PASSWORD, user.getPassword());
        value.put(ROLE_ID, user.getUserRoleId());
        value.put(PHONE, user.getPhone());
        return db.update(TABLE_USER, value, " WHERE " + _ID + " = " + user.getId(), null);
    }

    public int deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DELETED, 1);
        return db.update(TABLE_USER, values, " WHERE " + _ID + " = " + id, null);
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

    public void addUserRole(UserRole userRole) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(USER_ROLE, userRole.getUserRole());
        db.insert(TABLE_USER_ROLE, null, value);
        db.close();
    }

    public int updateUserRole(UserRole userRole) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(USER_ROLE, userRole.getUserRole());
        return db.update(TABLE_USER_ROLE, value, " WHERE " + ROLE_ID + " = " + userRole.getUserRoleId(), null);
    }

    public int deleteUserRole(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DELETED, 1);
        return db.update(TABLE_USER_ROLE, values, " WHERE " + ROLE_ID + " = " + id, null);
    }

    // Getting single book
    Book getBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BOOK, new String[] { BOOK_ID,
                        BOOK_NAME, DESCRIPTION, AUTHOR, PAGE }, BOOK_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();

        Book book = new Book(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), Integer.parseInt(cursor.getString(4)));
        // return contact
        return book;
    }

    // Getting All Books
    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_BOOK;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book(Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), Integer.parseInt(cursor.getString(4)));
                // Adding user to list
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return user list
        return bookList;
    }

    public void addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(BOOK_NAME, book.getBookName());
        value.put(DESCRIPTION, book.getDescription());
        value.put(AUTHOR, book.getAuthor());
        value.put(PAGE, book.getPage());
        db.insert(TABLE_BOOK, null, value);
        db.close();
    }

    public int updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(BOOK_NAME, book.getBookName());
        value.put(DESCRIPTION, book.getDescription());
        value.put(AUTHOR, book.getAuthor());
        value.put(PAGE, book.getPage());
        return db.update(TABLE_BOOK, value, " WHERE " + BOOK_ID + " = " + book.getBookId(), null);
    }

    public int deleteBook(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DELETED, 1);
        return db.update(TABLE_BOOK, values, " WHERE " + BOOK_ID + " = " + id, null);
    }
}
