package com.example.mobilneaplikacije.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeeUpdateWorkTimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeUpdateWorkTimeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner daySpinner;
    private TimePicker startTimePicker, endTimePicker;
    private Map<String, String> workSchedule;

    public EmployeeUpdateWorkTimeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
    // * @param param1 Parameter 1.
    // * @param param2 Parameter 2.
     * @return A new instance of fragment EmployeeUpdateWorkTimeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmployeeUpdateWorkTimeFragment newInstance(Employee employee) {
        EmployeeUpdateWorkTimeFragment fragment = new EmployeeUpdateWorkTimeFragment();
        Bundle args = new Bundle();
        args.putParcelable("employee", employee); // Use putParcelable for Parcelable objects
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

    private void setTimeToPicker(TimePicker timePicker, String time) {
        if (time != null && time.contains(":")) {
            String[] timeParts = time.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_update_work_time, container, false);
        // Initialize UI elements
        daySpinner = view.findViewById(R.id.daySpinner);
        startTimePicker = view.findViewById(R.id.startTimePicker);
        endTimePicker = view.findViewById(R.id.endTimePicker);
        Button eventsButton = view.findViewById(R.id.eventsButton);
        Employee employee = getArguments().getParcelable("employee");

        workSchedule = employee.getWorkSchedule();

        // Populate the Spinner with the days of the week
        List<String> days = new ArrayList<>(workSchedule.keySet());
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, days);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(spinnerAdapter);

        // Set the TimePickers based on the selected day
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedDay = days.get(position);
                String schedule = workSchedule.get(selectedDay);
                if (schedule != null) {
                    String[] times = schedule.split(" - ");
                    if (times.length == 2) {
                        String startTime = times[0];
                        String endTime = times[1];

                        // Parse start and end times into hour and minute
                        setTimeToPicker(startTimePicker, startTime);
                        setTimeToPicker(endTimePicker, endTime);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing if no day is selected
            }
        });

        // Prikazivanje podataka o zaposlenom
        // Pronalaženje TextView komponenti u view-u
        TextView nameTextView = view.findViewById(R.id.nameText);
        TextView surnameTextView = view.findViewById(R.id.employeeSurname);
  // Dodajte TextView za adresu
        TextView phoneTextView = view.findViewById(R.id.employeePhone);      // Dodajte TextView za telefon
        TextView emailTextView = view.findViewById(R.id.employeeMail);      // Dodajte TextView za email
        TextView adress =view.findViewById(R.id.employeeAdress);
// Postavljanje teksta u odgovarajuće TextView komponente iz objekta employee
        nameTextView.setText(employee.getFirstName());
        surnameTextView.setText(employee.getLastName());
           // Pretpostavljamo da imate getter za adresu
        phoneTextView.setText(employee.getPhone());       // Pretpostavljamo da imate getter za telefon
        emailTextView.setText(employee.getEmail());
        adress.setText(employee.getAddress());
        eventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Zamjena sadržaja RelativeLayout s EmployeeEventsAddFragment
                FragmentManager fragmentManager = getParentFragmentManager(); // Ako koristite stariju verziju Fragmenta koristite getSupportFragmentManager()
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                EmployeeEventsAddFragment employeeEventsAddFragment = EmployeeEventsAddFragment.newInstance(null, null);
                fragmentTransaction.replace(R.id.employeeConstraint, employeeEventsAddFragment);
                fragmentTransaction.addToBackStack(null);  // Dodajemo na back stack da se možemo vratiti na prethodni fragment
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}