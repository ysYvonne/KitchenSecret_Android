package com.example.mykitchen.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mykitchen.R;
import com.example.mykitchen.activity.RecipeAddActivity;
import com.example.mykitchen.activity.RecipeDetailActivity;
import com.example.mykitchen.bean.RecipeBean;
import com.example.mykitchen.util.AsynImageLoader;
import com.example.mykitchen.util.GlobalParams;
import com.example.mykitchen.util.KitchenRestClient;
import com.example.mykitchen.util.SharedPreferencesUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heyi on 2016/7/14.
 */
public class RecipeAdapter extends BaseAdapter {

    public List<RecipeBean> mRecipeList = new ArrayList<RecipeBean>();
    private LayoutInflater mInflater;
    private Context mContext;
    private boolean isUpdate = false;
    private boolean isUserRecipe = false;
    private RecipeViewHolder rvh = null;
    //    private String pictureBaseUrl = "http://5.196.19.84:1337/";
    private String pictureBaseUrl = "http://120.27.95.40:8000/";

    public RecipeAdapter(Context context, List<RecipeBean> RecipeList) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mRecipeList = RecipeList;
    }

    public RecipeAdapter(Context context, boolean update) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        isUpdate = update;
    }

    public RecipeAdapter(Context context, boolean update, boolean userRecipe) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        isUpdate = update;
        isUserRecipe = userRecipe;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_recipe, null);
            rvh = new RecipeViewHolder();
            rvh.Recipe_item_rl = (LinearLayout) convertView.findViewById(R.id.recipe_item_ll);
            rvh.name = (TextView) convertView.findViewById(R.id.recipe_name_tv);
            rvh.description = (TextView) convertView.findViewById(R.id.recipe_description_tv);
            rvh.time = (TextView) convertView.findViewById(R.id.recipe_time_tv);
            rvh.picture = (ImageView) convertView.findViewById(R.id.recipe_picture_img);
            convertView.setTag(rvh);
        } else {
            rvh = (RecipeViewHolder) convertView.getTag();
        }

        final RecipeBean RecipeBean = (RecipeBean) mRecipeList.get(position);
        rvh.name.setText(RecipeBean.getName());
        rvh.description.setText(RecipeBean.getDescription());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = df.format(RecipeBean.getTime());
        rvh.time.setText(strDate);

        AsynImageLoader asynImageLoader = new AsynImageLoader();
        if (RecipeBean.getPhoto() == null || "".equals(RecipeBean.getPhoto().trim()) || "null".equals(RecipeBean.getPhoto().trim())) {
            rvh.picture.setImageResource(R.mipmap.default_recipe_picture);
        } else {
            asynImageLoader.showImageAsyn(rvh.picture, pictureBaseUrl + RecipeBean.getPhoto(), R.mipmap.default_recipe_picture);
        }

        rvh.Recipe_item_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("RecipeBean", RecipeBean);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        if (isUserRecipe) {
            rvh.Recipe_item_rl.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Confirm delete");
                    builder.setIcon(android.R.drawable.ic_dialog_info);
                    builder.setMessage("Are you sure to delete the Recipe ?");
                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteUserRecipe(RecipeBean);
                        }
                    });
                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.create().show();
                    return true;
                }
            });
        }

        return convertView;
    }

    public void deleteUserRecipe(final RecipeBean RecipeBean) {
        String url = "Recipees/" + RecipeBean.get_id();

        SharedPreferences sp = mContext.getSharedPreferences(GlobalParams.TAG_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        String username = sp.getString(SharedPreferencesUtil.TAG_USER_NAME, "");
        String password = sp.getString(SharedPreferencesUtil.TAG_PASSWORD, "");

        KitchenRestClient.deleteWithLogin(url, username, password, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("delete Recipe", response.toString());
                if (statusCode == 204) {
                    mRecipeList.remove(RecipeBean);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Deleting Recipe is wrong. Please try it again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(mContext, "Deleting Recipe is wrong. Please try it again.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(mContext, "Deleting Recipe is wrong. Please try it again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public Object getItem(int position) {
        return mRecipeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mRecipeList.size();
    }

    static class RecipeViewHolder {
        TextView name;
        TextView description;
        TextView time;
        ImageView picture;
        LinearLayout Recipe_item_rl;
    }
}
