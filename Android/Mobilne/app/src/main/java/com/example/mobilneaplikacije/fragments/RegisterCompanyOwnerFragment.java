package com.example.mobilneaplikacije.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.model.RegistrationRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterCompanyOwnerFragment extends Fragment {

    private ImageView imageViewProfilePicture;
    private EditText emailEditText, passwordEditText, repeatPasswordEditText, nameEditText, surnameEditText, addressEditText, phoneEditText;
    private EditText companyNameEditText, companyEmailEditText, companyAddressEditText, companyPhoneEditText, companyDescriptionEditText;
    private LinearLayout categoriesLayout, eventTypesLayout, workingHoursLayout;
    private Button buttonChoosePicture, buttonSubmit, buttonAddSample;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public static RegisterCompanyOwnerFragment newInstance() {
        return new RegisterCompanyOwnerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_company_owner, container, false);
        initializeViews(view);
        setupListeners();
        return view;
    }

    private void initializeViews(View view) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Personal Info
        imageViewProfilePicture = view.findViewById(R.id.imageViewProfilePicture);
        buttonChoosePicture = view.findViewById(R.id.buttonChoosePicture);
        emailEditText = view.findViewById(R.id.editTextEmail);
        passwordEditText = view.findViewById(R.id.editTextPassword);
        repeatPasswordEditText = view.findViewById(R.id.editTextRepeatPassword);
        nameEditText = view.findViewById(R.id.editTextName);
        surnameEditText = view.findViewById(R.id.editTextSurname);
        addressEditText = view.findViewById(R.id.editTextAddress);
        phoneEditText = view.findViewById(R.id.editTextPhoneNumber);

        // Company Info
        companyNameEditText = view.findViewById(R.id.editTextCompanyName);
        companyEmailEditText = view.findViewById(R.id.editTextCompanyEmail);
        companyAddressEditText = view.findViewById(R.id.editTextCompanyAddress);
        companyPhoneEditText = view.findViewById(R.id.editTextCompanyPhone);
        companyDescriptionEditText = view.findViewById(R.id.editTextCompanyDescription);

        // Categories, Event Types, and Working Hours
        categoriesLayout = view.findViewById(R.id.categoriesLayout);
        eventTypesLayout = view.findViewById(R.id.eventTypesLayout);
        workingHoursLayout = view.findViewById(R.id.workingHoursLayout);

        // Add category fields
        addCategoryFields();

        // Add event type checkboxes
        addEventTypeCheckboxes();

        // Add working hours fields
        addWorkingHoursFields();

        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        buttonAddSample = view.findViewById(R.id.buttonAddSample); // Reference to the new button
    }

    private void addCategoryFields() {
        // Add initial category field
        addCategoryField();

        // Add button to add more categories
        Button addCategoryButton = new Button(getContext());
        addCategoryButton.setText("Add Category");
        addCategoryButton.setOnClickListener(v -> addCategoryField());
        categoriesLayout.addView(addCategoryButton);
    }

    private void addCategoryField() {
        LinearLayout categoryLayout = new LinearLayout(getContext());
        categoryLayout.setOrientation(LinearLayout.VERTICAL);

        EditText categoryNameEditText = new EditText(getContext());
        categoryNameEditText.setHint("Category Name");
        categoryLayout.addView(categoryNameEditText);

        EditText categoryDescriptionEditText = new EditText(getContext());
        categoryDescriptionEditText.setHint("Category Description");
        categoryLayout.addView(categoryDescriptionEditText);

        categoriesLayout.addView(categoryLayout, categoriesLayout.getChildCount() - 1);
    }

    private void addEventTypeCheckboxes() {
        String[] eventTypes = {"Private Events", "Corporate Events", "Cultural and Entertainment Events", "Sports Events",
                "Educational Events", "Charity and Fundraising Events", "Government and Political Events"};

        for (String eventType : eventTypes) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(eventType);
            eventTypesLayout.addView(checkBox);
        }
    }

    private void addWorkingHoursFields() {
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        for (String day : daysOfWeek) {
            LinearLayout dayLayout = new LinearLayout(getContext());
            dayLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView dayTextView = new TextView(getContext());
            dayTextView.setText(day);
            dayLayout.addView(dayTextView);

            EditText workingHoursEditText = new EditText(getContext());
            workingHoursEditText.setHint("e.g. 8-13");
            dayLayout.addView(workingHoursEditText);

            workingHoursLayout.addView(dayLayout);
        }
    }

    private void setupListeners() {
        buttonChoosePicture.setOnClickListener(v -> openGallery());
        buttonSubmit.setOnClickListener(v -> submitRegistration());
        buttonAddSample.setOnClickListener(v -> addSampleRequests()); // Set listener for the new button
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 102); // Using 102 for gallery pick
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 102 && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                imageViewProfilePicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void submitRegistration() {
        // Collect all data
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String repeatPassword = repeatPasswordEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String surname = surnameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String companyName = companyNameEditText.getText().toString();
        String companyEmail = companyEmailEditText.getText().toString();
        String companyAddress = companyAddressEditText.getText().toString();
        String companyPhone = companyPhoneEditText.getText().toString();
        String companyDescription = companyDescriptionEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty() || name.isEmpty() ||
                surname.isEmpty() || address.isEmpty() || phone.isEmpty() || companyName.isEmpty() ||
                companyEmail.isEmpty() || companyAddress.isEmpty() || companyPhone.isEmpty() || companyDescription.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(repeatPassword)) {
            Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Collect working hours data
        Map<String, String> workingHours = new HashMap<>();
        for (int i = 0; i < workingHoursLayout.getChildCount(); i++) {
            LinearLayout dayLayout = (LinearLayout) workingHoursLayout.getChildAt(i);
            TextView dayTextView = (TextView) dayLayout.getChildAt(0);
            EditText hoursEditText = (EditText) dayLayout.getChildAt(1);
            workingHours.put(dayTextView.getText().toString(), hoursEditText.getText().toString());
        }

        // Create RegistrationRequest object
        RegistrationRequest registrationRequest = new RegistrationRequest(
                email, password, name, surname, address, phone,
                companyName, companyEmail, companyAddress, companyPhone, companyDescription, workingHours
        );

        // Save registration request to Firestore
        db.collection("registrationRequests")
                .add(registrationRequest)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Registration request submitted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(getActivity(), "Failed to submit registration request: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addSampleRequests() {
        // Sample data for three registration requests
        Map<String, String> workingHours = new HashMap<>();
        workingHours.put("Monday", "8-16");
        workingHours.put("Tuesday", "8-16");
        workingHours.put("Wednesday", "8-16");
        workingHours.put("Thursday", "8-16");
        workingHours.put("Friday", "8-16");

        RegistrationRequest request1 = new RegistrationRequest(
                "owner1@example.com", "password1", "John", "Doe", "123 Main St", "1234567890",
                "Company One", "info@companyone.com", "123 Business Rd", "0987654321", "Company One Description", workingHours
        );

        RegistrationRequest request2 = new RegistrationRequest(
                "owner2@example.com", "password2", "Jane", "Smith", "456 Main St", "1234567890",
                "Company Two", "info@companytwo.com", "456 Business Rd", "0987654321", "Company Two Description", workingHours
        );

        RegistrationRequest request3 = new RegistrationRequest(
                "owner3@example.com", "password3", "Alice", "Johnson", "789 Main St", "1234567890",
                "Company Three", "info@companythree.com", "789 Business Rd", "0987654321", "Company Three Description", workingHours
        );

        // Add each request to Firestore
        db.collection("registrationRequests").add(request1);
        db.collection("registrationRequests").add(request2);
        db.collection("registrationRequests").add(request3);

        //message
    }
}
