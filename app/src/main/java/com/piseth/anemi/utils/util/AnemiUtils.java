package com.piseth.anemi.utils.util;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.piseth.anemi.ui.activities.MainActivity;
import com.piseth.anemi.utils.model.TokenUser;
import com.piseth.anemi.utils.model.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnemiUtils {

    public static final String SERVER_API = "https://10.0.2.2:7000/";
//    public static final String PUBLIC_KEY = "public_cjcNLhDV1p4OBh0HByyE2SbIdNQ=";
    public static final String PUBLIC_KEY = "45b2aab7ef429409451e";
    public static final String PRIVATE_KEY = "aace177b6fa28fc00d5a";
    public static final String[] AVATAR_TAG = {"AVATAR", "Portrait", "Profile"};
    public static final String IMAGEKIT_TOKEN = "";

    public static final String URL_ENDPOINT = "https://ik.imagekit.io/razeth";
//    public static final String SERVER_API = "https://127.0.0.1:7000/";
    public static final String LOGGED_IN_USER = "logged_user";
    public static final String USER_PHOTO = "user_photo";
    public static final int ROLE_ADMIN = 3;
    public static final int ROLE_USER = 1;
    public static final String USERNAME = "username";
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    public static final int ACTION_ADD = 1;
    public static final int ACTION_UPDATE = 2;
    public static final int NEW_ENTRY = 0;
//    public static final String NEW_BOOK_ENTRY = "NO_ID";
    public static final Long NEW_BOOK_ENTRY = 0L;
    public static final int PICK_IMAGE = 1;
    public static final int DUMMY_ID = 99;
    public static final int STARTING_POSITION = -1;
    public static final String NO_WHITE_SPACE = "\\A\\w{1,20}\\z";
    public static Context context = MainActivity.getContextOfApplication();
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String REFRESH_TOKEN = "REFRESH_TOKEN";
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

//    public static User getLoggedInUser(SharedPreferences loggedInUser) {
//        if (loggedInUser.contains(LOGGED_IN_USER)) {
//            Gson gson = new Gson();
//            String json = loggedInUser.getString(LOGGED_IN_USER, "");
//            return gson.fromJson(json, User.class);
////            byte[] photo = AnemiUtils.BASE64Decode(loggedInUser.getString(USER_PHOTO, ""));
////            imageToStore = AnemiUtils.getBitmapFromBytesArray(photo);
////            user.setPhoto(imageToStore);
//        }
//        return null;
//    }

    public static TokenUser getLoggedInUser(SharedPreferences loggedInUser) {
        if (loggedInUser.contains(LOGGED_IN_USER)) {
            Gson gson = new Gson();
            String json = loggedInUser.getString(LOGGED_IN_USER, "");
            return gson.fromJson(json, TokenUser.class);
        }
        return null;
    }

    public static void setUserPreference(SharedPreferences loggedInUser, User user){
        SharedPreferences.Editor prefsEditor = loggedInUser.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString(AnemiUtils.LOGGED_IN_USER, json);
        prefsEditor.apply();
    }

    public static void setUserPreference(SharedPreferences loggedInUser, TokenUser user){
        SharedPreferences.Editor prefsEditor = loggedInUser.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString(AnemiUtils.LOGGED_IN_USER, json);
        prefsEditor.putString(AnemiUtils.ACCESS_TOKEN, user.getAccessToken());
        prefsEditor.putString(AnemiUtils.REFRESH_TOKEN, user.getRefreshToken());
        prefsEditor.apply();
    }

    public static Retrofit getClientApi(HttpLoggingInterceptor.Level level) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_API)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getOkHttpClientLog(level))
                .build();
        return retrofit;
    }

    public static OkHttpClient getOkHttpClientLog(HttpLoggingInterceptor.Level level) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(level);
        OkHttpClient okHttpClient = getUnsafeOkHttpClient()
                .addInterceptor((new Interceptor() {
                    @NonNull
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Request ogRequest = chain.request();
                        Request newRequest = ogRequest.newBuilder().header("Authorization", "Bearer " + getAccessToken()).build();
                        return chain.proceed(newRequest);
                    }
                }))
                .addInterceptor(loggingInterceptor)
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();
        return okHttpClient;
    }

    public static OkHttpClient.Builder getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String
                                authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String
                                authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static final String getAccessToken() {
        SharedPreferences accessToken = context.getSharedPreferences(LOGGED_IN_USER, MODE_PRIVATE);
        String token = "";
        if (accessToken.contains(ACCESS_TOKEN)) {
            token = accessToken.getString(ACCESS_TOKEN, "");
        }
        return token;
    }

    public static final String getRefreshToken() {
        SharedPreferences accessToken = context.getSharedPreferences(LOGGED_IN_USER, MODE_PRIVATE);
        String token = "";
        if (accessToken.contains(REFRESH_TOKEN)) {
            token = accessToken.getString(REFRESH_TOKEN, "");
        }
        return token;
    }

    @SuppressLint("Range")
    public static String getFileName(Context context, Uri uri) {
        String result = null;
        String scheme = uri.getScheme();
        if (scheme != null && scheme.equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                assert cursor != null;
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
