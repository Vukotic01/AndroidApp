package com.example.mobilneaplikacije.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.util.Log;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.adapters.ProductAdapter;
import com.example.mobilneaplikacije.model.Budget;
import com.example.mobilneaplikacije.model.Product;
import com.example.mobilneaplikacije.model.Worker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private List<Worker> workerList;
    private FirebaseFirestore db;
    private static final String TAG = "ProductDetailsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productList = new ArrayList<>();
        workerList = new ArrayList<>(); // Initialize the worker list
/*
        productAdapter = new ProductAdapter(productList, workerList, new ProductAdapter.OnBuyClickListener() {
            @Override
            public void onBuyClick(Product product) {
                addProductToBudget(product, "Vencanje T i M"); // Example eventName
            }

        });
        recyclerView.setAdapter(productAdapter);

        db = FirebaseFirestore.getInstance();
        loadProducts();
        loadWorkers(); // Load workers
*/
        return view;
    }

    private void loadProducts() {
        db.collection("products")
                .whereEqualTo("subcategoryType", "product")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            productList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product product = document.toObject(Product.class);
                                productList.add(product);
                                Log.d(TAG, "Product loaded: " + product.getName());
                            }
                            productAdapter.notifyDataSetChanged();
                            if (productList.isEmpty()) {
                                Log.w(TAG, "No products found in the collection.");
                                Toast.makeText(getContext(), "No products found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e(TAG, "Error loading products", task.getException());
                            Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
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
                                workerList.add(worker);
                                Log.d(TAG, "Worker loaded: " + worker.getFirstName());
                            }
                            productAdapter.notifyDataSetChanged();
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

    private void addProductToBudget(Product product, String eventName) {
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
                                budget.getProducts().add(product);
                                budget.setTotalCost(budget.getTotalCost() + product.getPrice());

                                db.collection("budgets").document(document.getId()).set(budget)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d(TAG, "Product added to budget: " + product.getName());
                                            Toast.makeText(getContext(), "Product added to budget", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e(TAG, "Failed to add product to budget", e);
                                            Toast.makeText(getContext(), "Failed to add product to budget", Toast.LENGTH_SHORT).show();
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
