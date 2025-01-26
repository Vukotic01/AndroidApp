package com.example.mobilneaplikacije.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.adapters.AvailabilityCalendarAdapter;
import com.example.mobilneaplikacije.adapters.EventListAdapter;
import com.example.mobilneaplikacije.model.AvailabilityCalendar;
import com.example.mobilneaplikacije.model.Event;
import com.example.mobilneaplikacije.model.Events;

import java.util.ArrayList;
import java.util.List;

public class EmployeeEventsAddFragment extends Fragment implements EventListAdapter.OnMoreButtonClickListener {

    private static final String TAG = "EmployeeEventsAddFragment";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public EmployeeEventsAddFragment() {
        // Required empty public constructor
    }

    public static EmployeeEventsAddFragment newInstance(String param1, String param2) {
        EmployeeEventsAddFragment fragment = new EmployeeEventsAddFragment();
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

    private ListView calendarListView;


    public static EmployeeEventsAddFragment newInstance() {
        return new EmployeeEventsAddFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employee_events_add, container, false);

        // Initialize the ListView
        calendarListView = view.findViewById(R.id.calendarList);

        // Create dummy data for testing
        List<AvailabilityCalendar> availabilityCalendars = createDummyData();

        // Set adapter to the ListView
        AvailabilityCalendarAdapter adapter = new AvailabilityCalendarAdapter(requireContext(), availabilityCalendars);
        calendarListView.setAdapter(adapter);

        // Handle item clicks
        calendarListView.setOnItemClickListener((parent, view1, position, id) -> {
            AvailabilityCalendar selectedCalendar = availabilityCalendars.get(position);
            Toast.makeText(requireContext(), "Selected: " + selectedCalendar.getDate(), Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private List<AvailabilityCalendar> createDummyData() {
        List<AvailabilityCalendar> calendars = new ArrayList<>();
        calendars.add(new AvailabilityCalendar("2024-12-20", "09:00-17:00",
                new AvailabilityCalendar.TimeSlot("09:00", "10:00", "Meeting", AvailabilityCalendar.Status.RESERVED)));
        calendars.add(new AvailabilityCalendar("2024-12-21", "10:00-18:00",
                new AvailabilityCalendar.TimeSlot("10:00", "11:00", "Consultation", AvailabilityCalendar.Status.OCCUPIED)));
        calendars.add(new AvailabilityCalendar("2024-12-22", "08:00-16:00",
                new AvailabilityCalendar.TimeSlot("08:00", "09:00", "Workshop", AvailabilityCalendar.Status.RESERVED)));
        return calendars;
    }




    @Override
    public void onMoreButtonClick(Events event) {
        if (getContext() != null) {
            Toast.makeText(getContext(), "More button clicked for event: " + event.getName(), Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "Context is null");
        }
    }
}
