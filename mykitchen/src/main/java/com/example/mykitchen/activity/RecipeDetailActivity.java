package com.example.mykitchen.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mykitchen.MyApplication;
import com.example.mykitchen.R;
import com.example.mykitchen.bean.CommentBean;
import com.example.mykitchen.bean.JsonTobean;
import com.example.mykitchen.bean.LikeBean;
import com.example.mykitchen.bean.RecipeBean;
import com.example.mykitchen.bean.UserBean;
import com.example.mykitchen.util.AsynImageLoader;
import com.example.mykitchen.util.GlobalParams;
import com.example.mykitchen.util.KitchenRestClient;
import com.example.mykitchen.util.ObjectPersistence;
import com.example.mykitchen.util.SharedPreferencesUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by heyi on 2016/7/14.
 */
public class RecipeDetailActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String DISH_LIKES_DATA_PATH = "_recipe_likes_data.bean";
    private static final String DISH_COMMENTS_DATA_PATH = "_recipe_comments_data.bean";
    private RecipeBean recipeBean;
    private AsynImageLoader asynImageLoader;
    private Button back_btn;
    private ImageView recipe_picture_img, recipe_favor_img;
    private LinearLayout recipe_favor_ll, recipe_comment_ll;
    private TextView recipe_name_tv, recipe_description_tv, recipe_ingredient_tv, recipe_step_tv;
    private TextView favor_num_tv, comment_num_tv;
    private List<LikeBean> likeList;
    private List<CommentBean> commentList;
    //    private String pictureBaseUrl = "http://5.196.19.84:1337/";
    private String pictureBaseUrl = "http://120.27.95.40:8000/";
    private SharedPreferences sp;
    private boolean isLogin = false;
    private boolean isLike = false;
    private int favor_num = 0;
    private UserBean userBean;
    private LikeBean likeBean;
    private int request = 5;

    // add recipe pay ll
