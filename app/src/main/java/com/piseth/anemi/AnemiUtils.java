package com.piseth.anemi;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

public class AnemiUtils {

    public static final String LOGGED_IN_USER = "logged_user";
    public static final int ACTION_ADD = 1;
    public static final int ACTION_UPDATE = 2;
    public static final int NEW_ENTRY = 0;

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
