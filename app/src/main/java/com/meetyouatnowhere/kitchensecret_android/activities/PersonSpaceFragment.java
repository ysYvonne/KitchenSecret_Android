package com.meetyouatnowhere.kitchensecret_android.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.meetyouatnowhere.kitchensecret_android.util.SharedPreferencesUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class PersonSpaceFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private static final String DISHES_DATA_PATH = "_recipe_data.bean";
    private List<RecipeBean> recipeList;
    private SwipeRefreshLayout swipeLayout;
    private ListView recipeListView;
    private RecipeAdapter recipeAdapter;
    private ProgressDialog progress;
    private boolean isRefresh = false;
    private Button addRecipe_btn;
    private EditText search_et;
    private Button search_btn;
    private String search_content;
    private List<RecipeBean> searchRecipeList;
    private boolean isSearch = false;
    private boolean beforeChangeHaveText = true;
    private boolean afterChangeHaveText = true;
    private int searchIconDefault; // default search icon
    private int searchIconClear; // clear search text icon
    private Context mContext;
    private boolean isLogin = false;
    private int request = 1;
    private RecipeBean recipeAddBean; // the recipe added by user
    private SharedPreferences sp;
    private int GET_DATA_SUCCESS=1;
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what==GET_DATA_SUCCESS){
                List<RecipeBean> recipeBeanList=(List<RecipeBean>) msg.obj;
                recipeAdapter = new RecipeAdapter(getActivity(), recipeBeanList);
                recipeListView.setAdapter(recipeAdapter);
                recipeAdapter.notifyDataSetChanged();
                return true;
            }
            return false;
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_person_space, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();

        addRecipe_btn = (Button) getActivity().findViewById(R.id.btn_add_recipe);
        searchIconDefault = R.mipmap.search_icon;
        searchIconClear = R.mipmap.search_delete;
        search_btn = (Button) getActivity().findViewById(R.id.btn_search);
        search_et = (EditText) getActivity().findViewById(R.id.et_search_text);
        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (search_et.getText() != null) {
                    beforeChangeHaveText = true;
                } else {
                    beforeChangeHaveText = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (search_et.getText().toString().trim() != null && !"".equals(search_et.getText().toString().trim())) {
                    afterChangeHaveText = true;
                } else {
                    isSearch = true;
                    search_content = "";
                    getSearchRecipeList();
                    afterChangeHaveText = false;
                }
                if (beforeChangeHaveText && afterChangeHaveText) {
                    isSearch = true;
                    search_btn.setBackgroundResource(searchIconDefault);
                }
            }
        });
        addRecipe_btn.setOnClickListener(this);
        search_btn.setOnClickListener(this);

        swipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_myrecipe);
        swipeLayout.setOnRefreshListener(this);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        swipeLayout.setColorSchemeResources(android.R.color.holo_red_light,android.R.color.holo_green_light,android.R.color.holo_blue_bright,android.R.color.holo_orange_light);
        recipeListView = (ListView) getActivity().findViewById(R.id.recipeListView);

        sp = getActivity().getSharedPreferences(GlobalParams.TAG_LOGIN_PREFERENCES, Context.MODE_PRIVATE);

        progress = new ProgressDialog(getActivity());
        progress.setMessage("Loading...");
        progress.show();

        getAllRecipees();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_recipe:
                //isLogin = MyApplication.getInstance().isLogin();
                isLogin = sp.getBoolean(SharedPreferencesUtil.TAG_IS_LOGIN, false);
                Log.e("isLogin", String.valueOf(isLogin));
                if (isLogin) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    //startActivity(intent);
                    startActivityForResult(intent, request);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    //startActivity(intent);
                    startActivityForResult(intent, request);
                }
                break;
            case R.id.btn_search:
                if (isSearch) {
                    search_content = search_et.getText().toString().trim();
                    if (search_content == null || "".equals(search_content)) {
                        Toast.makeText(getActivity(), "Please enter the search content.", Toast.LENGTH_SHORT).show();
                    } else {
                        isSearch = false;
                        search_content = search_content.replaceAll(" ", "");
                        getSearchRecipeList();
                        search_btn.setBackgroundResource(searchIconClear);
                    }
                } else {
                    search_et.setText("");
                    isSearch = true;
                    search_btn.setBackgroundResource(searchIconDefault);
                    search_content = "";
                    getSearchRecipeList();
                }
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(search_et.getWindowToken(), 0);
                break;
            default:
                break;
        }
    }

    public void getSearchRecipeList() {
        searchRecipeList = new ArrayList<>();
        if (recipeList != null && recipeList.size() > 0) {
            for (int i = 0; i < recipeList.size(); i++) {
                RecipeBean recipeBean = recipeList.get(i);
                if (recipeBean.getName().contains(search_content) || search_content.contains(recipeBean.getName())) {
                    searchRecipeList.add(recipeBean);
                }
            }
            if (searchRecipeList != null && searchRecipeList.size() > 0) {
                if (recipeAdapter.mRecipeList != null && recipeAdapter.mRecipeList.size() > 0) {
                    recipeAdapter.mRecipeList.clear();
                }
                recipeAdapter.mRecipeList.addAll(searchRecipeList);
                sendData(searchRecipeList);
//                recipeListView.setAdapter(recipeAdapter);
//                recipeAdapter.notifyDataSetChanged();
            } else {
                // search result no recipe.
                search_et.setText("");
                search_et.requestFocus();
                search_btn.setBackgroundResource(searchIconDefault);
                search_content = "";
                Toast.makeText(getActivity(), "No match result. Please try again.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendData(Object recipes){
        Message message=new Message();
        message.obj=recipes;
        message.what=GET_DATA_SUCCESS;
        handler.sendMessage(message);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == LoginActivity.KEY_IS_LOGIN) {
            isLogin = true;
        } else if (resultCode == AddRecipeFragment.KEY_ADD_DISH) {
            isRefresh = true;
            getAllRecipees();
        }
    }

    @Override
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
    }

    public void getAllRecipees() {
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
                        sendData(recipeList);
//                        recipeListView.setAdapter(recipeAdapter);
//                        recipeAdapter.notifyDataSetChanged();
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
                        sendData(recipeList);
//                        recipeListView.setAdapter(recipeAdapter);
//                        recipeAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(mContext, "Network kitchendemo is wrong.", Toast.LENGTH_SHORT).show();
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
                    sendData(recipeList);
//                    recipeListView.setAdapter(recipeAdapter);
//                    recipeAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Network kitchendemo is wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getLocalRecipeesData() {
        List<RecipeBean> localRecipeList = (List<RecipeBean>) ObjectPersistence.readObjectFromFile(mContext, DISHES_DATA_PATH);
        if (localRecipeList != null) {
            recipeList = localRecipeList;
        }
    }
}
