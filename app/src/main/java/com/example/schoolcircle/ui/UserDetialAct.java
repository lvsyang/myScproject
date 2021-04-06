package com.example.schoolcircle.ui;

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

public class UserDetialAct extends BaseActivity {
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

    @BindView(R.id.tv_del)
    TextView tv_del; @BindView(R.id.tv_del2)
    TextView tv_del2;
    @BindView(R.id.tv_del3)
    TextView tv_del3;
    @BindView(R.id.tv_del1)
    TextView tv_del1;

//    private JuBao bean;
//    private CircleBean circleBean;
private User bean;
private User user;

    @Override
    public int intiLayout() {
        return R.layout.act_user_detial;
    }

    @Override
    public void initView() {
        Bundle extras = getIntent().getExtras();
        bean = (User) extras.getSerializable("bean");
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("用户处理");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.delete(UserDetialAct.this, new DeleteListener() {
                    @Override
                    public void onSuccess() {


                        String ak=bean.account;



                        //删除相关关注
                        String account0 = bean.account;
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

                        mainQuery.findObjects(UserDetialAct.this, new FindListener<GuanZhu>() {
                            @Override
                            public void onSuccess(List<GuanZhu> list) {
                                if (list!=null&&list.size()>0) {
                                    for (int i = 0; i < list.size(); i++) {
                                        GuanZhu user0 = list.get(i);
                                        user0.delete(UserDetialAct.this, new DeleteListener() {
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
                        String account2 = bean.account;
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

                        mainQuery2.findObjects(UserDetialAct.this, new FindListener<LaHei>() {
                            @Override
                            public void onSuccess(List<LaHei> list) {
                                if (list!=null&&list.size()>0) {
                                    for (int i = 0; i < list.size(); i++) {
                                        LaHei user2 = list.get(i);
                                        user2.delete(UserDetialAct.this, new DeleteListener() {
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
                        String account3 = bean.account;
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

                        mainQuery3.findObjects(UserDetialAct.this, new FindListener<PingLun>() {
                            @Override
                            public void onSuccess(List<PingLun> list) {
                                if (list!=null&&list.size()>0) {
                                    for (int i = 0; i < list.size(); i++) {
                                        PingLun user3 = list.get(i);
                                        user3.delete(UserDetialAct.this, new DeleteListener() {
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
                        String account4 = bean.account;
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

                        mainQuery4.findObjects(UserDetialAct.this, new FindListener<Zan>() {
                            @Override
                            public void onSuccess(List<Zan> list) {
                                if (list!=null&&list.size()>0) {
                                    for (int i = 0; i < list.size(); i++) {
                                        Zan user4 = list.get(i);
                                        user4.delete(UserDetialAct.this, new DeleteListener() {
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
                        String account1 = bean.account;
                        BmobQuery<CircleBean> bmobQuery=new BmobQuery<>();
                        bmobQuery.addWhereEqualTo("account",account1);
                        bmobQuery.findObjects(UserDetialAct.this, new FindListener<CircleBean>() {
                            @Override
                            public void onSuccess(List<CircleBean> list) {
                                if (list!=null&&list.size()>0) {
                                    for (int i = 0; i < list.size(); i++) {
                                        CircleBean user1 = list.get(i);

                                        //顺便删除相关的人的评论
                                        BmobQuery<PingLun> bmobQuerypl=new BmobQuery<>();
                                        bmobQuerypl.addWhereEqualTo("c_id",user1.getObjectId());
                                        bmobQuerypl.findObjects(UserDetialAct.this, new FindListener<PingLun>() {
                                            @Override
                                            public void onSuccess(List<PingLun> list) {
                                                if (list!=null&&list.size()>0) {
                                                    for (int i = 0; i < list.size(); i++) {
                                                        PingLun user1 = list.get(i);
                                                        final PingLun user2 = list.get(i);
                                                        user1.delete(UserDetialAct.this, new DeleteListener() {
                                                            @Override
                                                            public void onSuccess() {
                                                                BmobQuery<APingLun> bmobQuery02=new BmobQuery<>();
                                                                bmobQuery02.addWhereEqualTo("c_id",user2.getObjectId());
//                                                                BmobQuery<APingLun> bmobQuery03=new BmobQuery<>();
//                                                                bmobQuery03.addWhereEqualTo("uaccount",user2.account);
//                                                                BmobQuery<APingLun> bmobQuery04=new BmobQuery<>();
//                                                                bmobQuery04.addWhereEqualTo("iaccount",user2.account);
//
//                                                                //或条件
//                                                                List<BmobQuery<APingLun>> queries04 = new ArrayList<BmobQuery<APingLun>>();
//                                                                queries04.add(bmobQuery02);
//                                                                queries04.add(bmobQuery03);
//                                                                queries04.add(bmobQuery04);
//                                                                BmobQuery<APingLun> mainQuery04 = new BmobQuery<APingLun>();
//                                                                mainQuery04.or(queries04);


                                                                bmobQuery02.findObjects(UserDetialAct.this, new FindListener<APingLun>() {
                                                                    @Override
                                                                    public void onSuccess(List<APingLun> list) {
                                                                        if (list!=null&&list.size()>0) {
                                                                            for (int i = 0; i < list.size(); i++) {
                                                                                APingLun user3 = list.get(i);
                                                                                user3.delete(UserDetialAct.this, new DeleteListener() {
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
                                        bmobQuerydz.findObjects(UserDetialAct.this, new FindListener<Zan>() {
                                            @Override
                                            public void onSuccess(List<Zan> list) {
                                                if (list!=null&&list.size()>0) {
                                                    for (int i = 0; i < list.size(); i++) {
                                                        Zan user2 = list.get(i);
                                                        user2.delete(UserDetialAct.this, new DeleteListener() {
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






                                    user1.delete(UserDetialAct.this, new DeleteListener() {
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

                        bean.delete(UserDetialAct.this, new DeleteListener() {
                            @Override
                            public void onSuccess() {
                                toast("删除成功10086");
                                finish();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                            }
                        });

                        toast("已经删除账户删除成功，相关动态也已被删除");
                        finish();


                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }
        });
        tv_del1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean.delete(UserDetialAct.this, new DeleteListener() {
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
        tv_del2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = bean.account;
                BmobQuery<User> bmobQuery=new BmobQuery<>();
                bmobQuery.addWhereEqualTo("account",account);
                bmobQuery.findObjects(UserDetialAct.this, new FindListener<User>() {
                    @Override
                    public void onSuccess(List<User> list) {
                        if (list!=null&&list.size()>0){
                            User user = list.get(0);

                            if(user.userlimit.equals("1")){
                                Toast.makeText(UserDetialAct.this, "封号失败，该用户为管理员", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                user.fh = "0";
                                user.update(UserDetialAct.this);
                                Toast.makeText(UserDetialAct.this, "操作成功，用户已正常使用登录", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }

                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }
        });




        tv_del3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = bean.account;
                BmobQuery<User> bmobQuery=new BmobQuery<>();
                bmobQuery.addWhereEqualTo("account",account);
                bmobQuery.findObjects(UserDetialAct.this, new FindListener<User>() {
                    @Override
                    public void onSuccess(List<User> list) {
                        if (list!=null&&list.size()>0){
                            User user = list.get(0);
//                                            user.delete(JuBaoDetialAct.this, new DeleteListener() {
//                                                @Override
//                                                public void onSuccess() {
//                                                    toast("删除成功");
//                                                    finish();
//                                                }
//
//                                                @Override
//                                                public void onFailure(int i, String s) {
//
//                                                }
//                                            });

                            if(user.userlimit.equals("1")){
                                Toast.makeText(UserDetialAct.this, "封号失败，该用户为管理员", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                user.fh = "1";
                                user.update(UserDetialAct.this);
                                Toast.makeText(UserDetialAct.this, "操作成功，用户已无法登录", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }

                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });


            }
        });



    }

    @Override
    public void initData() {
        getdata();
    }

    private void getdata(){
        BmobQuery<User> bmobQuery=new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account",bean.account);
        bmobQuery.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                user = list.get(0);
//                tvNick.setText(TextUtils.isEmpty(circleBean.nick)? circleBean.account: circleBean.nick);
                tvNick.setText("账号："+user.account);
//                tvContent.setText(circleBean.content);
                tvTime.setText("昵称："+ user.nick);

//                if (!TextUtils.isEmpty(user.img.getUrl())){
//                    Glide.with(UserDetialAct.this).load(user.img.getUrl()).into(ivHead);
//                }
//
//                if (!TextUtils.isEmpty(user.img.getUrl())){
//                    String[] split = user.img.getUrl().split("&");
//                    final ArrayList<String> pahtList = new ArrayList<>();
//                    for (int i = 0; i <split.length ; i++) {
//                        pahtList.add(split[i]);
//                    }
//                    recyclerImg.setLayoutManager(new GridLayoutManager(UserDetialAct.this,3));
//                    SimpleAdapter aaa = new SimpleAdapter<>(R.layout.item_photo, pahtList, new SimpleAdapter.ConVert<String>() {
//                        @Override
//                        public void convert(BaseViewHolder helper, final String path) {
//                            ImageView view = helper.getView(R.id.iv);
//
//                            Glide.with(UserDetialAct.this).load(path).into(view);
//
//
//
//                        }
//                    });
//                    recyclerImg.setAdapter(aaa);
//                    aaa.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("urls",pahtList);
//
//                            jumpAct(BigPhotoAct.class,bundle);
//
//                        }
//                    });
//                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
}
