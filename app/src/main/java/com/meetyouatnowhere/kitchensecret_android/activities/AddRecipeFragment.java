package com.meetyouatnowhere.kitchensecret_android.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.meetyouatnowhere.kitchensecret_android.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddRecipeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
/*public class AddRecipeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
   /* public static AddRecipeFragment newInstance(String param1, String param2) {
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_recipe, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}*/

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PersonSpaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRecipeFragment extends Fragment implements View.OnClickListener {
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
    public static int KEY_ADD_DISH = 2;
//    private OnFragmentInteractionListener mListener;

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
        params.add("name", recipe_name);
        params.add("description", recipe_description);
        params.add("calorie","1");
        params.add("makeTime","2");
        params.add("peopleNum","3");
        params.add("steps","4");
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
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                if (statusCode == 201) {
                    try {
                        recipeBean = JsonTobean.getBean(RecipeBean.class, response.toString());
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("recipeBean", recipeBean);
                        intent.putExtras(bundle);
//                        setResult(KEY_ADD_DISH, intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Adding recipe is wrong. Please try it again.", Toast.LENGTH_SHORT).show();
                }
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
            case R.id.img_recipe_picture:
                break;
            default:
                break;
        }
    }
}
