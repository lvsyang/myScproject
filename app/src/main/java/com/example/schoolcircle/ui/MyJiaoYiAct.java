package com.example.schoolcircle.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.YBuy;
import com.example.schoolcircle.utils.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class MyJiaoYiAct extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.recycler2)
    RecyclerView recycler2;
    private List<YBuy> list=new ArrayList<>();
    private List<YBuy> list2=new ArrayList<>();
    private SimpleAdapter<YBuy> adapter;
    private SimpleAdapter<YBuy> adapter2;




    @Override
    public int intiLayout() {
        return R.layout.act_myjiaoyi;
    }

    @Override
    public void initView() {

        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("交易管理");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleAdapter<>(R.layout.item_jb, list, new SimpleAdapter.ConVert<YBuy>() {
            @Override
            public void convert(BaseViewHolder helper, YBuy yBuy) {
                helper.setText(R.id.tv_account, TextUtils.isEmpty(yBuy.nick)?"买家："+yBuy.account:"买家："+yBuy.nick);//======


                helper.setText(R.id.tv_content,"申请类型：预定");
                helper.setText(R.id.tv_time,"交易状态："+yBuy.saleflag);
//                helper.itemView.findViewById(R.id.tv_content);

            }
        });
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("bean",list.get(position));
                jumpAct(JiaoYiDetialAct.class,bundle);
//                Toast.makeText(MyJiaoYiAct.this, "click me", Toast.LENGTH_SHORT).show();
            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////
        recycler2.setLayoutManager(new LinearLayoutManager(this));
        adapter2 = new SimpleAdapter<>(R.layout.item_jb, list2, new SimpleAdapter.ConVert<YBuy>() {

            @Override
            public void convert(BaseViewHolder helper, YBuy yBuy) {
                helper.setText(R.id.tv_account,"交易成功！！！");//======



                helper.setText(R.id.tv_content,"您购买的用户"+(TextUtils.isEmpty(yBuy.mainick)?yBuy.maiaccount:yBuy.mainick)+"的商品  卖家已经同意交易");
                helper.setText(R.id.tv_time,"");
//                helper.itemView.findViewById(R.id.tv_content);

            }
        });
        recycler2.setAdapter(adapter2);
        adapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                getbuymsg(list2.get(position).c_id);

//                Toast.makeText(MyJiaoYiAct.this, "click me", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getbuymsg(String buyc_id) {
        BmobQuery<YBuy> buymsg=new BmobQuery<>();
        buymsg.addWhereEqualTo("objectId",buyc_id);
        buymsg.findObjects(this, new FindListener<YBuy>() {
            @Override
            public void onSuccess(List<YBuy> listbmsg) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("bean",listbmsg.get(0));
                jumpAct(JiaoYiDetialAct.class,bundle);
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
        getData2();
    }


    //从数据库获取数据，并绑定到列表
    private void getData() {
        list.clear();
        BmobQuery<YBuy> bmobQuery = new BmobQuery<>();
        bmobQuery.order("-createdAt");
        bmobQuery.addWhereNotEqualTo("content","交易成功");
        bmobQuery.addWhereEqualTo("maiaccount",userbean.account);
        bmobQuery.findObjects(this, new FindListener<YBuy>() {
            @Override
            public void onSuccess(List<YBuy> list1) {
                list.addAll(list1);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MyJiaoYiAct.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getData2() {
        list2.clear();
        BmobQuery<YBuy> bmobQuery = new BmobQuery<>();
        bmobQuery.order("-createdAt");
        bmobQuery.addWhereEqualTo("content","交易成功");
        bmobQuery.addWhereEqualTo("account",userbean.account);
        bmobQuery.findObjects(this, new FindListener<YBuy>() {
            @Override
            public void onSuccess(List<YBuy> list1) {
                list2.addAll(list1);
                adapter2.notifyDataSetChanged();
//                Toast.makeText(MyJiaoYiAct.this, " "+list1.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MyJiaoYiAct.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void initData() {

    }
}
