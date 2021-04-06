package com.example.schoolcircle.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import com.example.schoolcircle.bean.APingLun;
import com.example.schoolcircle.bean.CircleBean;
import com.example.schoolcircle.bean.PingLun;
import com.example.schoolcircle.bean.Zan;
import com.example.schoolcircle.utils.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2020/1/1.
 */

public class MyPubAct extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private List<CircleBean> list = new ArrayList<>();

    private SimpleAdapter<CircleBean> adapter;
    private AlertDialog alertDialog;
    public String a;

    @Override
    public int intiLayout() {
        return R.layout.act_mypub;
    }

    @Override
    public void initView() {
        tvTitle.setText("我发布的");
        ivBack.setVisibility(View.VISIBLE);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleAdapter<>(R.layout.item_circle, list, new SimpleAdapter.ConVert<CircleBean>() {

            @Override
            public void convert(BaseViewHolder helper, CircleBean circleBean) {
                //列表绑定数据

                helper.setText(R.id.tv_nick, TextUtils.isEmpty(circleBean.nick)?circleBean.account:circleBean.nick);
                helper.setText(R.id.tv_content,circleBean.content);
                helper.setText(R.id.tv_time,"发布时间："+circleBean.getCreatedAt());

                ImageView view = helper.getView(R.id.iv_head);
                if (!TextUtils.isEmpty(circleBean.head)){
                    Glide.with(MyPubAct.this).load(circleBean.head).into(view);
                }
                RecyclerView recyclerView = helper.getView(R.id.recycler_img);
                if (!TextUtils.isEmpty(circleBean.imgs)){
                    String[] split = circleBean.imgs.split("&");
                    final ArrayList<String> pahtList = new ArrayList<>();
                    for (int i = 0; i <split.length ; i++) {
                        pahtList.add(split[i]);
                    }
                    recyclerView.setLayoutManager(new GridLayoutManager(MyPubAct.this,3));
                    SimpleAdapter    aaa = new SimpleAdapter<>(R.layout.item_photo, pahtList, new SimpleAdapter.ConVert<String>() {
                        @Override
                        public void convert(BaseViewHolder helper, final String path) {
                            ImageView view = helper.getView(R.id.iv);

                            Glide.with(MyPubAct.this).load(path).into(view);



                        }
                    });
                    recyclerView.setAdapter(aaa);
                    aaa.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("urls",pahtList);

                            jumpAct(BigPhotoAct.class,bundle);

                        }
                    });
                }


            }
        });
        recycler.setAdapter(adapter);
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MyPubAct.this);
                builder.setTitle("确定删除吗？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CircleBean circleBean = list.get(position);
                        a=circleBean.getObjectId();
                        circleBean.delete(MyPubAct.this, new DeleteListener() {
                            @Override
                            public void onSuccess() {
                                toast("删除成功");

                                //删除相关评论
//                                String account1 = bean.account;
                                BmobQuery<PingLun> bmobQuery=new BmobQuery<>();
                                bmobQuery.addWhereEqualTo("c_id",a);
                                bmobQuery.findObjects(MyPubAct.this, new FindListener<PingLun>() {
                                    @Override
                                    public void onSuccess(List<PingLun> list) {
                                        if (list!=null&&list.size()>0) {
                                            for (int i = 0; i < list.size(); i++) {
                                                PingLun user1 = list.get(i);
                                                final PingLun user2 = list.get(i);
                                                user1.delete(MyPubAct.this, new DeleteListener() {
                                                    @Override
                                                    public void onSuccess() {


                                                        BmobQuery<APingLun> bmobQuery02=new BmobQuery<>();
                                                        bmobQuery02.addWhereEqualTo("c_id",user2.getObjectId());
//                                                        BmobQuery<APingLun> bmobQuery03=new BmobQuery<>();
//                                                        bmobQuery03.addWhereEqualTo("uaccount",user2.account);
//                                                        BmobQuery<APingLun> bmobQuery04=new BmobQuery<>();
//                                                        bmobQuery04.addWhereEqualTo("iaccount",user2.account);
//
//                                                        //或条件
//                                                        List<BmobQuery<APingLun>> queries04 = new ArrayList<BmobQuery<APingLun>>();
//                                                        queries04.add(bmobQuery02);
//                                                        queries04.add(bmobQuery03);
//                                                        queries04.add(bmobQuery04);
//                                                        BmobQuery<APingLun> mainQuery04 = new BmobQuery<APingLun>();
//                                                        mainQuery04.or(queries04);


                                                        bmobQuery02.findObjects(MyPubAct.this, new FindListener<APingLun>() {
                                                            @Override
                                                            public void onSuccess(List<APingLun> list) {
                                                                if (list!=null&&list.size()>0) {
                                                                    for (int i = 0; i < list.size(); i++) {
                                                                        APingLun user3 = list.get(i);
                                                                        user3.delete(MyPubAct.this, new DeleteListener() {
                                                                            @Override
                                                                            public void onSuccess() {
                                                                            }
                                                                            @Override
                                                                            public void onFailure(int i, String s) {
                                                                            }
                                                                        });
                                                                    }
                                                                }

                                                            }

                                                            @Override
                                                            public void onError(int i, String s) {

                                                            }
                                                        });

                                                    }
                                                    @Override
                                                    public void onFailure(int i, String s) {
                                                    }
                                                });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(int i, String s) {

                                    }
                                });


                                      //删除相关点赞
//                                String account2 = bean.account;
                                BmobQuery<Zan> bmobQuery1=new BmobQuery<>();
                                bmobQuery1.addWhereEqualTo("c_id",a);
                                bmobQuery1.findObjects(MyPubAct.this, new FindListener<Zan>() {
                                    @Override
                                    public void onSuccess(List<Zan> list) {
                                        if (list!=null&&list.size()>0) {
                                            for (int i = 0; i < list.size(); i++) {
                                                Zan user2 = list.get(i);
                                                user2.delete(MyPubAct.this, new DeleteListener() {
                                                    @Override
                                                    public void onSuccess() {
                                                    }
                                                    @Override
                                                    public void onFailure(int i, String s) {
                                                    }
                                                });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(int i, String s) {

                                    }
                                });



                                BmobQuery<APingLun> bmobQuery2=new BmobQuery<>();
                                bmobQuery2.addWhereEqualTo("c_id",a);
                                bmobQuery2.findObjects(MyPubAct.this, new FindListener<APingLun>() {
                                    @Override
                                    public void onSuccess(List<APingLun> list) {
                                        if (list!=null&&list.size()>0) {
                                            for (int i = 0; i < list.size(); i++) {
                                                APingLun user3 = list.get(i);
                                                user3.delete(MyPubAct.this, new DeleteListener() {
                                                    @Override
                                                    public void onSuccess() {
                                                    }
                                                    @Override
                                                    public void onFailure(int i, String s) {
                                                    }
                                                });
                                            }
                                        }

                                    }

                                    @Override
                                    public void onError(int i, String s) {

                                    }
                                });




















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
    //从数据库获取数据，并绑定到列表
    private void getData() {
        list.clear();
        BmobQuery<CircleBean> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account",userbean.account);
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(this, new FindListener<CircleBean>() {
            @Override
            public void onSuccess(List<CircleBean> list1) {
                list.addAll(list1);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MyPubAct.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
