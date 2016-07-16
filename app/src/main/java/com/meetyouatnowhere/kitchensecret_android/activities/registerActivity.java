package com.meetyouatnowhere.kitchensecret_android.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.meetyouatnowhere.kitchensecret_android.R;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG_USER_ACCOUNT = "user.s";
    public static int KEY_IS_LOGIN = 1;

    private EditText mail_et;
    private EditText username_et;
    private EditText password_et;
    private EditText password_check_et;

    private Button sign_btn;
    private Button back_btn;
    private String mail;
    private String username;
    private String password;
    private String password_check;

    private ProgressDialog progress;

    private int request = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    public void initView() {
        mail_et = (EditText)this.findViewById(R.id.sign_et_email);
        username_et = (EditText) this.findViewById(R.id.sign_et_username);
        password_et = (EditText) this.findViewById(R.id.sign_et_password);
        password_check_et = (EditText)this.findViewById(R.id.sign_et_password_check) ;
        sign_btn = (Button) this.findViewById(R.id.btn_sign);

        //  login_btn.setOnClickListener();
        // back_btn.setOnClickListener(this);
    }

    //   @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign:
                checkSign();
                Intent intent = new Intent(this, MainActivity.class);
                startActivityForResult(intent, request);
                break;
            default:
                break;
        }
    }

    public void checkSign() {
        username = username_et.getText().toString().trim();
        password = password_et.getText().toString().trim();
        password_check = password_check_et.getText().toString().trim();
        if (username == null || "".equals(username)) {
            Toast.makeText(this, "请输入用户名嘿", Toast.LENGTH_SHORT).show();
            username_et.setFocusable(true);
        } else if (password == null || "".equals(password)) {
            Toast.makeText(this, "请输入密码嘿", Toast.LENGTH_SHORT).show();
            password_et.setFocusable(true);
        } else if(password != password_check){
            Toast.makeText(this, "密码和确认密码不一致呦", Toast.LENGTH_SHORT).show();
            password_et.setFocusable(true);
        }else{
           /*progress = new ProgressDialog(this);
            progress.setMessage("正在登录......");
            progress.show();
            login();*/

            Toast.makeText(this, "正在注册", Toast.LENGTH_SHORT).show();
        }
    }
}
