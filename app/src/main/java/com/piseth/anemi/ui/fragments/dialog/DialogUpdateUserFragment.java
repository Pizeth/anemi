package com.piseth.anemi.ui.fragments.dialog;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
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
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.piseth.anemi.R;
import com.piseth.anemi.firebase.viewmodel.FirebaseUserViewModel;
import com.piseth.anemi.retrofit.apiservices.UserCallBack;
import com.piseth.anemi.retrofit.viewmodel.UserViewModel;
import com.piseth.anemi.utils.model.User;
import com.piseth.anemi.utils.util.AnemiUtils;
import com.piseth.anemi.utils.util.DatabaseManageHandler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class DialogUpdateUserFragment extends DialogFragment {
    private DatabaseManageHandler db;
    private Uri imageToStore;
    private String realPath;
    private TextInputLayout txt_username, txt_email, txt_password, txt_re_password, txt_phone;
    private Button addPhoto, saveButton;
    private TextView errorLabel;
    private ImageView profileImage;
    private OnUpdateCompletedDialogListener listener;
    private FirebaseUserViewModel firebaseUserViewModel;
    private UserViewModel userViewModel;
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
        userViewModel = new ViewModelProvider((getActivity())).get(UserViewModel.class);
        firebaseUserViewModel = new ViewModelProvider(getActivity()).get(FirebaseUserViewModel.class);
//        String id;
        long id;

        if (getArguments() != null) {
//            id = getArguments().getString("user_id");
            id = getArguments().getLong("user_id");
            if (id != 0) {
                Log.d("Successful: ", "user_id to update is " + id);
//                User user = new User();
                userViewModel.getUserById(id, new UserCallBack() {
                    @Override
                    public void onSuccess(User user) {
                        Log.d("msg", "jol again");
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        txt_username.getEditText().setText(user.getUsername());
                        txt_email.getEditText().setText(user.getEmail());
                        txt_phone.getEditText().setText(user.getPhone());
                        if(user.getAvatar().isEmpty()) {
                            profileImage.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.baseline_person_outline_24));
                        } else {
                            Glide.with(profileImage.getContext()).load(AnemiUtils.SERVER_API + user.getAvatar()).into(profileImage);
                        }
                        profileImage.setCropToPadding(true);
                        profileImage.setClipToOutline(true);
                        if(currentUser != null && !currentUser.getUid().equals(id)) {
                            txt_email.getEditText().setEnabled(false);
                            txt_password.getEditText().setEnabled(false);
                            txt_re_password.getEditText().setEnabled(false);
                        }
                        Log.d("USERNAME: ", user.getUsername() + " 's data acquired'");

                        addPhoto.setOnClickListener(view1 -> {
                            pickImage.launch("image/*");
//                            Log.d("ok", imageToStore.toString());
//                            profileImage.setImageURI(imageToStore);
                        });
                        saveButton.setOnClickListener(view1 -> {
                            String username, email, password, re_password, phone, current_password = user.getPassword();
                            boolean isUpdateEmail = false, isUpdatePassword = false;

//                    doc_id = getArguments().getString("user_id");
                            username = (txt_username.getEditText() != null) ? txt_username.getEditText().getText().toString().trim() : "";
                            email = (txt_email.getEditText() != null) ? txt_email.getEditText().getText().toString().trim() : "";
                            password = (txt_password.getEditText() != null) ? txt_password.getEditText().getText().toString().trim() : "";
                            re_password = (txt_re_password.getEditText() != null) ? txt_re_password.getEditText().getText().toString().trim() : "";
                            phone = (txt_phone.getEditText() != null) ? txt_phone.getEditText().getText().toString().trim() : "";

//                                Validation still has some problem
//                                if (!isValidUsername(username) | !isValidPassword(password) |
//                                    !isValidRePassword(re_password, password) | !isValidPhoneNo(phone) |
//                                    !isValidPhoto(imageToStore)) {
//                                    return;
//                                }
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
                            updateUser(id, user, imageToStore, isUpdateEmail, isUpdatePassword, current_password);
                            Log.d("Successful: ", "Back to manage user Screen");
                            if (listener != null) {
                                listener.onFinishUpdateDialog(user);
                                Log.d("Update: ", " Position " + getArguments().getInt("position"));
                            }
                            dismiss();
                        });
                    }

                    @Override
                    public void onFailure() {
                        Log.d(TAG,"User not found!");
                    }
                });

//                userRef.document(id).get().addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            User user = document.toObject(User.class);
//                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                            txt_username.getEditText().setText(user.getUsername());
//                            txt_email.getEditText().setText(user.getEmail());
//                            txt_phone.getEditText().setText(user.getPhone());
//                            Glide.with(profileImage.getContext()).load(user.getAvatar()).into(profileImage);
//                            profileImage.setCropToPadding(true);
//                            profileImage.setClipToOutline(true);
//                            if(currentUser != null && !currentUser.getUid().equals(id)) {
//                                txt_email.getEditText().setEnabled(false);
//                                txt_password.getEditText().setEnabled(false);
//                                txt_re_password.getEditText().setEnabled(false);
//                            }
//                            Log.d("USERNAME: ", user.getUsername() + " 's data acquired'");
//
//                            addPhoto.setOnClickListener(view1 -> pickImage.launch("image/*"));
//                            saveButton.setOnClickListener(view1 -> {
//                                String doc_id, username, email, password, re_password, phone, current_password = user.getPassword();
//                                boolean isUpdateEmail = false, isUpdatePassword = false;
//
//                                doc_id = getArguments().getString("user_id");
//                                username = (txt_username.getEditText() != null) ? txt_username.getEditText().getText().toString().trim() : "";
//                                email = (txt_email.getEditText() != null) ? txt_email.getEditText().getText().toString().trim() : "";
//                                password = (txt_password.getEditText() != null) ? txt_password.getEditText().getText().toString().trim() : "";
//                                re_password = (txt_re_password.getEditText() != null) ? txt_re_password.getEditText().getText().toString().trim() : "";
//                                phone = (txt_phone.getEditText() != null) ? txt_phone.getEditText().getText().toString().trim() : "";
//
////                                Validation still has some problem
////                                if (!isValidUsername(username) | !isValidPassword(password) |
////                                    !isValidRePassword(re_password, password) | !isValidPhoneNo(phone) |
////                                    !isValidPhoto(imageToStore)) {
////                                    return;
////                                }
//                                if (!username.isEmpty() && !username.equals(user.getUsername())) user.setUsername(username);
//                                if (!email.isEmpty() && !email.equals(user.getEmail())) {
//                                    isUpdateEmail = true;
//                                    user.setEmail(email);
//                                }
//                                if (!password.isEmpty() && !re_password.isEmpty() && password.equals(re_password)) {
//                                    Log.d("hhh", "password " + password);
//                                    Log.d("hhh", "re password " + re_password);
//                                    isUpdatePassword = true;
//                                    current_password = user.getPassword();
//                                    user.setPassword(password);
//                                }
//                                if (!phone.isEmpty() && !phone.equals(user.getPhone())) user.setPhone(phone);
//                                updateUser(doc_id, user, imageToStore, isUpdateEmail, isUpdatePassword, current_password);
//                                Log.d("Successful: ", "Back to manage user Screen");
//                                if (listener != null) {
//                                    listener.onFinishUpdateDialog(user);
//                                    Log.d("Update: ", " Position " + getArguments().getInt("position"));
//                                }
//                                dismiss();
//                            });
//                        } else {
//                            Log.d(TAG, "No such document");
//                        }
//                    } else {
//                        Log.d(TAG, "get failed with ", task.getException());
//                    }
//                });
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseManageHandler(getContext());
        Log.d("API123", "onCreate");
    }

    private void updateUser(long id, /*String doc_Id,*/ User user, Uri photo, boolean isUpdateEmail, boolean isUpdatePassword, String current_password) {
        Log.d("Update", user.getUsername() + " " + user.getPassword() + " " + user.getPhone());
//        userViewModel.updateUser(id, RealPathUtil.getRealPath(getContext(), imageToStore), user);
        userViewModel.updateUser(id, createFile(getContext(), imageToStore), user);
//        firebaseUserViewModel.updateUser(photo, user, doc_Id, isUpdateEmail, isUpdatePassword, current_password);
        Toast.makeText(getContext(), user.getUsername() + "'s has been successfully updated", Toast.LENGTH_LONG).show();
    }

    private File createFile(Context context, Uri file) {
        Preconditions.checkNotNull(file);
        InputStream inputStream = null;
        long size = -1;
        try {
            ContentResolver resolver = getContext().getContentResolver();
            ParcelFileDescriptor fd = null;
            try {
                fd = resolver.openFileDescriptor(file, "r");
                if (fd != null) {
                    size = fd.getStatSize();
                    fd.close();
                }
            } catch (NullPointerException npe) {
                // happens under test.
                Log.w(TAG, "NullPointerException during file size calculation.", npe);
                size = -1;
            } catch (IOException checkSizeError) {
                Log.w(TAG, "could not retrieve file size for upload " + file.toString(), checkSizeError);
            }

            inputStream = resolver.openInputStream(file);
            if (inputStream != null) {
                if (size == -1) {
                    // If we had issues calculating the size, try stream.available -- it may still work
                    try {
                        int streamSize = inputStream.available();
                        if (streamSize >= 0) {
                            size = streamSize;
                        }
                    } catch (IOException e) {
                        // Ignore the error and continue without a size.  We document it may not be there.
                    }
                }
                inputStream = new BufferedInputStream(inputStream);
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "could not locate file for uploading:" + file.toString());
        }

//        byte[] bytes = ByteStreams.toByteArray(inputStream);
        File image = new File(context.getCacheDir(), AnemiUtils.getFileName(context, file));
        Log.d("image", "Image name: " + image.getName());
        Log.d("image", "Image name: " + image.getPath());
//        File image = null;
//        Files.copy(inputStream, image, StandardCopyOption.REPLACE_EXISTING);
        return image;
    }

//    private getFile(Uri selectedImage) {
//        // Get the Image from data
////        Uri selectedImage = data.getData();
//        String selectedFilePath = FilePath.getPath(this,selectedImage);
//        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//        assert cursor != null;
//        cursor.moveToFirst();
//        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//        imagePath = cursor.getString(columnIndex);
////                str1.setText(mediaPath);
//        // Set the Image in ImageView for Previewing the Media
//        imageView.setImageBitmap(BitmapFactory.decodeFile(selectedFilePath));
//        cursor.close();
//    }

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

    ActivityResultLauncher pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        imageToStore = result;
                        profileImage.setImageURI(result);
                      Log.d("img", "Image string " + result.toString());
                      Log.d("img", "Image path " + result.getPath());
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

    public void clickListeners() {

    }
}
