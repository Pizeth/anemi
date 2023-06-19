package com.piseth.anemi.ui.fragments.dialog;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
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
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.piseth.anemi.R;
import com.piseth.anemi.firebase.viewmodel.FirebaseBookViewModel;
import com.piseth.anemi.utils.model.Book;
import com.piseth.anemi.utils.util.AnemiUtils;
import com.piseth.anemi.utils.util.DatabaseManageHandler;

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
    private Uri imageToStore;
    private TextInputLayout txt_title, txt_author, txt_description;
    private ImageView bookCover;
    private Button addPhoto, saveButton, backButton;
    private FirebaseBookViewModel firebaseBookViewModel;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference bookRef;
    private OnCompletedDialogListener listener;

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
        addPhoto = view.findViewById(R.id.btnAddCover);
        saveButton = view.findViewById(R.id.btnSaveBook);
        backButton = view.findViewById(R.id.btnBack);
        firebaseFirestore = FirebaseFirestore.getInstance();
        bookRef = firebaseFirestore.collection("Books");
        firebaseBookViewModel = new ViewModelProvider(getActivity()).get(FirebaseBookViewModel.class);
        bookCover.setCropToPadding(true);
        bookCover.setClipToOutline(true);
        String id;

        if (getArguments() != null) {
            id = getArguments().getString("book_id");
            if (!id.isEmpty()) {
                Log.d("Successful: ", "book_id to update is " + id);
//                book = db.getBook((book_id));
                bookRef.document(id).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Book book = document.toObject(Book.class);

                            txt_title.getEditText().setText(book.getBookName());
                            txt_author.getEditText().setText(book.getAuthor());
                            txt_description.getEditText().setText(book.getDescription());
                            Glide.with(bookCover.getContext()).load(book.getCover()).into(bookCover);

                            Log.d("SUCCESS", book.getBookName() + " 's data acquired'");
                        } else {
                            Log.d(TAG, "No such document");
                        }
                        addPhoto.setOnClickListener(view1 -> pickImage.launch("image/*"));
                        backButton.setOnClickListener(view1 -> dismiss());
                        saveButton.setOnClickListener(view1 -> {
                            String doc_id, title, author, description;

                            doc_id = getArguments().getString("book_id");
                            title = (txt_title.getEditText() != null) ? txt_title.getEditText().getText().toString().trim() : "";
                            author = (txt_author.getEditText() != null) ? txt_author.getEditText().getText().toString().trim() : "";
                            description = (txt_description.getEditText() != null) ? txt_description.getEditText().getText().toString().trim() : "";
                            boolean result = false;
                            if(action == AnemiUtils.ACTION_ADD) {
                                Log.d("success", "Add new book action");
                                addBook(title, description, author, imageToStore);
                            } else if (action == AnemiUtils.ACTION_UPDATE) {
                                Log.d("success", "Update book action");
                                updateBook(doc_id, title, description, author, imageToStore);
                            }
                            if (result) {
                                Toast.makeText(getContext(), "Successfully " + (action == AnemiUtils.ACTION_ADD ? "add new " : "update ") + " book", Toast.LENGTH_SHORT).show();

                            }
                            if (listener != null) {
                                listener.onFinishUpdateDialog(book);
                            }
                            dismiss();
                        });
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });
            }
        }
    }

    private void updateBook(String doc_Id, String title, String description, String author, Uri cover) {
//        if (!isValidUsername(username) | !isValidPassword(password) |
//                !isValidRePassword(re_password, password) | !isValidPhoneNo(phone) |
//                !isValidPhoto(photo)) {
//            return;
//        }
//        if (!username.isEmpty() && !username.equals(user.getUsername())) user.setUsername(username);
//        if (!email.isEmpty() && !email.equals(user.getEmail())) user.setEmail(email);
//        if (!password.isEmpty() && !re_password.isEmpty() && password.equals(re_password))
//            user.setPassword(password);
//        if (!phone.isEmpty() && !phone.equals(user.getPhone())) user.setPhone(phone);
        Log.d("Update: ", "Update book: " + title + " " + description + " " + author);
        Book book = new Book(title, description, author);
        firebaseBookViewModel.updateBook(cover, book, doc_Id);
    }

//    public boolean updateBook(String title, String description, String author, Bitmap imageToStore) {
//        boolean checkOperation = false;
//        if(!title.isEmpty() && !title.equals(book.getBookName())) book.setBookName(title);
//        if(!description.isEmpty() && !description.equals(book.getDescription())) book.setDescription(description);
//        if(!author.isEmpty() && !author.equals(book.getAuthor())) book.setAuthor(author);
//        if(imageToStore != null && !imageToStore.sameAs(book.getCover())) book.setCover(imageToStore);
//        if(db.updateBook(book) > 0) {
//            checkOperation = true;
//            Log.d("success: ", "Update Book ID: " + book.getBookId() + " Book Title: " + book.getBookName() + " Author" + book.getAuthor());
//        }
//        return checkOperation;
//    }

//    public boolean addBook(String title, String description, String author, Bitmap imageToStore) {
//        boolean checkOperation = false;
//        if(!title.isEmpty() && !author.isEmpty() && !description.isEmpty() && imageToStore != null) {
//            book = new Book(DUMMY_ID, title, description, author, imageToStore);
//            int book_id = (int) db.addBook(book);
//            checkOperation = (book_id == -1) ? false : true;
//            book.setBookId(book_id);
//            Log.d("success: ", "Add new Book ID: " + book.getBookId() + " Book Title: " + book.getBookName() + " Author" + book.getAuthor());
//        }
//        return checkOperation;
//    }

    public void addBook(String title, String description, String author, Uri cover) {
        Log.d("Insert: ", "Insert book : " + title + " " + description + " " + author);
        Book book = new Book(title, description, author);
        firebaseBookViewModel.addNewBook(cover, book);
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
        void onFinishUpdateDialog(Book book);
    }

    public void setOnCompletedDialogListener(OnCompletedDialogListener listener) {
        this.listener = listener;
    }
}