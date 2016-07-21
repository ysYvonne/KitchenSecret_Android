package com.meetyouatnowhere.kitchensecret_android.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.meetyouatnowhere.kitchensecret_android.R;
import com.meetyouatnowhere.kitchensecret_android.activities.basic.PictureSelectFragment;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.meetyouatnowhere.kitchensecret_android.bean.JsonTobean;
import com.meetyouatnowhere.kitchensecret_android.bean.Material;
import com.meetyouatnowhere.kitchensecret_android.bean.RecipeBean;
import com.meetyouatnowhere.kitchensecret_android.bean.Step;
import com.meetyouatnowhere.kitchensecret_android.util.GlobalParams;
import com.meetyouatnowhere.kitchensecret_android.util.KitchenRestClient;
import com.meetyouatnowhere.kitchensecret_android.util.SharedPreferencesUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PersonSpaceFragment#} factory method to
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
    private EditText recipe_calorie;
    private EditText recipe_maketime;
    private EditText recipe_peoplenum;
    private Button recipe_add_btn;
    private ImageView recipe_picture;
    private String recipe_name;
    private String recipe_description;
    private String calorie;
    private String maketime;
    private String peoplenum;
    private SharedPreferences sp;
    private ProgressDialog progress;
    private RecipeBean recipeBean;
    private String picturePath = null;
    private Uri pictureUri=null;
    public static int KEY_ADD_DISH = 2;
    private ListView materials;
    private ListView steps;
    private MatrialAddViewAdapter matrialAddViewAdapter;
    private StepAddViewAdapter stepAddViewAdapter;
