package com.project.eelhea.vaportalk_android.DTO;

/**
 * Created by eelhea on 2017. 4. 24..
 */

public class UserContact {
    public String contact_name;
    public String contact_phonenum;

    public UserContact(){}

    public UserContact(String contact_name, String contact_phonenum){
        this.contact_name = contact_name;
        this.contact_phonenum = contact_phonenum;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_phonenum() {
        return contact_phonenum;
    }

    public void setContact_phonenum(String contact_phonenum) {
        this.contact_phonenum = contact_phonenum;
    }
}
