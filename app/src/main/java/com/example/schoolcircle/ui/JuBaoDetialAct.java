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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.APingLun;
import com.example.schoolcircle.bean.CircleBean;
import com.example.schoolcircle.bean.GuanZhu;
import com.example.schoolcircle.bean.JuBao;
import com.example.schoolcircle.bean.LaHei;
import com.example.schoolcircle.bean.PingLun;
import com.example.schoolcircle.bean.User;
import com.example.schoolcircle.bean.Zan;
import com.example.schoolcircle.utils.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2020/3/18.
 */

public class JuBaoDetialAct extends BaseActivity{
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
    @BindView(R.id.tv_del)
    TextView tv_del; @BindView(R.id.tv_del2)
    TextView tv_del2;
    @BindView(R.id.tv_del3)
    TextView tv_del3;
    @BindView(R.id.tv_del1)
    TextView tv_del1;
    @BindView(R.id.recycler_img)
    RecyclerView recyclerImg;
    private JuBao bean;
    private CircleBean circleBean;
    private AlertDialog alertDialog;

    @Override
    public int intiLayout() {
        return R.layout.act_jb_detial;
    }

    @Override
    public void initView() {
        Bundle extras = getIntent().getExtras();
        bean = (JuBao) extras.getSerializable("bean");
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("举报详情");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_del.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(JuBaoDetialAct.this);
                builder.setTitle("确定删除吗？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                circleBean.delete(JuBaoDetialAct.this, new DeleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        bean.delete(JuBaoDetialAct.this, new DeleteListener() {
                                            @Override
                                            public void onSuccess() {
                                                toast("删除成功");
                                                finish();
                                            }

                                            @Override
                                            public void onFailure(int i, String s) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {

                                    }
                                });
                            }
                        });
                alertDialog = builder.create();
                alertDialog.show();





            }
        });
        tv_del1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(JuBaoDetialAct.this);
                builder.setTitle("确定删除吗？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bean.delete(JuBaoDetialAct.this, new DeleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        toast("删除成功");
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {

                                    }
                                });

                            }
                        });

                alertDialog = builder.create();
                alertDialog.show();






            }
        });
        tv_del2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(JuBaoDetialAct.this);
                builder.setTitle("确定删除吗？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                if(circleBean.account.equals("null")){
//
//                                }

                                circleBean.delete(JuBaoDetialAct.this, new DeleteListener() {
                                    @Override
                                    public void onSuccess() {





                                        String ak=bean.uaccount;



                                        //删除相关关注
                                        String account0 = bean.uaccount;
                                        BmobQuery<GuanZhu> bmobQuery0=new BmobQuery<>();
                                        bmobQuery0.addWhereEqualTo("u_id",account0);

                                        BmobQuery<GuanZhu> bmobQuery00=new BmobQuery<>();
                                        bmobQuery00.addWhereEqualTo("account",account0);

                                        //或条件
                                        List<BmobQuery<GuanZhu>> queries = new ArrayList<BmobQuery<GuanZhu>>();
                                        queries.add(bmobQuery0);
                                        queries.add(bmobQuery00);
                                        BmobQuery<GuanZhu> mainQuery = new BmobQuery<GuanZhu>();
                                        mainQuery.or(queries);

                                        mainQuery.findObjects(JuBaoDetialAct.this, new FindListener<GuanZhu>() {
                                            @Override
                                            public void onSuccess(List<GuanZhu> list) {
                                                if (list!=null&&list.size()>0) {
                                                    for (int i = 0; i < list.size(); i++) {
                                                        GuanZhu user0 = list.get(i);
                                                        user0.delete(JuBaoDetialAct.this, new DeleteListener() {
                                                            @Override
                                                            public void onSuccess() {
//                                                Toast.makeText(UserDetialAct.this, "aaa", Toast.LENGTH_SHORT).show();
                                                            }
                                                            @Override
                                                            public void onFailure(int i, String s) {
//                                                Toast.makeText(UserDetialAct.this, "bbb", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                            @Override
                                            public void onError(int i, String s) {

                                            }
                                        });




                                        //删除相关拉黑
                                        String account2 = bean.uaccount;
                                        BmobQuery<LaHei> bmobQuery2=new BmobQuery<>();
                                        bmobQuery2.addWhereEqualTo("u_id",account2);
                                        BmobQuery<LaHei> bmobQuery20=new BmobQuery<>();
                                        bmobQuery20.addWhereEqualTo("account",account2);

                                        //或条件
                                        List<BmobQuery<LaHei>> queries2 = new ArrayList<BmobQuery<LaHei>>();
                                        queries2.add(bmobQuery2);
                                        queries2.add(bmobQuery20);
                                        BmobQuery<LaHei> mainQuery2 = new BmobQuery<LaHei>();
                                        mainQuery2.or(queries2);

                                        mainQuery2.findObjects(JuBaoDetialAct.this, new FindListener<LaHei>() {
                                            @Override
                                            public void onSuccess(List<LaHei> list) {
                                                if (list!=null&&list.size()>0) {
                                                    for (int i = 0; i < list.size(); i++) {
                                                        LaHei user2 = list.get(i);
                                                        user2.delete(JuBaoDetialAct.this, new DeleteListener() {
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





                                        //删除相关评论
                                        String account3 = bean.uaccount;
                                        BmobQuery<PingLun> bmobQuery3=new BmobQuery<>();
                                        bmobQuery3.addWhereEqualTo("account",account3);
                                        BmobQuery<PingLun> bmobQuery30=new BmobQuery<>();
                                        bmobQuery30.addWhereEqualTo("account",account3);

                                        //或条件
                                        List<BmobQuery<PingLun>> queries3 = new ArrayList<BmobQuery<PingLun>>();
                                        queries3.add(bmobQuery3);
                                        queries3.add(bmobQuery30);
                                        BmobQuery<PingLun> mainQuery3 = new BmobQuery<PingLun>();
                                        mainQuery3.or(queries3);

                                        mainQuery3.findObjects(JuBaoDetialAct.this, new FindListener<PingLun>() {
                                            @Override
                                            public void onSuccess(List<PingLun> list) {
                                                if (list!=null&&list.size()>0) {
                                                    for (int i = 0; i < list.size(); i++) {
                                                        PingLun user3 = list.get(i);
                                                        user3.delete(JuBaoDetialAct.this, new DeleteListener() {
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






                                        //删除相关点赞
                                        String account4 = bean.uaccount;
                                        BmobQuery<Zan> bmobQuery4=new BmobQuery<>();
                                        bmobQuery4.addWhereEqualTo("account",account4);
                                        BmobQuery<Zan> bmobQuery40=new BmobQuery<>();
                                        bmobQuery40.addWhereEqualTo("account",account4);

                                        //或条件
                                        List<BmobQuery<Zan>> queries4 = new ArrayList<BmobQuery<Zan>>();
                                        queries4.add(bmobQuery4);
                                        queries4.add(bmobQuery40);
                                        BmobQuery<Zan> mainQuery4 = new BmobQuery<Zan>();
                                        mainQuery4.or(queries4);

                                        mainQuery4.findObjects(JuBaoDetialAct.this, new FindListener<Zan>() {
                                            @Override
                                            public void onSuccess(List<Zan> list) {
                                                if (list!=null&&list.size()>0) {
                                                    for (int i = 0; i < list.size(); i++) {
                                                        Zan user4 = list.get(i);
                                                        user4.delete(JuBaoDetialAct.this, new DeleteListener() {
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











                                        //删除动态
                                        String account1 = bean.uaccount;
                                        BmobQuery<CircleBean> bmobQuery=new BmobQuery<>();
                                        bmobQuery.addWhereEqualTo("account",account1);
                                        bmobQuery.findObjects(JuBaoDetialAct.this, new FindListener<CircleBean>() {
                                            @Override
                                            public void onSuccess(List<CircleBean> list) {
                                                if (list!=null&&list.size()>0) {
                                                    for (int i = 0; i < list.size(); i++) {
                                                        CircleBean user1 = list.get(i);

                                                        //顺便删除相关的人的评论
                                                        BmobQuery<PingLun> bmobQuerypl=new BmobQuery<>();
                                                        bmobQuerypl.addWhereEqualTo("c_id",user1.getObjectId());
                                                        bmobQuerypl.findObjects(JuBaoDetialAct.this, new FindListener<PingLun>() {
                                                            @Override
                                                            public void onSuccess(List<PingLun> list) {
                                                                if (list!=null&&list.size()>0) {
                                                                    for (int i = 0; i < list.size(); i++) {
                                                                        final PingLun user1 = list.get(i);
                                                                        final PingLun user2 = list.get(i);
                                                                        user1.delete(JuBaoDetialAct.this, new DeleteListener() {
                                                                            @Override
                                                                            public void onSuccess() {
                                                                                BmobQuery<APingLun> bmobQuery02=new BmobQuery<>();
                                                                                bmobQuery02.addWhereEqualTo("c_id",user2.getObjectId());
//                                                                                BmobQuery<APingLun> bmobQuery03=new BmobQuery<>();
//                                                                                bmobQuery03.addWhereEqualTo("uaccount",user2.account);
//                                                                                BmobQuery<APingLun> bmobQuery04=new BmobQuery<>();
//                                                                                bmobQuery04.addWhereEqualTo("iaccount",user2.account);
//
//                                                                                //或条件
//                                                                                List<BmobQuery<APingLun>> queries04 = new ArrayList<BmobQuery<APingLun>>();
//                                                                                queries04.add(bmobQuery02);
//                                                                                queries04.add(bmobQuery03);
//                                                                                queries04.add(bmobQuery04);
//                                                                                BmobQuery<APingLun> mainQuery04 = new BmobQuery<APingLun>();
//                                                                                mainQuery04.or(queries04);


                                                                                bmobQuery02.findObjects(JuBaoDetialAct.this, new FindListener<APingLun>() {
                                                                                    @Override
                                                                                    public void onSuccess(List<APingLun> list) {
                                                                                        if (list!=null&&list.size()>0) {
                                                                                            for (int i = 0; i < list.size(); i++) {
                                                                                                APingLun user3 = list.get(i);
                                                                                                user3.delete(JuBaoDetialAct.this, new DeleteListener() {
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



                                                        //顺便删除相关的人的点赞
                                                        BmobQuery<Zan> bmobQuerydz=new BmobQuery<>();
                                                        bmobQuerydz.addWhereEqualTo("c_id",user1.getObjectId());
                                                        bmobQuerydz.findObjects(JuBaoDetialAct.this, new FindListener<Zan>() {
                                                            @Override
                                                            public void onSuccess(List<Zan> list) {
                                                                if (list!=null&&list.size()>0) {
                                                                    for (int i = 0; i < list.size(); i++) {
                                                                        Zan user2 = list.get(i);
                                                                        user2.delete(JuBaoDetialAct.this, new DeleteListener() {
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






                                                        user1.delete(JuBaoDetialAct.this, new DeleteListener() {
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


                                       //删除动态相关盖楼






                                        bean.delete(JuBaoDetialAct.this, new DeleteListener() {
                                            @Override
                                            public void onSuccess() {
                                                String account = circleBean.account;
                                                BmobQuery<User> bmobQuery=new BmobQuery<>();
                                                bmobQuery.addWhereEqualTo("account",account);
                                                bmobQuery.findObjects(JuBaoDetialAct.this, new FindListener<User>() {
                                                    @Override
                                                    public void onSuccess(List<User> list) {
                                                        if (list!=null&&list.size()>0){
                                                            User user = list.get(0);
                                                            user.delete(JuBaoDetialAct.this, new DeleteListener() {
                                                                @Override
                                                                public void onSuccess() {
                                                                    toast("删除成功");
                                                                    finish();
                                                                }

                                                                @Override
                                                                public void onFailure(int i, String s) {

                                                                }
                                                            });
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

                                    @Override
                                    public void onFailure(int i, String s) {
                                        toast("失败失败");

                                    }
                                });

                            }
                        });

                alertDialog = builder.create();
                alertDialog.show();




            }
        });




        tv_del3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(JuBaoDetialAct.this);
                builder.setTitle("确定删除吗？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                circleBean.delete(JuBaoDetialAct.this, new DeleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        bean.delete(JuBaoDetialAct.this, new DeleteListener() {
                                            @Override
                                            public void onSuccess() {
                                                String account = circleBean.account;
                                                BmobQuery<User> bmobQuery=new BmobQuery<>();
                                                bmobQuery.addWhereEqualTo("account",account);
                                                bmobQuery.findObjects(JuBaoDetialAct.this, new FindListener<User>() {
                                                    @Override
                                                    public void onSuccess(List<User> list) {
                                                        if (list!=null&&list.size()>0){
                                                            User user = list.get(0);

                                                            if(user.userlimit.equals("1")){
                                                                Toast.makeText(JuBaoDetialAct.this, "封号失败，该用户为管理员", Toast.LENGTH_SHORT).show();
                                                            }
                                                            else {
                                                                user.fh = "1";
                                                                user.update(JuBaoDetialAct.this);
                                                                Toast.makeText(JuBaoDetialAct.this, "操作成功，用户已无法登录", Toast.LENGTH_SHORT).show();
                                                                finish();
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

                                    @Override
                                    public void onFailure(int i, String s) {

                                    }
                                });


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

    private void getdata(){
        BmobQuery<CircleBean> bmobQuery=new BmobQuery<>();
        bmobQuery.addWhereEqualTo("objectId",bean.c_id);
        bmobQuery.findObjects(this, new FindListener<CircleBean>() {
            @Override
            public void onSuccess(List<CircleBean> list) {
                if (list.size() > 0){
                    circleBean = list.get(0);
                tvNick.setText(TextUtils.isEmpty(circleBean.nick) ? circleBean.account : circleBean.nick);
                tvContent.setText(circleBean.content);
                tvTime.setText("发布时间：" + circleBean.getCreatedAt());

                if (!TextUtils.isEmpty(circleBean.head)) {
                    Glide.with(JuBaoDetialAct.this).load(circleBean.head).into(ivHead);
                }

                if (!TextUtils.isEmpty(circleBean.imgs)) {
                    String[] split = circleBean.imgs.split("&");
                    final ArrayList<String> pahtList = new ArrayList<>();
                    for (int i = 0; i < split.length; i++) {
                        pahtList.add(split[i]);
                    }
                    recyclerImg.setLayoutManager(new GridLayoutManager(JuBaoDetialAct.this, 3));
                    SimpleAdapter aaa = new SimpleAdapter<>(R.layout.item_photo, pahtList, new SimpleAdapter.ConVert<String>() {
                        @Override
                        public void convert(BaseViewHolder helper, final String path) {
                            ImageView view = helper.getView(R.id.iv);

                            Glide.with(JuBaoDetialAct.this).load(path).into(view);


                        }
                    });
                    recyclerImg.setAdapter(aaa);
                    aaa.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("urls", pahtList);

                            jumpAct(BigPhotoAct.class, bundle);

                        }
                    });
                }
            }
                else {


                    tv_del.setVisibility(View.GONE);

                    tv_del2.setVisibility(View.GONE);
                    tv_del3.setVisibility(View.GONE);
                    tv_del1.setText("删除该条举报");
                    getajunick(bean.uaccount);
//                    tvNick.setText(""+bean.account);
                    tvTime.setText("");


                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void getajunick(String uaccount) {
        BmobQuery<User> juj=new BmobQuery<>();
        juj.addWhereEqualTo("account",uaccount);
        juj.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> listjuj) {
                if(listjuj.size()>0){

                    tvNick.setText(TextUtils.isEmpty(listjuj.get(0).nick) ? listjuj.get(0).account : listjuj.get(0).nick);
                    tvContent.setText("该用户已删除动态");
                }
                else {
                    tvContent.setText("该用户已经销号");
                    tvNick.setText("用户已经销号");
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


}
