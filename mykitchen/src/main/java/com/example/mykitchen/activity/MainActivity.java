package com.example.mykitchen.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mykitchen.MyApplication;
import com.example.mykitchen.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(!MyApplication.getInstance().isLogin()){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        setContentView(R.layout.activity_main);
        TextView nick=(TextView)findViewById(R.id.nickname);
        TextView email=(TextView)findViewById(R.id.email);
        TextView intro=(TextView)findViewById(R.id.intro);
        Button btn=(Button)findViewById(R.id.button);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentFrame, new RecipeFragment()).commit();

        nick.setText(MyApplication.getInstance().getUserBean().getNickname());
        email.setText(MyApplication.getInstance().getUserBean().getEmail());
        intro.setText(MyApplication.getInstance().getUserBean().getIntro());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getInstance().clearUserBean();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}
