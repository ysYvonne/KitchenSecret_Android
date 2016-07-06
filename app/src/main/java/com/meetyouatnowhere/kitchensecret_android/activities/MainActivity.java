package com.meetyouatnowhere.kitchensecret_android.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.meetyouatnowhere.kitchensecret_android.R;

public class MainActivity extends AppCompatActivity {

    private Button btn_recipe;
    private Button btn_myCommunity;
    private Button btn_search;
    private Button btn_mySetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_recipe = (Button) findViewById(R.id.recipe_btn);
        btn_myCommunity = (Button) findViewById(R.id.myCommunity_btn);
        btn_search = (Button) findViewById(R.id.search_btn);
        btn_mySetting = (Button) findViewById(R.id.mySetting_btn);

       // FragmentManager fragmentManager = getSupportFragmentManager();
       // fragmentManager.beginTransaction().replace(R.id.contentFrame,new )

        btn_recipe.setOnClickListener(new View.OnClickListener(){

        });
    }


}
