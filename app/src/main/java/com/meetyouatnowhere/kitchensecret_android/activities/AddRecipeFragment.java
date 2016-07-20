package com.meetyouatnowhere.kitchensecret_android.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.meetyouatnowhere.kitchensecret_android.R;
import com.meetyouatnowhere.kitchensecret_android.activities.basic.PictureSelectFragment;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.meetyouatnowhere.kitchensecret_android.R;
import com.meetyouatnowhere.kitchensecret_android.bean.JsonTobean;
import com.meetyouatnowhere.kitchensecret_android.bean.RecipeBean;
import com.meetyouatnowhere.kitchensecret_android.util.GlobalParams;
import com.meetyouatnowhere.kitchensecret_android.util.KitchenRestClient;
import com.meetyouatnowhere.kitchensecret_android.util.SharedPreferencesUtil;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PersonSpaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRecipeFragment extends PictureSelectFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private EditText recipe_name_et;
    private EditText recipe_description_et;
    private Button recipe_add_btn;
    private ImageView recipe_picture;
    private String recipe_name;
    private String recipe_description;
    private SharedPreferences sp;
    private ProgressDialog progress;
    private RecipeBean recipeBean;
    private String picturePath = null;
    private Uri pictureUri=null;
    public static int KEY_ADD_DISH = 2;
//    private OnFragmentInteractionListener mListener;


    private static final String MULTIPART_FORM_DATA="multipart/form-data";
    private static final String TWOHYPHENS = "--";
    private static final String BOUNDARY = "---------------------------"+ UUID.randomUUID();
    private static final String LINEEND = "\r\n";
    private static final String FORMNAME="userfile";

    public AddRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonSpaceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddRecipeFragment newInstance(String param1, String param2) {
        AddRecipeFragment fragment = new AddRecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progress = new ProgressDialog(getActivity());
        sp = getActivity().getSharedPreferences(GlobalParams.TAG_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add_recipe, container, false);
        initView(view);
        return view;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_add_recipe;
    }

    @Override
    public void initViews(View view) {

    }

    @Override
    public void initEvents() {
        recipe_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture();
            }
        });
        // 设置裁剪图片结果监听
        setOnPictureSelectedListener(new OnPictureSelectedListener() {
            @Override
            public void onPictureSelected(Uri fileUri, Bitmap bitmap) {
                recipe_picture.setImageBitmap(bitmap);

                String filePath = fileUri.getEncodedPath();
                String imagePath = Uri.decode(filePath);
                picturePath=imagePath;
                Toast.makeText(mContext, "图片已经保存到:" + imagePath, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initView(View view) {
        recipe_name_et = (EditText) view.findViewById(R.id.recipeName_edit);
        recipe_description_et = (EditText) view.findViewById(R.id.recipeDescription_editText);
        recipe_add_btn = (Button) view.findViewById(R.id.okay_btn);
        recipe_picture = (ImageView) view.findViewById(R.id.img_recipe_picture);

        recipe_add_btn.setOnClickListener(this);
        recipe_picture.setOnClickListener(this);
    }

    /*public String encodeBase64File(String path){
        try {
            File file = new File(path);
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }*/

    public void addRecipe(String recipe_name, String recipe_description) {
        RequestParams params = new RequestParams();
        params.put("name", recipe_name);
        params.put("description", recipe_description);
        params.put("calorie","1");
        params.put("makeTime","2");
        params.put("peopleNum","3");
        params.put("steps","4");
        try{
            params.put("picture",new File(picturePath));
        }catch (Exception e){
            Toast.makeText(getActivity(),"请上传图片",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        /*if(picturePath != null && "".equals(picturePath.trim())){
            picturePath = encodeBase64File("nn");
            try {
                params.add("picture", URLEncoder.encode(picturePath, "UTF-8"));
            }catch (Exception e){
                e.printStackTrace();
            }
        }*/

        String username = sp.getString(SharedPreferencesUtil.TAG_USER_NAME, "");
        String password = sp.getString(SharedPreferencesUtil.TAG_PASSWORD, "");

        KitchenRestClient.postWithLogin("recipe", params, username, password, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("add recipe", response.toString());
                Toast.makeText(getActivity(), "上传成功！", Toast.LENGTH_SHORT).show();
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                if (statusCode == 201) {
                    try {
                        recipeBean = JsonTobean.getBean(RecipeBean.class, response.toString());
                        Intent intent = new Intent(getActivity(),RecipeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("RecipeBean", recipeBean);
                        intent.putExtras(bundle);
//                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Adding recipe is wrong. Please try it again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                Log.e("upload",responseString);
                Toast.makeText(getActivity(), "Adding recipe is wrong. Please try it again.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                Toast.makeText(getActivity(), "Adding recipe is wrong. Please try it again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okay_btn:
                recipe_name = recipe_name_et.getText().toString().trim();
                recipe_description = recipe_description_et.getText().toString().trim();
                if (recipe_name == null || "".equals(recipe_name)) {
                    recipe_name_et.requestFocus();
                    Toast.makeText(getActivity(), "Please enter the recipe name.", Toast.LENGTH_SHORT).show();
                } else if (recipe_description == null || "".equals(recipe_description)) {
                    recipe_description_et.requestFocus();
                    Toast.makeText((getActivity()), "Please enter the recipe description.", Toast.LENGTH_SHORT).show();
                } else {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(recipe_name_et.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(recipe_description_et.getWindowToken(), 0);
                    progress.setMessage("Add recipe...");
                    progress.show();
                    addRecipe(recipe_name, recipe_description);
                }
                break;
//            case R.id.img_recipe_picture:
//                selectPicture();
//                break;
            default:
                break;
        }
    }
}
