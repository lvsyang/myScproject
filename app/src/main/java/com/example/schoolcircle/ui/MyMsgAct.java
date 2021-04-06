package com.example.schoolcircle.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.CircleBean;
import com.example.schoolcircle.bean.MsgRead;
import com.example.schoolcircle.bean.PingLun;
import com.example.schoolcircle.bean.YBuy;
import com.example.schoolcircle.bean.Zan;
import com.example.schoolcircle.utils.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class MyMsgAct extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recycler)
    RecyclerView recycler;
//    @BindView(R.id.recycler4)
//    RecyclerView recycler2;
//    @BindView(R.id.recycler5)
//    RecyclerView recycler3;
    @BindView(R.id.zan_click)
    LinearLayout zan_click;
    @BindView(R.id.apl_click)
    LinearLayout apl_click;
    @BindView(R.id.mzan)
    TextView mzan;
    @BindView(R.id.glpl)
    TextView glpl;
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
        return R.layout.act_mymsg;
    }

    @Override
    public void initView() {

        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("我的信息");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delmsg();
                finish();
            }
        });


        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleAdapter<>(R.layout.item_mypl, list, new SimpleAdapter.ConVert<PingLun>() {
            @Override
            public void convert(BaseViewHolder helper, PingLun pingLun) {
                helper.setText(R.id.myu_nick, TextUtils.isEmpty(pingLun.nick)?pingLun.account:pingLun.nick);//======

                helper.setText(R.id.myu_time,pingLun.getCreatedAt());
                helper.setText(R.id.myu_content,pingLun.content);

                ImageView view = helper.getView(R.id.iv_head);
                if (!TextUtils.isEmpty(pingLun.head)){

                    Glide.with(MyMsgAct.this).load(pingLun.head).into(view);
                }


            }
        });
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                getcirs(list.get(position).c_id);   //跳转到指定动态

            }


        });

        ///////////////////////////////////////////////////////////////////////////////////////2222


