package com.example.mobilneaplikacije.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.model.Product;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class AddProductFragment extends Fragment {

    private EditText editTextProductName;
    private EditText editTextProductDescription;
    private EditText editTextProductPrice;
    private EditText editTextProductDiscount;
    private EditText editTextPriceWithDiscount;
    private EditText editTextProductCategory;
    private EditText editTextProductSubCategory;
    private EditText editTextProductImages;
    private EditText editTextProductEventTypes;
    private Button buttonAddProduct;

    private FirebaseFirestore db;

    public AddProductFragment() {
        // Required empty public constructor
    }

    public static AddProductFragment newInstance() {
        return new AddProductFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextProductName = view.findViewById(R.id.editTextProductName);
        editTextProductDescription = view.findViewById(R.id.editTextProductDescription);
        editTextProductPrice = view.findViewById(R.id.editTextProductPrice);
        editTextProductDiscount = view.findViewById(R.id.editTextProductDiscount);
        editTextPriceWithDiscount = view.findViewById(R.id.editTestPriceWithDiscount);
        editTextProductCategory = view.findViewById(R.id.editTextProductCategory);
        editTextProductSubCategory = view.findViewById(R.id.editTextProductSubCategory);
        editTextProductImages = view.findViewById(R.id.editTextProductImages);
        editTextProductEventTypes = view.findViewById(R.id.editTextProductEventTypes);
        buttonAddProduct = view.findViewById(R.id.buttonAddProduct);

        db = FirebaseFirestore.getInstance();

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
    }

    private void addProduct() {
        String name = editTextProductName.getText().toString();
        String description = editTextProductDescription.getText().toString();
        double price = Double.parseDouble(editTextProductPrice.getText().toString());
        double discount = Double.parseDouble(editTextProductDiscount.getText().toString());
        double priceWithDiscount = Double.parseDouble(editTextPriceWithDiscount.getText().toString());
        String categoryName = editTextProductCategory.getText().toString();
        String subCategoryName = editTextProductSubCategory.getText().toString();
        String images = editTextProductImages.getText().toString();
        String eventTypes = editTextProductEventTypes.getText().toString();


        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setDiscount(discount);
        product.setPriceWithDiscount(priceWithDiscount);
        product.setCategory(categoryName);
        product.setSubCategory(subCategoryName);
        product.setImages(Arrays.asList(images.split(",")));
        product.setEventTypes(Arrays.asList(eventTypes.split(",")));
        product.setVisibility(true);
        product.setAvailability(true);

        db.collection("products")
                .add(product)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Product added successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to add product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
