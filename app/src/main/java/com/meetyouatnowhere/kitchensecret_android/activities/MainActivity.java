package com.meetyouatnowhere.kitchensecret_android.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.meetyouatnowhere.kitchensecret_android.MyApplication;
import com.meetyouatnowhere.kitchensecret_android.R;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost mTabHost;
    private ViewPager mViewPager;
    private List<Fragment> mFragmentList;
    private Class mClass[] = {RecipeFragment.class,SortFragment.class,AddRecipeFragment.class,SearchFragment.class,PersonSpaceFragment.class};
    private Fragment mFragment[] = {new RecipeFragment(),new SortFragment(),new AddRecipeFragment(),new SearchFragment(),new PersonSpaceFragment()};
    private String mTitles[] = {"食谱","筛选","","搜搜","我的"};
    private int mImages[] = {
            R.drawable.tab_home,
            R.drawable.tab_report,
            R.mipmap.add_btn,
            R.drawable.tab_message,
            R.drawable.tab_mine
    };

    private int request = 1;
    /*private Button btn_recipe;
    private Button btn_myCommunity;
    private Button btn_search;
    private Button btn_mySetting;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* if(!MyApplication.getInstance().isLogin()){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }*/


        setContentView(R.layout.activity_main);

       /* btn_recipe = (Button) findViewById(R.id.recipe_btn);
        btn_myCommunity = (Button) findViewById(R.id.myCommunity_btn);
        btn_search = (Button) findViewById(R.id.search_btn);
        btn_mySetting = (Button) findViewById(R.id.mySetting_btn);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentFrame, new RecipeFragment()).commit();

        btn_recipe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentFrame, new RecipeFragment()).commit();
            }
        });
*/
        /////////////btn_clickListener

        init();

    }

    private void init(){
        initView();
        initEvent();
    }

    private void initView() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mFragmentList = new ArrayList<Fragment>();

        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);

        for (int i = 0;i < mClass.length;i++){
                TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTitles[i]).setIndicator(getTabView(i));
                mTabHost.addTab(tabSpec,mClass[i],null);
                mFragmentList.add(mFragment[i]);
                mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.WHITE);
        }

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });
    }

    private View getTabView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item, null);

        ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView title = (TextView) view.findViewById(R.id.title);

        image.setImageResource(mImages[index]);
        title.setText(mTitles[index]);

        return view;
    }

    private void initEvent() {

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                mViewPager.setCurrentItem(mTabHost.getCurrentTab());
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabHost.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        if (id == R.id.login_settings){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, request);
            return true;
        }else if(id == R.id.sign_settings) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivityForResult(intent, request);
            return true;
        }else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivityForResult(intent, request);
            return true;
        }else if (id == R.id.exit_settings){
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
