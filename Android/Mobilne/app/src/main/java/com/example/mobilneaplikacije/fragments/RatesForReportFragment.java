package com.example.mobilneaplikacije.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.adapters.RateListAdapter;
import com.example.mobilneaplikacije.model.Rate;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RatesForReportFragment extends Fragment {

    private FirebaseFirestore db;
    private ArrayList<Rate> rates;
    private RateListAdapter adapter;
    private Spinner filterChoose; // Spinner reference to access its selected item




    public RatesForReportFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rates_for_report, container, false);

        db = FirebaseFirestore.getInstance();
        rates = new ArrayList<>();
        adapter = new RateListAdapter(getContext(), rates);

        ListView listView = view.findViewById(R.id.rateRepListView);
        listView.setAdapter(adapter);

        filterChoose = view.findViewById(R.id.filterChoose);
        // Define sorting options
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sorting_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterChoose.setAdapter(spinnerAdapter);

        // Set spinner item selection listener
        filterChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    sortRatesLowToHigh();
                } else if (position == 1) {
                    sortRatesHighToLow();
                } else if(position==2)
                {
                    sortRatesOldestToNewest();
                }else if(position==3)
                {
                    sortRatesNewestToOldest();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Optional: Handle when nothing is selected
            }
        });

        loadRatesFromFirestore();

        return view;
    }

    private void sortRatesNewestToOldest() {
        Collections.sort(rates, new Comparator<Rate>() {
            @Override
            public int compare(Rate r1, Rate r2) {
                return r2.getTimeAdded().compareTo(r1.getTimeAdded());
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void sortRatesOldestToNewest() {
        Collections.sort(rates, new Comparator<Rate>() {
            @Override
            public int compare(Rate r1, Rate r2) {
                return r1.getTimeAdded().compareTo(r2.getTimeAdded());
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void loadRatesFromFirestore() {
        db.collection("rates")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        rates.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Rate rate = document.toObject(Rate.class);
                            rate.setId(document.getId()); // Set the Firestore document ID

                            if (!rate.getObrisano()) { // Check if isDeleted is false
                                rates.add(rate);
                            }
                        }
                        // After loading data, sort according to the spinner's current selection
                        updateSortOrderBasedOnSpinner();
                    } else {
                        // Handle error
                    }
                });
    }


    private void updateSortOrderBasedOnSpinner() {
        // Get the selected item position from the Spinner
        int selectedPosition = filterChoose.getSelectedItemPosition();

        if (selectedPosition == 0) {
            sortRatesLowToHigh();
        } else if (selectedPosition == 1) {
            sortRatesHighToLow();
        }
    }

    // Sort rates from low to high
    private void sortRatesLowToHigh() {
        Collections.sort(rates, new Comparator<Rate>() {
            @Override
            public int compare(Rate r1, Rate r2) {
                // Get the ratings, and if they are null, default them to 0
                Integer rating1 = r1.getRating();
                Integer rating2 = r2.getRating();

                // Log null ratings for debugging purposes (optional)
                if (rating1 == null) {
                    Log.d("RatesForReportFragment", "r1 rating is null: " + r1);
                }
                if (rating2 == null) {
                    Log.d("RatesForReportFragment", "r2 rating is null: " + r2);
                }

                // Handle null by assigning 0 if the rating is null
                rating1 = (rating1 != null) ? rating1 : 0;
                rating2 = (rating2 != null) ? rating2 : 0;

                // Compare the ratings (low to high)
                return Integer.compare(rating1, rating2);
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void sortRatesHighToLow() {
        Collections.sort(rates, new Comparator<Rate>() {
            @Override
            public int compare(Rate r1, Rate r2) {
                // Get the ratings, and if they are null, default them to 0
                Integer rating1 = r1.getRating();
                Integer rating2 = r2.getRating();

                // Log null ratings for debugging purposes (optional)
                if (rating1 == null) {
                    Log.d("RatesForReportFragment", "r1 rating is null: " + r1);
                }
                if (rating2 == null) {
                    Log.d("RatesForReportFragment", "r2 rating is null: " + r2);
                }

                // Handle null by assigning 0 if the rating is null
                rating1 = (rating1 != null) ? rating1 : 0;
                rating2 = (rating2 != null) ? rating2 : 0;

                // Compare the ratings (high to low)
                return Integer.compare(rating2, rating1);
            }
        });
        adapter.notifyDataSetChanged();
    }

}
