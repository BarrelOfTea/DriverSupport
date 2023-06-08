package com.barreloftea.driversupport.domain.models;

public class BluetoothDeviceM implements Device{

    public BluetoothDeviceM(){

    }

    public BluetoothDeviceM(String name, String address, boolean isSaved, String type) {
        this.name = name;
        this.address = address;
        this.isSaved = isSaved;
        this.type = type;
    }

    String name;
    String address;
    boolean isSaved;

    String type;

    @Override
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean isSaved() {
        return isSaved;
    }

    @Override
    public String getType() {
        return type;
    }
}
