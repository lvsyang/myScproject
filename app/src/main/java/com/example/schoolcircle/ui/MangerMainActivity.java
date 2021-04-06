package com.example.schoolcircle.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.User;
import com.example.schoolcircle.utils.ACache;

/**
 * Created by Administrator on 2020/3/17.
 */

public class MangerMainActivity extends BaseActivity {
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_right;
    private TextView tv_gwxx;
    private TextView tv_gg;
    private TextView tv_jb;
    private TextView tv_manuser;
    private TextView tv_mancircle;
    private TextView tv_ggao;
    private TextView tv_rmanager;
    private TextView tv_zhuce;
    private TextView okk;

    private ImageView iv_right;
    private User user;


//    @BindView(R.id.muser)
//    TextView muser;
    private TextView muser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_manger);
        initView();
    }

    @Override
    public int intiLayout() {
        return R.layout.act_manger;
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
////        userbean = (User) ACache.get(mActivity).getAsObject("userbean");
//
////        muser.setText(userbean.account+"dsdasda");
//
//    }

    @Override
    public void initView() {


//        muser.setText(userbean.account+"dsdasda");

        muser= (TextView) findViewById(R.id.muser);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_jb = (TextView) findViewById(R.id.tv_jb);
        tv_gwxx = (TextView) findViewById(R.id.tv_gwxx);
        tv_gg = (TextView) findViewById(R.id.tv_gg);
        tv_ggao= (TextView) findViewById(R.id.tv_ggao);
        tv_zhuce= (TextView) findViewById(R.id.tv_zhuce);
        okk= (TextView) findViewById(R.id.okk);
        if(userbean.userlimit.equals("0")){
            okk.setText("超级管理员欢迎你");
        }


        tv_manuser = (TextView) findViewById(R.id.tv_manuser);
        tv_mancircle = (TextView) findViewById(R.id.tv_mancircle);
        tv_rmanager= (TextView) findViewById(R.id.tv_rmanager);


        tv_title.setText("校园说管理系统");

        muser.setText(userbean.account+"\t");

        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setText("退出");
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ACache.get(MangerMainActivity.this).clear();
                finish();
                jumpAct(LoginActivity.class);
            }
        });
        if(userbean.userlimit.equals("0")){
            tv_rmanager.setVisibility(View.VISIBLE);
            tv_rmanager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jumpAct(MangerRegAct.class);
                }
            });

        }
        tv_gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpAct(PubGongGaoAct.class);
            }
        });
        tv_ggao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpAct(GongGaoAct.class);
            }
        });
        tv_jb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpAct(JuBaoAct.class);
            }
        });
        tv_gwxx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpAct(PubGuanWangCircleAct.class);
            }
        });


        tv_manuser.setOnClickListener(new View.OnClickListener() {//点击进入用户管理
            @Override
            public void onClick(View v) {
                jumpAct(UserMangerAct.class);
            }
        });

        tv_mancircle.setOnClickListener(new View.OnClickListener() {//点击进入动态管理
            @Override
            public void onClick(View v) {
                jumpAct(CircleMangerAct.class);
            }
        });

        tv_zhuce.setOnClickListener(new View.OnClickListener() {//
            @Override
            public void onClick(View v) {
                jumpAct(RegMangerAct.class);
            }
        });


    }

    @Override
    public void initData() {

    }
}
