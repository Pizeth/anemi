package com.piseth.anemi;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DialogUpdateBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DialogUpdateBookFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int PICK_IMAGE = 1;
    public static final int DUMMY_ID = 99;
    private int action;
    private DatabaseManageHandler db;
    private Book book;
    private Bitmap imageToStore;
    private TextInputLayout txt_title, txt_author, txt_description;
    private ImageView bookCover;
    private DialogUpdateBookFragment.DialogListener dialogListener;

    public DialogUpdateBookFragment(Bundle savedInstanceState, int action, DialogUpdateBookFragment.DialogListener dialogListener) {
        super.setArguments(savedInstanceState);
        this.action = action;
        this.dialogListener = dialogListener;
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            if (getArguments().getBoolean("notAlertDialog")) {
                return super.onCreateDialog(savedInstanceState);
            }
        }
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity(), R.style.anemi);
        if(action == AnemiUtils.ACTION_ADD) {
            builder.setTitle("Add Book");
            builder.setMessage("Add Book's information");
        } else if (action == AnemiUtils.ACTION_UPDATE) {
            builder.setTitle("Update Book");
            builder.setMessage("Update Book's information");
        }
        builder.setNeutralButton("OK", (dialog, which) -> dismiss());
        builder.setPositiveButton("SAVE", (dialog, which) -> dismiss());
        builder.setNegativeButton("CANCEL", (dialog, which) -> dismiss());
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.shape);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseManageHandler(getContext());
        Log.d("API123", "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt_title = view.findViewById(R.id.title);
        txt_author = view.findViewById(R.id.author);
        txt_description = view.findViewById(R.id.description);
        bookCover = view.findViewById(R.id.book_cover);
        if (getArguments() != null) {
            int book_id = (int) getArguments().getLong("book_id");
            if (book_id != 0) {
                Log.d("Successful: ", "book_id to update is " + book_id);
                book = db.getBook((book_id));
            }
        }
        if(book != null) {
            txt_title.getEditText().setText(book.getBookName());
            txt_author.getEditText().setText(book.getAuthor());
            txt_description.getEditText().setText(book.getDescription());
            bookCover.setImageBitmap(book.getCover());
            imageToStore = book.getCover();
            bookCover.setCropToPadding(true);
            bookCover.setClipToOutline(true);
            Log.d("Book Title: ", book.getBookName() + " 's data acquired'");
        }

        Button addPhoto = view.findViewById(R.id.btnAddCover);
        Button saveButton = view.findViewById(R.id.btnSaveBook);

        addPhoto.setOnClickListener(view1 -> chooseImage());
        saveButton.setOnClickListener(view1 -> {
            String title, author, description;

            title = (txt_title.getEditText() != null) ? txt_title.getEditText().getText().toString().trim() : "";
            author = (txt_author.getEditText() != null) ? txt_author.getEditText().getText().toString().trim() : "";
            description = (txt_description.getEditText() != null) ? txt_description.getEditText().getText().toString().trim() : "";
            boolean result = false;
            if(action == AnemiUtils.ACTION_ADD && imageToStore != null) {
                Log.d("success", "Add new book action");
                result = addBook(title, description, author, imageToStore);
            } else if (action == AnemiUtils.ACTION_UPDATE && imageToStore != null) {
                Log.d("success", "Update new book action");
                result = updateBook(title, description, author, imageToStore);
            }
            if (result) {
                Toast.makeText(getContext(), "Successfully " + (action == AnemiUtils.ACTION_ADD ? "add new " : "update ") + " book", Toast.LENGTH_SHORT).show();
//                DialogListener dialogListener = (DialogListener) getContext();
//                DialogListener dialogListener = getListener();
                if (dialogListener != null) {
                    dialogListener.onFinishUpdateDialog(getArguments().getInt("position"), book);
                }
                dismiss();
            }
        });
    }

    public boolean updateBook(String title, String description, String author, Bitmap imageToStore) {
        boolean checkOperation = false;
        if(!title.isEmpty() && !title.equals(book.getBookName())) book.setBookName(title);
        if(!description.isEmpty() && !description.equals(book.getDescription())) book.setDescription(description);
        if(!author.isEmpty() && !author.equals(book.getAuthor())) book.setAuthor(author);
        if(imageToStore != null && !imageToStore.sameAs(book.getCover())) book.setCover(imageToStore);
        if(db.updateBook(book) > 0) {
            checkOperation = true;
            Log.d("success: ", "Update Book ID: " + book.getBookId() + " Book Title: " + book.getBookName() + " Author" + book.getAuthor());
        }
        return checkOperation;
    }

    public boolean addBook(String title, String description, String author, Bitmap imageToStore) {
        boolean checkOperation = false;
        if(!title.isEmpty() && !author.isEmpty() && !description.isEmpty() && imageToStore != null) {
            book = new Book(DUMMY_ID, title, description, author, imageToStore);
            int book_id = (int) db.addBook(book);
            checkOperation = (book_id == -1) ? false : true;
            book.setBookId(book_id);
            Log.d("success: ", "Add new Book ID: " + book.getBookId() + " Book Title: " + book.getBookName() + " Author" + book.getAuthor());
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
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private DialogUpdateBookFragment.DialogListener getListener(){
        DialogUpdateBookFragment.DialogListener listener;
        try{
            Fragment onInputSelected_Fragment = getTargetFragment();
            if (onInputSelected_Fragment != null){
                listener = (DialogUpdateBookFragment.DialogListener) onInputSelected_Fragment;
                Log.d("success cast: ", "dialog " + getTargetFragment());
            }
            else {
                Activity onInputSelected_Activity = getActivity();
                listener = (DialogUpdateBookFragment.DialogListener) onInputSelected_Activity;
                Log.d("success cast: ", "activity");
            }
            return listener;
        }catch(ClassCastException e){
            Log.e("Custom Dialog", "onAttach: ClassCastException: " + e.getMessage());
        }
        return null;
    }

    public interface DialogListener {
        void onFinishUpdateDialog(int position, Book book);
    }
}