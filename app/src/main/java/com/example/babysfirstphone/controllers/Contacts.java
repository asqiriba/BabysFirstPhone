package com.example.babysfirstphone.controllers;

import java.io.Serializable;

class Contacts implements Serializable{

    private String contactName;
    private String contactNumber;
    private int contactImageId;

    public Contacts(String name, String number, int imageId) {
        this.contactName = name;
        this.contactNumber = number;
        this.contactImageId = imageId;
    }

    public String getName() {
        return contactName;
    }

    public void setName(String name) {
        this.contactName = name;
    }

    public String getNumber() {
        return contactNumber;
    }

    public void setNumber(String number) {
        this.contactNumber = number;
    }

    public int getImageId() {
        return contactImageId;
    }

    public void setImageId(int imageId) {
        this.contactImageId = imageId;
    }
}
