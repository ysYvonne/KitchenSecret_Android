package com.meetyouatnowhere.kitchensecret_android.activities;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.dodowaterfall.Helper;
import com.dodowaterfall.widget.ScaleImageView;
import com.meetyouatnowhere.kitchensecret_android.activities.bannerview.ImagePagerAdapter;
import com.meetyouatnowhere.kitchensecret_android.R;
import com.meetyouatnowhere.kitchensecret_android.activities.bannerview.CircleFlowIndicator;
import com.meetyouatnowhere.kitchensecret_android.activities.bannerview.ViewFlow;
import com.meetyouatnowhere.kitchensecret_android.activities.pulldown.PullToRefreshSampleActivity;
import com.meetyouatnowhere.kitchensecret_android.activities.view.XListView;
import com.meetyouatnowhere.kitchensecret_android.util.ImageFetcher;
import com.model.DuitangInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeFragment extends Fragment implements XListView.IXListViewListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ViewFlow mViewFlow;
    private CircleFlowIndicator mFlowIndicator;
    private ArrayList<String> imageUrlList = new ArrayList<String>();
    ArrayList<String> linkUrlArray= new ArrayList<String>();
    ArrayList<String> titleList= new ArrayList<String>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageFetcher mImageFetcher;
    private XListView mAdapterView = null;
    private StaggeredAdapter mAdapter = null;
    private int currentPage = 0;
    ContentTask task = new ContentTask(this.getActivity(), 2);
//    private OnFragmentInteractionListener mListener;
private class ContentTask extends AsyncTask<String, Integer, List<DuitangInfo>> {

    private Context mContext;
    private int mType = 1;

    public ContentTask(Context context, int type) {
        super();
        mContext = context;
        mType = type;
    }

