package com.example.schoolcircle.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.CircleBean;
import com.example.schoolcircle.bean.YBuy;
import com.example.schoolcircle.utils.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class JiaoYiDetialAct extends BaseActivity {


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
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_salecontent)
    TextView salecontent;


    @BindView(R.id.tv_del)
    TextView tv_del; @BindView(R.id.tv_del2)
    TextView tv_del2;


    @BindView(R.id.recycler_img)
    RecyclerView recyclerImg;
    private YBuy bean;
    private CircleBean circleBean;
    private AlertDialog alertDialog;


    @Override
    public int intiLayout() {
        return R.layout.act_jy_detial;
    }

    @Override
    public void initView() {

        Bundle extras = getIntent().getExtras();
        bean = (YBuy) extras.getSerializable("bean");
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("详情");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(!userbean.account.equals(bean.maiaccount)||bean.saleflag.equals("售出")){
            tv_del.setVisibility(View.GONE);
            tv_del2.setVisibility(View.GONE);
        }


        tv_del.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(JiaoYiDetialAct.this);
                builder.setTitle("确定同意吗？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toast("交易成功，请按时与买家进行联系进行交易");
                        bean.saleflag="售出";
                        bean.update(JiaoYiDetialAct.this);
                        getupdat();
                        circleBean.saleflag="卖出";
                        circleBean.update(JiaoYiDetialAct.this);
//                        getreturn();

                        YBuy yes=new YBuy();
                        yes.content="交易成功";
                        yes.account=bean.account;
                        yes.maiaccount=bean.maiaccount;
                        yes.nick=bean.nick;
                        yes.mainick=bean.mainick;
                        yes.saleflag="已买";
                        yes.c_id=bean.getObjectId();//保存交易后卖家的与买家的物品信息页面
                        yes.save(JiaoYiDetialAct.this);


                        getdata();


                        finish();

                    }


                });
                alertDialog = builder.create();
                alertDialog.show();







            }

        });

        tv_del2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(JiaoYiDetialAct.this);
                builder.setTitle("确定不同意此次交易吗？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bean.delete(JiaoYiDetialAct.this);
                        finish();


                    }
                });

                alertDialog = builder.create();
                alertDialog.show();




            }
        });






    }

    @Override
    public void initData() {
        getdata();
    }
    private void getreturn() {
        YBuy yes=new YBuy();
        yes.content="交易成功";
        yes.account=bean.account;
        yes.maiaccount=bean.maiaccount;
        yes.nick=bean.nick;
        yes.mainick=bean.mainick;
        yes.save(JiaoYiDetialAct.this);

    }
    private void getupdat() {
        BmobQuery<YBuy> cc=new BmobQuery<>();
        cc.addWhereEqualTo("c_id",bean.c_id);
        cc.addWhereEqualTo("saleflag","待交易");
        cc.findObjects(this, new FindListener<YBuy>() {
            @Override
            public void onSuccess(List<YBuy> list) {
                YBuy y=new YBuy();
                if(list.size()>0) {
                    for (int l = 0; l < list.size(); l++) {
                        y = list.get(l);
                        y.delete(JiaoYiDetialAct.this);

                    }
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

    private void getdata(){
        BmobQuery<CircleBean> bmobQuery=new BmobQuery<>();
        bmobQuery.addWhereEqualTo("objectId",bean.c_id);
        bmobQuery.findObjects(this, new FindListener<CircleBean>() {
            @Override
            public void onSuccess(List<CircleBean> list) {
                circleBean = list.get(0);
                tvNick.setText(TextUtils.isEmpty(circleBean.nick)? circleBean.account: circleBean.nick);
                tvContent.setText(circleBean.content);
                tvTime.setText("发布时间："+ circleBean.getCreatedAt());
                salecontent.setText(bean.content);

                if (!TextUtils.isEmpty(circleBean.head)){
                    Glide.with(JiaoYiDetialAct.this).load(circleBean.head).into(ivHead);
                }

                if (!TextUtils.isEmpty(circleBean.imgs)){
                    String[] split = circleBean.imgs.split("&");
                    final ArrayList<String> pahtList = new ArrayList<>();
                    for (int i = 0; i <split.length ; i++) {
                        pahtList.add(split[i]);
                    }
                    recyclerImg.setLayoutManager(new GridLayoutManager(JiaoYiDetialAct.this,3));
                    SimpleAdapter aaa = new SimpleAdapter<>(R.layout.item_photo, pahtList, new SimpleAdapter.ConVert<String>() {
                        @Override
                        public void convert(BaseViewHolder helper, final String path) {
                            ImageView view = helper.getView(R.id.iv);

                            Glide.with(JiaoYiDetialAct.this).load(path).into(view);



                        }
                    });
                    recyclerImg.setAdapter(aaa);
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

            @Override
            public void onError(int i, String s) {

            }
        });
    }

}
