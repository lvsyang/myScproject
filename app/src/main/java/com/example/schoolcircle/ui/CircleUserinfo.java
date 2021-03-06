package com.example.schoolcircle.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.CircleBean;
import com.example.schoolcircle.bean.GuanZhu;
import com.example.schoolcircle.bean.PingLun;
import com.example.schoolcircle.bean.User;
import com.example.schoolcircle.bean.Zan;
import com.example.schoolcircle.utils.SimpleAdapter;
import com.guoxiaoxing.phoenix.core.listener.ImageLoader;
import com.guoxiaoxing.phoenix.picker.Phoenix;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class CircleUserinfo extends BaseActivity {

    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    Unbinder unbinder;
    @BindView(R.id.tv_title)
    TextView tvTitle; @BindView(R.id.tv_gz_num)
    TextView tv_gz_num;
    @BindView(R.id.tv_fs_num)
    TextView tv_fs_num;
    Unbinder unbinder1;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    private TextView mai;



    private User user;



    @BindView(R.id.recycler1)
    RecyclerView recycler;
    private List<CircleBean> list = new ArrayList<>();

    private SimpleAdapter<CircleBean> adapter;
    private AlertDialog alertDialog;
    private String a;


    @Override
    public int intiLayout() {
        return R.layout.act_gzinfo;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        a=intent.getStringExtra("uuu");

//        Toast.makeText(this, ""+a, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initView() {

        tvTitle.setText("????????????");
        ivBack.setVisibility(View.VISIBLE);


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
        getuserinfo();





        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleAdapter<>(R.layout.item_circle, list, new SimpleAdapter.ConVert<CircleBean>() {

            @Override
            public void convert(BaseViewHolder helper, CircleBean circleBean) {
                //??????????????????

                helper.setText(R.id.tv_nick, TextUtils.isEmpty(circleBean.nick)?circleBean.account:circleBean.nick);
                helper.setText(R.id.tv_content,circleBean.content);
                helper.setText(R.id.tv_time,"???????????????"+circleBean.getCreatedAt());

                mai=helper.itemView.findViewById(R.id.maichu);
                if(circleBean.type.equals("?????????")&&circleBean.saleflag.equals("??????"))
                {

                    mai.setVisibility(View.VISIBLE);
                }


//                mai.setVisibility(View.VISIBLE);

                TextView tv_plnum = helper.getView(R.id.tv_plnum);
                TextView tv_zan_num = helper.getView(R.id.tv_zan_num);
                getPlNum(circleBean.getObjectId(),tv_plnum);
                getZnNum(circleBean.getObjectId(),tv_zan_num);

                ImageView view = helper.getView(R.id.iv_head);
                if (!TextUtils.isEmpty(circleBean.head)){
                    Glide.with(CircleUserinfo.this).load(circleBean.head).into(view);
                }
                RecyclerView recyclerView = helper.getView(R.id.recycler_img);
                if (!TextUtils.isEmpty(circleBean.imgs)){
                    String[] split = circleBean.imgs.split("&");
                    final ArrayList<String> pahtList = new ArrayList<>();
                    for (int i = 0; i <split.length ; i++) {
                        pahtList.add(split[i]);
                    }
                    recyclerView.setLayoutManager(new GridLayoutManager(CircleUserinfo.this,3));
                    SimpleAdapter    aaa = new SimpleAdapter<>(R.layout.item_photo, pahtList, new SimpleAdapter.ConVert<String>() {
                        @Override
                        public void convert(BaseViewHolder helper, final String path) {
                            ImageView view = helper.getView(R.id.iv);

                            Glide.with(CircleUserinfo.this).load(path).into(view);



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

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {//???????????????????????????
                //???????????????????????????
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", list.get(position));
                jumpAct(CircleBeanInfo.class, bundle);
            }
        });


    }






    private void getuserinfo() {
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        Intent in1=getIntent();
        bmobQuery.addWhereEqualTo("account", in1.getStringExtra("uuu"));
        bmobQuery.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                Log.e("COUNT", list.size() + "---");

                user = list.get(0);
                tvAccount.append("????????? "+user.account);

                if(user.nick==null){tvNick.append("?????????????????????");}
                else {
                    tvNick.append("????????? "+user.nick);
                }


                if (user.img != null) {
                    Glide.with(CircleUserinfo.this).load(user.img.getUrl()).into(ivHead);
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(CircleUserinfo.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }





    @Override
    public void initData() {
        getData();
        getFenSiNum();
        getRealFenSiNum();


    }



    private void getFenSiNum(){
        BmobQuery<GuanZhu> bmobQuery=new BmobQuery<>();
        Intent in2=getIntent();
        bmobQuery.addWhereEqualTo("account",in2.getStringExtra("uuu"));
        bmobQuery.findObjects(this, new FindListener<GuanZhu>() {
            @Override
            public void onSuccess(List<GuanZhu> list) {
                tv_gz_num.setText(list.size()+"");
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void getRealFenSiNum(){
        BmobQuery<GuanZhu> bmobQuery=new BmobQuery<>();
        Intent in3=getIntent();
        bmobQuery.addWhereEqualTo("u_id",in3.getStringExtra("uuu"));
        bmobQuery.findObjects(this, new FindListener<GuanZhu>() {
            @Override
            public void onSuccess(List<GuanZhu> list) {
                tv_fs_num.setText(list.size()+"");
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    //?????????????????????????????????????????????
    private void getPlNum(String c_id, final TextView  textView) {

        BmobQuery<PingLun> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("c_id",c_id);
        bmobQuery.findObjects(this, new FindListener<PingLun>() {
            @Override
            public void onSuccess(List<PingLun> list1) {

                textView.setText("?????????"+list1.size()+"");

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }  //?????????????????????????????????????????????
    private void getZnNum(String c_id, final TextView  textView) {

        BmobQuery<Zan> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("c_id",c_id);
        bmobQuery.findObjects(this, new FindListener<Zan>() {
            @Override
            public void onSuccess(List<Zan> list1) {

                textView.setText("?????????"+list1.size());

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    //?????????????????????????????????????????????
    private void getData() {
        list.clear();
        BmobQuery<CircleBean> bmobQuery = new BmobQuery<>();
        Intent in4=getIntent();
        bmobQuery.addWhereEqualTo("account",in4.getStringExtra("uuu"));
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(this, new FindListener<CircleBean>() {
            @Override
            public void onSuccess(List<CircleBean> list1) {
                list.addAll(list1);

                adapter.notifyDataSetChanged();
//                Toast.makeText(MyGzinfo.this, list.size()+"??????", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(CircleUserinfo.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
//        ACache.get(MyGzinfo.this).clear();
        finish();
    }


}
