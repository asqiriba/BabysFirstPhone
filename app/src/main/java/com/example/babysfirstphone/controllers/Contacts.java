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
    private String contactImage;

    public Contacts(String name, String number, String imagePath, String type) {
        this.contactName = name;
        this.contactNumber = number;
        this.contactImage = imagePath;
        this.contactType = type;
    }

    public String getName() {
        return contactName;
    }

    public void setName(String name) {
        this.contactName = name;
    }

    public String getNumber() {
        return contactNumber.replaceAll(Constants.REGEX_CLEAN_PHONE_NO, "");
    }

    public String getNumberFormat() { return contactNumber; }

    public void setNumber(String number) {
        this.contactNumber = number;
    }

    public String getImage() {
        if(contactImage != null) {
            return contactImage;
        }
        else {
            return Constants.DEFAULT_IMG;
        }
    }

    public void setImageId(String imageId) {
        this.contactImage = imageId;
    }

    public String getType() {
        return contactType;
    }

    public void setContactType(String type) {
        this.contactType = type;
    }
}
