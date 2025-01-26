package com.example.mobilneaplikacije.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.model.Events;
import com.example.mobilneaplikacije.model.Rate;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

public class AddRateFragment extends Fragment {

    private static final String ARG_EVENT = "selected_event";
    private Events selectedEvent;
    private FirebaseFirestore db;

    public AddRateFragment() {
        // Required empty public constructor
    }

    public static AddRateFragment newInstance(Events event) {
        AddRateFragment fragment = new AddRateFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EVENT, (Serializable) event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        if (getArguments() != null) {
            selectedEvent = (Events) getArguments().getSerializable(ARG_EVENT);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_rate, container, false);
    }

    private void updateEventRatingStatus(Events event) {
        // Query events based on a unique field (e.g., eventName)
        db.collection("events")
                .whereEqualTo("name", event.getName())  // Replace with a field you want to use to find the event
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            // Update the first matching event document
                            db.collection("events")
                                    .document(document.getId())  // Get the document ID dynamically
                                    .update("ocenjen", true)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("AddRateFragment", "Event rating status updated successfully.");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.d("AddRateFragment", "Error updating event: ", e);
                                    });
                        }
                    } else {
                        Log.d("AddRateFragment", "No matching event found.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("AddRateFragment", "Error querying events: ", e);
                });
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        EditText editTextComment = view.findViewById(R.id.editTextComment);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        Button buttonSubmit = view.findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(v -> {
            String comment = editTextComment.getText().toString().trim();
            int rating = (int) ratingBar.getRating();

            Rate rate = new Rate();

            rate.setComment(comment);
            rate.setRating(rating);
            selectedEvent.setOcenjen(true);
            rate.setEvents(selectedEvent); // Set the selected event
            rate.setTimeAdded(Timestamp.now());




            db.collection("rates")
                    .add(rate)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getContext(), "Rate submitted successfully", Toast.LENGTH_SHORT).show();
                        editTextComment.setText("");
                        ratingBar.setRating(0);

                        updateEventRatingStatus(selectedEvent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to submit rate", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
