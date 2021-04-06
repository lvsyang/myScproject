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
import com.example.schoolcircle.bean.LaHei;
import com.example.schoolcircle.bean.User;
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

public class MyLaheiAct extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recycler)
    RecyclerView recycler;
//    private List<CircleBean> list = new ArrayList<>();
    private List<LaHei> list = new ArrayList<>();
    LaHei laHei = new LaHei();
    private User user;

    private SimpleAdapter<LaHei> adapter;
    private AlertDialog alertDialog;


    @Override
    public int intiLayout() {
        return R.layout.act_mylahei;
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



        tvTitle.setText("我的黑名单");
        ivBack.setVisibility(View.VISIBLE);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleAdapter<>(R.layout.item_lahei1, list, new SimpleAdapter.ConVert<LaHei>() {

            @Override
            public void convert(BaseViewHolder helper, LaHei laHei) {
                //列表绑定数据

                helper.setText(R.id.tv_u_id, TextUtils.isEmpty(laHei.u_id)?"账号："+laHei.u_id:"账号："+laHei.u_id);

                TextView tv_ckgds = helper.getView(R.id.tv_u_nick);//
                getnick(laHei.u_id,tv_ckgds);





            }




        });
        recycler.setAdapter(adapter);
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MyLaheiAct.this);
                builder.setTitle("确定移出黑名单吗？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LaHei laHei = list.get(position);
                        laHei.delete(MyLaheiAct.this, new DeleteListener() {
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
    @Override
    public void initData() {
        getData();

    }


    //从数据库获取数据，并绑定到列表
    private void getData() {
        list.clear();
        BmobQuery<LaHei> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account",userbean.account);
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(this, new FindListener<LaHei>() {
            @Override
            public void onSuccess(List<LaHei> list2) {
                list.addAll(list2);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MyLaheiAct.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }



}
