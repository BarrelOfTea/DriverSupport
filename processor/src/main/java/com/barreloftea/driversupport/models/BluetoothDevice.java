package com.barreloftea.driversupport.models;

public class BluetoothDevice implements Device{

    public BluetoothDevice(){

    }

    public BluetoothDevice(String name, int ID, boolean isConnected) {
        this.name = name;
        this.ID = ID;
        this.isConnected = isConnected;
    }

    String name;
    int ID;
    boolean isConnected;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }
}
