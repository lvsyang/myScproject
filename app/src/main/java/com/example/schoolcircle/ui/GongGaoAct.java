package com.example.schoolcircle.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.schoolcircle.bean.GongGao;
import com.example.schoolcircle.utils.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

public class GongGaoAct extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private List<GongGao> list=new ArrayList<>();
    private SimpleAdapter<GongGao> adapter;
    private String a;

    private AlertDialog alertDialog;



    @Override
    public int intiLayout() {
        return R.layout.act_jb;//通用的底
    }

    @Override
    public void initView() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("公告管理");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleAdapter<>(R.layout.item_mangonggao, list, new SimpleAdapter.ConVert<GongGao>() {
            @Override
            public void convert(BaseViewHolder helper, GongGao gongGao) {
                helper.setText(R.id.tv_account,"标题："+gongGao.content);
                helper.setText(R.id.tv_content,"内容："+gongGao.msg);
                helper.setText(R.id.tv_time,""+gongGao.getCreatedAt());

            }
        });
        recycler.setAdapter(adapter);
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                AlertDialog.Builder builder=new AlertDialog.Builder(GongGaoAct.this);
                builder.setTitle("确定删除公告吗？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GongGao gongGao = list.get(position);
                        gongGao.delete(GongGaoAct.this, new DeleteListener() {
                            @Override
                            public void onSuccess() {
                                toast("删除成功");
                                getData();
                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });
                        alertDialog.dismiss();
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
                return false;
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
        String limit="2";
        BmobQuery<GongGao> bmobQuery = new BmobQuery<>();
//        bmobQuery.addWhereEqualTo("userlimit",limit);//管理员无法管理管理员
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(this, new FindListener<GongGao>() {
            @Override
            public void onSuccess(List<GongGao> list1) {
                list.addAll(list1);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(GongGaoAct.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
