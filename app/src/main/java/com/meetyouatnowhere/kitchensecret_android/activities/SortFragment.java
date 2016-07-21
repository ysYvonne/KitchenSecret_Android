package com.meetyouatnowhere.kitchensecret_android.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SortFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SortFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button okay_btn;//完成btn
    private ImageButton entree;//主菜1,1
    private ImageButton desert;//甜点1,2
    private ImageButton drink;//饮料1,3
    private ImageButton snack;//小吃2,1
    private ImageButton western;//西餐2,2
    private ImageButton seafood;//海鲜2,3
    private ImageButton difficult;
    private ImageButton medium;
    private ImageButton easy;
    private ImageButton vegetatian;
    private ImageButton sugarFree;
    private ImageButton lowFat;
    private ImageButton spicy;
    private ImageButton milkSugarFree;
    private ImageButton lowCalorie;

    private int sort = -1;//分类选项
    private String[] sortArray = {"","","","","",""};
    private int difficulty = -1;//难易程度
    private String[] difficultyArray = {"","",""};
    private String[] preferenceArray = {"","","","","",""};//饮食偏好
    private int preference = 0;//个人喜好

    private static final String DISHES_DATA_PATH = "_recipe_data.bean";
    private List<RecipeBean> recipeList;
    public static RecipeAdapter recipeAdapter;

    private boolean isRefresh = false;
    private ProgressDialog progress;

    private Context mContext;
    private List<RecipeBean> searchRecipeList;
    private boolean isSearch = false;
    private int searchIconDefault; // default search icon
    private int searchIconClear; // clear search text icon

    private boolean beforeChangeHaveText = true;
    private boolean afterChangeHaveText = true;

    private boolean isLogin = false;
    private SharedPreferences sp;
    private String TAG="SearchFragment";



