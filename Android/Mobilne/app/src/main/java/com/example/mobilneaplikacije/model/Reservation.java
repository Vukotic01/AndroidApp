package com.example.mobilneaplikacije.model;

public class Reservation {

    public enum ResStatus{
            APPROVED,
            NEW,
            REALIZED,
            REJECTED_BY_ORGANIZER,
            REJECTED_BY_ADMIN,

            REJECTED
        }


        private String id;

        private Product product;

        private EventPackage pack;


        private EventOrganizer eventOrganizer;

        private String dateToCancle;

        private ResStatus status;


    public EventPackage getPack() {
        return pack;
    }

    public void setPack(EventPackage pack) {
        this.pack = pack;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Reservation(String id, Product product, EventPackage pack, EventOrganizer eventOrganizer, String dateToCancle, ResStatus status) {
        this.id = id;
        this.product = product;
        this.pack = pack;
        this.eventOrganizer = eventOrganizer;
        this.dateToCancle = dateToCancle;
        this.status = status;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ResStatus getStatus() {
        return status;
    }

    public void setStatus(ResStatus status) {
        this.status = status;
    }

    public Reservation() {
    }

    public EventOrganizer getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(EventOrganizer eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }

    public String getDateToCancle() {
        return dateToCancle;
    }

    public void setDateToCancle(String dateToCancle) {
        this.dateToCancle = dateToCancle;
    }
}
