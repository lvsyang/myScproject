package com.example.schoolcircle.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.APingLun;
import com.example.schoolcircle.bean.CircleBean;
import com.example.schoolcircle.bean.PingLun;
import com.example.schoolcircle.bean.YBuy;
import com.example.schoolcircle.utils.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class MyAplunMsgAct extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    //    @BindView(R.id.recycler)
//    RecyclerView recycler;
    @BindView(R.id.recyclerzan)
    RecyclerView recycler2;
    //    @BindView(R.id.recycler5)
//    RecyclerView recycler3;
    private List<PingLun> list=new ArrayList<>();
    private List<APingLun> list2=new ArrayList<>();
    private List<YBuy> list3=new ArrayList<>();
    private List<CircleBean> list4=new ArrayList<>();
    private SimpleAdapter<PingLun> adapter;
    private SimpleAdapter<APingLun> adapter2;
    private SimpleAdapter<YBuy> adapter3;
    private SimpleAdapter<CircleBean> adapter4;


    @Override
    public int intiLayout() {
        return R.layout.act_mymsgzan;
    }

    @Override
    public void initView() {

        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("我的盖楼评论");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
        recycler2.setLayoutManager(new LinearLayoutManager(this));
        adapter2 = new SimpleAdapter<>(R.layout.item_myaplun, list2, new SimpleAdapter.ConVert<APingLun>() {

            @Override
            public void convert(BaseViewHolder helper, APingLun aPingLun) {

                helper.setText(R.id.myu_nick,(TextUtils.isEmpty(aPingLun.nick)?aPingLun.iaccount:aPingLun.nick));
                helper.setText(R.id.myu_time,aPingLun.getCreatedAt());
                helper.setText(R.id.myu_content,aPingLun.content);

//                helper.setText(R.id.my_account,userbean.account);

                helper.setText(R.id.my_account,(TextUtils.isEmpty(userbean.nick)?userbean.account+"：":userbean.nick+"："));

                TextView plcontent=helper.getView(R.id.mypl_content);
                getpinglun(aPingLun.c_id,plcontent);
//                helper.setText(R.id.mypl_content,zan.zcircontent);

                ImageView view = helper.getView(R.id.iv_head);
                if (!TextUtils.isEmpty(aPingLun.head)){

                    Glide.with(MyAplunMsgAct.this).load(aPingLun.head).into(view);
                }

            }


        });
        recycler2.setAdapter(adapter2);
        adapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                getcirs(list2.get(position).c_id);   //跳转到指定动态

            }


        });

    }

    private void getpinglun(String cid, final TextView plcontent) {
        BmobQuery<PingLun> pl=new BmobQuery<>();
        pl.addWhereEqualTo("objectId",cid);
        pl.findObjects(this, new FindListener<PingLun>() {
            @Override
            public void onSuccess(List<PingLun> listpl) {
                if(listpl.size()>0)
                {
                    plcontent.setText(""+listpl.get(0).content);
                }

            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

    private void getcirs(String cid) {//跳转到指定动态

        final BmobQuery<PingLun> getcir=new BmobQuery<>();
        getcir.addWhereEqualTo("objectId",cid);
        getcir.findObjects(this, new FindListener<PingLun>() {
            @Override
            public void onSuccess(List<PingLun> listpl) {
                getcirs2(listpl.get(0).c_id);
//                //点击列表跳转详情页
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("bean", listpl.get(0));
//                jumpAct(CircleBeanInfo.class, bundle);
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

    private void getcirs2(String cid1) {
        final BmobQuery<CircleBean> getcir=new BmobQuery<>();
        getcir.addWhereEqualTo("objectId",cid1);
        getcir.findObjects(this, new FindListener<CircleBean>() {
            @Override
            public void onSuccess(List<CircleBean> listcir) {

                //点击列表跳转详情页
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", listcir.get(0));
                jumpAct(CircleBeanInfo.class, bundle);
            }

            @Override
            public void onError(int i, String s) {

            }
        });


    }

    @Override
    public void initData() {

    }
    @Override
    protected void onResume() {
        super.onResume();
//        getData2();
        getDataa();
    }

    private void getDataa() {
        list2.clear();
        BmobQuery<APingLun> apl=new BmobQuery<>();
        apl.addWhereNotEqualTo("iaccount",userbean.account);
        apl.addWhereEqualTo("uaccount",userbean.account);
        apl.findObjects(this, new FindListener<APingLun>() {
            @Override
            public void onSuccess(List<APingLun> listapl) {
                list2.addAll(listapl);
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


//    private void getData2() {
//        list2.clear();
//        BmobQuery<CircleBean> cir=new BmobQuery<>();
//        cir.addWhereEqualTo("account",userbean.account);
//        cir.order("-createdAt");
//        cir.findObjects(this, new FindListener<CircleBean>() {
//            @Override
//            public void onSuccess(List<CircleBean> listc) {
//                for(int i=0;i<listc.size();i++)
//                {
//                    getcirpl(listc.get(i).getObjectId());
//
//
//                }
//
//            }
//            @Override
//            public void onError(int i, String s) {
//
//            }
//        });
//    }

//    private void getcirpl(String cd) {
//        BmobQuery<PingLun> plun=new BmobQuery<>();
//        plun.addWhereEqualTo("c_id",cd);
//        plun.findObjects(this, new FindListener<PingLun>() {
//            @Override
//            public void onSuccess(List<PingLun> listpl) {
//                for(int i=0;i<listpl.size();i++){
//                    getcirapl(listpl.get(i).getObjectId());
//                }
//            }
//
//            @Override
//            public void onError(int i, String s) {
//
//            }
//        });
//    }


//    private void getcirapl(String cirid) {
//        BmobQuery<APingLun> bmobQuery = new BmobQuery<>();
//        bmobQuery.order("-createdAt");
//        bmobQuery.addWhereEqualTo("c_id",cirid);
////        bmobQuery.addWhereEqualTo("account",userbean.account);
//        bmobQuery.findObjects(this, new FindListener<APingLun>() {
//            @Override
//            public void onSuccess(List<APingLun> listapl) {
////                toast("sssss"+listzan.size());
//                if(listapl.size()!=0){
//                    list2.addAll(listapl);
//                    adapter2.notifyDataSetChanged();}
//                toast(""+listapl.size());
//            }
//
//            @Override
//            public void onError(int i, String s) {
//                Toast.makeText(MyAplunMsgAct.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//    }





}
