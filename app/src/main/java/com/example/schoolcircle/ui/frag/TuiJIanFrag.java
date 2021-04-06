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

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseFragment;
import com.example.schoolcircle.bean.CircleBean;
import com.example.schoolcircle.bean.LaHei;
import com.example.schoolcircle.bean.PingLun;
import com.example.schoolcircle.bean.Tuizpt;
import com.example.schoolcircle.bean.Tuizpu;
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

public class TuiJIanFrag extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private List<CircleBean> list = new ArrayList<>();
    private List<CircleBean> listcf = new ArrayList<>();
    private List<CircleBean> listcd = new ArrayList<>();
    private List<LaHei> listblack = new ArrayList<>();
    private SimpleAdapter<CircleBean> adapter;
    private User userbean;
     private String  guzhuids="";
     private String  zanids="";
     private String acc="";
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frg_gz,container,false);
    }

    @Override
    public void initData() {
        super.initData();
        tvTitle.setText("推荐");
        userbean = (User) ACache.get(mActivity).getAsObject("userbean");          //获取当前登录用户
        recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new SimpleAdapter<>(R.layout.item_circle, list, new SimpleAdapter.ConVert<CircleBean>() {

            @Override
            public void convert(BaseViewHolder helper, CircleBean circleBean) {


                //列表绑定数据

                helper.setText(R.id.tv_nick, TextUtils.isEmpty(circleBean.nick)?circleBean.account:circleBean.nick);
                helper.setText(R.id.tv_content,circleBean.content);
                helper.setText(R.id.tv_time,"发布时间："+circleBean.getCreatedAt());
                TextView tv_plnum = helper.getView(R.id.tv_plnum);
                TextView tv_zan_num = helper.getView(R.id.tv_zan_num);
                getPlNum(circleBean.getObjectId(),tv_plnum);//-------------------------------------------------------
                getZnNum(circleBean.getObjectId(),tv_zan_num);//-----------------------------------------------------
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
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //点击列表跳转详情页
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", list.get(position));
                jumpAct(CircleBeanInfo.class, bundle);
            }
        });
    }

    private void getlh() {
        BmobQuery<LaHei> glh=new BmobQuery<>();
        glh.addWhereEqualTo("account",userbean.account);
        glh.findObjects(mActivity, new FindListener<LaHei>() {
            @Override
            public void onSuccess(List<LaHei> listglh) {
                listblack.addAll(listglh);
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
        getttop();
        gettzan();
    }



    private void gettzan(){
        list.clear();
        BmobQuery<Tuizpu> tzp=new BmobQuery<>();
        tzp.addWhereEqualTo("account",userbean.account);
        tzp.addWhereGreaterThan("ucount",5);
        tzp.order("-createdAt");
        tzp.findObjects(mActivity, new FindListener<Tuizpu>() {
            @Override
            public void onSuccess(List<Tuizpu> list1) {
                if(list1.size()>0){
                    for (int i = 0; i < list1.size(); i++) {
                        final Tuizpu tui=list1.get(i);
                        final BmobQuery<LaHei> llh=new BmobQuery<>();
                        llh.addWhereEqualTo("account",userbean.account);
                        llh.addWhereEqualTo("u_id",tui.uaccount);
                        llh.findObjects(mActivity, new FindListener<LaHei>() {
                            @Override
                            public void onSuccess(List<LaHei> listllh) {
                                if(listllh.size()==0&&!tui.uaccount.equals(userbean.account)){
                                    qdhmd(tui.uaccount);
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

            }
        });


    }

    private void qdhmd(final String accountlh) {
        BmobQuery<CircleBean> lhcir=new BmobQuery<>();
        lhcir.order("-createdAt");
        lhcir.addWhereEqualTo("account",accountlh);
        lhcir.findObjects(mActivity, new FindListener<CircleBean>() {
            @Override
            public void onSuccess(List<CircleBean> listclh) {


                list.addAll(listclh);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
    private void getttop(){
        list.clear();
        listcf.clear();
        BmobQuery<Tuizpt> tzp=new BmobQuery<>();
        tzp.addWhereGreaterThan("ucount",8);
        tzp.order("-createdAt");
        tzp.findObjects(mActivity, new FindListener<Tuizpt>() {
            @Override
            public void onSuccess(List<Tuizpt> list1) {
                if(list1.size()>0){
                    for (int i = 0; i < list1.size(); i++) {



                        BmobQuery<CircleBean> cir=new BmobQuery<>();
                        cir.order("-createdAt");
                        cir.addWhereNotEqualTo("account",userbean.account);
                        cir.addWhereContains("topic",list1.get(i).utopic);//匹配主题,用contains模糊搜索改进------------------------
                        cir.findObjects(mActivity, new FindListener<CircleBean>() {
                            @Override
                            public void onSuccess(List<CircleBean> list3) {

                                for (int j=0;j<list3.size();j++){

                                    pclh(list3.get(j).account,list3.get(j));





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

            }
        });


    }


    private void pclh(String accountpc, final CircleBean circleBeansss) {
        BmobQuery<LaHei> pcllh=new BmobQuery<>();
        pcllh.addWhereEqualTo("account",userbean.account);
        pcllh.addWhereEqualTo("u_id",accountpc);
        pcllh.findObjects(mActivity, new FindListener<LaHei>() {
            @Override
            public void onSuccess(List<LaHei> listpclh) {
                if(listpclh.size()==0){
                    pccc(circleBeansss);

                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void pccc(final CircleBean iscf) {
        BmobQuery<Tuizpu> isok=new BmobQuery<>();
        isok.addWhereEqualTo("account",userbean.account);
        isok.addWhereEqualTo("uaccount",iscf.account);
        isok.addWhereGreaterThan("ucount",5);
        isok.findObjects(mActivity, new FindListener<Tuizpu>() {
            @Override
            public void onSuccess(List<Tuizpu> listiscf) {
                if(listiscf.size()==0){
                    list.add(iscf);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }





}
