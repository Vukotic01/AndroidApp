package com.example.mobilneaplikacije.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;
import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.model.Reservation;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReservationListAdapter extends ArrayAdapter<Reservation> {

    public ReservationListAdapter(@NonNull Context context, ArrayList<Reservation> reservations) {
        super(context, 0, reservations);
    }


    public void updateList(ArrayList<Reservation> updatedList) {
        clear(); // Clear current list
        addAll(updatedList); // Add updated list data
        notifyDataSetChanged(); // Notify the adapter that the data has changed
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reservation_cart, parent, false);
        }

        TextView productName = convertView.findViewById(R.id.textViewProductName);
        TextView productStatus = convertView.findViewById(R.id.textViewProductStatus);
        TextView dateToCancel = convertView.findViewById(R.id.textViewDateToCancle);
        Button cancelButton = convertView.findViewById(R.id.buttonAccept);

        if (productName != null && reservation != null) {
            productName.setText(reservation.getProduct().getName());
        }

        if (productStatus != null && reservation != null) {
            productStatus.setText(reservation.getStatus().name());
        }

        if (dateToCancel != null && reservation != null) {
            dateToCancel.setText(reservation.getDateToCancle());
        }

        // Handle button clicks if necessary
        cancelButton.setOnClickListener(v -> {
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
}
