package com.meetyouatnowhere.kitchensecret_android.activities;

import android.app.Activity;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.meetyouatnowhere.kitchensecret_android.R;

public class LoginActivity extends Activity {

    public static final String TAG_USER_ACCOUNT = "user.s";
    public static int KEY_IS_LOGIN = 1;

    private EditText username_et;
    private EditText password_et;
    private Button login_btn;
    private Button back_btn;
    private String username;
    private String password;

    private ProgressDialog progress;

    private int request = 1;



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

      //  login_btn.setOnClickListener();
       // back_btn.setOnClickListener(this);
    }

 //   @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                checkLogin();
                Intent intent = new Intent(this, MainActivity.class);
                startActivityForResult(intent, request);
                break;
            default:
                break;
        }
    }

    public void setUserNameAndPassWord(String username, String password){
        this.username = username;
        this.password = password;

        login();
    }


    public void checkLogin() {
        username = username_et.getText().toString().trim();
        password = password_et.getText().toString().trim();
        if (username == null || "".equals(username)) {
            Toast.makeText(this, "请输入用户名嘿", Toast.LENGTH_SHORT).show();
            username_et.setFocusable(true);
        } else if (password == null || "".equals(password)) {
            Toast.makeText(this, "请输入密码嘿", Toast.LENGTH_SHORT).show();
            password_et.setFocusable(true);
        } else {
            progress = new ProgressDialog(this);
            progress.setMessage("正在登录......");
            progress.show();
            login();
        }
    }

    @SuppressLint("NewApi")
    public void login() {
        String str = username + ":" + password;
        String encodeStr = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
        String loginStr = "Basic " + encodeStr;

        /*连接服务器
        NourritureRestClient.addHeader(loginStr);
        NourritureRestClient.post("login", null, new JsonHttpResponseHandler() {
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
                        Toast.makeText(LoginActivity.this, "Login Success.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        setResult(KEY_IS_LOGIN, intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    username_et.setText(null);
                    password_et.setText(null);
                    username_et.requestFocus();
                    Toast.makeText(LoginActivity.this, "Username or Password is wrong.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginActivity.this, "Username or Password is wrong.", Toast.LENGTH_SHORT).show();
            }
        });
        */
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
