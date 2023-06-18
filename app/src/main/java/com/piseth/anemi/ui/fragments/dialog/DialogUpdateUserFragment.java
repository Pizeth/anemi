package com.piseth.anemi.ui.fragments.dialog;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.piseth.anemi.R;
import com.piseth.anemi.firebase.viewmodel.FirebaseUserViewModel;
import com.piseth.anemi.room.viewmodel.UserRoomViewModel;
import com.piseth.anemi.utils.model.User;
import com.piseth.anemi.utils.util.AnemiUtils;
import com.piseth.anemi.utils.util.DatabaseManageHandler;

public class DialogUpdateUserFragment extends DialogFragment {

    public static final int PICK_IMAGE = 1;
    private DatabaseManageHandler db;
    //    private User user;
    private Uri imageToStore;
    private TextInputLayout txt_username, txt_email, txt_password, txt_re_password, txt_phone;
    private Button addPhoto, saveButton;
    private TextView errorLabel;
    private ImageView profileImage;
    private OnUpdateCompletedDialogListener listener;
    private UserRoomViewModel userRoomViewModel;
    private FirebaseUserViewModel firebaseUserViewModel;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference userRef;

    public DialogUpdateUserFragment(Bundle savedInstanceState) {
        super.setArguments(savedInstanceState);
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

        builder.setTitle("Update User");
        builder.setMessage("Update User's information");
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
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
//            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.shape);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt_username = view.findViewById(R.id.username);
        txt_email = view.findViewById(R.id.email);
        txt_password = view.findViewById(R.id.password);
        txt_re_password = view.findViewById(R.id.re_password);
        txt_phone = view.findViewById(R.id.phone);
        profileImage = view.findViewById(R.id.image_logo);
        errorLabel = view.findViewById(R.id.lbl_error);
        addPhoto = view.findViewById(R.id.buttonAddPhoto);
        saveButton = view.findViewById(R.id.btnSave);
        firebaseFirestore = FirebaseFirestore.getInstance();
        userRef = firebaseFirestore.collection("Users");
        userRoomViewModel = new ViewModelProvider(getActivity()).get(UserRoomViewModel.class);
        firebaseUserViewModel = new ViewModelProvider(getActivity()).get(FirebaseUserViewModel.class);
        String id;

        if (getArguments() != null) {
//            int user_id = (int) getArguments().getLong("user_id");
            id = getArguments().getString("user_id");
            if (!id.isEmpty()) {
                Log.d("Successful: ", "user_id to update is " + id);
//                user = db.getUser((user_id));
                userRef.document(id).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User user = document.toObject(User.class);
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            txt_username.getEditText().setText(user.getUsername());
                            txt_email.getEditText().setText(user.getEmail());
                            txt_phone.getEditText().setText(user.getPhone());
                            Glide.with(profileImage.getContext()).load(user.getPhoto()).into(profileImage);
                            profileImage.setCropToPadding(true);
                            profileImage.setClipToOutline(true);
                            if(!currentUser.getUid().equals(id)) {
                                txt_email.getEditText().setEnabled(false);
                                txt_password.getEditText().setEnabled(false);
                                txt_re_password.getEditText().setEnabled(false);
                            }
                            Log.d("USERNAME: ", user.getUsername() + " 's data acquired'");

                            addPhoto.setOnClickListener(view1 -> pickImage.launch("image/*"));
                            saveButton.setOnClickListener(view1 -> {
                                String doc_id, username, email, password, re_password, phone, current_password = user.getPassword();
                                boolean isUpdateEmail = false, isUpdatePassword = false;

                                doc_id = getArguments().getString("user_id");
                                username = (txt_username.getEditText() != null) ? txt_username.getEditText().getText().toString().trim() : "";
                                email = (txt_email.getEditText() != null) ? txt_email.getEditText().getText().toString().trim() : "";
                                password = (txt_password.getEditText() != null) ? txt_password.getEditText().getText().toString().trim() : "";
                                re_password = (txt_re_password.getEditText() != null) ? txt_re_password.getEditText().getText().toString().trim() : "";
                                phone = (txt_phone.getEditText() != null) ? txt_phone.getEditText().getText().toString().trim() : "";

//                            if (!isValidUsername(username) | !isValidPassword(password) |
//                                !isValidRePassword(re_password, password) | !isValidPhoneNo(phone) |
//                                !isValidPhoto(photo)) {
//                                return;
//                            }
                                if (!username.isEmpty() && !username.equals(user.getUsername())) user.setUsername(username);
                                if (!email.isEmpty() && !email.equals(user.getEmail())) {
                                    isUpdateEmail = true;
                                    user.setEmail(email);
                                }
                                if (!password.isEmpty() && !re_password.isEmpty() && password.equals(re_password)) {
                                    Log.d("hhh", "password " + password);
                                    Log.d("hhh", "re password " + re_password);
                                    isUpdatePassword = true;
                                    current_password = user.getPassword();
                                    user.setPassword(password);
                                }
                                if (!phone.isEmpty() && !phone.equals(user.getPhone())) user.setPhone(phone);
                                updateUser(doc_id, user, imageToStore, isUpdateEmail, isUpdatePassword, current_password);
                                Log.d("Successful: ", "Back to manage user Screen");
                                if (listener != null) {
                                    listener.onFinishUpdateDialog(user);
                                    Log.d("Update: ", " Position " + getArguments().getInt("position"));
                                }
                                dismiss();
                            });
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });
            }
        }

