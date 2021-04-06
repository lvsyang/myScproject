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
import com.example.schoolcircle.bean.JuBao;
import com.example.schoolcircle.utils.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2020/3/17.
 */

public class JuBaoAct extends BaseActivity{
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private List<JuBao> list=new ArrayList<>();
    private SimpleAdapter<JuBao> adapter;

    @Override
    public int intiLayout() {
        return R.layout.act_jb;
    }

    @Override
    public void initView() {
            ivBack.setVisibility(View.VISIBLE);
            tvTitle.setText("举报管理");
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleAdapter<>(R.layout.item_jb, list, new SimpleAdapter.ConVert<JuBao>() {
            @Override
            public void convert(BaseViewHolder helper, JuBao juBao) {
                    helper.setText(R.id.tv_account,"举报人："+juBao.account);
                    helper.setText(R.id.tv_content,"理由："+juBao.content);
                    helper.setText(R.id.tv_time,juBao.getCreatedAt());
            }
        });
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("bean",list.get(position));
                jumpAct(JuBaoDetialAct.class,bundle);
            }
        });

    }

    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    //从数据库获取数据，并绑定到列表
    private void getData() {
        list.clear();
        BmobQuery<JuBao> bmobQuery = new BmobQuery<>();
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(this, new FindListener<JuBao>() {
            @Override
            public void onSuccess(List<JuBao> list1) {
                list.addAll(list1);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(JuBaoAct.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
