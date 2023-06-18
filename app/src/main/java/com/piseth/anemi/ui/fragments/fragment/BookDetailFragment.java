package com.piseth.anemi.ui.fragments.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.piseth.anemi.R;
import com.piseth.anemi.firebase.viewmodel.FirebaseBookViewModel;
import com.piseth.anemi.firebase.viewmodel.FirebasePageViewModel;
import com.piseth.anemi.utils.adapter.FirestoreRecyclerBookListAdapter;
import com.piseth.anemi.utils.adapter.FirestoreRecyclerPageListAdapter;
import com.piseth.anemi.utils.model.Book;
import com.piseth.anemi.utils.model.Page;
import com.piseth.anemi.utils.model.User;
import com.piseth.anemi.utils.util.AnemiUtils;
import com.piseth.anemi.utils.util.DatabaseManageHandler;
import com.piseth.anemi.utils.util.WrapContentLinearLayoutManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseManageHandler db;
    private SharedPreferences loggedInUser;
    private TextView txt_title, txt_author, txt_description;
    private ImageView bookCover;
    private Uri imageToStore;
    private RecyclerView recyclerView;
    private BookManagementFragment bookManagementFragment;
    //    private BookDetailFragment bookDetailFragment;
    private FloatingActionButton fabAddBook, fabBack;
    private FirebasePageViewModel firebasePageViewModel;
    private FirestoreRecyclerPageListAdapter fireAdapter;
    private String id;


    public BookDetailFragment() {
        // Required empty public constructor
    }

    public BookDetailFragment(Bundle savedInstanceState) {
        // Required empty public constructor
        super.setArguments(savedInstanceState);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmentt_book_detail.
     */
    // TODO: Rename and change types and number of parameters
    public static BookDetailFragment newInstance(String param1, String param2) {
        BookDetailFragment fragment = new BookDetailFragment();
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
        if (getActivity() != null) {
            loggedInUser = getActivity().getSharedPreferences(AnemiUtils.LOGGED_IN_USER, MODE_PRIVATE);
        }
//        db = new DatabaseManageHandler(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txt_title = view.findViewById(R.id.book_title);
        txt_author = view.findViewById(R.id.author);
        txt_description = view.findViewById(R.id.description);
        bookCover = view.findViewById(R.id.book_cover);
        if (getArguments() != null) {
//            book = db.getBook(getArguments().getInt("book_id"));
            id = getArguments().getString("book_id");
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            CollectionReference bookRef = firebaseFirestore.collection("Books");
            bookRef.document(id).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    Book book = doc.toObject(Book.class);
                    if (book != null) {
                        txt_title.setText(book.getBookName());
                        txt_author.setText(book.getAuthor());
                        txt_description.setText(book.getDescription());
                        Glide.with(bookCover.getContext()).load(book.getCover()).into(bookCover);
                        Log.d("success!", book.getBookName() + " 's data acquired'");
                    }
                }
            });

            firebasePageViewModel = new ViewModelProvider(getActivity()).get(FirebasePageViewModel.class);
            FirestoreRecyclerOptions<Page> options = new FirestoreRecyclerOptions.Builder<Page>()
                    .setQuery(firebasePageViewModel.getAllPageFromBookQuery(id), Page.class)
                    .build();
            fireAdapter = new FirestoreRecyclerPageListAdapter(options);
            recyclerView = view.findViewById(R.id.recyclerPageView);
            recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(fireAdapter);

            fabAddBook = view.findViewById(R.id.floating_add_page);
            fabBack = view.findViewById(R.id.floating_back_button);
            User user = AnemiUtils.getLoggedInUser(loggedInUser);
            if (user != null) {
                if (user.getUserRoleId() != AnemiUtils.ROLE_ADMIN) {
                    fabAddBook.setVisibility(View.INVISIBLE);
                }
            }
            fabAddBook.setOnClickListener(view1 -> {
                pickImage.launch("image/*");
            });
            fabBack.setOnClickListener(view1 -> {
                bookManagementFragment = new BookManagementFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.container, bookManagementFragment).commit();
            });
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    deleteBookDialog(viewHolder.getAbsoluteAdapterPosition());
                }
            }).attachToRecyclerView(recyclerView);

            fireAdapter.setOnPageListClickListener(new FirestoreRecyclerPageListAdapter.OnPageListClickListener() {
                @Override
                public void onUpdate(int p) {
//                    dialogAction(fireAdapter.getDocumentId(p), p, AnemiUtils.ACTION_UPDATE);
                    pickImage.launch("image/*");
                }

                @Override
                public void onDelete(int p) {
                    deleteBookDialog(p);
                }

                @Override
                public void onView(int p) {
                    Bundle book_id = new Bundle();
//                    book_id.putString("book_id", fireAdapter.getDocumentId(p));
//                    bookDetailFragment = new BookDetailFragment(book_id);
//                    getParentFragmentManager().beginTransaction().replace(R.id.container, bookDetailFragment).commit();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView.getRecycledViewPool().clear();
        fireAdapter.startListening();
    }

    public void deleteBookDialog(int p) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Remove Book");
        alertDialog.setMessage("Delete this book??");
        alertDialog.setPositiveButton("CANCEL", (dialog, which) -> dialog.cancel());
        alertDialog.setNegativeButton("YES", (dialog, which) -> {
            // DO SOMETHING HERE
//            firebasePageViewModel.deleteBook(fireAdapter.getDocumentId(p));
//            Log.d("BookManagement", "Successfully Delete book" + fireAdapter.getItem(p).getBookName());
//            Toast.makeText(getContext(), "Successfully Delete Book name " + fireAdapter.getItem(p).getBookName(), Toast.LENGTH_LONG).show();
        });

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    ActivityResultLauncher pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        imageToStore = result;
//                        bookCover.setImageURI(result);
                        Page page = new Page();
                        int pageNo = fireAdapter.getItemCount();
                        if(pageNo == 0) {
                            pageNo = 1;
                        } else {
                            pageNo ++;
                        }
//                        pageNo = pageNo == 0 ? 1 : pageNo++;
                        page.setBook_id(id);
                        page.setPageNumber(pageNo);
                        firebasePageViewModel.uploadPageToFirebase(imageToStore, page, id);
                        Toast.makeText(getContext(), "Page added succefull!", Toast.LENGTH_LONG).show();
                    }
                }
            });
}