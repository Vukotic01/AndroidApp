package com.example.mobilneaplikacije.fragments;

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
import com.example.mobilneaplikacije.adapters.ReservationOwnerListAdapter;
import com.example.mobilneaplikacije.model.EventOrganizer;
import com.example.mobilneaplikacije.model.EventPackage;
import com.example.mobilneaplikacije.model.Product;
import com.example.mobilneaplikacije.model.Reservation;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationForOwner#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationForOwner extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PackageAdapter packageAdapter;
    public ReservationForOwner() {
        // Required empty public constructor
    }


    private static final String TAG = "ReservationForOwner";

    private ListView reservationListView;
    private ListView packageReservationListView;
    private ArrayList<Reservation> reservationList = new ArrayList<>();
    private ArrayList<Reservation> packageReservations = new ArrayList<>();
    private ReservationOwnerListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation_for_owner, container, false);

        // Initialize ListViews and Adapters
        reservationListView = view.findViewById(R.id.reservationOwnerListView);
        packageReservationListView = view.findViewById(R.id.reservationPackageOwnerListView);

        // Initialize Spinner
        Spinner filterSpinner = view.findViewById(R.id.filterr);

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

        // Check if ListViews are properly initialized
        if (reservationListView == null || packageReservationListView == null) {
            Log.e(TAG, "ListView is null, check your layout XML for correct IDs.");
        } else {
            adapter = new ReservationOwnerListAdapter(getContext(), reservationList);
            reservationListView.setAdapter(adapter);

            packageAdapter = new PackageAdapter(getContext(), packageReservations);
            packageReservationListView.setAdapter(packageAdapter);
        }

        // Fetch data from Firebase
        fetchReservations();

        return view;
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
        {   fetchReservations();
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


    private void fetchReservations() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("reservations")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reservationList.clear(); // Očisti postojeću listu
                        packageReservations.clear(); // Očisti listu sa paketima

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Reservation reservation = document.toObject(Reservation.class);
                            if (reservation.getPack() != null) {
                                packageReservations.add(reservation); // Dodaj rezervacije sa paketom
                            } else {
                                reservationList.add(reservation); // Dodaj rezervacije bez paketa
                            }
                        }

                        // Nakon što se podaci učitaju, ponovo filtriraj na osnovu selektovanog statusa
                        Spinner filterSpinner = getView().findViewById(R.id.filterr);
                        String selectedStatus = filterSpinner.getSelectedItem().toString();
                        filterReservationsByStatus(selectedStatus); // Ponovno primeni filtriranje nakon učitavanja podataka

                        Log.d(TAG, "Reservations loaded: " + reservationList.size());
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }




    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReservationForOwner.
     */
    // TODO: Rename and change types and number of parameters
    public static ReservationForOwner newInstance(String param1, String param2) {
        ReservationForOwner fragment = new ReservationForOwner();
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


}