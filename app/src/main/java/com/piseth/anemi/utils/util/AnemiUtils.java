package com.piseth.anemi.utils.util;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.gson.Gson;
import com.piseth.anemi.utils.model.User;

import java.io.ByteArrayOutputStream;

public class AnemiUtils {

    public static final String LOGGED_IN_USER = "logged_user";
    public static final String USER_PHOTO = "user_photo";
    public static final int ROLE_ADMIN = 3;
    public static final int ROLE_USER = 1;
    public static final int ACTION_ADD = 1;
    public static final int ACTION_UPDATE = 2;
    public static final int NEW_ENTRY = 0;
    public static final int PICK_IMAGE = 1;
    public static final int DUMMY_ID = 99;
    public static final int STARTING_POSITION = -1;
    public static final String NO_WHITE_SPACE = "\\A\\w{1,20}\\z";
    public static final String REQUIREMENT = "^" +
                                            //"(?=.*[0-9])" +         //at least 1 digit
                                            //"(?=.*[a-z])" +         //at least 1 lower case letter
                                            //"(?=.*[A-Z])" +         //at least 1 upper case letter
                                            "(?=.*[a-zA-Z])" +      //any letter
                                            "(?=.*[@#$%^&+=])" +    //at least 1 special character
                                            "(?=\\S+$)" +           //no white spaces
                                            ".{4,}" +               //at least 4 characters
                                            "$";

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public static Bitmap getBitmapFromBytesArray(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static String BASE64Encode(byte[] data) {
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    public static byte[] BASE64Decode(String data) {
        return Base64.decode(data.getBytes(), Base64.DEFAULT);
    }

    public static User getLoggedInUser(SharedPreferences loggedInUser) {
        Gson gson = new Gson();
        if (loggedInUser.contains(LOGGED_IN_USER)) {
            String json = loggedInUser.getString(LOGGED_IN_USER, "");
            return gson.fromJson(json, User.class);
//            byte[] photo = AnemiUtils.BASE64Decode(loggedInUser.getString(USER_PHOTO, ""));
//            imageToStore = AnemiUtils.getBitmapFromBytesArray(photo);
//            user.setPhoto(imageToStore);
        }
        return null;
    }
}
