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

import com.example.mobilneaplikacije.model.Event;
import com.example.mobilneaplikacije.R;
import com.example.mobilneaplikacije.model.Events;

import java.util.ArrayList;

public class EventListAdapter extends ArrayAdapter<Events> {
    private ArrayList<Events> aEvents;
    private OnMoreButtonClickListener moreButtonClickListener;

    public EventListAdapter(Context context, ArrayList<Events> events) {
        super(context, R.layout.event_for_rate_cart, events);
        this.aEvents = events;
    }

    public interface OnMoreButtonClickListener {
        void onMoreButtonClick(Events event);
    }

    public void setOnMoreButtonClickListener(OnMoreButtonClickListener listener) {
        this.moreButtonClickListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Events event = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_for_rate_cart,
                    parent, false);
        }

        TextView eventName = convertView.findViewById(R.id.textViewEventForRateName);
        Button rateButton = convertView.findViewById(R.id.buttonRate);

        if (event != null) {
            eventName.setText(event.getName());
        }

        rateButton.setOnClickListener(v -> {
            if (moreButtonClickListener != null) {
                moreButtonClickListener.onMoreButtonClick(event);
            }
        });

        return convertView;
    }
}
