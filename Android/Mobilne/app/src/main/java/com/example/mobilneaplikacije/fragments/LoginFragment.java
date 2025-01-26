package com.example.mobilneaplikacije.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.activities.AdminActivity;
import com.example.mobilneaplikacije.activities.EventOrganizerActivity;
import com.example.mobilneaplikacije.activities.OwnerMainActivity;
import com.example.mobilneaplikacije.activities.LoginActivity;
import com.example.mobilneaplikacije.activities.WorkerActivity;
import com.example.mobilneaplikacije.model.BaseUser;
import com.example.mobilneaplikacije.model.enums.UserRole;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LoginFragment extends Fragment {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonGoToRegisterOrganizer;
    private Button buttonGotoOwnerMainAcitivty;
    private Button GoToRegisterOwner;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonLogin = view.findViewById(R.id.buttonLogin);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                loginUser(email, password);
            }
        });


        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser != null) {
                            firebaseUser.getIdToken(true)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            String token = task1.getResult().getToken();
                                            saveToken(token);
                                            db.collection("users")
                                                    .whereEqualTo("email", email)
                                                    .get()
                                                    .addOnCompleteListener(task2 -> {
                                                        if (task2.isSuccessful() && !task2.getResult().isEmpty()) {
                                                            for (QueryDocumentSnapshot document : task2.getResult()) {
                                                                BaseUser user = document.toObject(BaseUser.class);
                                                                Intent intent;
                                                                if (user.getRole() == UserRole.ADMIN) {
                                                                    intent = new Intent(getActivity(), AdminActivity.class);
                                                                } else if (user.getRole() == UserRole.COMPANY_OWNER) {
                                                                    intent = new Intent(getActivity(), OwnerMainActivity.class);
                                                                } else if (user.getRole() == UserRole.EVENT_ORGANIZER) {
                                                                    intent = new Intent(getActivity(), EventOrganizerActivity.class);
                                                                } else {
                                                                    Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                                                                    intent = new Intent(getActivity(), WorkerActivity.class);

                                                                    //return; // Or handle other roles
                                                                }
                                                                startActivity(intent);
                                                                getActivity().finish(); // Finish the login activity
                                                            }
                                                        } else {
                                                            Toast.makeText(getActivity(), "User role not found", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(getActivity(), "Failed to retrieve token", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(getActivity(), "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("auth_token", token);
        editor.apply();
    }

    private void createAdminUser() {
        // Create an admin user
        BaseUser adminUser = new BaseUser("admin@gmail.com", "admin", "Admin", "User", "Admin Address", "123456789", UserRole.ADMIN);

        // Save the admin user to Firestore
        db.collection("users").add(adminUser)
                .addOnSuccessListener(documentReference -> {
                    String docId = documentReference.getId(); // Retrieve the document ID
                    Toast.makeText(getActivity(), "Admin User Created with ID: " + docId, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Error creating admin user", Toast.LENGTH_SHORT).show());
    }
}
