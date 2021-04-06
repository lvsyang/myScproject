package com.example.schoolcircle.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.CircleBean;
import com.example.schoolcircle.bean.PingLun;
import com.example.schoolcircle.bean.YBuy;
import com.example.schoolcircle.bean.Zan;
import com.example.schoolcircle.utils.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class MyZanMsgAct extends BaseActivity {

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
    private List<Zan> list2=new ArrayList<>();
    private List<YBuy> list3=new ArrayList<>();
    private List<CircleBean> list4=new ArrayList<>();
    private SimpleAdapter<PingLun> adapter;
    private SimpleAdapter<Zan> adapter2;
    private SimpleAdapter<YBuy> adapter3;
    private SimpleAdapter<CircleBean> adapter4;

    @Override
    public int intiLayout() {
        return R.layout.act_mymsgzan;
    }

    @Override
    public void initView() {

        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("我收到的赞");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recycler2.setLayoutManager(new LinearLayoutManager(this));
        adapter2 = new SimpleAdapter<>(R.layout.item_myzan, list2, new SimpleAdapter.ConVert<Zan>() {

            @Override
            public void convert(BaseViewHolder helper, Zan zan) {
                helper.setText(R.id.myu_nick,(TextUtils.isEmpty(zan.nick)?zan.account:zan.nick));
                helper.setText(R.id.myu_time,zan.getCreatedAt());

//                helper.setText(R.id.my_account,userbean.account);

                helper.setText(R.id.my_account,(TextUtils.isEmpty(userbean.nick)?userbean.account+"：":userbean.nick+"："));
                helper.setText(R.id.mycir_content,zan.zcircontent);
                ImageView view = helper.getView(R.id.iv_head);
                if (!TextUtils.isEmpty(zan.head)){

                    Glide.with(MyZanMsgAct.this).load(zan.head).into(view);
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

    private void getcirs(String cid) {

        BmobQuery<CircleBean> getcir=new BmobQuery<>();
        getcir.addWhereEqualTo("objectId",cid);
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
        getData2();
    }






    /////////////////////--------------------------------------------------------------------------2
    private void getData2() {
        list2.clear();
        BmobQuery<CircleBean> cir=new BmobQuery<>();
        cir.addWhereEqualTo("account",userbean.account);
        cir.order("-createdAt");
        cir.findObjects(this, new FindListener<CircleBean>() {
            @Override
            public void onSuccess(List<CircleBean> listc) {
                for(int i=0;i<listc.size();i++)
                {

                    getcirzan(listc.get(i).getObjectId());
                }

            }
            @Override
            public void onError(int i, String s) {

            }
        });
    }
    private void getcirzan(String cirid) {
        BmobQuery<Zan> bmobQuery = new BmobQuery<>();
        bmobQuery.order("-createdAt");
        bmobQuery.addWhereNotEqualTo("account",userbean.account);
        bmobQuery.addWhereEqualTo("c_id",cirid);
//        bmobQuery.addWhereEqualTo("account",userbean.account);
        bmobQuery.findObjects(this, new FindListener<Zan>() {
            @Override
            public void onSuccess(List<Zan> listzan) {
//                toast("sssss"+listzan.size());
                if(listzan.size()!=0){
                    list2.addAll(listzan);
                    adapter2.notifyDataSetChanged();}
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MyZanMsgAct.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });


    }





}
