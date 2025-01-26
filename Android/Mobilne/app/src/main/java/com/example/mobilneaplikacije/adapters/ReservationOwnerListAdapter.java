package com.example.mobilneaplikacije.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.model.Reservation;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReservationOwnerListAdapter extends ArrayAdapter<Reservation> {

    public ReservationOwnerListAdapter(@NonNull Context context, ArrayList<Reservation> reservations) {
        super(context, 0, reservations);
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Reservation reservation = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reservation_for_owner_card, parent, false);
        }

        TextView productName = convertView.findViewById(R.id.textViewProdName);
        TextView productStatus = convertView.findViewById(R.id.textViewProdStatus);
        TextView dateToCancel = convertView.findViewById(R.id.textViewDateToCancle);
        Button acceptButton = convertView.findViewById(R.id.buttonAcc); // Correct ID reference
        Button rejectButton = convertView.findViewById(R.id.buttonrRej); // Correct ID reference

        productName.setText(reservation.getProduct().getName());
        productStatus.setText(reservation.getStatus().name());
        dateToCancel.setText(reservation.getDateToCancle());



        // Handle button clicks if necessary
        acceptButton.setOnClickListener(v -> {
            if (reservation != null) {
                // Poređenje trenutnog datuma sa dateToCancel

                if(reservation.getStatus() == Reservation.ResStatus.NEW)
                {String currentDate = getCurrentDate(); // Dobavi trenutni datum u formatu "yyyy-MM-dd"
                    if (currentDate.compareTo(reservation.getDateToCancle()) < 0 ) {
                        // Postavi status na REJECTED_BY_ORGANIZER
                        reservation.setStatus(Reservation.ResStatus.REJECTED_BY_ORGANIZER);

                        // Ažuriraj rezervaciju u bazi podataka
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("reservations")
                                .document(reservation.getId())  // Pretpostavljam da imaš ID rezervacije
                                .update("status", Reservation.ResStatus.APPROVED)
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

        rejectButton.setOnClickListener(v -> {
            if (reservation != null) {
                // Poređenje trenutnog datuma sa dateToCancel

                if(reservation.getStatus() == Reservation.ResStatus.NEW)
                {String currentDate = getCurrentDate(); // Dobavi trenutni datum u formatu "yyyy-MM-dd"
                    if (currentDate.compareTo(reservation.getDateToCancle()) < 0 ) {
                        // Postavi status na REJECTED_BY_ORGANIZER
                        reservation.setStatus(Reservation.ResStatus.REJECTED);

                        // Ažuriraj rezervaciju u bazi podataka
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("reservations")
                                .document(reservation.getId())  // Pretpostavljam da imaš ID rezervacije
                                .update("status", Reservation.ResStatus.APPROVED)
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
        clear(); // Clear current list
        addAll(updatedList); // Add updated list data
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }


}
