package com.example.mobilneaplikacije.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.model.Availability;

import java.text.SimpleDateFormat;
import java.util.List;

public class AvailabilityAdapter extends RecyclerView.Adapter<AvailabilityAdapter.AvailabilityViewHolder> {

    private List<Availability> availabilityList;

    public AvailabilityAdapter(List<Availability> availabilityList) {
        this.availabilityList = availabilityList;
    }

    @NonNull
    @Override
    public AvailabilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_availability, parent, false);
        return new AvailabilityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailabilityViewHolder holder, int position) {
        Availability availability = availabilityList.get(position);
        holder.bind(availability);
    }

    @Override
    public int getItemCount() {
        return availabilityList.size();
    }

    static class AvailabilityViewHolder extends RecyclerView.ViewHolder {
        private TextView timeTextView, statusTextView;

        public AvailabilityViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
        }

        public void bind(Availability availability) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String time = sdf.format(availability.getStartTime()) + " - " + sdf.format(availability.getEndTime());
            timeTextView.setText(time);
            statusTextView.setText(availability.getStatus());
        }
    }
}
