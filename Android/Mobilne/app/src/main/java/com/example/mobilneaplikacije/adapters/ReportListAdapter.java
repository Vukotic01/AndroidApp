package com.example.mobilneaplikacije.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.model.Report;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ReportListAdapter extends ArrayAdapter<Report> {

    public ReportListAdapter(Context context, ArrayList<Report> reports) {
        super(context, R.layout.report_cart, reports);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Report report = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.report_cart, parent, false);
        }

        TextView commentTextView = convertView.findViewById(R.id.textViewReportedRateComment);
        TextView ratingTextView = convertView.findViewById(R.id.textViewReportedRateRatingValue);
        TextView reasonTextView = convertView.findViewById(R.id.textViewReportReason);

        if (report != null) {
            if (report.getRate() != null) {
                commentTextView.setText(report.getRate().getComment());
                ratingTextView.setText("Rating: " + report.getRate().getRating());
            }
            reasonTextView.setText(report.getReason());
        }

        Button acceptButton = convertView.findViewById(R.id.buttonAccept);
        Button rejectButton = convertView.findViewById(R.id.buttonReject);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Handle Accept button click
        acceptButton.setOnClickListener(v -> {
            report.setStatus(Report.RepStatus.APPROVED); // Change the status to APPROVED

            // Update the "reports" collection
            db.collection("reports")
                    .document(report.getId())
                    .update("status", Report.RepStatus.APPROVED.name())
                    .addOnSuccessListener(aVoid -> {
                        // Now, update the corresponding rate to set obrisano to true
                        if (report.getRate() != null) {
                            String rateId = report.getRate().getId(); // Get the rate ID associated with the report
                            db.collection("rates")
                                    .document(rateId)  // Update the specific rate document
                                    .update("obrisano", true)  // Set 'obrisano' field to true
                                    .addOnSuccessListener(aVoid1 -> {
                                        Toast.makeText(getContext(), "Report Accepted and Rate Marked as Deleted", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getContext(), "Failed to delete the rate: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to update report: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        // Handle Reject button click
        rejectButton.setOnClickListener(v -> {
            report.setStatus(Report.RepStatus.CANCELED); // Change the status to CANCELED

            // Update the "reports" collection
            db.collection("reports")
                    .document(report.getId()) // Ensure report has a unique ID
                    .update("status", Report.RepStatus.CANCELED.name()) // Update the status field
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Report Rejected", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to update report: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        return convertView;
    }
}
