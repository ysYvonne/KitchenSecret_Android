package com.meetyouatnowhere.kitchensecret_android.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.meetyouatnowhere.kitchensecret_android.R;


public class SettingActivity extends AppCompatActivity {

    private View setting_view;
    private View edit_view;
    private View contact_view;
    private View share_view;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();

       setting_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SettingActivity.this, "!!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingActivity.this,Setting_detailActivity.class);
                startActivity(intent);
            }
        });
        edit_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SettingActivity.this, "!!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingActivity.this,EditPersonalInfoActivity.class);
                startActivity(intent);
            }
        });


    }

    public void initView() {
        setting_view = (View) findViewById(R.id.setting_layout);
        edit_view = (View) findViewById(R.id.edit_info_layout);
        contact_view = (View) findViewById(R.id.contact_us_layout);
        share_view = (View) findViewById(R.id.share_layout);
        //  login_btn.setOnClickListener();
        // back_btn.setOnClickListener(this);
    }

    /*public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_layout:
                Toast.makeText(SettingActivity.this, "!!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingActivity.this,Setting_detailActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }*/

}