//    private OnFragmentInteractionListener mListener;

    public SortFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SortFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SortFragment newInstance(String param1, String param2) {
        SortFragment fragment = new SortFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mContext = getActivity();
        getAllRecipees();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sort, container,false);
        okay_btn = (Button)view.findViewById(R.id.okay_btn);
        entree = (ImageButton)view.findViewById(R.id.entree_btn);
        desert = (ImageButton)view.findViewById(R.id.desert_btn);
        drink = (ImageButton)view.findViewById(R.id.drink_btn);
        snack = (ImageButton)view.findViewById(R.id.snack_btn);
        western = (ImageButton)view.findViewById(R.id.western_btn);
        seafood = (ImageButton)view.findViewById(R.id.seafood_btn);

        difficult = (ImageButton)view.findViewById(R.id.difficult_btn);
        medium = (ImageButton)view.findViewById(R.id.medium_btn);
        easy = (ImageButton)view.findViewById(R.id.easy_btn);

        vegetatian = (ImageButton)view.findViewById(R.id.vegetaian_btn);
        sugarFree = (ImageButton)view.findViewById(R.id.sugarFree_btn);
        lowFat = (ImageButton)view.findViewById(R.id.lowFat_btn);
        spicy= (ImageButton)view.findViewById(R.id.spicy_btn);
        milkSugarFree = (ImageButton)view.findViewById(R.id.milkSugarFree_btn);
        lowCalorie = (ImageButton)view.findViewById(R.id.lowCalorie_btn);




        okay_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (sort == -1 && difficulty == -1 && preference == -1) {
                    Toast.makeText(getActivity(), "请选择要筛选的类型呀.", Toast.LENGTH_SHORT).show();
                } else {
                    /*Intent intent = new Intent(getActivity(),SortResultActivity.class);
                    intent.putExtra("sort",sort);
                    intent.putExtra("difficuty",difficulty);
                    intent.putExtra("preference",preference);
                    intent.putExtra("sortArray",sortArray);
                    intent.putExtra("difficutyArray",difficultyArray);
                    intent.putExtra("preferenceArray",preferenceArray);
                    startActivity(intent);*/

                    getSearchRecipeList();
                }

            }
        });

        entree.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                if (sort != 0) {
                    entree.setBackground(getResources().getDrawable(R.mipmap.entree_grey));
                    switch(sort) {
                        case 1:
                            desert.setBackground(getResources().getDrawable(R.mipmap.desert));
                            break;
                        case 2:
                            drink.setBackground(getResources().getDrawable(R.mipmap.drink));
                            break;
                        case 3:
                            snack.setBackground(getResources().getDrawable(R.mipmap.snack));
                            break;
                        case 4:
                            western.setBackground(getResources().getDrawable(R.mipmap.western));
                            break;
                        case 5:
                            seafood.setBackground(getResources().getDrawable(R.mipmap.seafood));
                            break;
                        default:
                            break;
                    }
                    sort = 0;
                    sortArray[sort] = "主菜";
                } else{
                    entree.setBackground(getResources().getDrawable(R.mipmap.entree));
                    sort = -1;
                    sortArray[0] = "";
                }
                /*if (!entreeFlag) {
                    entree.setBackground(getResources().getDrawable(R.mipmap.entree_grey));
                    entreeFlag = true;
                } else {
                    entree.setBackground(getResources().getDrawable(R.mipmap.entree));
                    entreeFlag = false;
                }*/
            }
        });

        desert.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (sort != 1) {
                    desert.setBackground(getResources().getDrawable(R.mipmap.desert_grey));
                    switch(sort) {
                        case 0:
                            entree.setBackground(getResources().getDrawable(R.mipmap.entree));
                            break;
                        case 2:
                            drink.setBackground(getResources().getDrawable(R.mipmap.drink));
                            break;
                        case 3:
                            snack.setBackground(getResources().getDrawable(R.mipmap.snack));
                            break;
                        case 4:
                            western.setBackground(getResources().getDrawable(R.mipmap.western));
                            break;
                        case 5:
                            seafood.setBackground(getResources().getDrawable(R.mipmap.seafood));
                            break;
                        default:
                            break;
                    }
                    sort = 1;
                    sortArray[sort] = "甜点";
                } else {
                    desert.setBackground(getResources().getDrawable(R.mipmap.desert));
                    sort = -1;
                    sortArray[1] = "";
                }
            }
        });

        drink.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (sort != 2){
                    drink.setBackground(getResources().getDrawable(R.mipmap.drink_grey));
                    switch(sort) {
                        case 0:
                            entree.setBackground(getResources().getDrawable(R.mipmap.entree));
                            break;
                        case 1:
                            desert.setBackground(getResources().getDrawable(R.mipmap.desert));
                            break;
                        case 3:
                            snack.setBackground(getResources().getDrawable(R.mipmap.snack));
                            break;
                        case 4:
                            western.setBackground(getResources().getDrawable(R.mipmap.western));
                            break;
                        case 5:
                            seafood.setBackground(getResources().getDrawable(R.mipmap.seafood));
                            break;
                        default:
                            break;
                    }
                    sort = 2;
                    sortArray[sort] = "饮料";
                } else {
                    drink.setBackground(getResources().getDrawable(R.mipmap.drink));
                    sort = -1;
                    sortArray[2] = "";
                }
            }
        });

        snack.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (sort != 3) {
                    snack.setBackground(getResources().getDrawable(R.mipmap.snack_grey));
                    switch(sort) {
                        case 0:
                            entree.setBackground(getResources().getDrawable(R.mipmap.entree));
                            break;
                        case 1:
                            desert.setBackground(getResources().getDrawable(R.mipmap.desert));
                            break;
                        case 2:
                            drink.setBackground(getResources().getDrawable(R.mipmap.drink));
                            break;
                        case 4:
                            western.setBackground(getResources().getDrawable(R.mipmap.western));
                            break;
                        case 5:
                            seafood.setBackground(getResources().getDrawable(R.mipmap.seafood));
                            break;
                        default:
                            break;
                    }
                    sort = 3;
                    sortArray[sort] = "小吃";
                } else {
                    snack.setBackground(getResources().getDrawable(R.mipmap.snack));
                    sort = -1;
                    sortArray[3] = "";
                }
            }
        });

        western.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (sort != 4) {
                    western.setBackground(getResources().getDrawable(R.mipmap.western_grey));
                    switch(sort) {
                        case 0:
                            entree.setBackground(getResources().getDrawable(R.mipmap.entree));
                            break;
                        case 1:
                            desert.setBackground(getResources().getDrawable(R.mipmap.desert));
                            break;
                        case 2:
                            drink.setBackground(getResources().getDrawable(R.mipmap.drink));
                            break;
                        case 3:
                            snack.setBackground(getResources().getDrawable(R.mipmap.snack));
                            break;
                        case 5:
                            seafood.setBackground(getResources().getDrawable(R.mipmap.seafood));
                            break;
                        default:
                            break;
                    }
                    sort = 4;
                    sortArray[sort] = "西餐";
                } else {
                    western.setBackground(getResources().getDrawable(R.mipmap.western));
                    sort = -1;
                    sortArray[4] = "";
                }
            }
        });

        seafood.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (sort != 5) {
                    seafood.setBackground(getResources().getDrawable(R.mipmap.seafood_grey));
                    switch(sort) {
                        case 0:
                            entree.setBackground(getResources().getDrawable(R.mipmap.entree));
                            break;
                        case 1:
                            desert.setBackground(getResources().getDrawable(R.mipmap.desert));
                            break;
                        case 2:
                            drink.setBackground(getResources().getDrawable(R.mipmap.drink));
                            break;
                        case 3:
                            snack.setBackground(getResources().getDrawable(R.mipmap.snack));
                            break;
                        case 4:
                            western.setBackground(getResources().getDrawable(R.mipmap.western));
                            break;
                        default:
                            break;
                    }
                    sort = 5;
                    sortArray[sort] = "海鲜";
                } else {
                    seafood.setBackground(getResources().getDrawable(R.mipmap.seafood));
                    sort = -1;
                    sortArray[5] = "";
                }
            }
        });

        difficult.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (difficulty != 0) {
                    difficult.setBackground(getResources().getDrawable(R.mipmap.difficult_grey));
                    switch(difficulty) {
                        case 1:
                            medium.setBackground(getResources().getDrawable(R.mipmap.medium));
                            break;
                        case 2:
                            easy.setBackground(getResources().getDrawable(R.mipmap.easy));
                            break;
                        default:
                            break;
                    }
                    difficulty = 0;
                    difficultyArray[difficulty] = "困难";
                } else {
                    difficult.setBackground(getResources().getDrawable(R.mipmap.difficult));
                    difficulty = -1;
                    difficultyArray[0] = "";
                }
            }
        });

        medium.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (difficulty != 1) {
                    medium.setBackground(getResources().getDrawable(R.mipmap.medium_grey));
                    switch(difficulty) {
                        case 0:
                            difficult.setBackground(getResources().getDrawable(R.mipmap.difficult));
                            break;
                        case 2:
                            easy.setBackground(getResources().getDrawable(R.mipmap.easy));
                            break;
                        default:
                            break;
                    }
                    difficulty = 1;
                    difficultyArray[difficulty] = "中等";
                } else {
                    medium.setBackground(getResources().getDrawable(R.mipmap.medium));
                    difficulty = -1;
                    difficultyArray[1] = "";
                }
            }
        });

        easy.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (difficulty != 2) {
                    easy.setBackground(getResources().getDrawable(R.mipmap.easy_grey));
                    switch(difficulty) {
                        case 0:
                            difficult.setBackground(getResources().getDrawable(R.mipmap.difficult));
                            break;
                        case 1:
                            medium.setBackground(getResources().getDrawable(R.mipmap.medium));
                            break;
                        default:
                            break;
                    }
                    difficulty = 2;
                    difficultyArray[difficulty] = "简单";
                } else {
                    easy.setBackground(getResources().getDrawable(R.mipmap.easy));
                    difficulty = -1;
                    difficultyArray[2] = "";
                }
            }
        });

        vegetatian.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (preferenceArray[0] == "") {
                    vegetatian.setBackground(getResources().getDrawable(R.mipmap.vegetarian_grey));
                    preferenceArray[0] = "noMeat";
                    preference++;
                } else {
                    vegetatian.setBackground(getResources().getDrawable(R.mipmap.vegetarian));
                    preferenceArray[0] = "";
                    preference--;
                }
            }
        });

        sugarFree.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (preferenceArray[1] == "") {
                    sugarFree.setBackground(getResources().getDrawable(R.mipmap.sugarfree_grey));
                    preferenceArray[1] = "noSugar";
                    preference++;
                } else {
                    sugarFree.setBackground(getResources().getDrawable(R.mipmap.sugarfree));
                    preferenceArray[1] = "";
                    preference--;
                }
            }
        });

        lowCalorie.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (preferenceArray[2] == "") {
                    lowCalorie.setBackground(getResources().getDrawable(R.mipmap.lowcalorie_grey));
                    preferenceArray[2] = "lowCal";
                    preference++;
                } else {
                    lowCalorie.setBackground(getResources().getDrawable(R.mipmap.lowcalorie));
                    preferenceArray[2] = "";
                    preference--;
                }
            }
        });

        lowFat.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (preferenceArray[3] == "") {
                    lowFat.setBackground(getResources().getDrawable(R.mipmap.lowfat_grey));
                    preferenceArray[3] = "lowFat";
                    preference++;
                } else {
                    lowFat.setBackground(getResources().getDrawable(R.mipmap.lowfat));
                    preferenceArray[3] = "";
                    preference--;
                }
            }
        });

        spicy.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (preferenceArray[4] == "") {
                    spicy.setBackground(getResources().getDrawable(R.mipmap.spicy_grey));
                    preferenceArray[4] = "spicy";
                    preference++;
                } else {
                    spicy.setBackground(getResources().getDrawable(R.mipmap.spicy));
                    preferenceArray[4] = "";
                    preference--;
                }
            }
        });

        milkSugarFree.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (preferenceArray[5] == "") {
                    milkSugarFree.setBackground(getResources().getDrawable(R.mipmap.milksugarfree_grey));
                    preferenceArray[5] = "noLactose";
                    preference++;
                } else {
                    milkSugarFree.setBackground(getResources().getDrawable(R.mipmap.milksugarfree));
                    preferenceArray[5] = "";
                    preference--;
                }
            }
        });

        return view;
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



    public void getAllRecipees() {
        KitchenRestClient.get("recipe", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.e("recipe", response.toString());
                /*if (progress.isShowing()) {
                    progress.dismiss();
                }*/
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
                        //recipeListView.setAdapter(recipeAdapter);
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
                        //recipeListView.setAdapter(recipeAdapter);
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
                    //recipeListView.setAdapter(recipeAdapter);
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

    public void getSearchRecipeList() {
        searchRecipeList = new ArrayList<>();
        if (recipeList != null && recipeList.size() > 0) {
            for (int i = 0; i < recipeList.size(); i++) {
                RecipeBean recipeBean = recipeList.get(i);

                if ((sort!=-1 && difficulty != -1)){
                    if(recipeBean.getLabels().equals(sortArray[sort])){
                        if(recipeBean.getLevel().equals(difficultyArray[difficulty])){
                            if(preference == 0)
                                searchRecipeList.add(recipeBean);
                            else{//如果选择饮食偏好
                                if(preferenceArray[0] != "" && recipeBean.getNoMeat()
                                        || preferenceArray[1] != "" && recipeBean.getNoSugar()
                                        ||preferenceArray[2] != "" && recipeBean.getLowCal()
                                        ||preferenceArray[3] != "" && recipeBean.getLowFat()
                                        ||preferenceArray[4]!= "" && recipeBean.getSpicy()
                                        ||preferenceArray[5]!=""&& recipeBean.getLowLactose()){
                                    searchRecipeList.add(recipeBean);
                                }
                            }
                        }
                    }
                }else if((sort!=-1 && difficulty == -1) && (recipeBean.getLabels().equals(sortArray[sort]))) {
                    if(preference == 0)
                        searchRecipeList.add(recipeBean);
                    else{//如果选择饮食偏好
                        if(preferenceArray[0] != "" && recipeBean.getNoMeat()
                                || preferenceArray[1] != "" && recipeBean.getNoSugar()
                                ||preferenceArray[2] != "" && recipeBean.getLowCal()
                                ||preferenceArray[3] != "" && recipeBean.getLowFat()
                                ||preferenceArray[4]!= "" && recipeBean.getSpicy()
                                ||preferenceArray[5]!=""&& recipeBean.getLowLactose()){
                            searchRecipeList.add(recipeBean);
                        }
                    }
                }else if((difficulty!=-1 && sort == -1) && (recipeBean.getLevel().equals(difficultyArray[difficulty]))) {
                    if(preference == 0)
                        searchRecipeList.add(recipeBean);
                    else{//如果选择饮食偏好
                        if(preferenceArray[0] != "" && recipeBean.getNoMeat()
                                || preferenceArray[1] != "" && recipeBean.getNoSugar()
                                ||preferenceArray[2] != "" && recipeBean.getLowCal()
                                ||preferenceArray[3] != "" && recipeBean.getLowFat()
                                ||preferenceArray[4]!= "" && recipeBean.getSpicy()
                                ||preferenceArray[5]!=""&& recipeBean.getLowLactose()){
                            searchRecipeList.add(recipeBean);
                        }
                    }
                }

            }
            if (searchRecipeList != null && searchRecipeList.size() > 0) {
                if (recipeAdapter.mRecipeList != null && recipeAdapter.mRecipeList.size() > 0) {
                    recipeAdapter.mRecipeList.clear();
                }
                // Toast.makeText(this.getActivity(), "啊啊啊啊啊啊啊啊啊啊啊啊啊啊", Toast.LENGTH_LONG).show();
                recipeAdapter.mRecipeList.addAll(searchRecipeList);

                Intent intent = new Intent(getActivity(),SortResultActivity.class);
                //Bundle bundle = new Bundle();
                // bundle.putSerializable("Adapter",recipeAdapter);
                //intent.putExtras(bundle);
                startActivity(intent);


                //recipeListView.setAdapter(recipeAdapter);
                // recipeAdapter.notifyDataSetChanged();
            } else {
                // search result no recipe.
                Toast.makeText(this.getActivity(), "我们找不到要搜索的结果...再试一次吧.", Toast.LENGTH_LONG).show();
            }
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


}




//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
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
