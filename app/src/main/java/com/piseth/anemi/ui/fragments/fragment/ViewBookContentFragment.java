package com.piseth.anemi.ui.fragments.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.piseth.anemi.R;
import com.piseth.anemi.firebase.viewmodel.FirebasePageViewModel;
import com.piseth.anemi.utils.adapter.ViewPagerAdapter;
import com.piseth.anemi.utils.model.Page;
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewBookContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewBookContentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ViewPager viewPager;
    private TextView book_title;
    private View back;
    private HomeFragment homeFragment;
    private FirebasePageViewModel firebasePageViewModel;

    public ViewBookContentFragment() {
        // Required empty public constructor
    }
    public ViewBookContentFragment(Bundle savedInstanceState) {
        super.setArguments(savedInstanceState);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewBookContentActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewBookContentFragment newInstance(String param1, String param2) {
        ViewBookContentFragment fragment = new ViewBookContentFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_book_content_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.view_pager);
        book_title = view.findViewById(R.id.current_title);
        back = view.findViewById(R.id.back_to_home);
        firebasePageViewModel = new ViewModelProvider(getActivity()).get(FirebasePageViewModel.class);
        if (getArguments() != null) {
            String id = getArguments().getString("book_id");
            String title = getArguments().getString("book_title");
            book_title.setText(title);
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            CollectionReference pageRef = firebaseFirestore.collection("Pages");
            pageRef.whereEqualTo("book_id", id).addSnapshotListener((value, error) -> {
                List<Page> pages = new ArrayList<>();
                if (error != null) {
                    return;
                }
                if (value != null) {
                    for (QueryDocumentSnapshot documentSnapshot : value) {
                        Page page = documentSnapshot.toObject(Page.class);
                        pages.add(page);
                    }
                    ViewPagerAdapter adapter = new ViewPagerAdapter(pages);
                    viewPager.setAdapter(adapter);
                    BookFlipPageTransformer bookFlip = new BookFlipPageTransformer();
                    bookFlip.setScaleAmountPercent(10f);
                    viewPager.setPageTransformer(true, bookFlip);
                }
            });
        }

        back.setOnClickListener(view1 -> {
            homeFragment = new HomeFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        });
    }
}