package com.example.schoolcircle.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
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

public class MyGzinfo extends BaseActivity {

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


    @Override
    public int intiLayout() {
        return R.layout.act_gzinfo;
    }


    @Override
    public void initView() {

        tvTitle.setText("关注用户信息");
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
                //列表绑定数据

                helper.setText(R.id.tv_nick, TextUtils.isEmpty(circleBean.nick)?circleBean.account:circleBean.nick);
                helper.setText(R.id.tv_content,circleBean.content);
                helper.setText(R.id.tv_time,"发布时间："+circleBean.getCreatedAt());

                mai=helper.itemView.findViewById(R.id.maichu);
                if(circleBean.type.equals("任性卖")&&circleBean.saleflag.equals("卖出"))
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
                    Glide.with(MyGzinfo.this).load(circleBean.head).into(view);
                }
                RecyclerView recyclerView = helper.getView(R.id.recycler_img);
                if (!TextUtils.isEmpty(circleBean.imgs)){
                    String[] split = circleBean.imgs.split("&");
                    final ArrayList<String> pahtList = new ArrayList<>();
                    for (int i = 0; i <split.length ; i++) {
                        pahtList.add(split[i]);
                    }
                    recyclerView.setLayoutManager(new GridLayoutManager(MyGzinfo.this,3));
                    SimpleAdapter    aaa = new SimpleAdapter<>(R.layout.item_photo, pahtList, new SimpleAdapter.ConVert<String>() {
                        @Override
                        public void convert(BaseViewHolder helper, final String path) {
                            ImageView view = helper.getView(R.id.iv);

                            Glide.with(MyGzinfo.this).load(path).into(view);



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
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {//点击跳转到详情页面
                //点击列表跳转详情页
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", list.get(position));
                jumpAct(CircleBeanInfo.class, bundle);
            }
        });


    }



//    @Override
//    public void onResume() {
//        super.onResume();
//        //控件设置数据
//        userbean = (User) ACache.get(mActivity).getAsObject("userbean");
//
//        tvAccount.setText(userbean.account);
//        tvNick.setText("昵称:" + (TextUtils.isEmpty(userbean.nick) ? "无" : userbean.nick));
//        if (userbean.img != null) {
//            Glide.with(mActivity).load(userbean.img.getUrl()).into(ivHead);
//        }
//        getFenSiNum();
//        getRealFenSiNum();
//    }


    private void getuserinfo() {
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account", gzinfo.u_id);

        bmobQuery.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                Log.e("COUNT", list.size() + "---");
                user = list.get(0);



                tvAccount.append("账号： "+user.account);

                if(user.nick==null){tvNick.append("该用户暂无昵称");}
                else {
                    tvNick.append("昵称： "+user.nick);
                }


                if (user.img != null) {
                    Glide.with(MyGzinfo.this).load(user.img.getUrl()).into(ivHead);
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MyGzinfo.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
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
        bmobQuery.addWhereEqualTo("account",gzinfo.u_id);
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
        bmobQuery.addWhereEqualTo("u_id",gzinfo.u_id);
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

    //从数据库获取数据，并绑定到列表
    private void getPlNum(String c_id, final TextView  textView) {

        BmobQuery<PingLun> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("c_id",c_id);
        bmobQuery.findObjects(this, new FindListener<PingLun>() {
            @Override
            public void onSuccess(List<PingLun> list1) {

                textView.setText("评论数"+list1.size()+"");

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }  //从数据库获取数据，并绑定到列表
    private void getZnNum(String c_id, final TextView  textView) {

        BmobQuery<Zan> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("c_id",c_id);
        bmobQuery.findObjects(this, new FindListener<Zan>() {
            @Override
            public void onSuccess(List<Zan> list1) {

                textView.setText("点赞数"+list1.size());

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    //从数据库获取数据，并绑定到列表
    private void getData() {
        list.clear();
        BmobQuery<CircleBean> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account",gzinfo.u_id);
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(this, new FindListener<CircleBean>() {
            @Override
            public void onSuccess(List<CircleBean> list1) {
                list.addAll(list1);

                adapter.notifyDataSetChanged();
//                Toast.makeText(MyGzinfo.this, list.size()+"成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MyGzinfo.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
//        ACache.get(MyGzinfo.this).clear();
        finish();
    }


}
