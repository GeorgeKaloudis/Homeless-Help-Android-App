package com.example.homelessapp.DTO;

public class MarkersInformation {

    private String latitude,longitude,gender,locality,country,information;



    public MarkersInformation(String latitude, String longitude, String gender, String locality, String country, String information) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.gender = gender;
        this.locality = locality;
        this.country = country;
        this.information = information;
    }

    public MarkersInformation(){

    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getGender() {
        return gender;
    }

    public String getLocality() {
        return locality;
    }

    public String getCountry() {
        return country;
    }

    public String getInformation() {
        return information;
    }
}
