package com.example.mobilneaplikacije.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.adapters.EventListAdapter;
import com.example.mobilneaplikacije.model.Events;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RateEventsFragment extends Fragment {

    private FirebaseFirestore db;

    public RateEventsFragment() {
        // Required empty public constructor
    }

    public static RateEventsFragment newInstance() {
        return new RateEventsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate_events, container, false);
        db = FirebaseFirestore.getInstance();

        ListView listView = view.findViewById(R.id.eventListView);
        ArrayList<Events> events = new ArrayList<>();
        EventListAdapter adapter = new EventListAdapter(getContext(), events);

        adapter.setOnMoreButtonClickListener(event -> {
            // Navigate to AddRateFragment with the selected event
            AddRateFragment addRateFragment = AddRateFragment.newInstance(event);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, addRateFragment); // Replace the fragment container ID with your actual container ID
            transaction.addToBackStack(null); // Optional: add to backstack to allow user to go back
            transaction.commit();
        });

        db.collection("events")
                .get(Source.SERVER)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Log the entire document to check if isRated is present
                            Log.d(TAG, "Document data: " + document.getData());

                            // Check if the field is present in the document
                            if (document.contains("isRated")) {
                                boolean isRated = document.getBoolean("isRated");
                                Log.d(TAG, "isRated value from Firestore: " + isRated);
                            }

                            Events event = document.toObject(Events.class);

                            // Log the value of 'isRated' after mapping
                            Log.d(TAG, "isRated value in event object: " + event.getOcenjen());

                            if (!event.getOcenjen() && isEventWithinRatingPeriod(event.getTimeDone())) {
                                events.add(event);
                            }
                        }
                        listView.setAdapter(adapter);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });


        return view;
    }

    private boolean isEventWithinRatingPeriod(Timestamp timeDone) {
        if (timeDone == null) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        long eventTime = timeDone.toDate().getTime(); // Convert Firebase Timestamp to Java Date, then get the time in milliseconds
        long difference = currentTime - eventTime;

        // Check if the event is within 5 days (5 days * 24 hours * 60 minutes * 60 seconds * 1000 milliseconds)
        long fiveDaysInMillis = TimeUnit.DAYS.toMillis(5);

        return difference <= fiveDaysInMillis;
    }


}


