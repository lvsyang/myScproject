package com.example.schoolcircle.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.schoolcircle.bean.GuanZhu;
import com.example.schoolcircle.bean.User;
import com.example.schoolcircle.utils.ACache;
import com.example.schoolcircle.utils.SimpleAdapter;
import com.guoxiaoxing.phoenix.core.listener.ImageLoader;
import com.guoxiaoxing.phoenix.picker.Phoenix;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

public class MyFensiAct extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    //    private List<CircleBean> list = new ArrayList<>();
    private List<GuanZhu> list = new ArrayList<>();
    GuanZhu guanZhu = new GuanZhu();
    private User user;

    private SimpleAdapter<GuanZhu> adapter;
    private AlertDialog alertDialog;





    @Override
    public int intiLayout() {
        return R.layout.act_myguanz;
    }

    @Override
    public void initView() {
        Phoenix.config()
                .imageLoader(new ImageLoader() {
                    @Override
                    public void loadImage(Context mContext, ImageView imageView
                            , String imagePath, int type) {
                        Glide.with(mContext)
                                .load(imagePath)
                                .into(imageView);
                    }
                });



        tvTitle.setText("我的粉丝");
        ivBack.setVisibility(View.VISIBLE);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleAdapter<>(R.layout.item_lahei, list, new SimpleAdapter.ConVert<GuanZhu>() {

            @Override
            public void convert(BaseViewHolder helper, GuanZhu guanZhu) {
                //列表绑定数据

                helper.addOnClickListener(R.id.tv_uinfo);//实现点击查看个人信息栏跳转

//                helper.setText(R.id.tv_u_id, TextUtils.isEmpty(guanZhu.account)?guanZhu.account:guanZhu.account);


                helper.setText(R.id.tv_u_id, TextUtils.isEmpty(guanZhu.account)?"账号："+guanZhu.account:"账号："+guanZhu.account);


                TextView tv_ckgds = helper.getView(R.id.tv_u_nick);//
                getnick(guanZhu.account,tv_ckgds);
            }
        });
        recycler.setAdapter(adapter);



        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {//点击个人信息栏进行跳转
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                GuanZhu guanZhu = list.get(position);
                ACache.get(MyFensiAct.this).put("gzinfo", guanZhu);
                jumpAct(MyFsinfo.class);

            }
        });



        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MyFensiAct.this);
                builder.setTitle("确定移除粉丝吗？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GuanZhu guanZhu = list.get(position);
                        guanZhu.delete(MyFensiAct.this, new DeleteListener() {
                            @Override
                            public void onSuccess() {
                                toast("移除成功");
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
        getData();

    }

    private void getnick(String acc,final TextView  textView) {
        BmobQuery<User> yu=new BmobQuery<>();
        yu.addWhereEqualTo("account",acc);
        yu.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> listgn) {
                if(listgn.size()>0&&!listgn.get(0).nick.equals("")){
                    textView.setText("昵称："+listgn.get(0).nick);}
                else {
                    textView.setText("昵称：暂无");
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
        BmobQuery<GuanZhu> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("u_id",userbean.account);
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(this, new FindListener<GuanZhu>() {
            @Override
            public void onSuccess(List<GuanZhu> list2) {
                list.addAll(list2);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MyFensiAct.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
