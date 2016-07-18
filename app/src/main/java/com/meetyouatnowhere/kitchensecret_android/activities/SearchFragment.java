package com.meetyouatnowhere.kitchensecret_android.activities;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.util.Log;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.meetyouatnowhere.kitchensecret_android.util.SharedPreferencesUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.meetyouatnowhere.kitchensecret_android.R;

public class SearchFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String DISHES_DATA_PATH = "_recipe_data.bean";
    private List<RecipeBean> recipeList;
    private RecipeAdapter recipeAdapter;
    private ListView recipeListView;
    private SwipeRefreshLayout swipeLayout;

    private boolean isRefresh = false;
    private ProgressDialog progress;

    private Context mContext;

    private EditText search_et;
    private Button search_btn;
    private String search_content;
    private List<RecipeBean> searchRecipeList;
    private boolean isSearch = false;
    private int searchIconDefault; // default search icon
    private int searchIconClear; // clear search text icon

    private boolean beforeChangeHaveText = true;
    private boolean afterChangeHaveText = true;

    private boolean isLogin = false;
    private SharedPreferences sp;
    private String TAG="SearchFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public void onAttach(Context context) {
        Log.i(TAG,"onAttach");
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        Log.i(TAG,"onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG,"onResume");
        super.onResume();
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        Log.i(TAG,"onInflate");
        super.onInflate(context, attrs, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG,"onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG,"onCreateView");
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();

        searchIconDefault = R.mipmap.search_icon;
        searchIconClear = R.mipmap.search_delete;
        search_btn = (Button) getActivity().findViewById(R.id.search_button);
        search_et = (EditText) getActivity().findViewById(R.id.et_search);
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

        search_btn.setOnClickListener(this);

        swipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_recipe);
        swipeLayout.setOnRefreshListener(this);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        swipeLayout.setColorSchemeResources(android.R.color.holo_red_light,android.R.color.holo_green_light,android.R.color.holo_blue_bright,android.R.color.holo_orange_light);
        recipeListView = (ListView) getActivity().findViewById(R.id.history_lView);

        sp = getActivity().getSharedPreferences(GlobalParams.TAG_LOGIN_PREFERENCES, Context.MODE_PRIVATE);

        progress = new ProgressDialog(getActivity());
        progress.setMessage("Loading...");
        progress.show();

        getAllRecipees();
    }


    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.search_button:
                if (isSearch) {
                    search_content = search_et.getText().toString().trim();
                    if (search_content == null || "".equals(search_content)) {
                        Toast.makeText(getActivity(), "请输入搜索内容呀.", Toast.LENGTH_SHORT).show();
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
                recipeListView.setAdapter(recipeAdapter);
                recipeAdapter.notifyDataSetChanged();
            } else {
                // search result no recipe.
                search_et.setText("");
                search_et.requestFocus();
                search_btn.setBackgroundResource(searchIconDefault);
                search_content = "";
                Toast.makeText(getActivity(), "我们找不到要搜索的结果...再试一次吧.", Toast.LENGTH_LONG).show();
            }
        }
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
    }

    private void getLocalRecipeesData() {
        List<RecipeBean> localRecipeList = (List<RecipeBean>) ObjectPersistence.readObjectFromFile(mContext, DISHES_DATA_PATH);
        if (localRecipeList != null) {
            recipeList = localRecipeList;
        }
    }
}



    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

