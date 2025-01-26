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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.adapters.CategoryAdapter;
import com.example.mobilneaplikacije.model.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryManagementFragment extends Fragment {
    private EditText editTextName, editTextDescription;
    private Button buttonAddCategory;
    private RecyclerView recyclerViewCategories;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_management, container, false);

        editTextName = view.findViewById(R.id.editTextCategoryName);
        editTextDescription = view.findViewById(R.id.editTextCategoryDescription);
        buttonAddCategory = view.findViewById(R.id.buttonAddCategory);
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);

        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList, getContext()); // Pass the context of the fragment's activity
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCategories.setAdapter(categoryAdapter);

        firestore = FirebaseFirestore.getInstance();

        loadCategories();

        buttonAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });

        return view;
    }

    private void loadCategories() {
        firestore.collection("categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    categoryList.clear();
                    for (DocumentSnapshot document : task.getResult()) {
                        Category category = document.toObject(Category.class);
                        categoryList.add(category);
                    }
                    categoryAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void addCategory() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (!name.isEmpty() && !description.isEmpty()) {
            Category category = new Category(name, description);

            // Add the category to Firestore
            firestore.collection("categories").add(category)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String docId = documentReference.getId(); // Retrieve the document ID
                            Toast.makeText(getContext(), "Category added with ID: " + docId, Toast.LENGTH_SHORT).show();
                            loadCategories();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error adding category", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }
}
