package com.example.schoolcircle.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.User;

import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegManDetialAct extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle; @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_time)
    TextView tvTime;

    @BindView(R.id.tv_del)
    TextView tv_del; @BindView(R.id.tv_del2)
    TextView tv_del2;


    //    private JuBao bean;
//    private CircleBean circleBean;
    private User bean;
    private User user;

    @Override
    public int intiLayout() {
        return R.layout.act_regman_detial;
    }

    @Override
    public void initView() {
        Bundle extras = getIntent().getExtras();
        bean = (User) extras.getSerializable("bean");
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("用户处理");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User us=new User();
                us=bean;
                us.stuverify="1";
                us.update(RegManDetialAct.this);
//                tguo();
                toast("审核通过");
                finish();
            }
        });

        tv_del2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User udel=new User();
                udel=bean;
                udel.delete(RegManDetialAct.this);
                toast("已删除");
                finish();
            }
        });

    }

    private void tguo() {
        BmobQuery<User> bmobQuery1=new BmobQuery<>();
        bmobQuery1.addWhereEqualTo("account",bean.account);
        bmobQuery1.addWhereEqualTo("stuverify","0");
        bmobQuery1.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                user = list.get(0);
//                tvNick.setText(TextUtils.isEmpty(circleBean.nick)? circleBean.account: circleBean.nick);
                tvNick.setText("账号："+user.account);
//                tvContent.setText(circleBean.content);
                tvTime.setText("姓名："+ user.stuname+"\n"+"学号："+user.stuaccount);


            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    @Override
    public void initData() {
        getdata();
    }

    private void getdata(){
        BmobQuery<User> bmobQuery=new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account",bean.account);
        bmobQuery.addWhereEqualTo("stuverify","0");
        bmobQuery.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                user = list.get(0);
//                tvNick.setText(TextUtils.isEmpty(circleBean.nick)? circleBean.account: circleBean.nick);
                tvNick.setText("账号："+user.account);
//                tvContent.setText(circleBean.content);
                tvTime.setText("姓名："+ user.stuname+"\n"+"学号："+user.stuaccount);


            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
}

