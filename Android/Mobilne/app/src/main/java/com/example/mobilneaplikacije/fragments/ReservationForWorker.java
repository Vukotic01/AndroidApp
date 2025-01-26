package com.example.mobilneaplikacije.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.adapters.PackageAdapter;
import com.example.mobilneaplikacije.adapters.ReservationListAdapter;
import com.example.mobilneaplikacije.model.Reservation;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationForWorker#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationForWorker extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FirebaseFirestore db;
    private ListView reservationListView;
    private ListView packageReservationListView;
    private ArrayList<Reservation> reservationList = new ArrayList<>();
    private ArrayList<Reservation> packageReservations = new ArrayList<>();
    private ReservationListAdapter adapter;
    private PackageAdapter packageAdapter;

    public ReservationForWorker() {
        // Required empty public constructor
    }

    public static ReservationForWorker newInstance(String param1, String param2) {
        ReservationForWorker fragment = new ReservationForWorker();
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

    private void fetchReservations() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("reservations")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reservationList.clear();
                        packageReservations.clear(); // Clear the list before adding new data
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Reservation reservation = document.toObject(Reservation.class);
                            if (reservation.getPack() != null) {
                                packageReservations.add(reservation); // Add reservation with package
                            } else {
                                reservationList.add(reservation); // Add reservation without package
                            }
                        }
                        // After data is loaded, filter based on selected status
                        Spinner filterSpinner = getView().findViewById(R.id.filterrWorker);
                        if (filterSpinner != null) {
                            String selectedStatus = filterSpinner.getSelectedItem().toString();
                            filterReservationsByStatus(selectedStatus);
                        }
                        Log.d(TAG, "Reservations loaded: " + reservationList.size());
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void filterReservationsByStatus(String status) {
        ArrayList<Reservation> filteredReservations = new ArrayList<>();
        ArrayList<Reservation> filteredPackageReservations = new ArrayList<>();

        if (status.equals("ALL")) {
            fetchReservations(); // Reload all reservations if "ALL" is selected
            filteredReservations.addAll(reservationList);
            filteredPackageReservations.addAll(packageReservations);
        } else {
            // Filter reservations based on selected status
            fetchReservations();
            for (Reservation reservation : reservationList) {
                if (reservation.getStatus().name().equals(status)) {
                    filteredReservations.add(reservation);
                }
            }
            for (Reservation reservation : packageReservations) {
                if (reservation.getStatus().name().equals(status)) {
                    filteredPackageReservations.add(reservation);
                }
            }
        }

        // Update the adapters with the filtered lists
        adapter.updateList(filteredReservations);
        packageAdapter.updateList(filteredPackageReservations);

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged();
        packageAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reservation_for_worker, container, false);

        // Initialize ListViews and Adapters
        reservationListView = view.findViewById(R.id.reservationWorkerListView);
        packageReservationListView = view.findViewById(R.id.reservationPackageWorkerListView);

        // Initialize Spinner
        Spinner filterSpinner = view.findViewById(R.id.filterrWorker);

        // Create an ArrayAdapter for Spinner (you can add your own status names)
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.reservation_statuses, // This is the array of statuses
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(spinnerAdapter);

        // Spinner item selected listener
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedStatus = (String) parentView.getItemAtPosition(position);
                filterReservationsByStatus(selectedStatus);  // Filter reservations based on selected status
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No action needed
            }
        });

        // Check if the ListViews are properly initialized
        if (reservationListView == null || packageReservationListView == null) {
            Log.e(TAG, "ListView is null, check your layout XML for correct IDs.");
        } else {
            adapter = new ReservationListAdapter(getContext(), reservationList);
            reservationListView.setAdapter(adapter);

            packageAdapter = new PackageAdapter(getContext(), packageReservations);
            packageReservationListView.setAdapter(packageAdapter);
        }

        // Fetch data from Firebase
        fetchReservations();

        return view;
    }
}
