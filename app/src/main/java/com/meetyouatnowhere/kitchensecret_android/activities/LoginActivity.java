package com.meetyouatnowhere.kitchensecret_android.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.meetyouatnowhere.kitchensecret_android.bean.JsonTobean;
import com.meetyouatnowhere.kitchensecret_android.MyApplication;
import com.meetyouatnowhere.kitchensecret_android.util.KitchenRestClient;
import com.meetyouatnowhere.kitchensecret_android.R;
import com.meetyouatnowhere.kitchensecret_android.util.SharedPreferencesUtil;
import com.meetyouatnowhere.kitchensecret_android.bean.UserBean;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.List;

/**
 * Created by ping on 2016/7/12.
 */
public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    public static final String TAG_USER_ACCOUNT = "user.s";
    public static int KEY_IS_LOGIN = 1;

    private EditText username_et;
    private EditText password_et;
    private Button login_btn;
    private Button back_btn;
    private String username;
    private String password;
    private UserBean userBean;
    private List<UserBean> userList;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    public void initView() {
        username_et = (EditText) this.findViewById(R.id.et_username);
        password_et = (EditText) this.findViewById(R.id.et_password);
        login_btn = (Button) this.findViewById(R.id.btn_login);
//        back_btn = (Button) this.findViewById(R.id.btn_back);
        login_btn.setOnClickListener(this);
//        back_btn.setOnClickListener(this);
    }

    public void checkLogin() {
        username = username_et.getText().toString().trim();
        password = password_et.getText().toString().trim();
        if (username == null || "".equals(username)) {
            Toast.makeText(this, "请输入用户名啦~", Toast.LENGTH_SHORT).show();
            username_et.setFocusable(true);
        } else if (password == null || "".equals(password)) {
            Toast.makeText(this, "请输入密码咯~", Toast.LENGTH_SHORT).show();
            password_et.setFocusable(true);
        } else {
            progress = new ProgressDialog(this);
            progress.setMessage("登录ing......");
            progress.show();
            login();
        }
    }

    @SuppressLint("NewApi")
    public void login() {
        String str = username + ":" + password;
        String encodeStr = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
        String loginStr = "Basic " + encodeStr;
        KitchenRestClient.addHeader(loginStr);
        KitchenRestClient.post("login", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.e("login", response.toString());
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                if (statusCode == 200) {
                    try {
                        userList = JsonTobean.getList(UserBean[].class, response.toString());
                        MyApplication.getInstance().updateOrSaveUserBean(userList.get(0));
                        MyApplication.getInstance().islogin = true;
                        SharedPreferencesUtil.saveLogin(getBaseContext(), username, password, true);
                        Toast.makeText(LoginActivity.this, "登录成功~", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this,PersonSpaceFragment.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getBaseContext().startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    username_et.setText(null);
                    password_et.setText(null);
                    username_et.requestFocus();
                    Toast.makeText(LoginActivity.this, "用户名或者密码有误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                username_et.setText(null);
                password_et.setText(null);
                username_et.requestFocus();
                Toast.makeText(LoginActivity.this, "用户名或者密码错误.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("NewApi")
    public void register(String nickname,String password,String birth,String sex,String username,String info) {
        RequestParams params = new RequestParams();
        params.add("nickname",nickname);
        KitchenRestClient.post("users", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                username_et.setText(null);
                password_et.setText(null);
                username_et.requestFocus();
                Toast.makeText(LoginActivity.this, "用户名或者密码错误.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                checkLogin();
                break;
//            case R.id.btn_back:
//                finish();
//                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
