package com.example.mobilneaplikacije.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.adapters.AvailabilityAdapter;
import com.example.mobilneaplikacije.adapters.ServiceAdapter;
import com.example.mobilneaplikacije.model.Availability;
import com.example.mobilneaplikacije.model.Budget;
import com.example.mobilneaplikacije.model.Product;
import com.example.mobilneaplikacije.model.Service;
import com.example.mobilneaplikacije.model.Worker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceDetailsFragment extends Fragment {

    private RecyclerView recyclerView;
    private CalendarView calendarView;
    private TextView selectedDateTextView;
    private RecyclerView availabilityRecyclerView;
    private AvailabilityAdapter availabilityAdapter;
    private List<Availability> availabilityList;

    private ServiceAdapter serviceAdapter;
    private List<Service> serviceList;
    private List<Worker> workerList;
    private FirebaseFirestore db;
    private String selectedWorkerId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_details, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewServices);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        serviceList = new ArrayList<>();
        workerList = new ArrayList<>();

        calendarView = view.findViewById(R.id.calendarView);
        selectedDateTextView = view.findViewById(R.id.selectedDateTextView);
        availabilityRecyclerView = view.findViewById(R.id.availabilityRecyclerView);
        availabilityRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SamplesManager samplesManager = new SamplesManager();
        //samplesManager.createSampleWorker();
        //samplesManager.createSampleAvailabilities("jgttEecRNW3Rzq0eKBnW");
        //samplesManager.createSampleServices();

        serviceAdapter = new ServiceAdapter(serviceList, workerList, new ServiceAdapter.OnReserveClickListener() {
            @Override
            public void onReserveClick(Service service, Worker worker, Availability availability, Date startTime, Date endTime) {
                setSelectedWorkerId(worker.getWorkerId()); // Use getWorkerId method
                addServiceToBudget(service, "Vencanje T i M"); // Example eventName
                Toast.makeText(getContext(), "Reserved " + service.getName() + " with " + worker.getFirstName(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(serviceAdapter);

        db = FirebaseFirestore.getInstance();
        availabilityList = new ArrayList<>();
        availabilityAdapter = new AvailabilityAdapter(availabilityList);
        availabilityRecyclerView.setAdapter(availabilityAdapter);

        loadServices();
        loadWorkers();

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            String dateString = dayOfMonth + "/" + (month + 1) + "/" + year;
            selectedDateTextView.setText(dateString);
            fetchAvailabilityForDate(year, month, dayOfMonth);
        });

        return view;
    }

    public void setSelectedWorkerId(String workerId) {
        this.selectedWorkerId = workerId;
    }

    private void loadServices() {
        db.collection("services")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            serviceList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Service service = document.toObject(Service.class);
                                serviceList.add(service);
                                Log.d(TAG, "Service loaded: " + service.getName());
                            }
                            serviceAdapter.notifyDataSetChanged();
                            if (serviceList.isEmpty()) {
                                Log.w(TAG, "No services found in the collection.");
                                Toast.makeText(getContext(), "No services found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e(TAG, "Error loading services", task.getException());
                            Toast.makeText(getContext(), "Failed to load services", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loadWorkers() {
        db.collection("workers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            workerList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Worker worker = document.toObject(Worker.class);
                                worker.setWorkerId(document.getId()); // Set the document ID as workerId
                                workerList.add(worker);
                                Log.d(TAG, "Worker loaded: " + worker.getFirstName());
                            }
                            serviceAdapter.notifyDataSetChanged();
                            if (workerList.isEmpty()) {
                                Log.w(TAG, "No workers found in the collection.");
                                Toast.makeText(getContext(), "No workers found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e(TAG, "Error loading workers", task.getException());
                            Toast.makeText(getContext(), "Failed to load workers", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void fetchAvailabilityForDate(int year, int month, int dayOfMonth) {
        if (selectedWorkerId == null) {
            Toast.makeText(getContext(), "No worker selected", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("workers")
                .document(selectedWorkerId)
                .collection("availability")
                .whereEqualTo("date", new Date(year - 1900, month, dayOfMonth))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            availabilityList.clear();
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                Availability availability = document.toObject(Availability.class);
                                availabilityList.add(availability);
                            }
                            availabilityAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getContext(), "Failed to load availability", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addServiceToBudget(Service service, String eventName) {
        db.collection("budgets")
                .whereEqualTo("eventName", eventName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            Budget budget = document.toObject(Budget.class);
                            if (budget != null) {
                                budget.getServices().add(service);
                                budget.setTotalCost(budget.getTotalCost() + service.getPrice());

                                db.collection("budgets").document(document.getId()).set(budget)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d(TAG, "Service reserved: " + service.getName());
                                            Toast.makeText(getContext(), "Service reserved", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e(TAG, "Failed to reserve service", e);
                                            Toast.makeText(getContext(), "Failed to reserve service", Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                Log.w(TAG, "Failed to parse budget");
                                Toast.makeText(getContext(), "Failed to parse budget", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.w(TAG, "Budget not found");
                            Toast.makeText(getContext(), "Budget not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
