package com.meetyouatnowhere.kitchensecret_android.bean;

/**
 * Created by heyi on 2016/7/21.
 */
public class Material {
    private String name;
    private String amount;

    public Material(){
        name="";
        amount="";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
