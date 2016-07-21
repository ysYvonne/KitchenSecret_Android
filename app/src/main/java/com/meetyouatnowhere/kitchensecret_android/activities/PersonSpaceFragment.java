package com.meetyouatnowhere.kitchensecret_android.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.meetyouatnowhere.kitchensecret_android.MyApplication;
import com.meetyouatnowhere.kitchensecret_android.R;
import com.meetyouatnowhere.kitchensecret_android.activities.adapter.RecipeAdapter;
import com.meetyouatnowhere.kitchensecret_android.bean.JsonTobean;
import com.meetyouatnowhere.kitchensecret_android.bean.RecipeBean;
import com.meetyouatnowhere.kitchensecret_android.bean.UserBean;
import com.meetyouatnowhere.kitchensecret_android.util.GlobalParams;
import com.meetyouatnowhere.kitchensecret_android.util.ImageFetcher;
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
    private TextView name,birth,sex,email,desp;
    private ImageView img;
    ImageFetcher mImageFetcher;
    private List<RecipeBean> myRecipeList;
    private SwipeRefreshLayout swipeLayout;
    private ListView recipeListView;
    private RecipeAdapter myRecipeAdapter;
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
    private int GET_DATA_SUCCESS=2;
    private String userid;
    private String username;
    private String password;
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what==GET_DATA_SUCCESS){
                List<RecipeBean> recipeBeens= (List<RecipeBean>) msg.obj;
                myRecipeAdapter = new RecipeAdapter(getActivity(), false);
                myRecipeAdapter.mRecipeList.addAll(recipeBeens);
                recipeListView.setAdapter(myRecipeAdapter);
                myRecipeAdapter.notifyDataSetChanged();
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
        View view=inflater.inflate(R.layout.fragment_person_space, container, false);
        sp = getActivity().getSharedPreferences(GlobalParams.TAG_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        isLogin = sp.getBoolean(SharedPreferencesUtil.TAG_IS_LOGIN, false);
        Log.e("isLogin", String.valueOf(isLogin));
        img=(ImageView)view.findViewById(R.id.photo_img);
        mImageFetcher = new ImageFetcher(this.getActivity(), 140);
        mImageFetcher.setLoadingImage(R.mipmap.default_avatar);
        if (isLogin) {
            name=(TextView)view.findViewById(R.id.name_tv);
            birth=(TextView)view.findViewById(R.id.birth_tv);
            sex=(TextView)view.findViewById(R.id.sex_tv);
            email=(TextView)view.findViewById(R.id.email_tv);
            desp=(TextView)view.findViewById(R.id.description_tv);
            try{
                UserBean user=MyApplication.getInstance().getUserBeanFromFile();
                if(user.get_id()==null&&user.get_id()==""){
                    isLogin=false;
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putBoolean(SharedPreferencesUtil.TAG_IS_LOGIN, false);
                    editor.apply();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                userid=user.get_id();
                username=user.getEmail();
                password=user.getPassword();
                name.setText(user.getNickname());
//                birth.setText(user.getBirth().toString());
                sex.setText(user.getSex());
                email.setText(user.getEmail());
                desp.setText(user.getIntro());
                mImageFetcher.setLoadingImage(R.mipmap.default_avatar2);
                mImageFetcher.loadImage("http://120.27.95.40:8000/"+user.getPhoto(),img);
            }catch (Exception e){
                isLogin=false;
                SharedPreferences.Editor editor=sp.edit();
                editor.putBoolean(SharedPreferencesUtil.TAG_IS_LOGIN, false);
                editor.apply();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                e.printStackTrace();
            }
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
//            startActivityForResult(intent, request);
        }
        return view;
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
        myRecipeAdapter =new RecipeAdapter(getActivity(),false);
        recipeListView.setAdapter(myRecipeAdapter);

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
        if (myRecipeList != null && myRecipeList.size() > 0) {
            for (int i = 0; i < myRecipeList.size(); i++) {
                RecipeBean recipeBean = myRecipeList.get(i);
                if (recipeBean.getName().contains(search_content) || search_content.contains(recipeBean.getName())) {
                    searchRecipeList.add(recipeBean);
                }
            }
            if (searchRecipeList != null && searchRecipeList.size() > 0) {
                if (myRecipeAdapter.mRecipeList != null && myRecipeAdapter.mRecipeList.size() > 0) {
                    myRecipeAdapter.mRecipeList.clear();
                }
                myRecipeAdapter.mRecipeList.addAll(searchRecipeList);
                sendData(searchRecipeList);
//                recipeListView.setAdapter(myRecipeAdapter);
//                myRecipeAdapter.notifyDataSetChanged();
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
        KitchenRestClient.getWithLogin("getownrecipe/"+userid,null, username,password, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.e("recipe", response.toString());
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                if (statusCode == 200) {
                    try {
                        myRecipeList = JsonTobean.getList(RecipeBean[].class, response.toString());
                        Collections.reverse(myRecipeList);
                        ObjectPersistence.writeObjectToFile(mContext, myRecipeList, DISHES_DATA_PATH);
                        if (isRefresh) {
                            //myRecipeAdapter = new RecipeAdapter(getActivity(), myRecipeList);
                            if (myRecipeAdapter.mRecipeList != null && myRecipeAdapter.mRecipeList.size() > 0) {
                                myRecipeAdapter.mRecipeList.clear();
                            }
//                            myRecipeAdapter.mRecipeList.addAll(myRecipeList);
                            isRefresh = false;
                        } else {
                            myRecipeAdapter = new RecipeAdapter(mContext, false);
                            myRecipeAdapter.mRecipeList.addAll(myRecipeList);
                        }
                        sendData(myRecipeList);
//                        recipeListView.setAdapter(myRecipeAdapter);
//                        myRecipeAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    getLocalRecipeesData();
                    if (myRecipeList != null && myRecipeList.size() > 0) {
                        if (isRefresh) {
                            if (myRecipeAdapter.mRecipeList != null && myRecipeAdapter.mRecipeList.size() > 0) {
                                myRecipeAdapter.mRecipeList.clear();
                            }
                            myRecipeAdapter.mRecipeList.addAll(myRecipeList);
                            isRefresh = false;
                        } else {
                            myRecipeAdapter = new RecipeAdapter(mContext, false);
                            myRecipeAdapter.mRecipeList.addAll(myRecipeList);
                        }
                        sendData(myRecipeList);
//                        recipeListView.setAdapter(myRecipeAdapter);
//                        myRecipeAdapter.notifyDataSetChanged();
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
                if (myRecipeList != null && myRecipeList.size() > 0) {
                    if (isRefresh) {
                        if (myRecipeAdapter.mRecipeList != null && myRecipeAdapter.mRecipeList.size() > 0) {
                            myRecipeAdapter.mRecipeList.clear();
                        }
                        myRecipeAdapter.mRecipeList.addAll(myRecipeList);
                        isRefresh = false;
                    } else {
                        myRecipeAdapter = new RecipeAdapter(mContext, false);
                        myRecipeAdapter.mRecipeList.addAll(myRecipeList);
                    }
                    sendData(myRecipeList);
//                    recipeListView.setAdapter(myRecipeAdapter);
//                    myRecipeAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Network kitchendemo is wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getLocalRecipeesData() {
        List<RecipeBean> localRecipeList = (List<RecipeBean>) ObjectPersistence.readObjectFromFile(mContext, DISHES_DATA_PATH);
        if (localRecipeList != null) {
            myRecipeList = localRecipeList;
        }
    }
}
