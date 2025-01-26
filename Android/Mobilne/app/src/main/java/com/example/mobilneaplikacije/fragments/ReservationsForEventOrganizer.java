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
import com.example.mobilneaplikacije.adapters.ReservationListAdapter;
import com.example.mobilneaplikacije.adapters.*;

import com.example.mobilneaplikacije.model.EventPackage;
import com.example.mobilneaplikacije.model.Product;
import com.example.mobilneaplikacije.model.Reservation;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationsForEventOrganizer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationsForEventOrganizer extends Fragment {

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
    private PackageAdapterOrganizer packageAdapter;
    public ReservationsForEventOrganizer() {
        // Required empty public constructor
    }

    public static ReservationsForEventOrganizer newInstance(String param1, String param2) {
        ReservationsForEventOrganizer fragment = new ReservationsForEventOrganizer();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservations_for_event_organizer, container, false);

        // Initialize ListViews and Adapters
        reservationListView = view.findViewById(R.id.reservationOrganizerListView);
        packageReservationListView = view.findViewById(R.id.reservationPackageOrganizerListView);

        // Initialize Spinner
        Spinner filterSpinner = view.findViewById(R.id.filterrOrganizer);

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

            packageAdapter = new PackageAdapterOrganizer(getContext(), packageReservations);
            packageReservationListView.setAdapter(packageAdapter);
        }

        // Fetch data from Firebase
        fetchReservations();

        return view;
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

                        // Now that data is fetched, filter based on spinner selection
                        Spinner filterSpinner = getView().findViewById(R.id.filterrOrganizer);
                        if (filterSpinner != null) {
                            String selectedStatus = filterSpinner.getSelectedItem().toString();
                            filterReservationsByStatus(selectedStatus);
                        } else {
                            Log.e(TAG, "Filter Spinner is null");
                        }

                        Log.d(TAG, "Reservations loaded: " + reservationList.size());
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }


    private void filterReservationsByStatus(String status) {
        // Initialize filtered lists
        if (status.equals("ALL"))
        {
            fetchReservations();

            ArrayList<Reservation> filteredReservations = new ArrayList<>();
            ArrayList<Reservation> filteredPackageReservations = new ArrayList<>();

            for (Reservation reservation : reservationList) {

                filteredReservations.add(reservation);

            }

            // Loop through packageReservations and filter by status
            for (Reservation reservation : packageReservations) {

                filteredPackageReservations.add(reservation);

            }

            adapter.updateList(filteredReservations);  // Update the list view with filtered normal reservations
            packageAdapter.updateList(filteredPackageReservations);  // Update the list view with filtered package reservations

            // Notify the adapter that the data has changed
            adapter.notifyDataSetChanged();
            packageAdapter.notifyDataSetChanged();

        }
        else
        {   
            ArrayList<Reservation> filteredReservations = new ArrayList<>();
            ArrayList<Reservation> filteredPackageReservations = new ArrayList<>();

            // Loop through reservationList and filter by status
            for (Reservation reservation : reservationList) {
                if (reservation.getStatus().name().equals(status)) {
                    filteredReservations.add(reservation);
                }
            }

            // Loop through packageReservations and filter by status
            for (Reservation reservation : packageReservations) {
                if (reservation.getStatus().name().equals(status)) {
                    filteredPackageReservations.add(reservation);
                }
            }

            // Update the adapters with the filtered lists
            adapter.updateList(filteredReservations);  // Update the list view with filtered normal reservations
            packageAdapter.updateList(filteredPackageReservations);  // Update the list view with filtered package reservations

            // Notify the adapter that the data has changed
            adapter.notifyDataSetChanged();
            packageAdapter.notifyDataSetChanged();}

    }





}
