package com.piseth.anemi.ui.fragments.dialog;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.piseth.anemi.R;
import com.piseth.anemi.firebase.viewmodel.FirebaseBookViewModel;
import com.piseth.anemi.retrofit.viewmodel.BookViewModel;
import com.piseth.anemi.utils.model.Book;
import com.piseth.anemi.utils.model.TokenUser;
import com.piseth.anemi.utils.model.User;
import com.piseth.anemi.utils.util.AnemiUtils;
import com.uploadcare.android.library.api.UploadcareClient;
import com.uploadcare.android.library.api.UploadcareFile;
import com.uploadcare.android.library.callbacks.UploadFileCallback;
import com.uploadcare.android.library.exceptions.UploadcareApiException;
import com.uploadcare.android.library.upload.FileUploader;
import com.uploadcare.android.library.upload.Uploader;

public class DialogUpdateBookFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int PICK_IMAGE = 1;
    public static final int DUMMY_ID = 99;
    private int action;
    private Uri imageToStore;
    private TextInputLayout txt_title, txt_author, txt_description;
    private ImageView bookCover;
    private Button addPhoto, saveButton, backButton;
//    private FirebaseBookViewModel firebaseBookViewModel;
//    private FirebaseFirestore firebaseFirestore;
//    private CollectionReference bookRef;
    private BookViewModel bookViewModel;
    private OnCompletedDialogListener listener;
    private Long id;

    public DialogUpdateBookFragment(Bundle savedInstanceState, int action) {
        super.setArguments(savedInstanceState);
        this.action = action;
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
            dialog.setCanceledOnTouchOutside(true);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        addPhoto = view.findViewById(R.id.btnAddCover);
        saveButton = view.findViewById(R.id.btnSaveBook);
        backButton = view.findViewById(R.id.btnBack);

        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);

        bookCover.setCropToPadding(true);
        bookCover.setClipToOutline(true);

        addPhoto.setOnClickListener(view1 -> { pickImage.launch("image/*"); });
        bookCover.setOnClickListener(view1 -> { pickImage.launch("image/*"); });

        if (getArguments() != null) {
            id = getArguments().getLong("book_id");
            if (id != null ) {
                Log.d("Successful: ", "book_id to update is " + id);
                if(id == AnemiUtils.NEW_BOOK_ENTRY) {
                    Log.d("Successful: ", "no book_id to update is ");
                    saveButton.setOnClickListener(view1 -> {
                        populateDataFromDialog(id, new Book());
                    });
                } else {
                    bookViewModel.getBookById(id).observe(getViewLifecycleOwner(), new Observer<Book>() {
                        @Override
                        public void onChanged(Book book) {
                            if(book != null) {
                                txt_title.getEditText().setText(book.getBookTitle());
                                txt_author.getEditText().setText(book.getAuthor().getPenName());
                                txt_description.getEditText().setText(book.getDescription());
                                Glide.with(bookCover.getContext()).load(book.getCover()).into(bookCover);
                                Log.d("SUCCESS", book.getBookTitle() + " 's data acquired'");
                                saveButton.setOnClickListener(view1 -> {
                                    populateDataFromDialog(id, book);
                                });
                            }
                        }
                    });
                }
            }
        }


