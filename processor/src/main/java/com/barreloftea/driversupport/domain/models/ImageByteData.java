package com.barreloftea.driversupport.domain.models;

public class ImageByteData {

    byte[] bytes;
    int width;
    int height;
    int rotationDegrees;
    int format;

    public ImageByteData(byte[] bytes, int width, int height, int rotationDegrees, int format) {
        this.bytes = bytes;
        this.width = width;
        this.height = height;
        this.rotationDegrees = rotationDegrees;
        this.format = format;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getRotationDegrees() {
        return rotationDegrees;
    }

    public int getFormat() {
        return format;
    }
}
