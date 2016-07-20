package com.meetyouatnowhere.kitchensecret_android.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.meetyouatnowhere.kitchensecret_android.R;
import com.meetyouatnowhere.kitchensecret_android.activities.adapter.RecipeAdapter;
import com.meetyouatnowhere.kitchensecret_android.bean.JsonTobean;
import com.meetyouatnowhere.kitchensecret_android.bean.RecipeBean;
import com.meetyouatnowhere.kitchensecret_android.util.GlobalParams;
import com.meetyouatnowhere.kitchensecret_android.util.KitchenRestClient;
import com.meetyouatnowhere.kitchensecret_android.util.ObjectPersistence;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortResultActivity extends AppCompatActivity{

    private boolean isLogin = false;
    private boolean isRefresh = false;
    private SharedPreferences sp;
    private String TAG="SortResultFragment";
    private ProgressDialog progress;
    private static final String DISHES_DATA_PATH = "_recipe_data.bean";
    private List<RecipeBean> recipeList;
    private RecipeAdapter recipeAdapter;
    private ListView recipeListView;
    private SwipeRefreshLayout swipeLayout;

    private Context mContext;
    private List<RecipeBean> searchRecipeList;
    private boolean isSearch = false;

    private int sort;
    private String[] sortArray;
    private int difficuty;
    private String[]difficutyArray;
    private int preference;
    private String[]preferenceArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_result);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        recipeAdapter=(RecipeAdapter)intent.getSerializableExtra("Adapter");

        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_sort);
        swipeLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        swipeLayout.setColorSchemeResources(android.R.color.holo_red_light,android.R.color.holo_green_light,android.R.color.holo_blue_bright,android.R.color.holo_orange_light);

        recipeListView = (ListView)findViewById(R.id.sort_lView);

        sp = getSharedPreferences(GlobalParams.TAG_LOGIN_PREFERENCES, Context.MODE_PRIVATE);

        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.show();


        recipeListView.setAdapter(recipeAdapter);
        recipeAdapter.notifyDataSetChanged();

        //InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(search_et.getWindowToken(), 0);
        //getSearchRecipeList();
    }

   /* @Override
    public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    swipeLayout.setRefreshing(false);
                    getAllRecipees();
                }
            }, 3000);
        }
    }*/

   /* public void getSearchRecipeList() {
        searchRecipeList = new ArrayList<>();
        if (recipeList != null && recipeList.size() > 0) {
            for (int i = 0; i < recipeList.size(); i++) {
                RecipeBean recipeBean = recipeList.get(i);
                if ((sort!=-1 && difficuty != -1 && preference == 0) && (recipeBean.getLabels() == sortArray[sort])
                        &&(recipeBean.getLevel() == difficutyArray[difficuty])) {
                    searchRecipeList.add(recipeBean);
                }else if((sort!=-1 && difficuty == -1 && preference == 0) && (recipeBean.getLabels() == sortArray[sort])) {
                    searchRecipeList.add(recipeBean);
                }else if((difficuty!=-1 && sort == -1 && preference == 0) && (recipeBean.getLevel() == difficutyArray[difficuty])) {
                    searchRecipeList.add(recipeBean);
                }

               /*if(preference != 0){
                        if(preferenceArray[0] != "" && recipeBean.)

                    }*/

                //searchRecipeList.add(recipeBean);
               // }
/*}
            if (searchRecipeList != null && searchRecipeList.size() > 0) {
                if (recipeAdapter.mRecipeList != null && recipeAdapter.mRecipeList.size() > 0) {
                    recipeAdapter.mRecipeList.clear();
                }
                Toast.makeText(this, "啊啊啊啊啊啊啊啊啊啊啊啊啊啊", Toast.LENGTH_LONG).show();
                recipeAdapter.mRecipeList.addAll(searchRecipeList);
                recipeListView.setAdapter(recipeAdapter);
                recipeAdapter.notifyDataSetChanged();
            } else {
                // search result no recipe.
                Toast.makeText(this, "我们找不到要搜索的结果...再试一次吧.", Toast.LENGTH_LONG).show();
                this.finish();
            }
        }
    }
*/
   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == LoginActivity.KEY_IS_LOGIN) {
            isLogin = true;
        } else if (resultCode == AddRecipeFragment.KEY_ADD_DISH) {
            isRefresh = true;
            getAllRecipees();
        }
    }*/

   /* @Override
    public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    swipeLayout.setRefreshing(false);
                    getAllRecipees();
                }
            }, 3000);
        }
    }*/

   /* public void getAllRecipees() {
        KitchenRestClient.get("recipe", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.e("recipe", response.toString());
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                if (statusCode == 200) {
                    try {
                        recipeList = JsonTobean.getList(RecipeBean[].class, response.toString());
                        Collections.reverse(recipeList);
                        ObjectPersistence.writeObjectToFile(mContext, recipeList, DISHES_DATA_PATH);
                        if (isRefresh) {
                            //recipeAdapter = new RecipeAdapter(getActivity(), recipeList);
                            if (recipeAdapter.mRecipeList != null && recipeAdapter.mRecipeList.size() > 0) {
                                recipeAdapter.mRecipeList.clear();
                            }
                            recipeAdapter.mRecipeList.addAll(recipeList);
                            isRefresh = false;
                        } else {
                            recipeAdapter = new RecipeAdapter(mContext, false);
                            recipeAdapter.mRecipeList.addAll(recipeList);
                        }
                        recipeListView.setAdapter(recipeAdapter);
                        recipeAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    getLocalRecipeesData();
                    if (recipeList != null && recipeList.size() > 0) {
                        if (isRefresh) {
                            if (recipeAdapter.mRecipeList != null && recipeAdapter.mRecipeList.size() > 0) {
                                recipeAdapter.mRecipeList.clear();
                            }
                            recipeAdapter.mRecipeList.addAll(recipeList);
                            isRefresh = false;
                        } else {
                            recipeAdapter = new RecipeAdapter(mContext, false);
                            recipeAdapter.mRecipeList.addAll(recipeList);
                        }
                        recipeListView.setAdapter(recipeAdapter);
                        recipeAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(mContext, "网络挂掉了嘤嘤", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                getLocalRecipeesData();
                if (recipeList != null && recipeList.size() > 0) {
                    if (isRefresh) {
                        if (recipeAdapter.mRecipeList != null && recipeAdapter.mRecipeList.size() > 0) {
                            recipeAdapter.mRecipeList.clear();
                        }
                        recipeAdapter.mRecipeList.addAll(recipeList);
                        isRefresh = false;
                    } else {
                        recipeAdapter = new RecipeAdapter(mContext, false);
                        recipeAdapter.mRecipeList.addAll(recipeList);
                    }
                    recipeListView.setAdapter(recipeAdapter);
                    recipeAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "网络挂掉了嘤嘤嘤", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/

   /* private void getLocalRecipeesData() {
        List<RecipeBean> localRecipeList = (List<RecipeBean>) ObjectPersistence.readObjectFromFile(mContext, DISHES_DATA_PATH);
        if (localRecipeList != null) {
            recipeList = localRecipeList;
        }
    }*/
}
