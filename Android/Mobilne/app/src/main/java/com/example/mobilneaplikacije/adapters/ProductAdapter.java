package com.example.mobilneaplikacije.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.model.Product;
import com.example.mobilneaplikacije.model.Worker;

import java.util.List;

public class ProductAdapter extends BaseAdapter {

    public interface OnBuyClickListener {
        void onBuyClick(Product product);
    }

    private List<Product> productList;
    private List<Worker> workerList;
    private OnBuyClickListener onBuyClickListener;
    private LayoutInflater inflater;

    public ProductAdapter(Context context, List<Product> productList, List<Worker> workerList, OnBuyClickListener onBuyClickListener) {
        this.productList = productList;
        this.workerList = workerList;
        this.onBuyClickListener = onBuyClickListener;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.product_cart, parent, false);
        }

        Product product = productList.get(position);

        TextView textViewProductName = convertView.findViewById(R.id.textViewProductName);
        TextView textViewProductDescription = convertView.findViewById(R.id.textViewProductDescription);
        TextView textViewProductPrice = convertView.findViewById(R.id.textViewProductPrice);
      //  Button buttonBuy = convertView.findViewById(R.id.buttonBuy);
      //  Button buttonReserve = convertView.findViewById(R.id.buttonReserve);

        textViewProductName.setText(product.getName());
        textViewProductDescription.setText(product.getDescription());
        textViewProductPrice.setText(String.valueOf(product.getPrice()));

  /*      buttonBuy.setOnClickListener(v -> {
            if (onBuyClickListener != null) {
                onBuyClickListener.onBuyClick(product);
            }
        });*/

        return convertView;
    }
}