private TextView stepcontent;
    //    private OnFragmentInteractionListener mListener;
    List<Material> materialList=new ArrayList<Material>();
    List<Step> stepList=new ArrayList<Step>();

    private static final String MULTIPART_FORM_DATA="multipart/form-data";
    private static final String TWOHYPHENS = "--";
    private static final String BOUNDARY = "---------------------------"+ UUID.randomUUID();
    private static final String LINEEND = "\r\n";
    private static final String FORMNAME="userfile";
    private int M =1;
    private int S=0;
    public AddRecipeFragment() {
        // Required empty public constructor
    }

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what== M){
                Log.i("list","remove material"+materialList.toString());
                matrialAddViewAdapter=new MatrialAddViewAdapter(getActivity());
                materials.setAdapter(matrialAddViewAdapter);
                matrialAddViewAdapter.notifyDataSetChanged();
//                stepcontent.setText(materitalFormatter(materialList).toString());
            }
            if(msg.what== S){
                Log.i("list","remove step"+stepList.toString());
                stepAddViewAdapter=new StepAddViewAdapter(getActivity());
                steps.setAdapter(stepAddViewAdapter);
                stepAddViewAdapter.notifyDataSetChanged();
//                stepcontent.setText(stepFormatter(stepList).toString());
            }
            return false;
        }
    });

    private JSONArray stepFormatter(List<Step> steps){
        try{
            JSONArray array=new JSONArray();
            for(int i=0;i<steps.size();i++){
                JSONObject json=new JSONObject();
                json.put("detail",steps.get(i).getDetail());
                array.put(json);
            }
            Log.i("formatter",array.toString());
            return array;

        }catch (Exception e){

        }
        return null;
    }

    private JSONArray materitalFormatter(List<Material> materials){
        try{
            JSONArray array=new JSONArray();
            for(int i=0;i<materials.size();i++){
                JSONObject json=new JSONObject();
                json.put("name",materials.get(i).getName());
                json.put("amount",materials.get(i).getAmount());
                array.put(json);
            }
            Log.i("formatter",array.toString());
            return array;

        }catch (Exception e){

        }
        return null;
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
        recipe_calorie=(EditText)view.findViewById(R.id.calorie_edit);
        recipe_maketime=(EditText)view.findViewById(R.id.time_edit);
        recipe_peoplenum=(EditText)view.findViewById(R.id.people_edit);
        recipe_add_btn = (Button) view.findViewById(R.id.okay_btn);
        recipe_picture = (ImageView) view.findViewById(R.id.img_recipe_picture);
        materials=(ListView)view.findViewById(R.id.materials);
        materialList.add(new Material());
        matrialAddViewAdapter=new MatrialAddViewAdapter(getActivity());
        materials.setAdapter(matrialAddViewAdapter);

        steps=(ListView)view.findViewById(R.id.steps);
        stepList.add(new Step());
        stepAddViewAdapter=new StepAddViewAdapter(getActivity());
        steps.setAdapter(stepAddViewAdapter);
        recipe_add_btn.setOnClickListener(this);
        recipe_picture.setOnClickListener(this);

        stepcontent=(TextView)view.findViewById(R.id.step_content);
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

    public void addRecipe(String recipe_name, String recipe_description,String calorie,String maketime,String peoplenum) {
        RequestParams params = new RequestParams();
        params.put("name", recipe_name);
        params.put("description", recipe_description);
        params.put("calorie",calorie);
        params.put("makeTime",maketime);
        params.put("peopleNum",peoplenum);
        params.put("steps",stepFormatter(stepList));
        params.put("meterials",materitalFormatter(materialList));
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
                calorie=recipe_calorie.getText().toString().trim();
                maketime=recipe_maketime.getText().toString().trim();
                peoplenum=recipe_peoplenum.getText().toString().trim();
                if (recipe_name == null || "".equals(recipe_name)) {
                    recipe_name_et.requestFocus();
                    Toast.makeText(getActivity(), "请输入菜谱名称.", Toast.LENGTH_SHORT).show();
                } else if (recipe_description == null || "".equals(recipe_description)) {
                    recipe_description_et.requestFocus();
                    Toast.makeText((getActivity()), "请输入菜谱描述.", Toast.LENGTH_SHORT).show();
                } else if (calorie == null || "".equals(calorie)) {
                    recipe_calorie.requestFocus();
                    Toast.makeText((getActivity()), "请输入卡路里.", Toast.LENGTH_SHORT).show();
                } else if (maketime == null || "".equals(maketime)) {
                    recipe_maketime.requestFocus();
                    Toast.makeText((getActivity()), "请输入制作耗时.", Toast.LENGTH_SHORT).show();
                } else if (peoplenum == null || "".equals(peoplenum)) {
                    recipe_peoplenum.requestFocus();
                    Toast.makeText((getActivity()), "请输入适宜人数.", Toast.LENGTH_SHORT).show();
                } else {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(recipe_name_et.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(recipe_description_et.getWindowToken(), 0);
                    progress.setMessage("上传菜谱中...");
                    progress.show();
                    addRecipe(recipe_name, recipe_description,calorie,maketime,peoplenum);
                }
                break;
//            case R.id.img_recipe_picture:
//                selectPicture();
//                break;
            default:
                break;
        }
    }

    public class MatrialAddViewAdapter extends BaseAdapter{
        private LayoutInflater mInflater;
        private Context mContext;
        private ViewHolder holder=null;
        public MatrialAddViewAdapter(Context context) {
            mInflater=LayoutInflater.from(context);
            mContext = context;
        }


        @Override
        public int getCount() {
            return materialList.size();
        }

        @Override
        public Object getItem(int position) {
            return materialList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
           if(convertView==null){
               LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
               convertView=layoutInflator.inflate(R.layout.item_add_ingrediant,null);
               holder=new ViewHolder();
               holder.btn_add=(Button)convertView.findViewById(R.id.add_ingrediant_btn);
               holder.btn_cancel=(Button)convertView.findViewById(R.id.cancel_ingrediant_btn);
               holder.name_add=(EditText)convertView.findViewById(R.id.add_name_text);
               holder.amount_add=(EditText)convertView.findViewById(R.id.add_amount_text);
               convertView.setTag(holder);
           }else{
               holder=(ViewHolder)convertView.getTag();
           }
            holder.name_add.setText(materialList.get(position).getName());
            holder.amount_add.setText(materialList.get(position).getAmount());
            holder.name_add.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    final String name=s.toString();
                    holder.amount_add.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            Material m=new Material();
                            m.setName(name);
                            m.setAmount(s.toString());
                            materialList.set(position,m);
                        }
                    });
                }
            });


            holder.btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Material m=new Material();
                    materialList.add(m);
                    Message message=new Message();
                    message.what= M;
                    handler.sendMessage(message);
                }
            });
            holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    materialList.remove(position);
                    Message message=new Message();
                    message.what= M;
                    handler.sendMessage(message);
                }
            });
            return convertView;
        }

        class ViewHolder {
            EditText name_add;
            EditText amount_add;
            Button btn_add;
            Button btn_cancel;
        }
    }

    public class StepAddViewAdapter extends BaseAdapter{
        private LayoutInflater mInflater;
        private Context mContext;
        private ViewHolder holder=null;

        public StepAddViewAdapter(Context context) {
            mInflater=LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public int getCount() {
            return stepList.size();
        }

        @Override
        public Object getItem(int position) {
            return stepList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
                convertView=layoutInflator.inflate(R.layout.item_add_step,null);
                holder=new ViewHolder();
                holder.btn_add=(Button)convertView.findViewById(R.id.add_step_btn);
                holder.btn_cancel=(Button)convertView.findViewById(R.id.cancel_step_btn);
                holder.step_text=(EditText)convertView.findViewById(R.id.step_text);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder)convertView.getTag();
            }
            holder.step_text.setText(stepList.get(position).getDetail());
            holder.step_text.setHint("第"+(position+1)+"步");
            holder.step_text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Step step=new Step();
                    step.setDetail(s.toString());
                    stepList.set(position,step);
                }
            });
            holder.btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Step step=new Step();
                    stepList.add(step);
                    Message message=new Message();
                    message.what= S;
                    handler.sendMessage(message);
                }
            });
            holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stepList.remove(position);
                    Message message=new Message();
                    message.what= S;
                    handler.sendMessage(message);
                }
            });
            return convertView;
        }

        class ViewHolder {
            EditText step_text;
            Button btn_add;
            Button btn_cancel;
        }
    }
}
