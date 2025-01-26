package com.example.mobilneaplikacije.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Employee implements Parcelable {
    //  private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Map<String, String> workSchedule;
    private String address;
    private String phoneNumber;
    private String profilePicture; // Changed type to String for image path
    private String servicesProvided;
    private List<AvailabilityCalendar> availabilityCalendar;


    private Boolean active;

    private String documentId;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        active = active;
    }

    public Employee(Long id, String firstName, String lastName, String email, Map<String, String> workSchedule, String address, String phoneNumber, String profilePicture, String servicesProvided, List<AvailabilityCalendar> availabilityCalendar) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.workSchedule = workSchedule;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
        this.servicesProvided = servicesProvided;
        this.availabilityCalendar = availabilityCalendar;
        this.active = true;
    }

    public Employee() {
        this.active = true;
    }

    protected Employee(Parcel in) {
        // id = in.readLong();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        int size = in.readInt(); // Čitanje veličine mape

        // Inicijalizacija mape
        workSchedule = new HashMap<>();
        // Čitanje i dodavanje ključ-vrednost parova u mapu
        for (int i = 0; i < size; i++) {
            String key = in.readString(); // Čitanje ključa (dan u nedelji)
            String value = in.readString(); // Čitanje vrednosti (radno vreme)
            workSchedule.put(key, value); // Dodavanje ključ-vrednost para u mapu
        }

        address = in.readString();
        phoneNumber = in.readString();
        profilePicture = in.readString();
        servicesProvided = in.readString();
        availabilityCalendar = in.createTypedArrayList(AvailabilityCalendar.CREATOR);
    }

    public Employee(long l, String john, String doe, String mail, HashMap<Object, Object> objectObjectHashMap, String s, String s1, String pathToProfilePicture1, String s2, String s3) {
    }


    /* public Long getId() {
         return id;
     }

     public void setId(Long id) {
         this.id = id;
     }
 */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone()
    {
        return  phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getServicesProvided() {
        return servicesProvided;
    }

    public void setServicesProvided(String servicesProvided) {
        this.servicesProvided = servicesProvided;
    }

    public List<AvailabilityCalendar> getAvailabilityCalendar() {
        return availabilityCalendar;
    }

    public void setAvailabilityCalendar(List<AvailabilityCalendar> availabilityCalendar) {
        this.availabilityCalendar = availabilityCalendar;
    }

    public Map<String, String> getWorkSchedule() {
        return workSchedule;
    }

    public void setWorkSchedule(Map<String, String> workSchedule) {
        this.workSchedule = workSchedule;
    }

    @Override
    public String toString() {
        return "Employee{" +
                // "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", workSchedule=" + workSchedule +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", servicesProvided='" + servicesProvided + '\'' +
                ", availabilityCalendar='" + availabilityCalendar + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        // dest.writeLong(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);

        // Upisujemo veličinu mape kako bismo je mogli rekonstruisati kasnije
        dest.writeInt(workSchedule.size());

        // Iteriramo kroz mapu i upisujemo ključ-vrednost parove
        for (Map.Entry<String, String> entry : workSchedule.entrySet()) {
            dest.writeString(entry.getKey()); // Upisujemo ključ
            dest.writeString(entry.getValue()); // Upisujemo vrednost
        }

        dest.writeString(address);
        dest.writeString(phoneNumber);
        dest.writeString(profilePicture);
        dest.writeString(servicesProvided);
        dest.writeTypedList(availabilityCalendar);
    }


    public static final Parcelable.Creator<Employee> CREATOR = new Parcelable.Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };
}
