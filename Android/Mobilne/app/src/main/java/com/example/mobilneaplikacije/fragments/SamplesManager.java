package com.example.mobilneaplikacije.fragments;

import com.example.mobilneaplikacije.model.Availability;
import com.example.mobilneaplikacije.model.Service;
import com.example.mobilneaplikacije.model.Worker;
import com.example.mobilneaplikacije.model.enums.UserRole;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.mobilneaplikacije.model.Budget;
import com.example.mobilneaplikacije.model.Product;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SamplesManager {

    private FirebaseFirestore db;

    public SamplesManager() {
        db = FirebaseFirestore.getInstance();
    }

    public void createSampleBudget() {
        // Create sample products
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setDescription("Description of Product 1");
        product1.setPrice(29.99);
        product1.setSubCategory("product");

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setDescription("Description of Product 2");
        product2.setPrice(39.99);
        product2.setSubCategory("product");

        // Create a sample budget
        Budget budget = new Budget();
        budget.setEventName("Sample Event");
        budget.getProducts().add(product1);
        budget.getProducts().add(product2);
        budget.setTotalCost(product1.getPrice() + product2.getPrice());

        // Save the budget to Firestore
        db.collection("budgets")
                .add(budget)
                .addOnSuccessListener(documentReference -> {
                    System.out.println("Budget added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error adding budget: " + e.getMessage());
                });
    }

    public void createSampleWorker() {
        // Define working hours for the worker
        Map<String, String> workingHours = new HashMap<>();
        workingHours.put("Monday", "08:00-16:00");
        workingHours.put("Tuesday", "08:00-16:00");
        workingHours.put("Wednesday", "08:00-16:00");
        workingHours.put("Thursday", "08:00-16:00");
        workingHours.put("Friday", "08:00-16:00");

        // Create a sample worker
        Worker worker = new Worker(
                "igor@example.com",
                "password123",
                "Igor",
                "Petrovic",
                "123 Main St",
                "123-456-7890",
                UserRole.WORKER,
                workingHours,
                null,
                null
        );

        // Save the worker to Firestore
        db.collection("workers")
                .add(worker)
                .addOnSuccessListener(documentReference -> {
                    String workerId = documentReference.getId();
                    db.collection("workers").document(workerId)
                            .update("workerId", workerId)
                            .addOnSuccessListener(aVoid -> {
                                System.out.println("Worker ID updated: " + workerId);
                                createSampleAvailabilities(workerId);
                            })
                            .addOnFailureListener(e -> {
                                System.err.println("Error updating worker ID: " + e.getMessage());
                            });
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error adding worker: " + e.getMessage());
                });
    }

    public void createSampleAvailabilities(String documentId) {
        // Define sample availability for 3 days
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 3; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, i);
            Date date = calendar.getTime();

            Availability availability1 = new Availability(new Timestamp(date), createTime(date, 8, 0), createTime(date, 10, 0), "free");
            Availability availability2 = new Availability(new Timestamp(date), createTime(date, 10, 0), createTime(date, 12, 0), "busy");
            Availability availability3 = new Availability(new Timestamp(date), createTime(date, 12, 0), createTime(date, 15, 0), "free");
            Availability availability4 = new Availability(new Timestamp(date), createTime(date, 15, 0), createTime(date, 16, 0), "busy");

            // Save availabilities to Firestore
            db.collection("workers").document(documentId).collection("availability")
                    .add(availability1)
                    .addOnSuccessListener(aVoid -> System.out.println("Availability added for worker with ID: " + documentId))
                    .addOnFailureListener(e -> System.err.println("Error adding availability: " + e.getMessage()));

            db.collection("workers").document(documentId).collection("availability")
                    .add(availability2)
                    .addOnSuccessListener(aVoid -> System.out.println("Availability added for worker with ID: " + documentId))
                    .addOnFailureListener(e -> System.err.println("Error adding availability: " + e.getMessage()));

            db.collection("workers").document(documentId).collection("availability")
                    .add(availability3)
                    .addOnSuccessListener(aVoid -> System.out.println("Availability added for worker with ID: " + documentId))
                    .addOnFailureListener(e -> System.err.println("Error adding availability: " + e.getMessage()));

            db.collection("workers").document(documentId).collection("availability")
                    .add(availability4)
                    .addOnSuccessListener(aVoid -> System.out.println("Availability added for worker with ID: " + documentId))
                    .addOnFailureListener(e -> System.err.println("Error adding availability: " + e.getMessage()));
        }
    }

    public void createSampleServices() {
        // Sample Service 1
        Service service1 = new Service(
                "Foto i video",
                "Snimanje dronom",
                "Snimanje dronom",
                "Ovo je snimanje iz vazduha sa dronom",
                "ne radimo praznicima",
                3000,
                0,
                Arrays.asList("Slika 1", "Slika 2", "Slika 3"),
                Arrays.asList("vencanje", "krstenje", "1 rodjendan"),
                true,
                true,
                Arrays.asList("Igor", "Pera"),
                "2",
                null,
                null,
                12,
                2,
                "ručno",
                "pending"
        );

        // Sample Service 2
        Service service2 = new Service(
                "Foto i video",
                "Videografija",
                "Snimanje kamerom 4k",
                "Ovo je snimanje u 4k rezoluciji",
                "/",
                5000,
                0,
                Arrays.asList("Slika 1", "Slika 2", "Slika 3"),
                Arrays.asList("vencanje", "krstenje", "1 rodjendan"),
                true,
                true,
                Arrays.asList("Igor", "Pera"),
                null,
                "1",
                "5",
                12,
                2,
                "ručno",
                "pending"
        );

        // Sample Service 3
        Service service3 = new Service(
                "Foto i video",
                "Fotografisanje",
                "Fotografisanje događaja",
                "Fotografisanje događaja sa najboljim kamerama.",
                "/",
                5000,
                0,
                Arrays.asList("Slika 1", "Slika 2", "Slika 3"),
                Arrays.asList("vencanje", "krstenje", "1 rodjendan"),
                true,
                true,
                Arrays.asList("Pera"),
                null,
                "1h",
                "5h",
                12,
                2,
                "ručno",
                "pending"
        );

        // Save the services to Firestore
        addServiceToFirestore(service1);
        addServiceToFirestore(service2);
        addServiceToFirestore(service3);
    }

    private void addServiceToFirestore(Service service) {
        db.collection("services")
                .add(service)
                .addOnSuccessListener(documentReference -> {
                    System.out.println("Service added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error adding service: " + e.getMessage());
                });
    }

    private Timestamp createTime(Date date, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return new Timestamp(calendar.getTime());
    }
}
