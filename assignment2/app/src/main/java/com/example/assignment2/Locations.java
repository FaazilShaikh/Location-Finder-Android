package com.example.assignment2;

public class Locations{
    private int id;
    private String address;
    private String longitude;
    private String latitude;


    public Locations( int id, String address, String latitude,String longitude) {
        this.id = id;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }



}

