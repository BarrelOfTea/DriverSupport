package com.barreloftea.driversupport.domain.models;

public class WiFiDeviceM implements Device{
    public WiFiDeviceM() {
    }

    //TODO password and
    public WiFiDeviceM(String name, String rtsp_link, String username, String password, boolean isSaved, String type) {
        this.name = name;
        this.rtsp_link = rtsp_link;
        this.isSaved = isSaved;
        this.type = type;
        this.password = password;
        this.username = username;
    }

    String name;

    //TODO for now wifi address is gonna be rtsp link that will be saved
    String rtsp_link;

    String username;
    String password;
    boolean isSaved;
    String type;

    @Override
    public String getName() {
        return name;
    }

    public String getRtsp_link() {
        return rtsp_link;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean isSaved() {
        return isSaved;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
