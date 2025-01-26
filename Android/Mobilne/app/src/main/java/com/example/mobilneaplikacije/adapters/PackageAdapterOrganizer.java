package com.example.mobilneaplikacije.adapters;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.model.EventPackage;  // Updated import
import com.example.mobilneaplikacije.model.Product;
import com.example.mobilneaplikacije.model.Reservation;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PackageAdapterOrganizer extends BaseAdapter {

    private Context context;
    private ArrayList<Reservation> reservations;
    private LayoutInflater inflater;

    public PackageAdapterOrganizer(Context context, ArrayList<Reservation> reservations) {
        this.context = context;
        this.reservations = reservations;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return reservations.size();
    }

    @Override
    public Object getItem(int position) {
        return reservations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.reserwation_for_organizer_card, parent, false);
        }

        Reservation reservation = reservations.get(position);
        EventPackage pkg = reservation.getPack(); // Get the EventPackage object from the Reservation

        // Display Reservation details
        TextView dateToCancleTextView = convertView.findViewById(R.id.textViewDTC);
        if (dateToCancleTextView != null) {
            dateToCancleTextView.setText(reservation.getDateToCancle());
        }

        TextView reservationStatusTextView = convertView.findViewById(R.id.textViewSt);
        if (reservationStatusTextView != null) {
            reservationStatusTextView.setText(reservation.getStatus().name());
        }

        // Display Package details if available
        if (pkg != null) {
            TextView packageNameTextView = convertView.findViewById(R.id.textViewPN);
            if (packageNameTextView != null) {
                packageNameTextView.setText(pkg.getName());
            }

            TextView packageDescriptionTextView = convertView.findViewById(R.id.textViewProductDescription);
            if (packageDescriptionTextView != null) {
                packageDescriptionTextView.setText(pkg.getDescription());
            }

            // Display the products in the package
            ListView productsListView = convertView.findViewById(R.id.productsL);

            ProductAdapter productAdapter = new ProductAdapter(
                    context,
                    new ArrayList<>(pkg.getProducts()),
                    null,
                    null
            );
            productsListView.setAdapter(productAdapter);
        }
//buttonCanc
        Button buttonCanc= convertView.findViewById(R.id.buttonCanc);
        buttonCanc.setOnClickListener(v -> {
            if (reservation != null) {
                // Poređenje trenutnog datuma sa dateToCancel

                if(reservation.getStatus() == Reservation.ResStatus.NEW || reservation.getStatus() == Reservation.ResStatus.APPROVED)
                {String currentDate = getCurrentDate(); // Dobavi trenutni datum u formatu "yyyy-MM-dd"
                    if (currentDate.compareTo(reservation.getDateToCancle()) < 0 ) {
                        // Postavi status na REJECTED_BY_ORGANIZER
                        reservation.setStatus(Reservation.ResStatus.REJECTED_BY_ORGANIZER);

                        // Ažuriraj rezervaciju u bazi podataka
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("reservations")
                                .document(reservation.getId())  // Pretpostavljam da imaš ID rezervacije
                                .update("status", Reservation.ResStatus.REJECTED_BY_ORGANIZER)
                                .addOnSuccessListener(aVoid -> {
                                    // Uspesno ažuriranje, možeš osvežiti prikaz
                                    notifyDataSetChanged();
                                    Log.d(TAG, "Reservation status updated successfully.");
                                })
                                .addOnFailureListener(e -> Log.e(TAG, "Error updating reservation: ", e));
                    } else {
                        Log.d(TAG, "Cannot cancel, date has passed.");
                    }
                }

            }
        });

        return convertView;
    }

    public void updateList(ArrayList<Reservation> updatedList) {
        reservations.clear(); // Clear current list
        reservations.addAll(updatedList); // Add updated list data
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }
}