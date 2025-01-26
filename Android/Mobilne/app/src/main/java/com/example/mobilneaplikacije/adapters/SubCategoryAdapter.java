package com.example.mobilneaplikacije.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.model.SubCategory;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder> {

    private List<SubCategory> subCategoryList;
    private Context context;

    public SubCategoryAdapter(List<SubCategory> subCategoryList, Context context) {
        this.subCategoryList = subCategoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subcategory, parent, false);
        return new SubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position) {
        SubCategory subCategory = subCategoryList.get(position);
        holder.bind(subCategory);
    }

    @Override
    public int getItemCount() {
        return subCategoryList.size();
    }

    class SubCategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewSubcategoryName;
        private TextView textViewSubcategoryDescription;

        public SubCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewSubcategoryName = itemView.findViewById(R.id.textViewSubcategoryName);
            textViewSubcategoryDescription = itemView.findViewById(R.id.textViewSubcategoryDescription);
        }

        public void bind(SubCategory subCategory) {
            textViewSubcategoryName.setText(subCategory.getName());
            textViewSubcategoryDescription.setText(subCategory.getDescription());
        }
    }
}
