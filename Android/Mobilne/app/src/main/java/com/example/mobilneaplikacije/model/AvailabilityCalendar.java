package com.example.mobilneaplikacije.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class AvailabilityCalendar implements Parcelable {

    private String date; // Datum
    private String workingHours; // Radno vreme
    private TimeSlot timeSlot; // Vremenski slot

    public AvailabilityCalendar(String date, String workingHours, TimeSlot timeSlot) {
        this.date = date;
        this.workingHours = workingHours;
        this.timeSlot = timeSlot;
    }

    public AvailabilityCalendar() {
    }

    protected AvailabilityCalendar(Parcel in) {
        date = in.readString();
        workingHours = in.readString();
        timeSlot = in.readParcelable(TimeSlot.class.getClassLoader());
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(workingHours);
        dest.writeParcelable(timeSlot, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<AvailabilityCalendar> CREATOR = new Parcelable.Creator<AvailabilityCalendar>() {
        @Override
        public AvailabilityCalendar createFromParcel(Parcel in) {
            return new AvailabilityCalendar(in);
        }

        @Override
        public AvailabilityCalendar[] newArray(int size) {
            return new AvailabilityCalendar[size];
        }
    };

    public static class TimeSlot implements Parcelable {

        private String from; // Vreme pocetka
        private String to; // Vreme kraja
        private String type; // Tip
        private Status status; // Rezervisano ili zauzeto

        public TimeSlot(String from, String to, String type, Status status) {
            this.from = from;
            this.to = to;
            this.type = type;
            this.status = status;
        }

        public TimeSlot() {
        }

        protected TimeSlot(Parcel in) {
            from = in.readString();
            to = in.readString();
            type = in.readString();
            status = Status.valueOf(in.readString());
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeString(from);
            dest.writeString(to);
            dest.writeString(type);
            dest.writeString(status.name());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Parcelable.Creator<TimeSlot> CREATOR = new Parcelable.Creator<TimeSlot>() {
            @Override
            public TimeSlot createFromParcel(Parcel in) {
                return new TimeSlot(in);
            }

            @Override
            public TimeSlot[] newArray(int size) {
                return new TimeSlot[size];
            }
        };
    }

    public enum Status {
        RESERVED,
        OCCUPIED;
    }
}
