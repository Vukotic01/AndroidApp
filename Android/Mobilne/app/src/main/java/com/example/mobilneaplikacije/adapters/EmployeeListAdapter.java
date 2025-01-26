package com.example.mobilneaplikacije.adapters;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.mobilneaplikacije.model.Employee;
import com.example.mobilneaplikacije.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class EmployeeListAdapter extends ArrayAdapter<Employee> {
    private ArrayList<Employee> aEmployees;
    private FirebaseFirestore db;
    private Context context;
    public ArrayList<Employee> filteredEmployees;

    private OnMoreButtonClickListener moreButtonClickListener;

    public EmployeeListAdapter(Context context, ArrayList<Employee> employees) {
        super(context, R.layout.employee_car, employees);
        aEmployees = employees;
        filteredEmployees = new ArrayList<>(employees); // Kopiraj početne podatke


        this.context = context;
    }

    public interface OnMoreButtonClickListener {
        void onMoreButtonClick(Employee employee);
    }

    public void setOnMoreButtonClickListener(OnMoreButtonClickListener listener) {
        this.moreButtonClickListener = listener;
    }

    @Override
    public int getCount() {
        return filteredEmployees.size();
    }









    @Nullable
    @Override
    public Employee getItem(int position) {
        return filteredEmployees.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Permission check and image loading




    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Employee employee = getItem(position);
        db = FirebaseFirestore.getInstance();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.employee_car, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.employeeImage);
        TextView employeeName = convertView.findViewById(R.id.employeeName);
        TextView employeeSurname = convertView.findViewById(R.id.employeeSurname);
        TextView employeeAddress = convertView.findViewById(R.id.employeeAdress);
        TextView employeePhone = convertView.findViewById(R.id.employeePhone);
        TextView employeeEmail = convertView.findViewById(R.id.employeeEmail);
        Button activateButton = convertView.findViewById(R.id.activateButton);
        Button deactivateButton = convertView.findViewById(R.id.deactivateButton);
        Button moreButton = convertView.findViewById(R.id.moreButton);

        if (employee != null) {
            Picasso.get().load(employee.getProfilePicture()).placeholder(R.drawable.ic_launcher_foreground).into(imageView);



        employeeName.setText(employee.getFirstName());
            employeeSurname.setText(employee.getLastName());
            employeeAddress.setText(employee.getAddress());
            employeePhone.setText(employee.getPhoneNumber());
            employeeEmail.setText(employee.getEmail());

            activateButton.setOnClickListener(v -> {
                employee.setActive(true);
                Toast.makeText(getContext(), "Activate clicked for " + employee.getFirstName() +
                        " " + employee.getLastName(), Toast.LENGTH_SHORT).show();

                updateEmployeeInFirestore(employee.getDocumentId(), "active", true);
            });

            deactivateButton.setOnClickListener(v -> {
                employee.setActive(false);
                Toast.makeText(getContext(), "Deactivate clicked for " + employee.getFirstName() +
                        " " + employee.getLastName(), Toast.LENGTH_SHORT).show();

                updateEmployeeInFirestore(employee.getDocumentId(), "active", false);
            });

            moreButton.setOnClickListener(v -> {
                if (moreButtonClickListener != null) {
                    moreButtonClickListener.onMoreButtonClick(employee);
                }
            });
        }

        return convertView;
    }

    public void updateEmployeeInFirestore(String documentId, String fieldName, Object value) {
        if (documentId == null || documentId.isEmpty()) {
            Log.e("FirestoreError", "Document ID is null or empty.");
            return;
        }

        FirebaseFirestore.getInstance()
                .collection("employees")
                .document(documentId)
                .update(fieldName, value)
                .addOnSuccessListener(aVoid -> Log.d("FirestoreUpdate", "Field " + fieldName + " updated successfully."))
                .addOnFailureListener(e -> Log.e("FirestoreUpdate", "Error updating field: " + fieldName, e));
    }

    public void filter(String text) {
        filteredEmployees.clear();
        if (text.isEmpty()) {
            // Ako je pretraga prazna, prikaži sve zaposlene
            filteredEmployees.addAll(aEmployees);
        } else {
            String lowerCaseText = text.toLowerCase();
            for (Employee employee : aEmployees) {
                // Proverite da li ime, prezime ili email sadrže uneseni tekst
                if (employee.getFirstName().toLowerCase().contains(lowerCaseText) ||
                        employee.getLastName().toLowerCase().contains(lowerCaseText) ||
                        employee.getEmail().toLowerCase().contains(lowerCaseText)) {
                    filteredEmployees.add(employee);
                }
            }
        }
        notifyDataSetChanged();
    }




}
