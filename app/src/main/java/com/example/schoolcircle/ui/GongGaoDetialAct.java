package com.example.schoolcircle.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.GongGao;

import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class GongGaoDetialAct extends BaseActivity  {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.news_title)
    TextView news_title;
    @BindView(R.id.news_content)
    TextView news_content;



//    @Override
//    public void message(String s) {
//        news_title.setText(s);
//    }

    public String a;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        a=intent.getStringExtra("www");

    }



    @Override
    public int intiLayout() {
        return R.layout.act_gonggao_detial;
    }

    @Override
    public void initView() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvTitle.setText("当前新闻");
        ivBack.setVisibility(View.VISIBLE);


    }

    @Override
    public void initData() {
        getData();

    }


    //从数据库获取数据，并绑定到列表
    private void getData() {
        BmobQuery<GongGao> bmobQuery = new BmobQuery<>();
        Intent in1=getIntent();
        bmobQuery.addWhereEqualTo("content",in1.getStringExtra("www"));
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(this, new FindListener<GongGao>() {
            @Override
            public void onSuccess(List<GongGao> list1) {
                GongGao gongGao=new GongGao();
                gongGao=list1.get(0);
                news_title.append(gongGao.content);
                news_content.append("  "+gongGao.msg);


//                Toast.makeText(MyGzinfo.this, list.size()+"成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(GongGaoDetialAct.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }




//    //获取fragment传递过来的值，通过接口
//        HomeFrag.setActivity(new HomeFrag.ICallBack() {
//        @Override
//        public void get_message_from_Fragment(String string) {
//            //给控件进行赋值
//            text.setText(string);
//        }
//    });

}
