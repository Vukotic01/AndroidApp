package com.example.mobilneaplikacije.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.fragments.SubmitReportFragment;
import com.example.mobilneaplikacije.model.Rate;
import java.util.ArrayList;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class RateListAdapter extends ArrayAdapter<Rate> {
    private ArrayList<Rate> rates;
    private FragmentActivity activity;

    public RateListAdapter(Context context, ArrayList<Rate> rates) {
        super(context, R.layout.rate_cart, rates);
        this.rates = rates;
        this.activity = (FragmentActivity) context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Rate rate = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rate_cart, parent, false);
        }

        TextView textViewComment = convertView.findViewById(R.id.textViewCommentForReport);
        TextView textViewRatingValue = convertView.findViewById(R.id.textViewRatingValue);
        Button buttonReport = convertView.findViewById(R.id.buttonReport);

        if (rate != null) {
            textViewComment.setText(rate.getComment());
            textViewRatingValue.setText("Rating: " + rate.getRating());
        }

        buttonReport.setOnClickListener(v -> {
            // Create a new instance of SubmitReportFragment with the selected Rate
            SubmitReportFragment submitReportFragment = SubmitReportFragment.newInstance(rate);

            // Replace the current fragment with SubmitReportFragment
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.employeeConstraint, submitReportFragment);
            fragmentTransaction.addToBackStack(null); // Add to back stack
            fragmentTransaction.commit();
        });

        return convertView;
    }
}
