package com.example.schoolcircle.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.MTopic;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class TopicSelectAct extends BaseActivity  {

    @BindView(R.id.tc_study)
    TextView tc_study;
    @BindView(R.id.tc_life)
    TextView tc_life;
    @BindView(R.id.tc_sport)
    TextView tc_sport;
    @BindView(R.id.tc_game)
    TextView tc_game;
    @BindView(R.id.tc_digit)
    TextView tc_digit;
    @BindView(R.id.tc_college)
    TextView tc_college;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @Override
    public int intiLayout() {
        return R.layout.act_topicselect;
    }

    @Override
    public void initView() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("选择话题");

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.tc_study, R.id.tc_life,R.id.tc_sport,R.id.tc_game,R.id.tc_digit,R.id.tc_college,R.id.iv_back})
    public void onViewClicked(View v) {

        switch (v.getId()) {
            case R.id.tc_study:
                String a="学习&";

                addtopic(a);
                finish();
                break;
            case R.id.tc_life:
                String b="生活&";
                addtopic(b);
                finish();
                break;
            case R.id.tc_sport:
                String c="运动&";
                addtopic(c);
                finish();
                break;
            case R.id.tc_game:
                String d="游戏&";
                addtopic(d);
                finish();
                break;
            case R.id.tc_digit:
                String e="数码&";
                addtopic(e);
                finish();
                break;
            case R.id.tc_college:
                String f="校园&";
                addtopic(f);
                finish();
                break;
            case R.id.iv_back:
                finish();
                break;

        }
    }

    private void addtopic(final String acontent) {
        BmobQuery<MTopic> mTopicBmobQuery=new BmobQuery<>();
        mTopicBmobQuery.addWhereEqualTo("account",userbean.account);
        mTopicBmobQuery.findObjects(this, new FindListener<MTopic>() {
            @Override
            public void onSuccess(List<MTopic> listmt) {
                if(listmt.size()>0) {
//                    toast(listmt.size()+"");
                    MTopic mTopic = new MTopic();
                    mTopic = listmt.get(0);
                    if(mTopic.content.contains(acontent)){
                        toast("请勿添加重复话题");
                    }
                    else {
                    mTopic.content =mTopic.content+acontent;
                    mTopic.update(TopicSelectAct.this);}
                }
                else {
                    MTopic mTopic = new MTopic();
                    mTopic = listmt.get(0);
                    mTopic.account=userbean.account;
                    mTopic.content =acontent;
                    mTopic.save(TopicSelectAct.this);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
}