//        recycler2.setLayoutManager(new LinearLayoutManager(this));
//        adapter2 = new SimpleAdapter<>(R.layout.item_myzan, list2, new SimpleAdapter.ConVert<Zan>() {
//            @Override
//            public void convert(BaseViewHolder helper, Zan zan) {
//                helper.setText(R.id.myu_nick,(TextUtils.isEmpty(zan.nick)?zan.account:zan.nick));
//                helper.setText(R.id.myu_time,zan.getCreatedAt());
//
//                helper.setText(R.id.my_account,userbean.account);
//                String circontent="";
////                getcontent(zan.c_id);
//                helper.setText(R.id.mycir_content,"111");
//            }
//
//
//        });
//        recycler2.setAdapter(adapter2);

        zan_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delmsgzan();
                jumpAct(MyZanMsgAct.class);


            }
        });

        apl_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delmsgapl();
                jumpAct(MyAplunMsgAct.class);

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
    protected void onResume() {
        super.onResume();
        getData();
//        getData2();
        
        getmzan();
        getglpl();
        
    }

    private void getglpl() {
        BmobQuery<MsgRead> mz=new BmobQuery<>();
        mz.addWhereEqualTo("account",userbean.account);
        mz.addWhereEqualTo("mtype","aPingLun");
        mz.findObjects(this, new FindListener<MsgRead>() {
            @Override
            public void onSuccess(List<MsgRead> listmz) {
                if(listmz.size()>0){
                    glpl.setText(""+listmz.get(0).countread);
                }
                else {
                    glpl.setText("0");
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void getmzan() {
        BmobQuery<MsgRead> mzap=new BmobQuery<>();
        mzap.addWhereEqualTo("account",userbean.account);
        mzap.addWhereEqualTo("mtype","Zan");
        mzap.findObjects(this, new FindListener<MsgRead>() {
            @Override
            public void onSuccess(List<MsgRead> listap) {
                if(listap.size()>0){
                    mzan.setText(""+listap.get(0).countread);
                }
                else {
                    mzan.setText("0");
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    //从数据库获取数据，并绑定到列表
    private void getData() {
        list.clear();
        BmobQuery<CircleBean> cir=new BmobQuery<>();
        cir.addWhereEqualTo("account",userbean.account);
        cir.order("-createdAt");
        cir.findObjects(this, new FindListener<CircleBean>() {
            @Override
            public void onSuccess(List<CircleBean> listc) {
                for(int i=0;i<listc.size();i++)
                {
                    getcir(listc.get(i).getObjectId());
                }

            }



            @Override
            public void onError(int i, String s) {

            }
        });


    }

//    private void getcontent(String c1_id) {
//        list4.clear();
//        final BmobQuery<CircleBean> cir = new BmobQuery();
//        cir.addWhereEqualTo("objectId",c1_id);
//        cir.findObjects(this, new FindListener<CircleBean>() {
//            @Override
//            public void onSuccess(List<CircleBean> listk) {
//
//                list4.addAll(listk);
//                adapter4.notifyDataSetChanged();
//
//
//
//            }
//
//            @Override
//            public void onError(int i, String s) {
//
//            }
//        });
//
//    }



//    private Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case 0:
//                    List<CircleBean> list= (List<CircleBean>) msg.obj;
//                    break;
//            }
//
//        }
//    };

    private void getcir(String cirid) {
        BmobQuery<PingLun> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereNotEqualTo("account",userbean.account);
        bmobQuery.addWhereEqualTo("c_id",cirid);
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(this, new FindListener<PingLun>() {
            @Override
            public void onSuccess(List<PingLun> list1) {
                if(list1.size()!=0){
                list.addAll(list1);
                adapter.notifyDataSetChanged();}
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MyMsgAct.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });

    }

/////////////////////--------------------------------------------------------------------------2


//    private void getData2() {
//        list2.clear();
//        BmobQuery<CircleBean> cir=new BmobQuery<>();
//        cir.addWhereEqualTo("account",userbean.account);
//        cir.findObjects(this, new FindListener<CircleBean>() {
//            @Override
//            public void onSuccess(List<CircleBean> listc) {
//                for(int i=0;i<listc.size();i++)
//                {
//
//                    getcirzan(listc.get(i).getObjectId());
//                }
//
//            }
//            @Override
//            public void onError(int i, String s) {
//
//            }
//        });
//    }
//    private void getcirzan(String cirid) {
//        BmobQuery<Zan> bmobQuery = new BmobQuery<>();
//        bmobQuery.order("-createdAt");
//        bmobQuery.addWhereEqualTo("c_id",cirid);
////        bmobQuery.addWhereEqualTo("account",userbean.account);
//        bmobQuery.findObjects(this, new FindListener<Zan>() {
//            @Override
//            public void onSuccess(List<Zan> listzan) {
////                toast("sssss"+listzan.size());
//                if(listzan.size()!=0){
//                list2.addAll(listzan);
//                adapter2.notifyDataSetChanged();}
//            }
//
//            @Override
//            public void onError(int i, String s) {
//                Toast.makeText(MyMsgAct.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//    }

    private void delmsgzan() {
        final BmobQuery<MsgRead> msgReadz=new BmobQuery<>();
        msgReadz.addWhereEqualTo("account",userbean.account);
        msgReadz.addWhereEqualTo("mtype","Zan");

        msgReadz.findObjects(this, new FindListener<MsgRead>() {
            MsgRead msg=new MsgRead();
            @Override
            public void onSuccess(List<MsgRead> listmsgz) {
                if(listmsgz.size()!=0){
                    for(int i=0;i<listmsgz.size();i++){
                        msg=listmsgz.get(i);
                        msg.delete(MyMsgAct.this);
                    }
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

    private void delmsgapl() {
        final BmobQuery<MsgRead> msgReadpl=new BmobQuery<>();
        msgReadpl.addWhereEqualTo("account",userbean.account);
        msgReadpl.addWhereEqualTo("mtype","aPingLun");
        msgReadpl.findObjects(this, new FindListener<MsgRead>() {
            MsgRead msg=new MsgRead();
            @Override
            public void onSuccess(List<MsgRead> listmsgpl) {
                if(listmsgpl.size()!=0){
                    for(int i=0;i<listmsgpl.size();i++){
                        msg=listmsgpl.get(i);
                        msg.delete(MyMsgAct.this);
                    }
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }


    private void delmsg() {
        final BmobQuery<MsgRead> msgRead=new BmobQuery<>();
        msgRead.addWhereEqualTo("account",userbean.account);
        msgRead.addWhereEqualTo("mtype","PingLun");
        msgRead.findObjects(this, new FindListener<MsgRead>() {
            MsgRead msg=new MsgRead();
            @Override
            public void onSuccess(List<MsgRead> listmsg) {
                if(listmsg.size()!=0){
                    for(int i=0;i<listmsg.size();i++){
                        msg=listmsg.get(i);
                        msg.delete(MyMsgAct.this);
                    }
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
    @Override
    public void initData() {
        getmzan();
        getglpl();

    }
}
