package com.example.mobilneaplikacije.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.adapters.ReportListAdapter;
import com.example.mobilneaplikacije.model.Report;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportReviewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView reportsListView;
    private ArrayList<Report> reportsList;
    private ReportListAdapter adapter;

    public ReportReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_review, container, false);

        reportsListView = view.findViewById(R.id.reportsListView);
        reportsList = new ArrayList<>();
        adapter = new ReportListAdapter(getContext(), reportsList);
        reportsListView.setAdapter(adapter);

        loadReportsFromFirestore();

        return view;
    }

    private void loadReportsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reports")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reportsList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Report report = document.toObject(Report.class);
                            report.setId(document.getId()); // Set the Firestore document ID
                            if(report.getStatus().equals(Report.RepStatus.HOLD))
                            {
                                reportsList.add(report);
                            }

                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        // Handle the error
                    }
                });
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportReviewFragment newInstance(String param1, String param2) {
        ReportReviewFragment fragment = new ReportReviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



}