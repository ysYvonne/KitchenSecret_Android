package com.meetyouatnowhere.kitchensecret_android;

import android.app.Application;
import android.content.Context;

import com.meetyouatnowhere.kitchensecret_android.activities.ActivityStack;
import com.meetyouatnowhere.kitchensecret_android.activities.LoginActivity;
import com.meetyouatnowhere.kitchensecret_android.bean.UserBean;
import com.meetyouatnowhere.kitchensecret_android.util.ObjectPersistence;

public class MyApplication extends Application {

    /** 上下文 */
    protected Context mContext          = null;
    /** Activity 栈 */
    public ActivityStack mActivityStack = null;

    public static String token;

    private static MyApplication instance;
    public boolean islogin = false;
    private UserBean userBean = new UserBean();
    ;

    public static MyApplication getInstance() {
        return instance;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean.initUserBean(userBean);
    }

    public boolean isLogin() {
        return islogin;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext=getApplicationContext();
        mActivityStack=new ActivityStack();
        //SDKInitializer.initialize(this);
    }

    public void updateOrSaveUserBean(UserBean userBean) {
        this.userBean.initUserBean(userBean);
        ObjectPersistence.writeObjectToFile(getBaseContext(), this.userBean, LoginActivity.TAG_USER_ACCOUNT);
    }

    public UserBean getUserBeanFromFile() {
        return (UserBean) ObjectPersistence.readObjectFromFile(getBaseContext(), LoginActivity.TAG_USER_ACCOUNT);
    }

    public void clearUserBean() {
        ObjectPersistence.writeObjectToFile(getBaseContext(), null, LoginActivity.TAG_USER_ACCOUNT);
    }
}
