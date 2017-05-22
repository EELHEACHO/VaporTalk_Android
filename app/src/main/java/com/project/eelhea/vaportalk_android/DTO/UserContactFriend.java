package com.project.eelhea.vaportalk_android.DTO;

/**
 * Created by eelhea on 2017. 4. 24..
 */

public class UserContactFriend {
    public String friend_name;
    public String friend_email;
    public int friend_status;

    public UserContactFriend(){}

    public UserContactFriend(String friend_name, String friend_email, int friend_status){
        this.friend_name = friend_name;
        this.friend_email = friend_email;
        this.friend_status = friend_status;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public String getFriend_email() {
        return friend_email;
    }

    public void setFriend_email(String friend_email) {
        this.friend_email = friend_email;
    }

    public int getFriend_status() {
        return friend_status;
    }

    public void setFriend_status(int friend_status) {
        this.friend_status = friend_status;
    }
}
