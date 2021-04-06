package com.example.babysfirstphone.controllers;

import java.io.Serializable;

/**
 * This class binds our ContactDataActivity with controllers/Contacts
 * to handle user data.
 */
public class Contacts implements Serializable {

    private String contactName;
    private String contactNumber;
    private String contactType;
    private int contactImage;

    public Contacts(String name, String number, int imageId, String type) {
        this.contactName = name;
        this.contactNumber = number;
        this.contactImage = imageId;
        this.contactType = type;
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

    public int getImage() {
        return contactImage;
    }

    public void setImageId(int imageId) {
        this.contactImage = imageId;
    }

    public String getType() {
        return contactType;
    }

    public void setContactType(String type) {
        this.contactType = type;
    }
}