    @Override
    protected List<DuitangInfo> doInBackground(String... params) {
        try {
            return parseNewsJSON(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<DuitangInfo> result) {
        if (mType == 1) {

            mAdapter.addItemTop(result);
            mAdapter.notifyDataSetChanged();
            mAdapterView.stopRefresh();

        } else if (mType == 2) {
            mAdapterView.stopLoadMore();
            mAdapter.addItemLast(result);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onPreExecute() {
    }

    public List<DuitangInfo> parseNewsJSON(String url) throws IOException {
        List<DuitangInfo> duitangs = new ArrayList<DuitangInfo>();
        String json = "";
        if (Helper.checkConnection(mContext)) {
            try {
                json = Helper.getStringFromUrl(url);

            } catch (IOException e) {
                Log.e("IOException is : ", e.toString());
                e.printStackTrace();
                return duitangs;
            }
        }
        Log.d("RecipeFragment", "json:" + json);

        try {
            if (null != json) {
                JSONObject newsObject = new JSONObject(json);
                JSONObject jsonObject = newsObject.getJSONObject("data");
                JSONArray blogsJson = jsonObject.getJSONArray("blogs");

                for (int i = 0; i < blogsJson.length(); i++) {
                    JSONObject newsInfoLeftObject = blogsJson.getJSONObject(i);
                    DuitangInfo newsInfo1 = new DuitangInfo();
                    newsInfo1.setAlbid(newsInfoLeftObject.isNull("albid") ? "" : newsInfoLeftObject.getString("albid"));
                    newsInfo1.setIsrc(newsInfoLeftObject.isNull("isrc") ? "" : newsInfoLeftObject.getString("isrc"));
                    newsInfo1.setMsg(newsInfoLeftObject.isNull("msg") ? "" : newsInfoLeftObject.getString("msg"));
                    newsInfo1.setHeight(newsInfoLeftObject.getInt("iht"));
                    duitangs.add(newsInfo1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return duitangs;
    }
}
    public RecipeFragment() {
        // Required empty public constructor
    }
    /**
     * 添加内容
     *
     * @param pageindex
     * @param type
     *            1为下拉刷新 2为加载更多
     */
    private void AddItemToContainer(int pageindex, int type) {
        if (task.getStatus() != AsyncTask.Status.RUNNING) {
            String url = "http://www.duitang.com/album/1733789/masn/p/" + pageindex + "/24/";
            Log.d("MainActivity", "current url:" + url);
            ContentTask task = new ContentTask(this.getActivity(), type);
            task.execute(url);

        }
    }

    public class StaggeredAdapter extends BaseAdapter {
        private Context mContext;
        private LinkedList<DuitangInfo> mInfos;
        private XListView mListView;

        public StaggeredAdapter(Context context, XListView xListView) {
            mContext = context;
            mInfos = new LinkedList<DuitangInfo>();
            mListView = xListView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            DuitangInfo duitangInfo = mInfos.get(position);

            if (convertView == null) {
                LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
                convertView = layoutInflator.inflate(R.layout.infos_list, null);
                holder = new ViewHolder();
                holder.imageView = (ScaleImageView) convertView.findViewById(R.id.news_pic);
                holder.contentView = (TextView) convertView.findViewById(R.id.news_title);
                convertView.setTag(holder);
            }

            holder = (ViewHolder) convertView.getTag();
            holder.imageView.setImageWidth(duitangInfo.getWidth());
            holder.imageView.setImageHeight(duitangInfo.getHeight());
            holder.contentView.setText(duitangInfo.getMsg());
            mImageFetcher.loadImage(duitangInfo.getIsrc(), holder.imageView);
            return convertView;
        }

        class ViewHolder {
            ScaleImageView imageView;
            TextView contentView;
            TextView timeView;
        }

        @Override
        public int getCount() {
            return mInfos.size();
        }

        @Override
        public Object getItem(int arg0) {
            return mInfos.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        public void addItemLast(List<DuitangInfo> datas) {
            mInfos.addAll(datas);
        }

        public void addItemTop(List<DuitangInfo> datas) {
            for (DuitangInfo info : datas) {
                mInfos.addFirst(info);
            }
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeFragment newInstance(String param1, String param2) {
        RecipeFragment fragment = new RecipeFragment();
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
        View view=inflater.inflate(R.layout.fragment_recipe, container, false);
        initView(view);
        // Inflate the layout for this fragment
        imageUrlList
                .add("http://photocdn.sohu.com/20150422/mp11846883_1429673028490_2.jpeg");
        imageUrlList
                .add("http://photocdn.sohu.com/20150422/mp11846883_1429673028490_8.jpeg");
        imageUrlList
                .add("http://photocdn.sohu.com/20150422/mp11846883_1429673028490_12.jpeg");
        imageUrlList
                .add("http://photocdn.sohu.com/20150422/mp11846883_1429673028490_15.jpeg");
        initBanner(imageUrlList);

        mAdapterView = (XListView)view.findViewById(R.id.list);
        mAdapterView.setPullLoadEnable(true);
        mAdapterView.setXListViewListener(this);

        mAdapter = new StaggeredAdapter(this.getActivity(), mAdapterView);

        mImageFetcher = new ImageFetcher(this.getActivity(), 240);
        mImageFetcher.setLoadingImage(R.mipmap.empty_photo);
        return view;
    }

    private void initView(View view) {
        mViewFlow = (ViewFlow) view.findViewById(R.id.view_flow);
        mFlowIndicator = (CircleFlowIndicator)view.findViewById(R.id.viewCircle_flow);

    }

    private void initBanner(ArrayList<String> imageUrlList) {

        mViewFlow.setAdapter(new ImagePagerAdapter(getContext(), imageUrlList,
                linkUrlArray, titleList).setInfiniteLoop(true));
        mViewFlow.setmSideBuffer(imageUrlList.size()); // 实际图片张数，
        // 我的ImageAdapter实际图片张数为3

        mViewFlow.setFlowIndicator(mFlowIndicator);
        mViewFlow.setTimeSpan(4500);
        mViewFlow.setSelection(imageUrlList.size() * 1000); // 设置初始位置
        mViewFlow.startAutoFlowTimer(); // 启动自动播放
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
        @Override
        public void onResume() {
            super.onResume();
            mImageFetcher.setExitTasksEarly(false);
            mAdapterView.setAdapter(mAdapter);
            AddItemToContainer(currentPage, 2);
        }
    @Override
    public void onRefresh() {
        AddItemToContainer(++currentPage, 1);

    }

    @Override
    public void onLoadMore() {
        AddItemToContainer(++currentPage, 2);

    }
}