//        if (getArguments() != null) {
//            id = getArguments().getString("book_id");
//            if (!id.isEmpty()) {
//                Log.d("Successful: ", "book_id to update is " + id);
//                if(id.equals(AnemiUtils.NEW_BOOK_ENTRY)) {
//                    Log.d("Successful: ", "no book_id to update is ");
//                    saveButton.setOnClickListener(view1 -> {
//                        populateDataFromDialog(new Book());
//                    });
//                } else {
//                    bookRef.document(id).get().addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                Book book = document.toObject(Book.class);
//                                txt_title.getEditText().setText(book.getBookTitle());
//                                txt_author.getEditText().setText(book.getAuthor());
//                                txt_description.getEditText().setText(book.getDescription());
//                                Glide.with(bookCover.getContext()).load(book.getCover()).into(bookCover);
//                                Log.d("SUCCESS", book.getBookTitle() + " 's data acquired'");
//                                saveButton.setOnClickListener(view1 -> {
//                                    populateDataFromDialog(book);
//                                });
//                            } else {
//                                Log.d(TAG, "No such document");
//                            }
//                        } else {
//                            Log.d(TAG, "get failed with ", task.getException());
//                        }
//                    });
//                }
//            }
//            addPhoto.setOnClickListener(view1 -> pickImage.launch("image/*"));
//            backButton.setOnClickListener(view1 -> dismiss());
//        }
    }

    public void populateDataFromDialog(Long id, Book book) {
        String title, author, description;

        title = (txt_title.getEditText() != null) ? txt_title.getEditText().getText().toString().trim() : "";
        author = (txt_author.getEditText() != null) ? txt_author.getEditText().getText().toString().trim() : "";
        description = (txt_description.getEditText() != null) ? txt_description.getEditText().getText().toString().trim() : "";
        if(action == AnemiUtils.ACTION_ADD) {
            book = new Book(title, description, author);
            Log.d("success", "Add new book action");
            saveBook(id, book);
        } else if (action == AnemiUtils.ACTION_UPDATE) {
            if (!title.isEmpty() && !title.equals(book.getBookTitle())) book.setBookTitle(title);
            if (!author.isEmpty() && !author.equals(book.getAuthor())) book.setAuthorId(Integer.parseInt(author));
            if (!description.isEmpty() && !description.equals(book.getDescription())) book.setDescription(description);
            Log.d("success", "Update book action");
           saveBook(id, book);
        }
        Toast.makeText(getContext(), "Successfully " + (action == AnemiUtils.ACTION_ADD ? "add new " : "update ") + " book", Toast.LENGTH_SHORT).show();
        if (listener != null) {
            listener.onFinishUpdateDialog(id, book);
        }
        Log.d("Successful: ", "Back to manage user Screen");
        dismiss();
    }

    public void saveBook(Long id, Book book) {
        if(imageToStore != null) {
            UploadcareClient uploadCare = new UploadcareClient(AnemiUtils.PUBLIC_KEY, AnemiUtils.PRIVATE_KEY);
            Uploader uploader = new FileUploader(uploadCare, imageToStore, getContext()).store(true);
            uploader.uploadAsync(new UploadFileCallback() {
                @Override
                public void onProgressUpdate(long l, long l1, double v) {}

                @Override
                public void onFailure(UploadcareApiException e) {
                    // Handle errors.
                    Log.d("Error", "Upload error " + e.getMessage());
                }

                @Override
                public void onSuccess(UploadcareFile file) {
                    Log.d("Success", "File OG URL is " + file.getOriginalFileUrl());
                    Log.d("Success", "File OG name is " + file.getOriginalFilename());
                    String name = file.getOriginalFileUrl().toString();
                    Log.d("Uploaded", "File cdn location " + name);
                    String avatarUrl = name.replace(file.getOriginalFilename(), book.getBookTitle());
                    Log.d("Success Rename", "File after rename " + avatarUrl);

                    book.setCover(avatarUrl);
                    Log.d("User Updated", book.getBookTitle() + "'s info updated");
                    listener.onFinishUpdateDialog(id, book);
                }
            });
        } else {
            listener.onFinishUpdateDialog(id, book);
        }
    }

    public void addBook(Book book, Uri cover) {
        Log.d("Insert: ", "Insert book : " + book.getBookTitle() + " " + book.getDescription() + " " + book.getAuthor());
//        firebaseBookViewModel.addNewBook(cover, book);
    }

    ActivityResultLauncher pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        imageToStore = result;
                        bookCover.setImageURI(result);
                    }
                }
            });

    public interface OnCompletedDialogListener {
        void onFinishUpdateDialog(Long id, Book book);
    }

    public void setOnCompletedDialogListener(OnCompletedDialogListener listener) {
        this.listener = listener;
    }
}