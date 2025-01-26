package com.example.mobilneaplikacije.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.EditText;
import com.example.mobilneaplikacije.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

public class RegisterEventOrganizerFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_IMAGE_PICK = 102;

    private ImageView imageViewProfilePicture;
    private Button buttonChoosePicture;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private View view;

    public static RegisterEventOrganizerFragment newInstance(String param1, String param2) {
        RegisterEventOrganizerFragment fragment = new RegisterEventOrganizerFragment();
        Bundle args = new Bundle();
        args.putString("ARG_PARAM1", param1);
        args.putString("ARG_PARAM2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_event_organizer, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        imageViewProfilePicture = view.findViewById(R.id.imageViewProfilePicture);
        buttonChoosePicture = view.findViewById(R.id.buttonChoosePicture);
        buttonChoosePicture.setOnClickListener(v -> openGallery());

        Button registerButton = view.findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(v -> registerEventOrganizer());

        return view;
    }

    private void registerEventOrganizer() {
        EditText emailEditText = view.findViewById(R.id.emailEditText);
        EditText passwordEditText = view.findViewById(R.id.passwordEditText);
        EditText repeatPasswordEditText = view.findViewById(R.id.repeatPasswordEditText);
        EditText nameEditText = view.findViewById(R.id.nameEditText);
        EditText surnameEditText = view.findViewById(R.id.surnameEditText);
        EditText addressEditText = view.findViewById(R.id.addressEditText);
        EditText phoneEditText = view.findViewById(R.id.phoneEditText);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String repeatPassword = repeatPasswordEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String surname = surnameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty() || name.isEmpty() ||
                surname.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(repeatPassword)) {
            Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new user account in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendActivationEmail();
                        } else {
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(getActivity(), "Failed to register: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendActivationEmail() {
        if (mAuth.getCurrentUser() != null) {
            mAuth.getCurrentUser().sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Verification email sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Failed to send verification email", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                    imageViewProfilePicture.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