//    private RelativeLayout recipe_pay_rl;
//    private TextView recipe_count_tv, recipe_price_tv;
//    private Button recipe_add_btn, recipe_remove_btn, recipe_pay_btn;
    private int recipe_count = 1;
    private int recipe_price = 0, total_price = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Intent intent = getIntent();
        recipeBean = (RecipeBean) intent.getSerializableExtra("RecipeBean");

        sp = getSharedPreferences(GlobalParams.TAG_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        isLogin = sp.getBoolean(SharedPreferencesUtil.TAG_IS_LOGIN, false);
        userBean = MyApplication.getInstance().getUserBeanFromFile();

        initView();
        initData();
    }

    public void initView() {
        recipe_name_tv = (TextView) this.findViewById(R.id.tv_recipe_name);
        recipe_description_tv = (TextView) this.findViewById(R.id.tv_recipe_description);
        recipe_ingredient_tv = (TextView) this.findViewById(R.id.tv_recipe_ingredient);
        recipe_step_tv = (TextView) this.findViewById(R.id.tv_recipe_step);
        recipe_picture_img = (ImageView) this.findViewById(R.id.img_recipe_picture);
        recipe_favor_img = (ImageView) this.findViewById(R.id.img_recipe_favor);
        recipe_favor_ll = (LinearLayout) this.findViewById(R.id.ll_recipe_favor);
        recipe_comment_ll = (LinearLayout) this.findViewById(R.id.ll_recipe_comment);
        favor_num_tv = (TextView) this.findViewById(R.id.tv_favor_num);
        comment_num_tv = (TextView) this.findViewById(R.id.tv_comment_num);
        back_btn = (Button) this.findViewById(R.id.btn_back);

//        recipe_pay_rl = (RelativeLayout) this.findViewById(R.id.recipe_pay_rl);
//        recipe_count_tv = (TextView) this.findViewById(R.id.tv_recipe_pay_count);
//        recipe_price_tv = (TextView) this.findViewById(R.id.tv_recipe_price);
//        recipe_add_btn = (Button) this.findViewById(R.id.btn_recipe_pay_add);
//        recipe_remove_btn = (Button) this.findViewById(R.id.btn_recipe_pay_remove);
//        recipe_pay_btn = (Button) this.findViewById(R.id.btn_recipe_pay);
//        recipe_add_btn.setOnClickListener(this);
//        recipe_remove_btn.setOnClickListener(this);
//        recipe_pay_btn.setOnClickListener(this);

        back_btn.setOnClickListener(this);
        recipe_favor_ll.setOnClickListener(this);
        recipe_comment_ll.setOnClickListener(this);
    }

    public void initData() {
        recipe_name_tv.setText(recipeBean.getName());
        recipe_description_tv.setText(recipeBean.getDescription());

        asynImageLoader = new AsynImageLoader();
        if (recipeBean.getPhoto() == null || "".equals(recipeBean.getPhoto().trim()) || "null".equals(recipeBean.getPhoto().trim())) {
            recipe_picture_img.setImageResource(R.mipmap.default_recipe_picture);
        } else {
            asynImageLoader.showImageAsyn(recipe_picture_img, pictureBaseUrl + recipeBean.getPhoto(), R.mipmap.default_recipe_picture);
        }

        recipe_ingredient_tv.setText(recipeBean.getMeterials()[0].toString());
        recipe_step_tv.setText(recipeBean.getSteps()[0].toString());
//        if(recipeBean.getRestaurant()==null || "".equals(recipeBean.getRestaurant().trim()) || "null".equals(recipeBean.getRestaurant().trim())){
//            recipe_pay_rl.setVisibility(View.GONE);
//        }else{
//            recipe_pay_rl.setVisibility(View.VISIBLE);
//        }

//        recipe_price = recipeBean.getPrice();
//        recipe_price_tv.setText("Price: " + String.valueOf(recipe_price));

//        getLikesFromRecipe();
    }

    public void getLikesFromRecipe() {
        String url = "getlikes/" + recipeBean.get_id();
        KitchenRestClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.e("getLikesFromRecipe", response.toString());
                if (statusCode == 200) {
                    try {
                        likeList = JsonTobean.getList(LikeBean[].class, response.toString());
                        ObjectPersistence.writeObjectToFile(RecipeDetailActivity.this, likeList, recipeBean.get_id() + DISH_LIKES_DATA_PATH);
                        if (likeList != null && likeList.size() > 0) {
                            favor_num = likeList.size();
                            favor_num_tv.setText(favor_num + " Favor");
                            for (int i = 0; i < likeList.size(); i++) {
                                if ((likeList.get(i).getUser()).equals(userBean.get_id())) {
                                    isLike = true;
                                    recipe_favor_img.setImageResource(R.mipmap.favor_btn_highlight);
                                    likeBean = likeList.get(i);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    getLocalLikesData();
                    if (likeList != null && likeList.size() > 0) {
                        favor_num = likeList.size();
                        favor_num_tv.setText(favor_num + " Favor");
                        for (int i = 0; i < likeList.size(); i++) {
                            if ((likeList.get(i).getUser()).equals(userBean.get_id())) {
                                isLike = true;
                                recipe_favor_img.setImageResource(R.mipmap.favor_btn_highlight);
                                likeBean = likeList.get(i);
                            } else {
                                isLike = false;
                                recipe_favor_img.setImageResource(R.mipmap.favor_btn_default);
                            }
                        }
                    } else {
                        Toast.makeText(RecipeDetailActivity.this, "Network kitchendemo is wrong.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                getLocalLikesData();
                if (likeList != null && likeList.size() > 0) {
                    favor_num = likeList.size();
                    favor_num_tv.setText(favor_num + " Favor");
                    for (int i = 0; i < likeList.size(); i++) {
                        if ((likeList.get(i).getUser()).equals(userBean.get_id())) {
                            isLike = true;
                            recipe_favor_img.setImageResource(R.mipmap.favor_btn_highlight);
                            likeBean = likeList.get(i);
                        } else {
                            isLike = false;
                            recipe_favor_img.setImageResource(R.mipmap.favor_btn_default);
                        }
                    }
                } else {
                    Toast.makeText(RecipeDetailActivity.this, "Network kitchendemo is wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getLocalLikesData() {
        List<LikeBean> localLikeList = (List<LikeBean>) ObjectPersistence.readObjectFromFile(RecipeDetailActivity.this, recipeBean.get_id() + DISH_LIKES_DATA_PATH);
        if (localLikeList != null) {
            likeList = localLikeList;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_recipe_favor:
                if (isLogin) {
                    if (isLike) {
                        deleteLike();
                        isLike = false;
                        favor_num = favor_num - 1;
                        if (favor_num > 0) {
                            favor_num_tv.setText(favor_num + " Favor");
                        } else {
                            favor_num_tv.setText(" Favor");
                        }
                        recipe_favor_img.setImageResource(R.mipmap.favor_btn_default);
                        Toast.makeText(this, "don't like it.", Toast.LENGTH_SHORT).show();
                    } else {
                        postLike();
                        isLike = true;
                        favor_num = favor_num + 1;
                        favor_num_tv.setText(favor_num + " Favor");
                        recipe_favor_img.setImageResource(R.mipmap.favor_btn_highlight);
                        Toast.makeText(this, "like it.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(RecipeDetailActivity.this, LoginActivity.class);
                    startActivityForResult(intent, request);
                }
                break;
//            case R.id.ll_recipe_comment:
//                Intent intent = new Intent(RecipeDetailActivity.this, RecipeCommentActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("recipeBean", recipeBean);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                break;
//            case R.id.btn_recipe_pay_add:
//                recipe_count++;
//                recipe_count_tv.setText(String.valueOf(recipe_count));
//                total_price = recipe_count * recipe_price;
//                recipe_price_tv.setText("Price: " + String.valueOf(total_price));
//                break;
//            case R.id.btn_recipe_pay_remove:
//                if(recipe_count != 1){
//                    recipe_count--;
//                    recipe_count_tv.setText(String.valueOf(recipe_count));
//                    total_price = recipe_count * recipe_price;
//                    recipe_price_tv.setText("Price: " + String.valueOf(total_price));
//                }
//                break;
//            case R.id.btn_recipe_pay:
//                if(isLogin){
//                    total_price = recipe_count * recipe_price;
//                    recipeBean.setRecipe_count(recipe_count);
//                    recipeBean.setTotal_price(total_price);
//                    Intent intent2 = new Intent(RecipeDetailActivity.this, RecipePayActivity.class);
//                    Bundle bundle2 = new Bundle();
//                    bundle2.putSerializable("recipeBean", recipeBean);
//                    intent2.putExtras(bundle2);
//                    startActivity(intent2);
//                } else {
//                    Intent intent2 = new Intent(RecipeDetailActivity.this, LoginActivity.class);
//                    startActivityForResult(intent2, request);
//                }
//                break;
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }
    }

    public void postLike() {
        RequestParams params = new RequestParams();
        params.add("recipe", recipeBean.get_id());
        params.add("like", String.valueOf(isLike));

        String username = sp.getString(SharedPreferencesUtil.TAG_USER_NAME, "");
        String password = sp.getString(SharedPreferencesUtil.TAG_PASSWORD, "");

        KitchenRestClient.postWithLogin("like", params, username, password, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("post like", response.toString());
                if (statusCode == 201) {
                    try {
                        likeBean = JsonTobean.getBean(LikeBean.class, response.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //Toast.makeText(getApplicationContext(), "like it is wrong. Please try it.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteLike() {
        String username = sp.getString(SharedPreferencesUtil.TAG_USER_NAME, "");
        String password = sp.getString(SharedPreferencesUtil.TAG_PASSWORD, "");

        String url = "deletelike/" + likeBean.get_id();
        KitchenRestClient.deleteWithLogin(url, username, password, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("delete like", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == LoginActivity.KEY_IS_LOGIN) {
            isLogin = true;
        }
    }
}
