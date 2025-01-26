package com.example.mobilneaplikacije.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.model.Category;
import com.example.mobilneaplikacije.model.SubCategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categoryList;
    private Context context;

    public CategoryAdapter(List<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewCategoryName;
        private TextView textViewCategoryDescription;
        private Button buttonToggleSubcategoryForm;
        private View subcategoryForm;
        private EditText editTextSubcategoryName, editTextSubcategoryDescription;
        private Button buttonAddSubcategory;
        private RecyclerView recyclerViewSubcategories;
        private SubCategoryAdapter subCategoryAdapter;
        private List<SubCategory> subCategoryList;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);
            textViewCategoryDescription = itemView.findViewById(R.id.textViewCategoryDescription);
            buttonToggleSubcategoryForm = itemView.findViewById(R.id.buttonToggleSubcategoryForm);
            subcategoryForm = itemView.findViewById(R.id.subcategoryForm);
            editTextSubcategoryName = itemView.findViewById(R.id.editTextSubcategoryName);
            editTextSubcategoryDescription = itemView.findViewById(R.id.editTextSubcategoryDescription);
            buttonAddSubcategory = itemView.findViewById(R.id.buttonAddSubcategory);
            recyclerViewSubcategories = itemView.findViewById(R.id.recyclerViewSubcategories);
        }

        public void bind(Category category) {
            textViewCategoryName.setText(category.getName());
            textViewCategoryDescription.setText(category.getDescription());

            subCategoryList = category.getSubcategories();
            if (subCategoryList == null) {
                subCategoryList = new ArrayList<>(); // Initialize to an empty list if null
            }

            subCategoryAdapter = new SubCategoryAdapter(subCategoryList, context);
            recyclerViewSubcategories.setLayoutManager(new LinearLayoutManager(context));
            recyclerViewSubcategories.setAdapter(subCategoryAdapter);

            buttonToggleSubcategoryForm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (subcategoryForm.getVisibility() == View.VISIBLE) {
                        subcategoryForm.setVisibility(View.GONE);
                    } else {
                        subcategoryForm.setVisibility(View.VISIBLE);
                    }
                }
            });

            buttonAddSubcategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addSubcategory(category);
                }
            });
        }

        private void addSubcategory(Category category) {
            String name = editTextSubcategoryName.getText().toString().trim();
            String description = editTextSubcategoryDescription.getText().toString().trim();
            String type = "Product"; // Or use a spinner for type selection

            if (!name.isEmpty() && !description.isEmpty() && !type.isEmpty()) {
                SubCategory subCategory = new SubCategory(category, name, description, type); // Ensure category is set

                FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                // Retrieve the document ID for the category
                firestore.collection("categories")
                        .whereEqualTo("name", category.getName())
                        .whereEqualTo("description", category.getDescription())
                        .limit(1) // Ensure we only get the document related to this category
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                    String documentId = document.getId();

                                    // Update the category document with the new subcategory
                                    firestore.collection("categories").document(documentId)
                                            .update("subcategories", FieldValue.arrayUnion(subCategory))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(context, "Subcategory added", Toast.LENGTH_SHORT).show();
                                                    subCategoryList.add(subCategory); // Add to the list
                                                    subCategoryAdapter.notifyDataSetChanged(); // Notify the adapter
                                                    subcategoryForm.setVisibility(View.GONE);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Error adding subcategory", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Toast.makeText(context, "Error finding category document", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
