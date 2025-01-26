package com.example.mobilneaplikacije.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.model.RegistrationRequest;
import com.example.mobilneaplikacije.model.enums.RequestStatus;
import com.example.mobilneaplikacije.model.Company;
import com.example.mobilneaplikacije.model.CompanyOwner;
import com.example.mobilneaplikacije.model.BaseUser;
import com.example.mobilneaplikacije.model.enums.UserRole;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationRequestAdapter extends RecyclerView.Adapter<RegistrationRequestAdapter.ViewHolder> {

    private List<RegistrationRequest> registrationRequests;
    private Context context;
    private FirebaseFirestore db;
    private Map<Integer, String> documentIdMap; // Map to store document IDs with position as key

    public RegistrationRequestAdapter(List<RegistrationRequest> registrationRequests, Context context) {
        this.registrationRequests = registrationRequests;
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.documentIdMap = new HashMap<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_registration_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RegistrationRequest request = registrationRequests.get(position);
        holder.companyNameTextView.setText(request.getCompanyName());
        holder.ownerEmailTextView.setText(request.getEmail());
        holder.companyEmailTextView.setText(request.getCompanyEmail());
        RequestStatus status = request.getStatus();
        holder.statusTextView.setText(status != null ? status.name() : "N/A");

        holder.firstNameTextView.setText("First Name: " + request.getFirstName());
        holder.lastNameTextView.setText("Last Name: " + request.getLastName());
        holder.addressTextView.setText("Address: " + request.getAddress());
        holder.phoneTextView.setText("Phone: " + request.getPhone());
        holder.companyAddressTextView.setText("Company Address: " + request.getCompanyAddress());
        holder.companyPhoneTextView.setText("Company Phone: " + request.getCompanyPhone());
        holder.companyDescriptionTextView.setText("Company Description: " + request.getCompanyDescription());
        holder.workingHoursTextView.setText("Working Hours: " + request.getWorkingHours().toString());

        holder.detailsButton.setOnClickListener(v -> {
            if (holder.detailsLayout.getVisibility() == View.GONE) {
                holder.detailsLayout.setVisibility(View.VISIBLE);
                holder.detailsButton.setText("Hide Details");
            } else {
                holder.detailsLayout.setVisibility(View.GONE);
                holder.detailsButton.setText("Details");
            }
        });

        holder.approveButton.setOnClickListener(v -> approveRequest(request, position));

        holder.rejectButton.setOnClickListener(v -> showRejectDialog(request, position));
    }

    @Override
    public int getItemCount() {
        return registrationRequests.size();
    }

    private void approveRequest(RegistrationRequest request, int position) {
        String documentId = documentIdMap.get(position);
        if (documentId != null) {
            Log.d("Firestore", "Approving request with document ID: " + documentId);

            // Create a new company
            Company company = new Company(
                    request.getCompanyEmail(),
                    request.getCompanyName(),
                    request.getCompanyAddress(),
                    request.getCompanyPhone(),
                    request.getCompanyDescription(),
                    documentId, // Owner will be set later
                    new ArrayList<>(), // Empty list of workers initially
                    request.getWorkingHours(), // Use working hours from the request
                    new ArrayList<>(), // Empty list of categories initially
                    new ArrayList<>() // Empty list of event types initially
            );

            // Create a new company owner
            CompanyOwner companyOwner = new CompanyOwner(
                    request.getEmail(),
                    request.getPassword(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getAddress(),
                    request.getPhone(),
                    UserRole.COMPANY_OWNER,
                    company // Set the company
            );

            // Create a new base user
            BaseUser baseUser = new BaseUser(
                    companyOwner.getEmail(),
                    companyOwner.getPassword(),
                    companyOwner.getFirstName(),
                    companyOwner.getLastName(),
                    companyOwner.getAddress(),
                    companyOwner.getPhone(),
                    UserRole.COMPANY_OWNER
            );

            // Save the company
            db.collection("companies")
                    .document(documentId)
                    .set(company)
                    .addOnSuccessListener(aVoid -> {
                        // Save the company owner
                        db.collection("companyOwners")
                                .document(documentId)
                                .set(companyOwner)
                                .addOnSuccessListener(aVoid1 -> {
                                    // Save the base user
                                    db.collection("users")
                                            .document(documentId)
                                            .set(baseUser)
                                            .addOnSuccessListener(aVoid2 -> {
                                                // Update the registration request status
                                                db.collection("registrationRequests")
                                                        .document(documentId)
                                                        .update("status", RequestStatus.APPROVED.name())
                                                        .addOnSuccessListener(aVoid3 -> {
                                                            request.setStatus(RequestStatus.APPROVED);
                                                            notifyDataSetChanged();
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Log.e("Firestore", "Failed to update request status", e);
                                                            Toast.makeText(context, "Failed to update request status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        });
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.e("Firestore", "Failed to save base user", e);
                                                Toast.makeText(context, "Failed to save base user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "Failed to save company owner", e);
                                    Toast.makeText(context, "Failed to save company owner: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Failed to save company", e);
                        Toast.makeText(context, "Failed to save company: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.e("Firestore", "No matching document found for approval");
            Toast.makeText(context, "No matching document found for approval", Toast.LENGTH_SHORT).show();
        }
    }

    private void showRejectDialog(RegistrationRequest request, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Reject Request");

        final EditText input = new EditText(context);
        input.setHint("Reason for rejection");

        builder.setView(input);

        builder.setPositiveButton("Reject", (dialog, which) -> {
            String reason = input.getText().toString().trim();
            if (!reason.isEmpty()) {
                rejectRequest(request, reason, position);
            } else {
                Toast.makeText(context, "Please provide a reason for rejection", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void rejectRequest(RegistrationRequest request, String reason, int position) {
        String documentId = documentIdMap.get(position);
        if (documentId != null) {
            Log.d("Firestore", "Rejecting request with document ID: " + documentId);

            // Update the status
            db.collection("registrationRequests")
                    .document(documentId)
                    .update("status", RequestStatus.REJECTED.name())
                    .addOnSuccessListener(aVoid -> {
                        request.setStatus(RequestStatus.REJECTED);
                        notifyDataSetChanged();

                        // Send rejection email to company owner
                        sendRejectionEmail(request.getEmail(), reason);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Failed to update request status", e);
                        Toast.makeText(context, "Failed to update request status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.e("Firestore", "No matching document found");
            Toast.makeText(context, "No matching document found", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendRejectionEmail(String email, String reason) {
        // Implementation for sending rejection email
    }

    public void setDocumentIdMap(Map<Integer, String> documentIdMap) {
        this.documentIdMap = documentIdMap;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView companyNameTextView;
        public TextView ownerEmailTextView;
        public TextView companyEmailTextView;
        public TextView statusTextView;
        public TextView firstNameTextView;
        public TextView lastNameTextView;
        public TextView addressTextView;
        public TextView phoneTextView;
        public TextView companyAddressTextView;
        public TextView companyPhoneTextView;
        public TextView companyDescriptionTextView;
        public TextView workingHoursTextView;
        public LinearLayout detailsLayout;
        public Button detailsButton;
        public Button approveButton;
        public Button rejectButton;

        public ViewHolder(View itemView) {
            super(itemView);
            companyNameTextView = itemView.findViewById(R.id.companyNameTextView);
            ownerEmailTextView = itemView.findViewById(R.id.ownerEmailTextView);
            companyEmailTextView = itemView.findViewById(R.id.companyEmailTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            firstNameTextView = itemView.findViewById(R.id.firstNameTextView);
            lastNameTextView = itemView.findViewById(R.id.lastNameTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            companyAddressTextView = itemView.findViewById(R.id.companyAddressTextView);
            companyPhoneTextView = itemView.findViewById(R.id.companyPhoneTextView);
            companyDescriptionTextView = itemView.findViewById(R.id.companyDescriptionTextView);
            workingHoursTextView = itemView.findViewById(R.id.workingHoursTextView);
            detailsLayout = itemView.findViewById(R.id.detailsLayout);
            detailsButton = itemView.findViewById(R.id.detailsButton);
            approveButton = itemView.findViewById(R.id.approveButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }
    }
}
