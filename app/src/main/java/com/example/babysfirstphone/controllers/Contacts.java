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
        return contactNumber.replaceAll(Constants.REGEX_CLEAN_PHONE_NO, "");
    }

    public void setNumber(String number) {
        this.contactNumber = number;
    }

    public int getImage() {
        if(contactImage != 0) {
            return contactImage;
        }
        else {
            return 2131231004;
        }
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
