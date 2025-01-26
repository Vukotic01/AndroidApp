package com.example.mobilneaplikacije.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.adapters.RegistrationRequestAdapter;
import com.example.mobilneaplikacije.model.RegistrationRequest;
import com.example.mobilneaplikacije.model.enums.RequestStatus;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class RegistrationRequestsFragment extends Fragment {

    private static final String TAG = "RegistrationRequests";
    private RecyclerView recyclerView;
    private RegistrationRequestAdapter adapter;
    private List<RegistrationRequest> registrationRequests;
    private FirebaseFirestore db;

    public RegistrationRequestsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_requests, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        registrationRequests = new ArrayList<>();
        adapter = new RegistrationRequestAdapter(registrationRequests,  getContext());
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        fetchRegistrationRequests();

        return view;
    }

    private void fetchRegistrationRequests() {
        db.collection("registrationRequests")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        registrationRequests.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            RegistrationRequest request = document.toObject(RegistrationRequest.class);
                            // Ensure status is not null
                            if (request.getStatus() == null) {
                                request.setStatus(RequestStatus.PENDING);
                            }
                            registrationRequests.add(request);
                        }
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "Successfully fetched registration requests");
                    } else {
                        Log.e(TAG, "Error fetching registration requests", task.getException());
                    }
                });
    }
}
