package com.example.mobilneaplikacije.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.model.Employee;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeeRegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeRegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

    private FirebaseFirestore db;

    private static final int PICK_IMAGE_REQUEST = 1;

    // Declare a variable to store the URI of the selected image
    private Uri imageUri;


    public EmployeeRegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmployeeRegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmployeeRegisterFragment newInstance(String param1, String param2) {
        EmployeeRegisterFragment fragment = new EmployeeRegisterFragment();
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            // Get the URI of the selected image
            imageUri = data.getData();

            // You may display the selected image or its name here if needed
            // For now, let's just show a toast message
            Toast.makeText(getActivity(), "Image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employee_register, container, false);

        // Inicijalizacija Firebase Firestore baze podataka
        db = FirebaseFirestore.getInstance();
        Employee employee = new Employee();
        Button addTimeButton = view.findViewById(R.id.button);
        addTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting references to spinner and time pickers
                Spinner daysSpinner = view.findViewById(R.id.daysSpinner);
                TimePicker startTimeView = view.findViewById(R.id.startTimeView);
                TimePicker endTimeView = view.findViewById(R.id.endTimeView);

                // Getting selected day from spinner
                String selectedDay = daysOfWeek[daysSpinner.getSelectedItemPosition()];

                // Getting selected start and end time from time pickers
                int startHour = startTimeView.getHour();
                int startMinute = startTimeView.getMinute();
                int endHour = endTimeView.getHour();
                int endMinute = endTimeView.getMinute();

                // Formatting start and end time
                String startTime = String.format("%02d:%02d", startHour, startMinute);
                String endTime = String.format("%02d:%02d", endHour, endMinute);

                // Constructing work hours string
                String workHours = startTime + " - " + endTime;

                // Getting reference to the employee object


                // Checking if workSchedule map is null
                if (employee.getWorkSchedule() == null) {
                    employee.setWorkSchedule(new HashMap<>());
                }

                // Adding day and work hours to workSchedule map
                employee.getWorkSchedule().put(selectedDay, workHours);

                // Optionally, you can display a message to indicate that the time has been added
                Toast.makeText(getActivity(), "Work hours added for " + selectedDay, Toast.LENGTH_SHORT).show();
            }
        });


        Button selectImageButton = view.findViewById(R.id.selectImageButton);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the image picker
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });


        // Pronalaženje dugmeta za registraciju i postavljanje klikača na njega
        Button registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dobijanje referenci za sva polja unosa
                EditText emailEditText = view.findViewById(R.id.emailAdd);
                EditText passwordEditText = view.findViewById(R.id.passwordAdd);
                EditText repeatPasswordEditText = view.findViewById(R.id.repeatPasswordAdd);
                EditText nameEditText = view.findViewById(R.id.nameAdd);
                EditText surnameEditText = view.findViewById(R.id.surnameAdd);
                EditText addressEditText = view.findViewById(R.id.adressAdd);
                EditText phoneEditText = view.findViewById(R.id.phoneAdd);

                // Dobijanje vrednosti unetih u polja
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String repeatPassword = repeatPasswordEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String surname = surnameEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String phone = phoneEditText.getText().toString();

                // Provera da li su sva polja popunjena
                if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty() || name.isEmpty() ||
                        surname.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                    // Ako neko od polja nije popunjeno, možete prikazati odgovarajuću poruku korisniku
                    Toast.makeText(getActivity(), "Molimo vas popunite sva polja", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Provera da li su lozinke iste
                if (!password.equals(repeatPassword)) {
                    // Ako lozinke nisu iste, prikaži poruku korisniku i ne nastavljaj sa registrovanjem
                    Toast.makeText(getActivity(), "Lozinke se ne podudaraju", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Sada možete kreirati novog zaposlenog objekta

                employee.setEmail(email);
                employee.setFirstName(name);
                employee.setLastName(surname);
                employee.setAddress(address);
                employee.setPhoneNumber(phone);

                if (imageUri != null) {
                    // Set the profile picture URL in the Employee object
                    employee.setProfilePicture(imageUri.toString());
                }


                // Možete postaviti i ostale vrednosti u zavisnosti od vaših potreba


                if (employee.getWorkSchedule() == null)
                {

                    employee.setWorkSchedule(new HashMap<>());
                    employee.getWorkSchedule().put("Monday", "09:00 - 17:00");


                    employee.getWorkSchedule().put("Tuesday", "09:00 - 17:00");
                    employee.getWorkSchedule().put("Wednesday", "09:00 - 17:00");
                    employee.getWorkSchedule().put("Thursday", "09:00 - 17:00");
                    employee.getWorkSchedule().put("Friday", "09:00 - 17:00");
                }


                // Sada upišite novog zaposlenog u bazu podataka
                db.collection("employees").add(employee)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // Ako je upis uspešan, možete obavestiti korisnika
                                Toast.makeText(getActivity(), "Uspešno registrovanje zaposlenog", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Ako dođe do greške prilikom upisa, prikažite odgovarajuću poruku
                                Toast.makeText(getActivity(), "Greška prilikom registrovanja zaposlenog", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return view;
    }

}