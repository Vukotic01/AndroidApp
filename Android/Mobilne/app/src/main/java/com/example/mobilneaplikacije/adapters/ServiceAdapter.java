package com.example.mobilneaplikacije.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.model.Availability;
import com.example.mobilneaplikacije.model.Service;
import com.example.mobilneaplikacije.model.Worker;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    public interface OnReserveClickListener {
        void onReserveClick(Service service, Worker worker, Availability availability, Date startTime, Date endTime);
    }

    private List<Service> serviceList;
    private List<Worker> workerList;
    private OnReserveClickListener onReserveClickListener;
    private FirebaseFirestore db;

    public ServiceAdapter(List<Service> serviceList, List<Worker> workerList, OnReserveClickListener onReserveClickListener) {
        this.serviceList = serviceList;
        this.workerList = workerList;
        this.onReserveClickListener = onReserveClickListener;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.bind(service, workerList, onReserveClickListener);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewProductName;
        private TextView textViewProductDescription;
        private TextView textViewProductPrice;
        private Spinner workerSpinner;
        private Spinner availabilitySpinner;
        private EditText editTextStartTime;
        private EditText editTextEndTime;
        private Button buttonReserve;

        private List<Availability> currentAvailabilities; // Add this to keep track of current availabilities

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductDescription = itemView.findViewById(R.id.textViewProductDescription);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            workerSpinner = itemView.findViewById(R.id.workerSpinner);
            availabilitySpinner = itemView.findViewById(R.id.availabilitySpinner);
            editTextStartTime = itemView.findViewById(R.id.editTextStartTime);
            editTextEndTime = itemView.findViewById(R.id.editTextEndTime);
            buttonReserve = itemView.findViewById(R.id.buttonReserve);
        }

        public void bind(Service service, List<Worker> workerList, OnReserveClickListener onReserveClickListener) {
            textViewProductName.setText(service.getName());
            textViewProductDescription.setText(service.getDescription());
            textViewProductPrice.setText(String.valueOf(service.getPrice()));

            List<String> workerNames = new ArrayList<>();
            for (Worker worker : workerList) {
                workerNames.add(worker.getFirstName() + " " + worker.getLastName());
            }

            ArrayAdapter<String> workerAdapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_item, workerNames);
            workerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            workerSpinner.setAdapter(workerAdapter);

            workerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Worker selectedWorker = workerList.get(position);
                    fetchAvailability(selectedWorker.getWorkerId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    availabilitySpinner.setAdapter(null);
                }
            });
            buttonReserve.setOnClickListener(v -> {
                int selectedWorkerPosition = workerSpinner.getSelectedItemPosition();
                int selectedAvailabilityPosition = availabilitySpinner.getSelectedItemPosition();
                if (selectedWorkerPosition >= 0 && selectedWorkerPosition < workerList.size() && selectedAvailabilityPosition >= 0) {
                    Worker selectedWorker = workerList.get(selectedWorkerPosition);
                    Availability selectedAvailability = currentAvailabilities.get(selectedAvailabilityPosition);

                    String startTimeString = editTextStartTime.getText().toString();
                    String endTimeString = editTextEndTime.getText().toString();
                    if (TextUtils.isEmpty(startTimeString)) {
                        Toast.makeText(itemView.getContext(), "Start time is required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Date startTime;
                    Date endTime;
                    try {
                        startTime = parseTime(startTimeString);
                        if (!TextUtils.isEmpty(endTimeString)) {
                            endTime = parseTime(endTimeString);
                        } else if (service.getDuration() != null) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(startTime);
                            calendar.add(Calendar.HOUR, Integer.parseInt(service.getDuration().split("h")[0]));  // Assuming duration is given in hours, e.g., "2h"
                            endTime = calendar.getTime();
                        } else {
                            Toast.makeText(itemView.getContext(), "End time is required if duration is not specified", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (ParseException e) {
                        Toast.makeText(itemView.getContext(), "Invalid time format", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Perform validation
                    if (!isValidTimeRange(startTime, endTime, selectedAvailability)) {
                        Toast.makeText(itemView.getContext(), "Selected time is outside of availability", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    onReserveClickListener.onReserveClick(service, selectedWorker, selectedAvailability, startTime, endTime);
                } else {
                    Toast.makeText(itemView.getContext(), "No worker or availability selected", Toast.LENGTH_SHORT).show();
                }
            });
        }




            private void fetchAvailability(String workerId) {
            db.collection("workers")
                    .document(workerId)
                    .collection("availability")
                    .whereEqualTo("status", "free")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null) {
                                currentAvailabilities = new ArrayList<>();
                                List<String> availabilityDescriptions = new ArrayList<>();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                                for (QueryDocumentSnapshot document : querySnapshot) {
                                    Availability availability = document.toObject(Availability.class);
                                    currentAvailabilities.add(availability);
                                    String description = sdf.format(availability.getStartTime().toDate()) + " - " +
                                            sdf.format(availability.getEndTime().toDate());
                                    availabilityDescriptions.add(description);
                                }

                                ArrayAdapter<String> availabilityAdapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_item, availabilityDescriptions);
                                availabilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                availabilitySpinner.setAdapter(availabilityAdapter);
                            }
                        } else {
                            Toast.makeText(itemView.getContext(), "Failed to load availability", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        private Date parseTime(String timeString) throws ParseException {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return timeFormat.parse(timeString);
        }
        private boolean isValidTimeRange(Date startTime, Date endTime, Availability availability) {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startTime);
            startCalendar.set(Calendar.YEAR, 1970);
            startCalendar.set(Calendar.MONTH, 0);
            startCalendar.set(Calendar.DAY_OF_MONTH, 1);

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endTime);
            endCalendar.set(Calendar.YEAR, 1970);
            endCalendar.set(Calendar.MONTH, 0);
            endCalendar.set(Calendar.DAY_OF_MONTH, 1);

            Calendar availabilityStartCalendar = Calendar.getInstance();
            availabilityStartCalendar.setTime(availability.getStartTime().toDate());
            availabilityStartCalendar.set(Calendar.YEAR, 1970);
            availabilityStartCalendar.set(Calendar.MONTH, 0);
            availabilityStartCalendar.set(Calendar.DAY_OF_MONTH, 1);

            Calendar availabilityEndCalendar = Calendar.getInstance();
            availabilityEndCalendar.setTime(availability.getEndTime().toDate());
            availabilityEndCalendar.set(Calendar.YEAR, 1970);
            availabilityEndCalendar.set(Calendar.MONTH, 0);
            availabilityEndCalendar.set(Calendar.DAY_OF_MONTH, 1);

            return !startCalendar.before(availabilityStartCalendar) && !endCalendar.after(availabilityEndCalendar);
        }


    }
}
