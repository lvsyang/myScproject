package com.example.schoolcircle.ui.frag;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseFragment;
import com.example.schoolcircle.bean.CircleBean;
import com.example.schoolcircle.bean.GuanZhu;
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

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2020/3/17.
 */

public class CircleFrag extends BaseFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private String type;
    private List<CircleBean> list = new ArrayList<>();

//    @BindView(R.id.sw)
//    SwipeRefreshLayout srlayout;   刷新的算法，需要用请解除
//    private SwipeRefreshLayout srlayout;

    private SimpleAdapter<CircleBean> adapter;
    private User userbean;
    private TextView mai;
    private ImageView my_gender;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frg_circle,container,false);

    }




//这是刷新的注释方法，需要用解除
//    private void Refresh() {
//        list.clear();
//        if (type.equals("关注区")){
//            getGzData();
//        }
//        else if(type.equals("热门区")){
//            gerRmData();
//        }
//        else {
//            getData();
//        }
//        srlayout.setRefreshing(false);
//
//    }


    public static CircleFrag newInstance(String type){
        CircleFrag childFragment = new CircleFrag();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        childFragment.setArguments(bundle);
        return childFragment;

    }

    @Override
    public void initData() {
        super.initData();
        type = getArguments().getString("type");
        userbean = (User) ACache.get(getActivity()).getAsObject("userbean");
        recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new SimpleAdapter<>(R.layout.item_circle, list, new SimpleAdapter.ConVert<CircleBean>() {

            @Override
            public void convert(BaseViewHolder helper, CircleBean circleBean) {
                //列表绑定数据

                ImageView my_genders = helper.getView(R.id.my_gender);//获取性别
                getgenders(circleBean.account,my_genders);

                helper.setText(R.id.tv_nick, TextUtils.isEmpty(circleBean.nick)?circleBean.account:circleBean.nick);
                helper.setText(R.id.tv_content,circleBean.content);
                helper.setText(R.id.tv_time,"发布时间："+circleBean.getCreatedAt());






                mai=helper.itemView.findViewById(R.id.maichu);
                if(circleBean.type.equals("任性卖")&&circleBean.saleflag.equals("卖出"))
                {

                    mai.setVisibility(View.VISIBLE);
                }


//                mai.setVisibility(View.VISIBLE);

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
        recycler.setAdapter(adapter);




        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {//点击跳转到详情页面
                //点击列表跳转详情页
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", list.get(position));
                jumpAct(CircleBeanInfo.class, bundle);
            }
        });

    }

    private void getgenders(String acc,final ImageView genders) {
        BmobQuery<User> yu=new BmobQuery<>();
        yu.addWhereEqualTo("account",acc);
        yu.findObjects(getActivity(), new FindListener<User>() {
            @Override
            public void onSuccess(List<User> listsex) {
                if(listsex.get(0).gender.equals("男")){

                    genders.setImageResource(R.drawable.man);
                }
                if(listsex.get(0).gender.equals("女")){
                    genders.setImageResource(R.drawable.girl);
                }

            }

            @Override
            public void onError(int i, String s) {

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
    public void onResume() {
        super.onResume();
        if (type.equals("关注区")){
            getGzData();
        }
        else if(type.equals("热门区")){
            gerRmData();
        }
        else if(type.equals("111")){
            gerzfaData();
        }
        else {
            getData();
        }





//这个是刷新的注释，想用的时候请解除注释
//        //初始刷新一次
//
//
//        srlayout.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_red_light,android.R.color.holo_blue_light);
//        srlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Refresh();
//            }
//
//
//        });





    }




    //从数据库获取数据，并绑定到列表
    private void getData() {
        list.clear();
        final BmobQuery<CircleBean> bmobQuery = new BmobQuery<>();
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(mActivity, new FindListener<CircleBean>() {
            @Override
            public void onSuccess(List<CircleBean> list1) {
                for (int i = 0; i < list1.size(); i++) {
                    final CircleBean circleBean = list1.get(i);
                    if (circleBean.type.equals(type)){
                        BmobQuery<LaHei>  bmobQuery1=new BmobQuery<>();
                        bmobQuery1.addWhereEqualTo("u_id",circleBean.account);
                        bmobQuery1.addWhereEqualTo("account",userbean.account);
                        bmobQuery1.findObjects(mActivity, new FindListener<LaHei>() {
                            @Override
                            public void onSuccess(List<LaHei> list2) {
                                if (list2.size()==0){
                                    list.add(circleBean);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onError(int i, String s) {

                            }
                        });

                    }
                }


            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(mActivity, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //从数据库获取数据，并绑定到列表

    private void getGzData() {
        list.clear();
        BmobQuery<GuanZhu> bmobQuery = new BmobQuery<>();
        bmobQuery.order("-createdAt");
        bmobQuery.addWhereEqualTo("account",userbean.account);
        bmobQuery.findObjects(mActivity, new FindListener<GuanZhu>() {
            @Override
            public void onSuccess(List<GuanZhu> list1) {
                    if (list1!=null&&list1.size()>0){
                        for (int i = 0; i < list1.size(); i++) {//先在关注表里面找到我关注的人的id相关的信息
                            BmobQuery<CircleBean> bmobQuery = new BmobQuery<>();
                            bmobQuery.order("-createdAt");
                            bmobQuery.addWhereEqualTo("account",list1.get(i).u_id);//u_id是我关注的人
                            bmobQuery.findObjects(mActivity, new FindListener<CircleBean>() {
                                @Override
                                public void onSuccess(List<CircleBean> list2) {

                                    for (int i = 0; i < list2.size(); i++) {//拉黑的不显示
                                        final CircleBean circleBean = list2.get(i);
                                            BmobQuery<LaHei>  bmobQuery1=new BmobQuery<>();
                                            bmobQuery1.addWhereEqualTo("u_id",circleBean.account);
                                            bmobQuery1.addWhereEqualTo("account",userbean.account);
                                            bmobQuery1.findObjects(mActivity, new FindListener<LaHei>() {
                                                @Override
                                                public void onSuccess(List<LaHei> list3) {
                                                    if (list3.size()==0){
                                                        list.add(circleBean);
                                                        adapter.notifyDataSetChanged();
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
                    }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

    private void gerzfaData() {
        list.clear();
    }

    //从数据库获取数据，并绑定到列表
    private void gerRmData() {
        list.clear();
        final BmobQuery<CircleBean> bmobQueryrm = new BmobQuery<>();

        bmobQueryrm.addWhereGreaterThan("ccplz",5);
//        bmobQueryrm.addWhereEqualTo("countplz","6");
        bmobQueryrm.order("-createdAt");
        bmobQueryrm.findObjects(mActivity, new FindListener<CircleBean>() {
            @Override
            public void onSuccess(List<CircleBean> list2) {

                for (int i = 0; i < list2.size(); i++) {//拉黑的不显示
                    final CircleBean circleBean = list2.get(i);
                    BmobQuery<LaHei>  bmobQuery1=new BmobQuery<>();
                    bmobQuery1.addWhereEqualTo("u_id",circleBean.account);
                    bmobQuery1.addWhereEqualTo("account",userbean.account);
                    bmobQuery1.findObjects(mActivity, new FindListener<LaHei>() {
                        @Override
                        public void onSuccess(List<LaHei> list3) {
                            if (list3.size()==0){
                                list.add(circleBean);
                                adapter.notifyDataSetChanged();
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


}
