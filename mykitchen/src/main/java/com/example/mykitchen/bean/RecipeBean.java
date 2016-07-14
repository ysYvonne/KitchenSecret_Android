package com.example.mykitchen.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by heyi on 2016/7/14.
 */
public class RecipeBean implements Serializable {

    private static final long serialVersionUID = -7060210544600464481L;

    private String _id;
    private String name;
    private String description;
    private Date time;
    private String photo;
    private String[] steps; /*  default: 'No recipeBean' */
    private String[] meterials;
    private String user;
    private int likeNum;
    private String labels;
    private String level;
    private int favorateNum;
    private String peopleNum;
    private String makeTime;
    private String calorie;
    public int getFavorateNum() {
        return favorateNum;
    }

    public void setFavorateNum(int favorateNum) {
        this.favorateNum = favorateNum;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String[] getSteps() {
        return steps;
    }

    public void setSteps(String[] recipe) {
        this.steps = recipe;
    }

    public String[] getMeterials() {
        return meterials;
    }

    public void setMeterials(String[] meterials) {
        this.meterials = meterials;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int price) {
        this.likeNum = likeNum;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(String peopleNum) {
        this.peopleNum = peopleNum;
    }

    public String getMakeTime() {
        return makeTime;
    }

    public void setMakeTime(String makeTime) {
        this.makeTime = makeTime;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }
}
