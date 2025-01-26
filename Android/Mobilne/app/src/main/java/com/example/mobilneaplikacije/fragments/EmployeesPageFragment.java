package com.example.mobilneaplikacije.fragments;
import static android.content.ContentValues.TAG;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.adapters.EmployeeListAdapter;
import com.example.mobilneaplikacije.model.Employee;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.local.ReferenceSet;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeesPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeesPageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseFirestore db;

    public EmployeesPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmployeesPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmployeesPageFragment newInstance(String param1, String param2) {
        EmployeesPageFragment fragment = new EmployeesPageFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the fragment_employees_page layout
        View view = inflater.inflate(R.layout.fragment_employees_page, container, false);
        db = FirebaseFirestore.getInstance();

        // Postavljanje adaptera za ListView
        ListView listView = view.findViewById(android.R.id.list);
        ArrayList<Employee> employees = new ArrayList<>();
        EmployeeListAdapter adapter = new EmployeeListAdapter(getContext(), employees);


        // Postavljanje adaptera odmah
        listView.setAdapter(adapter);

        // Dodavanje OnClickListener za FloatingActionButton
        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Zamjena sadržaja trenutnog fragmenta s EmployeeRegisterFragment
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                EmployeeRegisterFragment employeeRegisterFragment = EmployeeRegisterFragment.newInstance(null, null);
                fragmentTransaction.replace(R.id.employeeConstraint, employeeRegisterFragment);
                fragmentTransaction.addToBackStack(null); // Dodaj u backstack
                fragmentTransaction.commit();
            }
        });

        // Dodavanje OnClickListener za FloatingActionButton
        FloatingActionButton fab2 = view.findViewById(R.id.floatingActionButton2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Zamena sadržaja trenutnog fragmenta s RatesForReportFragment
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RatesForReportFragment ratesForReportFragment = new RatesForReportFragment();
                fragmentTransaction.replace(R.id.employeeConstraint, ratesForReportFragment);
                fragmentTransaction.addToBackStack(null); // Dodaj u backstack
                fragmentTransaction.commit();
            }
        });

        FloatingActionButton fab3 = view.findViewById(R.id.floatingActionButton3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Zamena sadržaja trenutnog fragmenta s RatesForReportFragment
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ReservationForOwner reservationForOwnerFragment = new ReservationForOwner();
                fragmentTransaction.replace(R.id.employeeConstraint, reservationForOwnerFragment);
                fragmentTransaction.addToBackStack(null); // Dodaj u backstack
                fragmentTransaction.commit();
            }
        });



        // Set click listener for the moreButton within the adapter
        adapter.setOnMoreButtonClickListener(new EmployeeListAdapter.OnMoreButtonClickListener() {
            @Override
            public void onMoreButtonClick(Employee employee) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Kreiranje novog fragmenta
                EmployeeUpdateWorkTimeFragment employeeUpdateWorkTimeFragment = EmployeeUpdateWorkTimeFragment.newInstance(employee);

                // Zamena trenutnog fragmenta sa novim
                fragmentTransaction.replace(R.id.employeeConstraint, employeeUpdateWorkTimeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        // Dohvatanje podataka iz Firestore-a
        // Dohvatanje podataka iz Firestore-a
        db.collection("employees")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Očisti liste pre dodavanja novih podataka
                        employees.clear(); // Originalna lista
                        adapter.filteredEmployees.clear(); // Filtrirana lista

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Employee employee = document.toObject(Employee.class);
                            employee.setDocumentId(document.getId());
                            employees.add(employee); // Dodaj u originalnu listu
                        }

                        // Kopiraj podatke u filtriranu listu
                        adapter.filteredEmployees.addAll(employees);

                        // Ažuriraj adapter
                        adapter.notifyDataSetChanged();

                        // Log za proveru
                        Log.d(TAG, "Employees loaded: " + employees.size());
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });


        // Implementacija pretrage
        SearchView searchView = view.findViewById(R.id.employeeSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Filtriranje kada korisnik pritisne "enter"
                adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Dinamičko filtriranje dok korisnik unosi tekst
                adapter.filter(newText);
                return false;
            }
        });

        return view;
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