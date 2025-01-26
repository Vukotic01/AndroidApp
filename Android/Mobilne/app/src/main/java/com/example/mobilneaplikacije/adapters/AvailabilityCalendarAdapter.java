package com.example.mobilneaplikacije.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;

import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.model.AvailabilityCalendar;

import java.util.List;

public class AvailabilityCalendarAdapter extends BaseAdapter {

    private final Context context;
    private final List<AvailabilityCalendar> availabilityCalendars;

    public AvailabilityCalendarAdapter(Context context, List<AvailabilityCalendar> availabilityCalendars) {
        this.context = context;
        this.availabilityCalendars = availabilityCalendars;
    }

    @Override
    public int getCount() {
        return availabilityCalendars.size();
    }

    @Override
    public Object getItem(int position) {
        return availabilityCalendars.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.event_card, parent, false);
            holder = new ViewHolder();
            holder.dateTextView = convertView.findViewById(R.id.calDate);
            holder.workingHoursTextView = convertView.findViewById(R.id.workingHours);
            holder.startTimeTextView = convertView.findViewById(R.id.eventStartTime);
            holder.endTimeTextView = convertView.findViewById(R.id.eventEndTime);
            holder.eventTypeTextView = convertView.findViewById(R.id.eventType);
            holder.eventStatusTextView = convertView.findViewById(R.id.eventStatuss);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AvailabilityCalendar calendar = availabilityCalendars.get(position);

        holder.dateTextView.setText(calendar.getDate());
        holder.workingHoursTextView.setText(calendar.getWorkingHours());
        holder.startTimeTextView.setText(calendar.getTimeSlot().getFrom());
        holder.endTimeTextView.setText(calendar.getTimeSlot().getTo());
        holder.eventTypeTextView.setText(calendar.getTimeSlot().getType());
        holder.eventStatusTextView.setText(calendar.getTimeSlot().getStatus().name());

        return convertView;
    }

    private static class ViewHolder {
        TextView dateTextView;
        TextView workingHoursTextView;
        TextView startTimeTextView;
        TextView endTimeTextView;
        TextView eventTypeTextView;
        TextView eventStatusTextView;
    }
}
