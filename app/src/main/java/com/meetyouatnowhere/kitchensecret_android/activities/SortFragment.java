package com.meetyouatnowhere.kitchensecret_android.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.meetyouatnowhere.kitchensecret_android.R;

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

    private boolean entreeFlag = false;
    private boolean desertFlag = false;
    private boolean drinkFlag = false;
    private boolean sneakFlag = false;
    private boolean westernFlag = false;
    private boolean seafoodFlag = false;
    private boolean difficultFlag = false;
    private boolean mediumFlag = false;
    private boolean easyFlag = false;
    private boolean vegetationFlag = false;
    private boolean sugarFreeFlag = false;
    private boolean lowFatFlag = false;
    private boolean spicyFlag = false;
    private boolean milkSugarFreeFlag = false;
    private boolean lowCalorisFlag = false;


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

            }
        });


        entree.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                if (!entreeFlag) {
                    entree.setBackground(getResources().getDrawable(R.mipmap.entree_grey));
                    entreeFlag = true;
                } else {
                    entree.setBackground(getResources().getDrawable(R.mipmap.entree));
                    entreeFlag = false;
                }
            }
        });

        desert.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (!desertFlag) {
                    desert.setBackground(getResources().getDrawable(R.mipmap.desert_grey));
                    desertFlag = true;
                } else {
                    desert.setBackground(getResources().getDrawable(R.mipmap.desert));
                    desertFlag = false;
                }
            }
        });

        drink.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (!drinkFlag){
                    drink.setBackground(getResources().getDrawable(R.mipmap.drink_grey));
                    drinkFlag = true;
                } else {
                    drink.setBackground(getResources().getDrawable(R.mipmap.drink));
                    drinkFlag = false;
                }
            }
        });

        snack.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (!sneakFlag) {
                    snack.setBackground(getResources().getDrawable(R.mipmap.snack_grey));
                    sneakFlag = true;
                } else {
                    snack.setBackground(getResources().getDrawable(R.mipmap.snack));
                    sneakFlag = false;
                }
            }
        });

        western.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (!westernFlag) {
                    western.setBackground(getResources().getDrawable(R.mipmap.western_grey));
                    westernFlag = true;
                } else {
                    western.setBackground(getResources().getDrawable(R.mipmap.western));
                    westernFlag = false;
                }
            }
        });

        seafood.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (!seafoodFlag) {
                    seafood.setBackground(getResources().getDrawable(R.mipmap.seafood_grey));
                    seafoodFlag = true;
                } else {
                    seafood.setBackground(getResources().getDrawable(R.mipmap.seafood));
                    seafoodFlag = false;
                }
            }
        });

        difficult.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (!difficultFlag) {
                    difficult.setBackground(getResources().getDrawable(R.mipmap.difficult_grey));
                    difficultFlag = true;
                } else {
                    difficult.setBackground(getResources().getDrawable(R.mipmap.difficult));
                    difficultFlag = false;
                }
            }
        });

        medium.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (!mediumFlag) {
                    medium.setBackground(getResources().getDrawable(R.mipmap.medium_grey));
                    mediumFlag = true;
                } else {
                    medium.setBackground(getResources().getDrawable(R.mipmap.medium));
                    mediumFlag = false;
                }
            }
        });

        easy.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (!easyFlag) {
                    easy.setBackground(getResources().getDrawable(R.mipmap.easy_grey));
                    easyFlag = true;
                } else {
                    easy.setBackground(getResources().getDrawable(R.mipmap.easy));
                    easyFlag = false;
                }
            }
        });

        vegetatian.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (!vegetationFlag) {
                    vegetatian.setBackground(getResources().getDrawable(R.mipmap.vegetarian_grey));
                    vegetationFlag = true;
                } else {
                    vegetatian.setBackground(getResources().getDrawable(R.mipmap.vegetarian));
                    vegetationFlag = false;
                }
            }
        });

        sugarFree.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (!sugarFreeFlag) {
                    sugarFree.setBackground(getResources().getDrawable(R.mipmap.sugarfree_grey));
                    sugarFreeFlag = true;
                } else {
                    sugarFree.setBackground(getResources().getDrawable(R.mipmap.sugarfree));
                    sugarFreeFlag = false;
                }
            }
        });

        lowCalorie.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (!lowCalorisFlag) {
                    lowCalorie.setBackground(getResources().getDrawable(R.mipmap.lowcalorie_grey));
                    lowCalorisFlag = true;
                } else {
                    lowCalorie.setBackground(getResources().getDrawable(R.mipmap.lowcalorie));
                    lowCalorisFlag = false;
                }
            }
        });

        lowFat.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (!lowFatFlag) {
                    lowFat.setBackground(getResources().getDrawable(R.mipmap.lowfat_grey));
                    lowFatFlag = true;
                } else {
                    lowFat.setBackground(getResources().getDrawable(R.mipmap.lowfat));
                    lowFatFlag = false;
                }
            }
        });

        spicy.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (!spicyFlag) {
                    spicy.setBackground(getResources().getDrawable(R.mipmap.spicy_grey));
                    spicyFlag = true;
                } else {
                    spicy.setBackground(getResources().getDrawable(R.mipmap.spicy));
                    spicyFlag = false;
                }
            }
        });

        milkSugarFree.setOnClickListener(new ImageButton.OnClickListener(){
            public void onClick(View v) {
                //如果ImageButton得到焦点onFocus则改变ImageButton的图片
                //更改按钮背景图
                //ImageButton aaa=(ImageButton) v;
                if (!milkSugarFreeFlag) {
                    milkSugarFree.setBackground(getResources().getDrawable(R.mipmap.milksugarfree_grey));
                    milkSugarFreeFlag = true;
                } else {
                    milkSugarFree.setBackground(getResources().getDrawable(R.mipmap.milksugarfree));
                    milkSugarFreeFlag = false;
                }
            }
        });



        return view;
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
}