//        if (user != null) {
//            txt_username.getEditText().setText(user.getUsername());
//            txt_phone.getEditText().setText(user.getPhone());
////            profileImage.setImageBitmap(user.getPhoto());
//            Glide.with(profileImage.getContext()).load(user.getPhoto()).into(profileImage);
//            profileImage.setCropToPadding(true);
//            profileImage.setClipToOutline(true);
//            Log.d("USERNAME: ", user.getUsername() + " 's data acquired'");
//        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseManageHandler(getContext());
        Log.d("API123", "onCreate");
    }

    private void updateUser(String doc_Id, User user, Uri photo, boolean isUpdateEmail, boolean isUpdatePassword, String current_password) {
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
        Log.d("Insert: ", user.getUsername() + " " + user.getPassword() + " " + user.getPhone());
        firebaseUserViewModel.updateUser(photo, user, doc_Id, isUpdateEmail, isUpdatePassword, current_password);
        Toast.makeText(getContext(), user.getUsername() + "'s has been succesfully updated", Toast.LENGTH_LONG).show();
    }

//    public boolean updateUser(String username, String email, String password, String re_password, String phone, Uri imageToStore) {
//        boolean checkOperation = false;
//        if(!username.isEmpty() && !username.equals(user.getUsername())) user.setUsername(username);
//        if(!email.isEmpty() && !email.equals(user.getEmail())) user.setEmail(email);
//        if(!password.isEmpty() && !re_password.isEmpty() && password.equals(re_password)) user.setPassword(password);
//        if(!phone.isEmpty() && !phone.equals(user.getPhone())) user.setPhone(phone);
////        if(imageToStore != null && !imageToStore.sameAs(user.getPhoto())) user.setPhoto(imageToStore);
//        if(db.updateUser(user) > 0) {
//            Log.d("success!", "Successfully update username: " + user.getUsername());
//            checkOperation = true;
//        }
//        return checkOperation;
//    }

//    private void chooseImage() {
//        try {
//            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
//            getIntent.setType("image/*");
////            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            Intent pickIntent = new Intent(Intent.ACTION_PICK);
//            pickIntent.setType("image/*");
//
//            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
//            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
//
//            startActivityForResult(chooserIntent, PICK_IMAGE);
//        } catch(Exception e) {
//            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        try {
//            super.onActivityResult(requestCode, requestCode, data);
//            if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null && data.getData() != null) {
//                Uri selectedImageUri = data.getData();
//                imageToStore = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);
//                profileImage.setImageBitmap(imageToStore);
//            }
//        } catch(Exception e) {
//            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

    ActivityResultLauncher pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        imageToStore = result;
                        profileImage.setImageURI(result);
                    }
                }
            });

    private OnUpdateCompletedDialogListener getListener() {
        OnUpdateCompletedDialogListener listener;
        try {
            Fragment onInputSelected_Fragment = getTargetFragment();
            if (onInputSelected_Fragment != null) {
                listener = (OnUpdateCompletedDialogListener) onInputSelected_Fragment;
                Log.d("success cast: ", "dialog " + getTargetFragment());
            } else {
                Activity onInputSelected_Activity = getActivity();
                listener = (OnUpdateCompletedDialogListener) onInputSelected_Activity;
                Log.d("success cast: ", "activity");
            }
            return listener;
        } catch (ClassCastException e) {
            Log.e("Custom Dialog", "onAttach: ClassCastException: " + e.getMessage());
        }
        return null;
    }

    public interface OnUpdateCompletedDialogListener {
        void onFinishUpdateDialog(User user);
    }

    public void setOnUpdateCompletedDialogListener(OnUpdateCompletedDialogListener listener) {
        this.listener = listener;
    }

    /* Input Validation */
    private boolean isValidUsername(String text_username) {
        if (text_username.isEmpty()) {
            txt_username.setError("Username cannot be empty!");
            return false;
        } else if (!text_username.matches(AnemiUtils.NO_WHITE_SPACE)) {
            txt_username.setError("White space is not allow!");
            return false;
        } else {
            txt_username.setError(null);
            txt_username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean isValidPassword(String text_password) {
        if (text_password.isEmpty()) {
            txt_password.setError("Password cannot be empty!");
            return false;
        } else if (!text_password.matches(AnemiUtils.NO_WHITE_SPACE)) {
            txt_password.setError("White space is not allow!");
            return false;
        } else {
            txt_password.setError(null);
            txt_password.setErrorEnabled(false);
            return true;
        }

    }

    private boolean isValidRePassword(String text_re_password, String text_password) {
        if (text_re_password.isEmpty()) {
            txt_re_password.setError("Re-Password cannot be empty!");
            return false;
        } else if (!text_re_password.matches(AnemiUtils.NO_WHITE_SPACE)) {
            txt_re_password.setError("White space is not allow!");
            return false;
        } else if (!text_re_password.equals(text_password)) {
            txt_re_password.setError("Password and Re-Password does not match!");
            return false;
        } else {
            txt_re_password.setError(null);
            txt_re_password.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean isValidPhoneNo(String text_phone) {
        if (text_phone.isEmpty()) {
            txt_phone.setError("Phone cannot be empty");
            return false;
        } else if (!text_phone.matches(AnemiUtils.NO_WHITE_SPACE)) {
            txt_phone.setError("White space is not allow!");
            return false;
        } else {
            txt_phone.setError(null);
            txt_phone.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean isValidPhoto(Uri photo) {
        if (photo == null) {
            errorLabel.setText(R.string.error_photo);
            errorLabel.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorLabel.setText("");
            errorLabel.setVisibility(View.GONE);
            return true;
        }
    }
}
