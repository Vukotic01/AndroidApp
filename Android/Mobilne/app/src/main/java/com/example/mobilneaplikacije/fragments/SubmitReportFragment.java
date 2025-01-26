package com.example.mobilneaplikacije.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.model.Rate;
import com.example.mobilneaplikacije.model.Report;
import com.google.firebase.firestore.FirebaseFirestore;

public class SubmitReportFragment extends Fragment {

    private static final String ARG_RATE = "rate";
    private Rate rate;

    private FirebaseFirestore db;

    public SubmitReportFragment() {
        // Required empty public constructor
    }

    public static SubmitReportFragment newInstance(Rate rate) {
        SubmitReportFragment fragment = new SubmitReportFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RATE, rate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rate = (Rate) getArguments().getSerializable(ARG_RATE);
        }
        db = FirebaseFirestore.getInstance(); // Initialize Firestore
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submit_report, container, false);

        EditText reasonEditText = view.findViewById(R.id.editReportReason);
        Button submitButton = view.findViewById(R.id.buttonSubmitReport);

        submitButton.setOnClickListener(v -> {
            String reason = reasonEditText.getText().toString().trim();
            if (!reason.isEmpty() && rate != null) {
                // Create the Report object
                Report report = new Report(reason, rate, Report.RepStatus.HOLD);

                // Save the Report object to the "reports" collection in Firestore
                db.collection("reports").add(report)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(getContext(), "Report submitted successfully!", Toast.LENGTH_SHORT).show();
                            // Optional: Navigate back or to another fragment
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Failed to submit report: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(getContext(), "Please enter a reason for the report.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
