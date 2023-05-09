package com.piseth.anemi;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

public class DialogUserUpdateFragment extends DialogFragment {

    public static final int PICK_IMAGE = 1;
    private DatabaseManageHandler db;
    private User user;
    private Bitmap imageToStore;
    private TextInputLayout txt_username, txt_password, txt_re_password, txt_phone;
    private ImageView profileImage;
    private DialogListener dialogListener;

    public DialogUserUpdateFragment(Bundle savedInstanceState, DialogListener dialogListener) {
        this.dialogListener = dialogListener;
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
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.anemi);
        builder.setTitle("Update User");
        builder.setMessage("Update User's information");
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
//            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
//        builder.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.shape));
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
//            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.shape);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        final EditText editText = view.findViewById(R.id.inEmail);
//
//        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("email")))
//            editText.setText(getArguments().getString("email"));
        txt_username = view.findViewById(R.id.username);
        txt_password = view.findViewById(R.id.password);
        txt_re_password = view.findViewById(R.id.re_password);
        txt_phone = view.findViewById(R.id.phone);
        profileImage = view.findViewById(R.id.image_logo);
        if (getArguments() != null) {
            int user_id = (int) getArguments().getLong("user_id");
            if (user_id != -1) {
                Log.d("Successful: ", "user_id to update is " + user_id);
                user = db.getUser((user_id));
            }
        }
//        if (loggedInUser.contains(LOGGED_IN_USER) && loggedInUser.contains(USER_PHOTO)) {
//            Gson gson = new Gson();
//            String json = loggedInUser.getString(LOGGED_IN_USER, "");
//            user = gson.fromJson(json, User.class);
//            byte[] photo = AnemiUtils.BASE64Decode(loggedInUser.getString(USER_PHOTO, ""));
//            imageToStore = AnemiUtils.getBitmapFromBytesArray(photo);
//            user.setPhoto(imageToStore);
        if(user != null) {
            txt_username.getEditText().setText(user.getUsername());
            txt_phone.getEditText().setText(user.getPhone());
            profileImage.setImageBitmap(user.getPhoto());
            profileImage.setCropToPadding(true);
            profileImage.setClipToOutline(true);
            Log.d("USERNAME: ", user.getUsername() + " 's data acquired'");
        }
//        }
        Button addPhoto = view.findViewById(R.id.buttonAddPhoto);
        Button saveButton = view.findViewById(R.id.btnSave);
//        addPhoto.setOnClickListener(this);
//        saveButton.setOnClickListener(this);
//
//        Button btnUpdate = view.findViewById(R.id.btnSave);
        addPhoto.setOnClickListener(view1 -> chooseImage());

        saveButton.setOnClickListener(view1 -> {
            String username, password, re_password, phone;

            username = (txt_username.getEditText() != null) ? txt_username.getEditText().getText().toString().trim() : "";
            password = (txt_password.getEditText() != null) ? txt_password.getEditText().getText().toString().trim() : "";
            re_password = (txt_re_password.getEditText() != null) ? txt_re_password.getEditText().getText().toString().trim() : "";
            phone = (txt_phone.getEditText() != null) ? txt_phone.getEditText().getText().toString().trim() : "";
            boolean result = updateUser(username, password, re_password, phone, imageToStore);
            if (result) {
                Toast.makeText(getContext(), "Successfully update user", Toast.LENGTH_SHORT).show();
                Log.d("Successful: ", "Back to manage user Screen");
//                DialogListener dialogListener = (DialogListener) getContext();
//                DialogListener dialogListener = getListener();
                if (dialogListener != null) {
                    dialogListener.onFinishUpdateDialog(getArguments().getInt("position"), user);
                    Log.d("Update: ", " Position " + getArguments().getInt("position"));
                }
                dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseManageHandler(getContext());
        Log.d("API123", "onCreate");
    }

    public boolean updateUser(String username, String password, String re_password, String phone, Bitmap imageToStore) {
        boolean checkOperation = false;
        if(!username.isEmpty() && !username.equals(user.getUsername())) user.setUsername(username);
        if(!password.isEmpty() && !re_password.isEmpty() && password.equals(re_password)) user.setPassword(password);
        if(!phone.isEmpty() && !phone.equals(user.getPhone())) user.setPhone(phone);
        if(imageToStore != null && !imageToStore.sameAs(user.getPhoto())) user.setPhoto(imageToStore);
        if(db.updateUser(user) > 0) {
//            SharedPreferences.Editor prefsEditor = loggedInUser.edit();
//            byte[] userPhoto = AnemiUtils.getBitmapAsByteArray(user.getPhoto());
//            Gson gson = new Gson();
//            String json = gson.toJson(user);
//            prefsEditor.putString(LOGGED_IN_USER, json);
//            prefsEditor.putString(USER_PHOTO, AnemiUtils.BASE64Encode(userPhoto));
//            prefsEditor.apply();
            Log.d("Update: ", user.getUsername() + " " + user.getPassword());
            Toast.makeText(getContext(), user.getUsername() + " " + user.getPassword(), Toast.LENGTH_SHORT).show();
            checkOperation = true;
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
                profileImage.setImageBitmap(imageToStore);
            }
        } catch(Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private DialogListener getListener(){
        DialogListener listener;
        try{
            Fragment onInputSelected_Fragment = getTargetFragment();
            if (onInputSelected_Fragment != null){
                listener = (DialogListener) onInputSelected_Fragment;
                Log.d("success cast: ", "dialog " + getTargetFragment());
            }
            else {
                Activity onInputSelected_Activity = getActivity();
                listener = (DialogListener) onInputSelected_Activity;
                Log.d("success cast: ", "activity");
            }
            return listener;
        }catch(ClassCastException e){
            Log.e("Custom Dialog", "onAttach: ClassCastException: " + e.getMessage());
        }
        return null;
    }

    public interface DialogListener {
        void onFinishUpdateDialog(int position, User user);
    }
}
