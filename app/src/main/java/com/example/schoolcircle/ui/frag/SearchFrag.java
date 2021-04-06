package com.example.schoolcircle.ui.frag;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseFragment;
import com.example.schoolcircle.bean.CircleBean;
import com.example.schoolcircle.bean.LaHei;
import com.example.schoolcircle.bean.PingLun;
import com.example.schoolcircle.bean.User;
import com.example.schoolcircle.bean.Zan;
import com.example.schoolcircle.ui.BigPhotoAct;
import com.example.schoolcircle.ui.CircleBeanInfo;
import com.example.schoolcircle.utils.ACache;
import com.example.schoolcircle.utils.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class SearchFrag extends BaseFragment {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.sc_content)
    EditText sc_content;
    @BindView(R.id.sc_search)
    ImageView sc_search;
    @BindView(R.id.search_rv)
    RecyclerView sc_rv;
//    @BindView(R.id.search_progress)
//    ProgressBar search_progress;
//    @BindView(R.id.search_isnone)
//    LinearLayout search_isnone;

//    @BindView(R.id.search_swipe)
//    SwipeRefreshLayout search_swipe;




    private SimpleAdapter<CircleBean> adapter;
    private User userbean;
    private List<CircleBean> list = new ArrayList<>();






    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.act_search,container,false);
    }





    @Override
    public void initData() {
        super.initData();
//        tvTitle.setText("推荐");

//        search_swipe.setColorSchemeResources(R.color.orange,android.R.color.holo_red_light,android.R.color.holo_blue_light);
//        search_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                //刷新时
//                getwant();
//            }
//        });

        sc_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getwant();


            }
        });


        userbean = (User) ACache.get(mActivity).getAsObject("userbean");
        sc_rv.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new SimpleAdapter<>(R.layout.item_circle, list, new SimpleAdapter.ConVert<CircleBean>() {

            @Override
            public void convert(BaseViewHolder helper, CircleBean circleBean) {
                //列表绑定数据

                helper.setText(R.id.tv_nick, TextUtils.isEmpty(circleBean.nick)?circleBean.account:circleBean.nick);
                helper.setText(R.id.tv_content,circleBean.content);
                helper.setText(R.id.tv_time,"发布时间："+circleBean.getCreatedAt());
                TextView tv_plnum = helper.getView(R.id.tv_plnum);
                TextView tv_zan_num = helper.getView(R.id.tv_zan_num);
                getPlNum(circleBean.getObjectId(),tv_plnum);
                getZnNum(circleBean.getObjectId(),tv_zan_num);
                ImageView view = helper.getView(R.id.iv_head);
                if (!TextUtils.isEmpty(circleBean.head)){
                    Glide.with(mActivity).load(circleBean.head).into(view);
                }

                RecyclerView recyclerView = helper.getView(R.id.recycler_img);
                if (!TextUtils.isEmpty(circleBean.imgs)){
                    String[] split = circleBean.imgs.split("&");
                    final ArrayList<String> pahtList = new ArrayList<>();
                    for (int i = 0; i <split.length ; i++) {
                        pahtList.add(split[i]);
                    }
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
                    SimpleAdapter    aaa = new SimpleAdapter<>(R.layout.item_photo, pahtList, new SimpleAdapter.ConVert<String>() {
                        @Override
                        public void convert(BaseViewHolder helper, final String path) {
                            ImageView view = helper.getView(R.id.iv);
                            Glide.with(getActivity()).load(path).into(view);
                        }
                    });
                    recyclerView.setAdapter(aaa);

                    aaa.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("urls",pahtList);

                            jumpAct(BigPhotoAct.class,bundle);

                        }
                    });

                }


            }
        });
        sc_rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //点击列表跳转详情页
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", list.get(position));
                jumpAct(CircleBeanInfo.class, bundle);
            }
        });

    }



    //从数据库获取数据，并绑定到列表
    private void getPlNum(String c_id, final TextView  textView) {

        BmobQuery<PingLun> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("c_id",c_id);
        bmobQuery.findObjects(getActivity(), new FindListener<PingLun>() {
            @Override
            public void onSuccess(List<PingLun> list1) {

                textView.setText("评论数"+list1.size()+"");

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }  //从数据库获取数据，并绑定到列表
    private void getZnNum(String c_id, final TextView  textView) {

        BmobQuery<Zan> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("c_id",c_id);
        bmobQuery.findObjects(getActivity(), new FindListener<Zan>() {
            @Override
            public void onSuccess(List<Zan> list1) {

                textView.setText("点赞数"+list1.size());

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        search_swipe.setColorSchemeResources(R.color.orange,android.R.color.holo_red_light,android.R.color.holo_blue_light);
//        search_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                //刷新时
//                getwant();
//            }
//        });

//        sc_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getwant();
//
//            }
//        });





    }



    private void getwant() {
//        search_progress.setVisibility(View.VISIBLE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String want = sc_content.getText().toString().trim();
                BmobQuery<CircleBean> bmobQuery = new BmobQuery<>();


//                for (int i=0;i<list.size();i++){ if(list.get(i).getFoodName().contains(mEtName.getText().toString

                bmobQuery.addWhereContains("content",want);
                bmobQuery.findObjects(mActivity, new FindListener<CircleBean>() {
                    @Override
                    public void onSuccess(List<CircleBean> list2) {
//                        search_swipe.setRefreshing(false);
                        if(list2.size()>0){
                            Toast.makeText(mActivity, "目前里面有数据", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mActivity, "目前里面没有数据", Toast.LENGTH_SHORT).show();

                        }

                        for (int i = 0; i < list2.size(); i++) {//拉黑的不显示
                            final CircleBean circleBean = list2.get(i);
                            BmobQuery<LaHei>  bmobQuery1=new BmobQuery<>();
                            bmobQuery1.addWhereEqualTo("u_id",circleBean.account);
                            bmobQuery1.addWhereEqualTo("account",userbean.account);
                            bmobQuery1.findObjects(mActivity, new FindListener<LaHei>() {
                                @Override
                                public void onSuccess(List<LaHei> list3) {
                                    if (list3.size()==0){
//                                        search_progress.setVisibility(View.GONE);
//                                        search_isnone.setVisibility(View.GONE);
//                                        search_swipe.setVisibility(View.VISIBLE);
                                        list.add(circleBean);
                                        adapter.notifyDataSetChanged();
                                    }
                                    else {
//                                        search_progress.setVisibility(View.GONE);
//                                        search_isnone.setVisibility(View.VISIBLE);
//                                        search_swipe.setVisibility(View.GONE);
                                    }

                                }

                                @Override
                                public void onError(int i, String s) {

                                }
                            });


                        }


                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(mActivity, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
                    }


                });






            }
        },250);




    }
}
