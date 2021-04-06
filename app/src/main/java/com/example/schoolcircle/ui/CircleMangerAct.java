package com.example.schoolcircle.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.User;
import com.example.schoolcircle.utils.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class CircleMangerAct extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private List<User> list=new ArrayList<>();
    private SimpleAdapter<User> adapter;
    private String a;




    @Override
    public int intiLayout() {
        return R.layout.act_jb;//通用的底
    }

    @Override
    public void initView() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("用户动态管理");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleAdapter<>(R.layout.item_manuser, list, new SimpleAdapter.ConVert<User>() {
            @Override
            public void convert(BaseViewHolder helper, User user) {
                helper.setText(R.id.tv_account,"用户账号："+user.account);
                helper.setText(R.id.tv_content,"昵称："+user.nick);
                a=user.fh;
                if(a.equals("1")){
                    helper.setText(R.id.tv_time,"账号状态：封禁");}
                else {
                    helper.setText(R.id.tv_time,"账号状态：正常");

                }
            }
        });
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("bean",list.get(position));
                jumpAct(CircleDetialAct.class,bundle);
            }
        });

    }

    @Override
    public void initData() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(userbean.userlimit.equals("1")){
        getData();}
        else {
            getadData();
        }
    }

    private void getadData() {
        list.clear();
        String limit0="0";
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereNotEqualTo("userlimit",limit0);//管理员无法管理管理员
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list1) {
                list.addAll(list1);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(CircleMangerAct.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });

    }


    //从数据库获取数据，并绑定到列表
    private void getData() {
        list.clear();
        String limit="2";
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("userlimit",limit);//管理员无法管理管理员
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list1) {
                list.addAll(list1);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(CircleMangerAct.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
