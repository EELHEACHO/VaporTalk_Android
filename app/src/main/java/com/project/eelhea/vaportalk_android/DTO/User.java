package com.project.eelhea.vaportalk_android.DTO;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eelhea on 2017. 4. 4..
 */

public class User {
    public String uid;
    public String email;
    public String name;
    public String sex;
    public String birthday;
    public String tel;
    public String isCommerceAgree;
    public String isNearAgree;
    public String profileImage;

    public User(){}

    public User(String uid, String email, String name, String sex, String birthday, String tel, String isCommerceAgree, String isNearAgree, String profileImage) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.tel = tel;
        this.isCommerceAgree = isCommerceAgree;
        this.isNearAgree = isNearAgree;
        this.profileImage = profileImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getIsCommerceAgree() {
        return isCommerceAgree;
    }

    public void setIsCommerceAgree(String isCommerceAgree) {
        this.isCommerceAgree = isCommerceAgree;
    }

    public String getIsNearAgree() {
        return isNearAgree;
    }

    public void setIsNearAgree(String isNearAgree) {
        this.isNearAgree = isNearAgree;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Map<String, String> toMap(){
        HashMap<String, String> userResult = new HashMap<>();
        userResult.put("name", name);
        userResult.put("email", email);
        userResult.put("sex", sex);
        userResult.put("birthday", birthday);
        userResult.put("tel", tel);
        userResult.put("isCommerceAgree", isCommerceAgree);
        userResult.put("isNearAgree", isNearAgree);
        userResult.put("profileImage", profileImage);

        return userResult;
    }
}
