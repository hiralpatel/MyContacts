package com.hpandro.contacts.model;

import android.net.Uri;


/**
 * Created by hpAndro on 12/8/2016.
 */
public class ContactData {

    public Uri photoUri;
    public String name;
    public String phonenum;
    public boolean isSent;

    public ContactData(Uri addressPhotoUri, String addressName, String addressNum, boolean addressisSent) {
        photoUri = addressPhotoUri;
        name = addressName;
        phonenum = addressNum;
        isSent = addressisSent;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

}