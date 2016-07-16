package com.example.mykitchen.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ping on 2014/12/21.
 */
public class UserBean implements Serializable {

    private static final long serialVersionUID = -7060210544600464481L;

    private String _id;
    private String nickname;
    private String password;
    private boolean admin;
//    private String firstName;
//    private String lastName;
    private String sex; /* 'male', 'female' */
    private String email;
//    private String status; /* 'online', 'away', 'offline' */
//    private String phoneNumber;
    private String intro;
    private Date birth;
//    private String role; /* 'normal', 'foodSupplier', 'gastronomist' */
    private String photo;
//    private String[] problems;
//    private String[] friends;

    public void initUserBean(UserBean userBean) {
        this._id = userBean._id;
        this.nickname = userBean.nickname;
        this.password = userBean.password;
        this.admin = userBean.admin;
//        this.firstName = userBean.firstName;
//        this.lastName = userBean.lastName;
        this.sex = userBean.sex;
        this.email = userBean.email;
//        this.status = userBean.status;
//        this.phoneNumber = userBean.phoneNumber;
        this.intro = userBean.intro;
//        this.birth = userBean.birth;
//        this.role = userBean.role;
        this.photo = userBean.photo;
//        this.problems = userBean.problems;
//        this.friends = userBean.friends;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

//    public String[] getProblems() {
//        return problems;
//    }
//
//    public void setProblems(String[] problems) {
//        this.problems = problems;
//    }
//
//    public String[] getFriends() {
//        return friends;
//    }
//
//    public void setFriends(String[] friends) {
//        this.friends = friends;
//    }
}
