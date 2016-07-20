package com.meetyouatnowhere.kitchensecret_android.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.meetyouatnowhere.kitchensecret_android.MyApplication;
import com.meetyouatnowhere.kitchensecret_android.R;
import com.meetyouatnowhere.kitchensecret_android.bean.UserBean;
import com.meetyouatnowhere.kitchensecret_android.util.GlobalParams;
import com.meetyouatnowhere.kitchensecret_android.util.SharedPreferencesUtil;

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
    SharedPreferences sp;
    private int request = 1;
    /*private Button btn_recipe;
    private Button btn_myCommunity;
    private Button btn_search;
    private Button btn_mySetting;*/
private boolean isLogin;
  private  UserBean user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = getSharedPreferences(GlobalParams.TAG_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);


//       if(!MyApplication.getInstance().isLogin()){
//            startActivity(new Intent(MainActivity.this, LoginActivity.class));
//            finish();
//        }


        setContentView(R.layout.activity_main);
//        TextView nick=(TextView)findViewById(R.id.name_tv);
//        TextView email=(TextView)findViewById(R.id.email_tv);
//        TextView intro=(TextView)findViewById(R.id.description_tv);
        //Button btn=(Button)findViewById(R.id.btn);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.contentLayout, new MyRecipeFragment()).commit();

//        nick.setText(MyApplication.getInstance().getUserBean().getNickname());
//        email.setText(MyApplication.getInstance().getUserBean().getEmail());
//        intro.setText(MyApplication.getInstance().getUserBean().getIntro());
        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getInstance().clearUserBean();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });*/

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
        try{
           user=MyApplication.getInstance().getUserBeanFromFile();
            if(user.get_id()==null&&user.get_id()==""){
                isLogin=false;
                SharedPreferences.Editor editor=sp.edit();
                editor.putBoolean(SharedPreferencesUtil.TAG_IS_LOGIN, false);
                editor.apply();
            }}catch (Exception e){
                isLogin=false;
                SharedPreferences.Editor editor=sp.edit();
                editor.putBoolean(SharedPreferencesUtil.TAG_IS_LOGIN, false);
                editor.apply();
        }
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isLogin=sp.getBoolean(SharedPreferencesUtil.TAG_IS_LOGIN, false);
        Log.i("user",isLogin+"");
        if(isLogin){
            menu.getItem(0).setTitle(user.getNickname());
            menu.getItem(0).setEnabled(false);
            menu.getItem(3).setEnabled(true);
            menu.getItem(4).setEnabled(true);
        }else{
            menu.getItem(0).setTitle("登录");
            menu.getItem(0).setEnabled(true);
            menu.getItem(3).setEnabled(false);
            menu.getItem(4).setEnabled(false);
        }
        return super.onPrepareOptionsMenu(menu);
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
            MyApplication.getInstance().clearUserBean();
            isLogin=false;
            SharedPreferences.Editor editor=sp.edit();
            editor.putBoolean(SharedPreferencesUtil.TAG_IS_LOGIN, false);
            editor.apply();
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
