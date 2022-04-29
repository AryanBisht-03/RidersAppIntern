package com.example.ridersappinternship;

public class MainUser {
    String name,url;
    Integer station_code;
    MainUser user;

    public MainUser(Integer station_code, String name, String url) {
        this.station_code = station_code;
        this.name = name;
        this.url = url;
    }

    public Integer getStation_code() {
        return station_code;
    }

    public void setStation_code(Integer station_code) {
        this.station_code = station_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
