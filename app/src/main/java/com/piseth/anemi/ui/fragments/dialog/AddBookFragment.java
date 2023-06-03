package com.piseth.anemi.ui.fragments.dialog;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.piseth.anemi.utils.util.DatabaseManageHandler;
import com.piseth.anemi.R;
import com.piseth.anemi.ui.fragments.fragment.HomeFragment;
import com.piseth.anemi.utils.model.Book;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBookFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int PICK_IMAGE = 1;
    public static final int DUMMY_ID = 99;
    public static String USER_PHOTO = "user_photo";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatabaseManageHandler db;
    private Book book;
    private Bitmap imageToStore;
    private TextInputLayout txt_title, txt_author, txt_description;
    private ImageView bookCover;
    private HomeFragment homeFragment;

    public AddBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddBookFragment newInstance(String param1, String param2) {
        AddBookFragment fragment = new AddBookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = new DatabaseManageHandler(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_book, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        loggedInUser = getContext().getSharedPreferences(LOGGED_IN_USER, MODE_PRIVATE);
        txt_title = view.findViewById(R.id.title);
        txt_author = view.findViewById(R.id.author);
        txt_description = view.findViewById(R.id.description);
        bookCover = view.findViewById(R.id.book_cover);

//        if (loggedInUser.contains(LOGGED_IN_USER) && loggedInUser.contains(USER_PHOTO)) {
//            Gson gson = new Gson();
//            String json = loggedInUser.getString(LOGGED_IN_USER, "");
//            user = gson.fromJson(json, User.class);
//            byte[] photo = AnemiUtils.BASE64Decode(loggedInUser.getString(USER_PHOTO, ""));
//            imageToStore = AnemiUtils.getBitmapFromBytesArray(photo);
//            user.setPhoto(imageToStore);
//            if(user != null) {
//                txt_username.getEditText().setText(user.getUsername());
////                txt_password.getEditText().setText(user.getPassword());
////                txt_re_password.getEditText().setText(user.getPassword());
//                txt_phone.getEditText().setText(user.getPhone());
//                profileImage.setImageBitmap(user.getPhoto());
////                profileImage.setImageDrawable(new BitmapDrawable(getResources(), AnemiUtils.getBitmapFromBytesArray(photo)));
//                profileImage.setCropToPadding(true);
//                profileImage.setClipToOutline(true);
//
//                Log.d("USERNAME: ", user.getUsername() + " 's data acquired'");
//            }
//        }
        Button addPhoto = view.findViewById(R.id.btnAddCover);
        Button saveButton = view.findViewById(R.id.btnSaveBook);
        addPhoto.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnAddCover) {
            chooseImage();
            return;
        } else if (id == R.id.btnSaveBook) {
            String title, author, description;

            if (txt_title.getEditText() != null && txt_author.getEditText() != null && txt_description.getEditText() != null && bookCover != null) {
                title = txt_title.getEditText().getText().toString().trim();
                author = txt_author.getEditText().getText().toString().trim();
                description = txt_description.getEditText().getText().toString().trim();
                Log.d("Insert Book: ", title + " " + author + " " + description);
                boolean result = addBook(title, description, author, imageToStore);
                if (result) {
                    Toast.makeText(getContext(), "Successfully added new Book", Toast.LENGTH_SHORT).show();
                    Log.d("Successful: ", "Back to Book Dashboard");
                    homeFragment = new HomeFragment();
                    replaceFragment(homeFragment);
                }
            }
        }
    }

    public boolean addBook(String title, String description, String author, Bitmap imageToStore) {
        boolean checkOperation = false;
        if(!title.isEmpty() && !author.isEmpty() && !description.isEmpty() && imageToStore != null) {

            Book book = new Book(DUMMY_ID, title, description, author, imageToStore);
//            SharedPreferences.Editor prefsEditor = loggedInUser.edit();
//            byte[] userPhoto = AnemiUtils.getBitmapAsByteArray(user.getPhoto());
//            Gson gson = new Gson();
            int book_id = (int) db.addBook(book);
            checkOperation = (book_id == -1) ? false : true;
            book.setBookId(book_id);
//            String json = gson.toJson(user);
//            prefsEditor.putString(LOGGED_IN_USER, json);
//            prefsEditor.putString(USER_PHOTO, AnemiUtils.BASE64Encode(userPhoto));
//            prefsEditor.apply();
            Log.d("Insert Book: ", "UserID: " + book.getBookId() + " Username: " + book.getBookName() + " Password" + book.getAuthor());
            Toast.makeText(getContext(), book.getBookId() + " " + book.getBookName() + " " + book.getAuthor(), Toast.LENGTH_SHORT).show();

        }
        return checkOperation;
    }

    private void chooseImage() {
        try {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");
//            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            Intent pickIntent = new Intent(Intent.ACTION_PICK);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

            startActivityForResult(chooserIntent, PICK_IMAGE);
        } catch(Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, requestCode, data);
            if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);
                bookCover.setImageBitmap(imageToStore);
            }
        } catch(Exception e) {
            Toast.makeText(getContext(), "Can't pick image " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}