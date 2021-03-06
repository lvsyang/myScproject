package com.example.schoolcircle.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import com.example.schoolcircle.bean.MsgRead;
import com.example.schoolcircle.bean.PingLun;
import com.example.schoolcircle.bean.Tuizpt;
import com.example.schoolcircle.bean.Tuizpu;
import com.example.schoolcircle.bean.User;
import com.example.schoolcircle.bean.YBuy;
import com.example.schoolcircle.bean.Zan;
import com.example.schoolcircle.utils.SimpleAdapter;
import com.example.schoolcircle.utils.TimeUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2020/1/1.
 */

public class CircleBeanInfo extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tv_right; @BindView(R.id.tv_right1)
    TextView tv_right1;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_logtime)
    TextView tvLogtime;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.recycler_img)
    RecyclerView recyclerImg;
    @BindView(R.id.recycler_pl)
    RecyclerView recyclerPl;
    @BindView(R.id.iv_zan)
    ImageView ivZan;
    @BindView(R.id.tv_zan_num)
    TextView tvZanNum;  @BindView(R.id.tv_gz)
    TextView tv_gz;
    @BindView(R.id.tv_plnum)
    TextView tv_plnum;
    @BindView(R.id.et_pl)
    EditText etPl;
    @BindView(R.id.yuding)
    Button yuding;
    private CircleBean circleBean;
    private List<PingLun> pingLuns=new ArrayList<>();
    private List<APingLun> apingLuns=new ArrayList<>();
    private SimpleAdapter<PingLun> lunSimpleAdapter;
    private SimpleAdapter<APingLun> ilunSimpleAdapter;
    private AlertDialog alertDialog;
    private RecyclerView an_recyclerView;
    private TextView aspl_textView,ckgd;
    private String a1,a2;

    private String zanshu,pinglunshu,countplz;
    private int ccc,cp,cz;

    private ArrayAdapter<String> sadapter;
    private ArrayAdapter<String> sadapter2;
    Calendar calendar = Calendar.getInstance();
    int month1 = calendar.get(Calendar.MONTH)+1;
    //???
    int day1 = calendar.get(Calendar.DAY_OF_MONTH);
    private String a=month1+"???"+day1+"???";

    int day2 = calendar.get(Calendar.DAY_OF_MONTH)+1;
    private String b=month1+"???"+day2+"???";

    int day3 = calendar.get(Calendar.DAY_OF_MONTH)+2;
    private String c=month1+"???"+day3+"???";

    int day4 = calendar.get(Calendar.DAY_OF_MONTH)+3;
    private String d=month1+"???"+day4+"???";

    int day5 = calendar.get(Calendar.DAY_OF_MONTH)+4;
    private String e=month1+"???"+day5+"???";
    private String mainame;


    private  final String[] m1={a,b,c,d,e};
    private static final String[] m2={"??????7???","??????8???","??????9???","??????10???","??????11???","??????12???","??????12???","??????1???","??????2???","??????3???",
            "??????4???","??????5???","??????6???","??????7???","??????8???","??????9???","??????10???","??????11???"};
    private String ydtime1="";
    private String ydtime2="";

    private TextView an_nick,an_logtime,an_time,an_pub,an_time2;
    private RecyclerView recycler_anpl;
    private EditText icontent;
    private LinearLayout an_click;
    private SensitiveWordpl sensitiveWordpl;
    private ImageView apl_head;


    @Override
    public int intiLayout() {
        return R.layout.act_circle_info;
    }

    @Override
    public void initView() {
        tvTitle.setText("??????");
        ivBack.setVisibility(View.VISIBLE);
        Bundle extras = getIntent().getExtras();
        circleBean = (CircleBean) extras.getSerializable("bean");
        tvNick.setText(TextUtils.isEmpty(circleBean.nick)?circleBean.account:circleBean.nick);
//        tvLogtime.setText(circleBean);
        tvContent.setText(circleBean.content);
//        tvContent.setMovementMethod(new ScrollingMovementMethod());
        tvTime.setText("???????????????"+circleBean.getCreatedAt());
        tv_right.setText("??????");
        tv_right1.setText("??????");
        if (!TextUtils.isEmpty(circleBean.head)){
            Glide.with(this).load(circleBean.head).into(ivHead);
        }

        if (!TextUtils.isEmpty(circleBean.imgs)){
            String[] split = circleBean.imgs.split("&");
            final ArrayList<String> pahtList = new ArrayList<>();
            for (int i = 0; i <split.length ; i++) {
                pahtList.add(split[i]);
            }
            recyclerImg.setLayoutManager(new GridLayoutManager(this,3));
            SimpleAdapter aaa = new SimpleAdapter<>(R.layout.item_photo, pahtList, new SimpleAdapter.ConVert<String>() {
                @Override
                public void convert(BaseViewHolder helper, final String path) {
                    ImageView view = helper.getView(R.id.iv);

                    Glide.with(CircleBeanInfo.this).load(path).into(view);



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

        recyclerPl.setLayoutManager(new LinearLayoutManager(this));
        lunSimpleAdapter = new SimpleAdapter<PingLun>(R.layout.item_pl, pingLuns, new SimpleAdapter.ConVert<PingLun>() {
            @Override
            public void convert(final BaseViewHolder helper, final PingLun pingLun) {
                    helper.setText(R.id.tv_time,pingLun.getCreatedAt());
                    helper.setText(R.id.tv_content,pingLun.content);
                    helper.setText(R.id.tv_nick,TextUtils.isEmpty(pingLun.nick)?pingLun.account:pingLun.nick);



                TextView tv_ckgds = helper.getView(R.id.tv_ckgd);//???????????????????????????????????????
                getPlNum(pingLun.getObjectId(),tv_ckgds);



                       //??????????????????
                ckgd=helper.itemView.findViewById(R.id.tv_ckgd);

                ckgd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getAData(pingLun.getObjectId().toString().trim());//?????????????????????????????????
//                        jumpAct(AnswerAct.class);

                        final Dialog dialog = new Dialog(CircleBeanInfo.this,R.style.BottomDialog);
                        LinearLayout root = (LinearLayout) LayoutInflater.from(CircleBeanInfo.this).inflate(R.layout.item_answerpl,null);


                        an_nick=root.findViewById(R.id.an_nick);
                        an_logtime=root.findViewById(R.id.an_logtime);

                        an_time=root.findViewById(R.id.an_time);
//                        apl_head=root.findViewById(R.id.apl_head);


                        recycler_anpl=root.findViewById(R.id.recycler_anpl);

                        an_nick.append(TextUtils.isEmpty(pingLun.nick)?pingLun.account:pingLun.nick);
                        an_logtime.append(pingLun.getCreatedAt());
                        an_time.append(pingLun.getCreatedAt());

                        ImageView view1 = root.findViewById(R.id.iv_headapl);

                        if (!TextUtils.isEmpty(pingLun.head)){
                            Glide.with(CircleBeanInfo.this).load(pingLun.head).into(view1);
                        }

//                        Toast.makeText(CircleBeanInfo.this, "okok", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(CircleBeanInfo.this, apingLuns.get(0).content+"lll", Toast.LENGTH_SHORT).show();

//                        recycler_anpl = LayoutInflater.from(CircleBeanInfo.this).inflate(R.layout.item, root, false);
                        recycler_anpl.setLayoutManager(new LinearLayoutManager(CircleBeanInfo.this));
                        ilunSimpleAdapter=new SimpleAdapter<APingLun>(R.layout.item_ipl, apingLuns, new SimpleAdapter.ConVert<APingLun>() {
                            @Override
                            public void convert(BaseViewHolder helper1, final APingLun aPingLun) {
                                helper1.setText(R.id.an_i_nick,TextUtils.isEmpty(aPingLun.nick)?aPingLun.iaccount:aPingLun.nick);
                                helper1.setText(R.id.an_u_nick,TextUtils.isEmpty(aPingLun.unick)?aPingLun.uaccount:aPingLun.unick);
                                helper1.setText(R.id.an_i_content,aPingLun.content);
                                helper.setText(R.id.tv_nick,TextUtils.isEmpty(pingLun.nick)?pingLun.account:pingLun.nick);
                                helper1.setText(R.id.an_time2,aPingLun.getCreatedAt());
                                ImageView view = helper1.getView(R.id.iv_head2);

                                if (!TextUtils.isEmpty(aPingLun.head)){
                                    Glide.with(CircleBeanInfo.this).load(aPingLun.head).into(view);
                                }



                                //??????????????????
                                an_click=helper1.itemView.findViewById(R.id.an_click);
                                an_click.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                        Toast.makeText(CircleBeanInfo.this, "777", Toast.LENGTH_SHORT).show();

                                        final Dialog dialog = new Dialog(CircleBeanInfo.this,R.style.BottomDialog);
                                        LinearLayout root1 = (LinearLayout) LayoutInflater.from(CircleBeanInfo.this).inflate(R.layout.item_pubanswerpl,null);

                                        an_pub=root1.findViewById(R.id.an_pub);
                                        icontent=root1.findViewById(R.id.an_content);
                                        an_pub.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                APingLun aPingLun1=new APingLun();
                                                if (userbean.imgflag.equals("1")){
                                                    aPingLun1.head=userbean.img.getUrl();
                                                }

//                                                aPingLun.head=pingLun.head;
                                                aPingLun1.iaccount=userbean.account;
                                                aPingLun1.uaccount=aPingLun.iaccount;
                                                sensitiveWordpl=new SensitiveWordpl();
                                                sensitiveWordpl.InitializationWork();
                                                aPingLun1.content=sensitiveWordpl.filterInfo(icontent.getText().toString().trim());
                                                aPingLun1.c_id=pingLun.getObjectId();
                                                aPingLun1.nick=userbean.nick;

                                                aPingLun1.unick=aPingLun.nick;


                                                aPingLun1.save(CircleBeanInfo.this);
                                                msgcount(aPingLun.iaccount,"aPingLun");
                                                Toast.makeText(CircleBeanInfo.this, "????????????", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();

                                            }
                                        });




                                        dialog.setContentView(root1);
                                        Window window = dialog.getWindow();
                                        window.setGravity(Gravity.BOTTOM);
                                        WindowManager.LayoutParams lp = window.getAttributes();
                                        lp.x = 0;
                                        lp.y = 0;
                                        lp.width = getResources().getDisplayMetrics().widthPixels;
                                        root1.measure(0,0);
                                        lp.height = root1.getMeasuredHeight();
                                        lp.alpha = 9f;
                                        window.setAttributes(lp);
                                        dialog.show();



                                    }
                                });





//                                Toast.makeText(CircleBeanInfo.this, aPingLun.content+"wewe", Toast.LENGTH_SHORT).show();
                            }

                        });
                        recycler_anpl.setAdapter(ilunSimpleAdapter);


                        dialog.setContentView(root);
                        Window window = dialog.getWindow();
                        window.setGravity(Gravity.BOTTOM);
                        WindowManager.LayoutParams lp = window.getAttributes();
                        lp.x = 0;
                        lp.y = 0;
                        lp.width = getResources().getDisplayMetrics().widthPixels;
                        root.measure(0,0);
                        lp.height = root.getMeasuredHeight();
                        lp.alpha = 9f;
                        window.setAttributes(lp);
                        dialog.show();
                    }
                });





               //??????????????????????????????????????????
                aspl_textView=helper.itemView.findViewById(R.id.tv_btpl);
                aspl_textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(CircleBeanInfo.this,R.style.BottomDialog);
                        LinearLayout root1 = (LinearLayout) LayoutInflater.from(CircleBeanInfo.this).inflate(R.layout.item_pubanswerpl,null);

                        an_pub=root1.findViewById(R.id.an_pub);
                        icontent=root1.findViewById(R.id.an_content);
                        an_pub.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                APingLun aPingLun=new APingLun();
                                aPingLun.iaccount=userbean.account;
                                aPingLun.uaccount=pingLun.account;
                                sensitiveWordpl=new SensitiveWordpl();
                                sensitiveWordpl.InitializationWork();

                                String acot=icontent.getText().toString().trim();
                                aPingLun.content=sensitiveWordpl.filterInfo(acot);
                                aPingLun.c_id=pingLun.getObjectId();
                                aPingLun.nick=userbean.nick;
                                if (userbean.img!=null){
                                    aPingLun.head=userbean.img.getUrl();
                                }
//                                aPingLun.head=pingLun.head;
                                aPingLun.unick=pingLun.nick;
                                aPingLun.save(CircleBeanInfo.this);

                                msgcount(pingLun.account,"aPingLun");

                                Toast.makeText(CircleBeanInfo.this, "????????????", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            }
                        });




                        dialog.setContentView(root1);
                        Window window = dialog.getWindow();
                        window.setGravity(Gravity.BOTTOM);
                        WindowManager.LayoutParams lp = window.getAttributes();
                        lp.x = 0;
                        lp.y = 0;
                        lp.width = getResources().getDisplayMetrics().widthPixels;
                        root1.measure(0,0);
                        lp.height = root1.getMeasuredHeight();
                        lp.alpha = 9f;
                        window.setAttributes(lp);
                        dialog.show();


                    }
                });



                ImageView view = helper.getView(R.id.iv_head);
                if (!TextUtils.isEmpty(pingLun.head)){
                    Glide.with(CircleBeanInfo.this).load(pingLun.head).into(view);
                }








            }


        });
        recyclerPl.setAdapter(lunSimpleAdapter);
    }

    @Override
    public void initData() {
        getData();

        getzannum();
        getzan();
        isGuzhu(0);
        getuse();


    }

    private void getAData(String ccid) {//??????????????????????????????
        apingLuns.clear();
        BmobQuery<APingLun> bmobQuery1 = new BmobQuery<>();
        bmobQuery1.addWhereEqualTo("c_id",ccid);//????????????????????????????????????
        bmobQuery1.order("-createdAt");
        bmobQuery1.findObjects(this, new FindListener<APingLun>() {
            @Override
            public void onSuccess(List<APingLun> listk) {
                apingLuns.addAll(listk);

                ilunSimpleAdapter.notifyDataSetChanged();
//                Toast.makeText(CircleBeanInfo.this, "okok", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(int i, String s) {

            }
        });


    }

    private void getuse() {//?????????????????????????????????
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account",circleBean.account);
        bmobQuery.findObjects(this, new FindListener<User>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(List<User> list1) {



          //?????????????????????????????????
                TimeUtil tt=new TimeUtil();

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//????????????
                Date date1 = null;
                try {
                    date1 = format.parse(list1.get(0).logtime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                return date;


                String tt1=tt.getTimeFormatText(date1);

                tvLogtime.append(tt1+"?????????");

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(CircleBeanInfo.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });


    }






    //?????????????????????????????????????????????
    private void getData() {
        pingLuns.clear();
        BmobQuery<PingLun> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("c_id",circleBean.getObjectId());//????????????????????????????????????
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(this, new FindListener<PingLun>() {
            @Override
            public void onSuccess(List<PingLun> list1) {
                if(circleBean.type.equals("?????????")){
                    if(circleBean.saleflag.equals("??????")){
                    yuding.setVisibility(View.GONE);
                    }
                    else { yuding.setVisibility(View.VISIBLE);}

                    if(circleBean.account.equals(userbean.account)){
                        pingLuns.addAll(list1);
                    }
                    else {
                        for(int i=0;i<list1.size();i++){
                            if(list1.get(i).account.equals(circleBean.account)||list1.get(i).account.equals(userbean.account)){
                                pingLuns.add(list1.get(i));
                            }
                        }
                    }

                }
                else{
                pingLuns.addAll(list1);
                }
                tv_plnum.setText("????????????("+list1.size()+")");//list1.size()????????????????????????


                if(circleBean.zanshu.isEmpty()) {
                    pinglunshu = "" + list1.size();//------------------------------------------------------------------------------

//                coup=Integer.parseInt(pinglunshu);
//                countplz=couz+"";
                    circleBean.pinglunshu = pinglunshu;
                    ccc = Integer.parseInt(pinglunshu);
                    circleBean.countplz = ccc + "";
                    circleBean.ccplz = ccc;
                    circleBean.update(CircleBeanInfo.this);//??????????????????????????????????????????????????????
                }
                else{

                    pinglunshu = "" + list1.size();//------------------------------------------------------------------------------

//                coup=Integer.parseInt(pinglunshu);
//                countplz=couz+"";

                    circleBean.pinglunshu = pinglunshu;
                    ccc = Integer.parseInt(pinglunshu) + Integer.parseInt(circleBean.zanshu);
                    circleBean.countplz = ccc + "";
                    circleBean.ccplz = ccc;
                    circleBean.update(CircleBeanInfo.this);//??????????????????????????????????????????????????????

                }



                lunSimpleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(CircleBeanInfo.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //?????????????????????????????????????????????
    private void getPlNum(String c_id, final TextView  textView) {

        BmobQuery<APingLun> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("c_id",c_id);
        bmobQuery.findObjects(this, new FindListener<APingLun>() {
            @Override
            public void onSuccess(List<APingLun> list1) {

                textView.setText("????????????"+list1.size()+"");

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


    @OnClick({R.id.iv_back, R.id.iv_zf,R.id.tv_right,R.id.tv_right1, R.id.iv_zan, R.id.tv_gz, R.id.tv_send,R.id.iv_head,R.id.yuding})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
                case R.id.tv_right:
                AlertDialog.Builder builder1=new AlertDialog.Builder(CircleBeanInfo.this);
                    View inflate = View.inflate(CircleBeanInfo.this, R.layout.dialog_jb, null);
                    final RadioButton rb1 = inflate.findViewById(R.id.rb1);
                    final RadioButton rb2 = inflate.findViewById(R.id.rb2);
                    final RadioButton rb3= inflate.findViewById(R.id.rb3);
                    TextView tv_ok= inflate.findViewById(R.id.tv_ok);
                    tv_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String contentjb="????????????/????????????";
                            if (rb1.isChecked()){
                                contentjb="????????????/????????????";
                            }  if (rb2.isChecked()){
                                contentjb="????????????????????????";
                            }  if (rb3.isChecked()){
                                contentjb="??????????????????";
                            }
                            jubao(contentjb);
                            alertDialog.dismiss();
                        }
                    });
                    builder1.setView(inflate);

                     alertDialog = builder1.create();
                     alertDialog.show();
                    break;
            case R.id.tv_right1:
                AlertDialog.Builder builder2=new AlertDialog.Builder(CircleBeanInfo.this);
                builder2.setTitle("??????????????????");
                builder2.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CircleBeanInfo.this.alertDialog.dismiss();
                    }
                });
                builder2.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        lahei();
                        CircleBeanInfo.this.alertDialog.dismiss();
                    }
                });
                this.alertDialog = builder2.create();
                this.alertDialog.show();
                break;
                case R.id.iv_zf:
                AlertDialog.Builder builder=new AlertDialog.Builder(CircleBeanInfo.this);
                builder.setTitle("??????????????????");
                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CircleBeanInfo.this.alertDialog.dismiss();
                    }
                });
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pub();
                        CircleBeanInfo.this.alertDialog.dismiss();
                    }
                });
                this.alertDialog = builder.create();
                this.alertDialog.show();
                break;
            case R.id.iv_zan:
                isZan();
                break;
            case R.id.tv_gz:
                isGuzhu(1);
                break;
            case R.id.tv_send:
                String s = etPl.getText().toString();
                if (TextUtils.isEmpty(s)){
                    toast("?????????");
                    return;
                }
                sendPl(s);
                if(circleBean.account.equals(userbean.account)){}
                else {
                msgcount(circleBean.account,"PingLun");}
                break;
            case R.id.iv_head:
                Intent intent = new Intent(this,CircleUserinfo.class);
                intent.putExtra("uuu",circleBean.account);
                startActivity(intent);//??????Activity
//                Toast.makeText(this, ""+circleBean.account, Toast.LENGTH_SHORT).show();
                break;
            case R.id.yuding:
                getcheck();


                break;
        }
    }



    private void msgcount(String msgaccount, final String mtypes) {
        final BmobQuery<MsgRead> msg=new BmobQuery<>();
        msg.addWhereEqualTo("account",msgaccount);
        msg.addWhereEqualTo("mtype",mtypes);
        msg.findObjects(this, new FindListener<MsgRead>() {
            @Override
            public void onSuccess(List<MsgRead> listmsg) {
                MsgRead msgRead=new MsgRead();
                if(listmsg.size()==0){
                    msgRead.account=circleBean.account;
                    msgRead.countread=1;
                    msgRead.mtype=mtypes;
                    msgRead.save(CircleBeanInfo.this);
                }
                else {
                    msgRead=listmsg.get(0);
                    msgRead.countread++;
                    msgRead.update(CircleBeanInfo.this);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }



    private void getcheck() {
        BmobQuery<YBuy> yy=new BmobQuery<>();
        yy.addWhereEqualTo("c_id",circleBean.getObjectId());
        yy.addWhereEqualTo("account",userbean.account);
        yy.findObjects(this, new FindListener<YBuy>() {
            @Override
            public void onSuccess(List<YBuy> list) {
                if(list.size()==0&&!circleBean.account.equals(userbean.account)){

                    AlertDialog.Builder builder3=new AlertDialog.Builder(CircleBeanInfo.this);
                    View inflate1 = View.inflate(CircleBeanInfo.this, R.layout.dialog_yd, null);


                    final EditText e1=inflate1.findViewById(R.id.yd_phone);
                    final EditText e2=inflate1.findViewById(R.id.yd_place);

                    final Spinner spinner1=inflate1.findViewById(R.id.spiner1);
                    final Spinner spinner2=inflate1.findViewById(R.id.spiner2);
                    //??????????????????ArrayAdapter????????????
                    sadapter = new ArrayAdapter<String>(CircleBeanInfo.this,android.R.layout.simple_spinner_item,m1);
                    sadapter2 = new ArrayAdapter<String>(CircleBeanInfo.this,android.R.layout.simple_spinner_item,m2);

                    //???????????????????????????
                    sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sadapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???adapter ?????????spinner???
                    spinner1.setAdapter(sadapter);
                    spinner2.setAdapter(sadapter2);

                    //????????????Spinner????????????

                    spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            ydtime1=m1[position];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            ydtime2=m2[position];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    //???????????????
                    spinner1.setVisibility(View.VISIBLE);
                    spinner2.setVisibility(View.VISIBLE);



                    TextView tv_ok1= inflate1.findViewById(R.id.tv_ok);
                    tv_ok1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            YBuy yBuy=new YBuy();
                            yBuy.account=userbean.account;
                            yBuy.maiaccount=circleBean.account;
                            yBuy.nick=userbean.nick;
                            yBuy.c_id=circleBean.getObjectId();
                            yBuy.mainick=circleBean.nick;

                            yBuy.content="?????????"+(TextUtils.isEmpty(userbean.nick)?userbean.account:userbean.nick)+"\n"+"????????????:"+e1.getText().toString().trim()+"\n"+
                                    "????????????:"+ydtime1+"  "+ydtime2+"\n"+"????????????:"+e2.getText();
                            yBuy.saleflag="?????????";
                            yBuy.save(CircleBeanInfo.this);
                            Toast.makeText(CircleBeanInfo.this, "????????????", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }





                    });
                    builder3.setView(inflate1);

                    alertDialog = builder3.create();
                    alertDialog.show();



                }
                else if(circleBean.account.equals(userbean.account)){
                    Toast.makeText(CircleBeanInfo.this, "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(CircleBeanInfo.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


    private void lahei() {
        if(circleBean.account.equals(userbean.account)){
            toast("?????????????????????");
        }
        else{

        LaHei zan=new LaHei();
        zan.account=userbean.account;
        zan.u_id=circleBean.account;
        zan.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                toast("???????????????????????????????????????????????????????????????");
                delgz();

                finish();

            }



            @Override
            public void onFailure(int i, String s) {

            }
        });}
    }

    private void delgz() {
        BmobQuery<GuanZhu> gz=new BmobQuery<>();
        gz.addWhereEqualTo("account",userbean.account);
        BmobQuery<GuanZhu> gz2=new BmobQuery<>();
        gz2.addWhereEqualTo("u_id",userbean.account);
        //?????????
        List<BmobQuery<GuanZhu>> queries = new ArrayList<BmobQuery<GuanZhu>>();
        queries.add(gz);
        queries.add(gz2);
        BmobQuery<GuanZhu> mainQuery = new BmobQuery<GuanZhu>();
        mainQuery.or(queries);
        mainQuery.findObjects(this, new FindListener<GuanZhu>() {
            GuanZhu gz=new GuanZhu();
            @Override
            public void onSuccess(List<GuanZhu> list) {
                for(int i=0;i<list.size();i++)
                {
                    gz=list.get(i);
                    gz.delete(CircleBeanInfo.this);

                }

            }

            @Override
            public void onError(int i, String s) {

            }
        });


    }


    private void jubao(final String contentss){
        if(circleBean.account.equals(userbean.account)){
            toast("?????????????????????");
        }
        else {

            BmobQuery<JuBao> jj=new BmobQuery<>();
            jj.addWhereEqualTo("account",userbean.account);
            jj.addWhereEqualTo("c_id",circleBean.getObjectId());
            jj.findObjects(this, new FindListener<JuBao>() {
                @Override
                public void onSuccess(List<JuBao> listjj) {
                    if(listjj.size()==0){

                        JuBao juBao = new JuBao();
                        juBao.account = userbean.account;
                        juBao.content = contentss;
                        juBao.c_id = circleBean.getObjectId();
                        juBao.uaccount=circleBean.account;
                        juBao.save(CircleBeanInfo.this);
                        toast("?????????????????????????????????????????????");
                    }
                    else {toast("??????????????????????????????????????????");}
                }

                @Override
                public void onError(int i, String s) {

                }
            });

        }
    }
    private void isGuzhu(final int type) {
        BmobQuery<GuanZhu> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("u_id",circleBean.account);//???????????????
        bmobQuery.addWhereEqualTo("account",userbean.account);//???????????????
        bmobQuery.findObjects(this, new FindListener<GuanZhu>() {
            @Override
            public void onSuccess(List<GuanZhu> list1) {
                if (type==0){//???????????????????????????????????????
                    if (list1.size()>0){
                        tv_gz.setText("?????????");
                    }else {
                        tv_gz.setText("??????");
                    }
                }else {//??????????????????????????????????????????
                    if (list1.size()>0){
                        GuanZhu guanZhu = list1.get(0);
                        guanZhu.delete(CircleBeanInfo.this, new DeleteListener() {
                            @Override
                            public void onSuccess() {
                                toast("????????????");
                                tv_gz.setText("??????");

                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });
                    }else {
                        guanzhu();
                    }
                }


            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(CircleBeanInfo.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guanzhu() {
        if(circleBean.account.equals(userbean.account)){
            toast("?????????????????????");
        }
        else{
        GuanZhu zan=new GuanZhu();
        zan.account=userbean.account;
        zan.u_id=circleBean.account;
        zan.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                toast("????????????");
                 tv_gz.setText("?????????");
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });

        }



    }

    private void pub(){
        CircleBean circleBean1=new CircleBean();
        circleBean1.account=userbean.account;

        circleBean1.content="?????????"+circleBean.account+":\n"+circleBean.content;
        circleBean1.type="?????????";
        if (!TextUtils.isEmpty(userbean.nick)){
            circleBean1.nick=userbean.nick;
        }
        if (userbean.img!=null){
            circleBean1.head=userbean.img.getUrl();
        }
        circleBean1.imgs=circleBean.imgs;
        circleBean1.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                toast("????????????");

            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }




//    private void pub2(){
//        CircleBean circleBean2=new CircleBean();
//        circleBean2.account=userbean.account;
//        circleBean2.content="?????????"+circleBean.account+":\n"+circleBean.content;
//        circleBean2.type="?????????";
//        if (!TextUtils.isEmpty(userbean.nick)){
//            circleBean2.nick=userbean.nick;
//        }
//        if (userbean.img!=null){
//            circleBean2.head=userbean.img.getUrl();
//        }
//        circleBean2.imgs=circleBean.imgs;
//        circleBean2.save(this, new SaveListener() {
//            @Override
//            public void onSuccess() {
//                toast("????????????");
//
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//
//            }
//        });
//    }




    private void zan(){
        Zan zan=new Zan();
        zan.account=userbean.account;
        zan.nick=userbean.nick;
        zan.zcircontent=circleBean.content;
        zan.c_id=circleBean.getObjectId();
        zan.head=circleBean.head;

        zan.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                toast("????????????");
                ivZan.setImageResource(R.mipmap.zan_s);
                getzannum();

                gettuizan();
                gettuitp();
                if(circleBean.account.equals(userbean.account)){}
                else {
                msgcount(circleBean.account,"Zan");}





            }




            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    private void gettuitp() {
        String acc=circleBean.topic;

         String strk="";

        for(int j=0;j<acc.length();j++){

            if(acc.charAt(j)!='&'){
                strk=strk+acc.charAt(j);

            }
            else {
                //???????????????strk?????????????????????User?????????account????????????????????????

                strk=strk+"&";
                System.out.println(strk);

        BmobQuery<Tuizpt> tzt=new BmobQuery();
        tzt.addWhereEqualTo("account",userbean.account);
        tzt.addWhereEqualTo("utopic",strk);//???????????????contain??????
                final String finalStrk = strk;
                tzt.findObjects(this, new FindListener<Tuizpt>() {
            @Override
            public void onSuccess(List<Tuizpt> list) {
                Tuizpt tuizpt=new Tuizpt();
                if(list.size()>0){

//                    for(int i=0;i<list.size();i++){
                        tuizpt=list.get(0);
                        tuizpt.ucount++;
                        tuizpt.update(CircleBeanInfo.this);
//                    }

//                    tuizpt=list.get(0);
//                    tuizpt.ucount++;
//                    tuizpt.update(CircleBeanInfo.this);
                }
                else {
                    Tuizpt tuizpt1=new Tuizpt();
                    tuizpt1.account=userbean.account;
                    //?????????????????????????????????????????????????????????topic??????????????????ok??????????????????topic???????????????
                    tuizpt1.utopic= finalStrk;
                    tuizpt1.ucount=1;
                    tuizpt1.save(CircleBeanInfo.this);

                }
            }

            @Override
            public void onError(int i, String s) {
                Tuizpt tuizpt=new Tuizpt();
                tuizpt.account=userbean.account;
                //?????????????????????????????????????????????????????????topic??????????????????ok??????????????????topic???????????????
                tuizpt.utopic=circleBean.topic;
                tuizpt.ucount=199;
                tuizpt.save(CircleBeanInfo.this);

            }
        });


                strk="";
//                j++;
            }

        }




    }


    private void getqutuitp() {
        String acc=circleBean.topic;

        String strk="";

        for(int j=0;j<acc.length();j++){

            if(acc.charAt(j)!='&'){
                strk=strk+acc.charAt(j);

            }
            else {
                //???????????????strk?????????????????????User?????????account????????????????????????

                strk=strk+"&";
                System.out.println(strk);

                BmobQuery<Tuizpt> tzt=new BmobQuery();
                tzt.addWhereEqualTo("account",userbean.account);
                tzt.addWhereEqualTo("utopic",strk);//???????????????contain??????
                final String finalStrk1 = strk;
                tzt.findObjects(this, new FindListener<Tuizpt>() {
                    @Override
                    public void onSuccess(List<Tuizpt> list) {
                        Tuizpt tuizpt=new Tuizpt();
                        if(list.size()>0){

//                    for(int i=0;i<list.size();i++){
                            tuizpt=list.get(0);
                            tuizpt.ucount--;
                            tuizpt.update(CircleBeanInfo.this);
//                    }

//                    tuizpt=list.get(0);
//                    tuizpt.ucount++;
//                    tuizpt.update(CircleBeanInfo.this);
                        }
                        else {
//                            Tuizpt tuizpt1=new Tuizpt();
//                            tuizpt1.account=userbean.account;
//                            //?????????????????????????????????????????????????????????topic??????????????????ok??????????????????topic???????????????
//                            tuizpt1.utopic= finalStrk1;
//                            tuizpt1.ucount=1;
//                            tuizpt1.save(CircleBeanInfo.this);

                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        Tuizpt tuizpt=new Tuizpt();
                        tuizpt.account=userbean.account;
                        //?????????????????????????????????????????????????????????topic??????????????????ok??????????????????topic???????????????
                        tuizpt.utopic=circleBean.topic;
                        tuizpt.ucount=199;
                        tuizpt.save(CircleBeanInfo.this);

                    }
                });


                strk="";
//                j++;
            }

        }
    }



    private void gettuizan() {

        BmobQuery<Tuizpu> tzp=new BmobQuery<>();
        tzp.addWhereEqualTo("account",userbean.account);
        tzp.addWhereEqualTo("uaccount",circleBean.account);
        tzp.findObjects(this, new FindListener<Tuizpu>() {
            @Override
            public void onSuccess(List<Tuizpu> list) {
                Tuizpu tuizpu=new Tuizpu();

                if(list.size()>0)
                {
                    tuizpu=list.get(0);
                    tuizpu.ucount++;
                    tuizpu.update(CircleBeanInfo.this);
                }
                else {
                    Tuizpu tuizpu1=new Tuizpu();
                    tuizpu1.account=userbean.account;
                    tuizpu1.uaccount=circleBean.account;
                    tuizpu1.ucount=1;
                    tuizpu1.save(CircleBeanInfo.this);

                }

            }

            @Override
            public void onError(int i, String s) {

            }
        });
//------------------------------------------------------------------------------------------------------------------

    }

    private void gettuipl() {
        BmobQuery<Tuizpu> tzp=new BmobQuery<>();
        tzp.addWhereEqualTo("account",userbean.account);
        tzp.addWhereEqualTo("uaccount",circleBean.account);
        tzp.findObjects(this, new FindListener<Tuizpu>() {
            @Override
            public void onSuccess(List<Tuizpu> list) {
                Tuizpu tuizpu=new Tuizpu();
                if(list.size()>0)
                {
                    tuizpu=list.get(0);
                    tuizpu.ucount++;
//                    circleBean.content.contains("sss");
                    tuizpu.update(CircleBeanInfo.this);
                }
                else {
                    Tuizpu tuizpu1=new Tuizpu();
                    tuizpu1.account=userbean.account;
                    tuizpu1.uaccount=circleBean.account;
                    tuizpu1.ucount=1;
                    tuizpu1.save(CircleBeanInfo.this);
                }

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void getqutuizan() {
        BmobQuery<Tuizpu> tzp=new BmobQuery<>();
        tzp.addWhereEqualTo("account",userbean.account);
        tzp.addWhereEqualTo("uaccount",circleBean.account);
        tzp.findObjects(this, new FindListener<Tuizpu>() {
            @Override
            public void onSuccess(List<Tuizpu> list) {
                Tuizpu tuizpu=new Tuizpu();

                if(list.size()>0)
                {
                    tuizpu=list.get(0);
                    tuizpu.ucount--;
                    tuizpu.update(CircleBeanInfo.this);
                }
//                else {
//
//                }

            }

            @Override
            public void onError(int i, String s) {


            }
        });

    }


    private void  getzan(){
        BmobQuery<Zan> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("c_id",circleBean.getObjectId());
        bmobQuery.addWhereEqualTo("account",userbean.account);
        bmobQuery.findObjects(this, new FindListener<Zan>() {
            @Override
            public void onSuccess(List<Zan> list1) {
               if (list1.size()>0){
                   ivZan.setImageResource(R.mipmap.zan_s);
               }else {
                   ivZan.setImageResource(R.mipmap.zan);
               }

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(CircleBeanInfo.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void isZan(){
        BmobQuery<Zan> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("c_id",circleBean.getObjectId());
        bmobQuery.addWhereEqualTo("account",userbean.account);
        bmobQuery.findObjects(this, new FindListener<Zan>() {
            @Override
            public void onSuccess(List<Zan> list1) {
                if (list1.size()>0){
                    Zan zan = list1.get(0);
                    zan.delete(CircleBeanInfo.this, new DeleteListener() {
                        @Override
                        public void onSuccess() {
                            toast("????????????");
                            ivZan.setImageResource(R.mipmap.zan);
                            getzannum();
                            getqutuizan();
                            getqutuitp();
//                            msgqxzan();
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                }else {
                    zan();
                }

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(CircleBeanInfo.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getzannum(){
        BmobQuery<Zan> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("c_id",circleBean.getObjectId());
        bmobQuery.findObjects(this, new FindListener<Zan>() {
            @Override
            public void onSuccess(List<Zan> list1) {
                  tvZanNum.setText(""+list1.size());
                if(circleBean.pinglunshu.isEmpty()) {
                    zanshu = "" + list1.size();//-------------------------------------------------------------------------------------

                    circleBean.zanshu = zanshu;
//                couz=list1.size()+coup;
//                countplz=couz+"";
                    ccc = Integer.parseInt(zanshu);
                    circleBean.countplz = ccc + "";
                    circleBean.ccplz = ccc;
                    circleBean.update(CircleBeanInfo.this);//??????????????????????????????????????????????????????
                }else {
                    zanshu = "" + list1.size();//-------------------------------------------------------------------------------------

                    circleBean.zanshu = zanshu;
//                couz=list1.size()+coup;
//                countplz=couz+"";
                    ccc = Integer.parseInt(zanshu) + Integer.parseInt(circleBean.pinglunshu);
                    circleBean.countplz = ccc + "";
                    circleBean.ccplz = ccc;
                    circleBean.update(CircleBeanInfo.this);//??????????????????????????????????????????????????????

                }
            }//????????????

            @Override
            public void onError(int i, String s) {
                Toast.makeText(CircleBeanInfo.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sendPl(String content){
        PingLun pingLun=new PingLun();
        pingLun.account=userbean.account;
        pingLun.c_id=circleBean.getObjectId();
        if (!TextUtils.isEmpty(userbean.nick)){
            pingLun.nick=userbean.nick;
        }
        if (userbean.img!=null){
            pingLun.head=userbean.img.getUrl();
        }

        sensitiveWordpl=new SensitiveWordpl();
        sensitiveWordpl.InitializationWork();
        content = sensitiveWordpl.filterInfo(content);//?????????????????????
        pingLun.content=content;
        pingLun.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                toast("????????????");
                etPl.setText("");
                getData();
                gettuipl();
                gettuitp();
            }



            @Override
            public void onFailure(int i, String s) {

            }
        });
    }



//    CircleBean p2 = new CircleBean();
//    p2.setPinglunshu(pinglunshu);
//    p2.update(, new UpdateListener() {
//
//        @Override
//        public void done(BmobException e) {
//            if(e==null){
//                toast("????????????:"+p2.getUpdatedAt());
//            }else{
//                toast("???????????????" + e.getMessage());
//            }
//        }
//
//    });
//p2.update("6b6c11c537", new UpdateListener() {
//
//        @Override
//        public void done(BmobException e) {
//            if(e==null){
//                toast("????????????:"+p2.getUpdatedAt());
//            }else{
//                toast("???????????????" + e.getMessage());
//            }
//        }
//
//    });
    /**
     * ??????????????? ?????????   -- ?????????????????????????????????
     * ???????????????????????????&????????????http://blog.csdn.net/hubiao_0618/article/details/45076871
     *
     * @author hubiao
     * @version 0.1
     * @CreateDate 2015???4???16??? 15:28:32
     */
    public class SensitiveWordpl {
        private StringBuilder replaceAll;//?????????
        private String encoding = "UTF-8";
        private String replceStr = "*";
        private int replceSize = 500;
        private String fileName = "CensorWords.txt";
        private List<String> arrayList;
        public Set<String> sensitiveWordSet;//?????????????????????????????????????????????
        public List<String> sensitiveWordList;//?????????????????????????????????????????????????????????

        /**
         * ?????????????????????src???resource????????????????????????CensorWords.txt
         *
         * @param fileName ???????????????(?????????)
         */
        public SensitiveWordpl(String fileName) {
            this.fileName = fileName;
        }

        /**
         * @param replceStr  ???????????????????????????
         * @param replceSize ??????????????????
         */
        public SensitiveWordpl(String replceStr, int replceSize) {
            this.replceStr = fileName;
            this.replceSize = replceSize;
        }

        public SensitiveWordpl() {
        }

        /*
         * @param str ?????????????????????
         * @return ??????????????????
         */
        public String filterInfo(String str) {
            sensitiveWordSet = new HashSet<String>();
            sensitiveWordList = new ArrayList<>();
            StringBuilder buffer = new StringBuilder(str);
            HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>(arrayList.size());
            String temp;
            for (int x = 0; x < arrayList.size(); x++) {
                temp = arrayList.get(x);
                int findIndexSize = 0;
                for (int start = -1; (start = buffer.indexOf(temp, findIndexSize)) > -1; ) {
                    //System.out.println("###replace="+temp);
                    findIndexSize = start + temp.length();//??????????????????????????????
                    Integer mapStart = hash.get(start);//????????????
                    if (mapStart == null || (mapStart != null && findIndexSize > mapStart))//??????1??????????????????map
                    {
                        hash.put(start, findIndexSize);
                        //System.out.println("###????????????"+buffer.substring(start, findIndexSize));
                    }
                }
            }
            Collection<Integer> values = hash.keySet();
            for (Integer startIndex : values) {
                Integer endIndex = hash.get(startIndex);
                //??????????????????????????????????????????????????????
                String sensitive = buffer.substring(startIndex, endIndex);
                //System.out.println("###????????????"+sensitive);
                if (!sensitive.contains("*")) {//????????????????????????
                    sensitiveWordSet.add(sensitive);
                    sensitiveWordList.add(sensitive);
                }
                buffer.replace(startIndex, endIndex, replaceAll.substring(0, endIndex - startIndex));
            }
            hash.clear();
            return buffer.toString();
        }

        /**
         * ?????????????????????
         */
        public void InitializationWork() {
            replaceAll = new StringBuilder(replceSize);
            for (int x = 0; x < replceSize; x++) {
                replaceAll.append(replceStr);
            }
            //????????????
            arrayList = new ArrayList<String>();
            InputStreamReader read = null;
            BufferedReader bufferedReader = null;
            try {
                //read = new InputStreamReader(SensitiveWord.class.getClassLoader().getResourceAsStream(fileName),encoding);
                //bufferedReader = new BufferedReader(read);
            /*for(String txt = null;(txt = bufferedReader.readLine()) != null;){
                if(!arrayList.contains(txt))
                    arrayList.add(txt);
            }*/

                //????????????
                String[] a = initAssets();
                for (int i = 0; i < a.length; i++) {
                    if (!arrayList.contains(a[i]))
                        arrayList.add(a[i]);
                }

            } finally {
                try {
                    if (null != bufferedReader)
                        bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (null != read)
                        read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /*
     * ?????? ???String ????????????
     *
     * @return
     */
    public String[] initAssets() {
        try {
            //???????????????
            InputStream inputStream = getAssets().open("CensorWords");//????????????????????????txt ??????????????????

            //??????????????????
            String str = getString(inputStream);
            //???????????? ?????????????????????
            String[] arr = str.split("\n");
            return arr;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * ??????????????????
     *
     * @param inputStream
     * @return
     */
    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        //?????????????????????
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            //??????????????????
            while ((line = reader.readLine()) != null) {
                //???????????????????????????
                sb.append(line);
                //????????????
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //???????????????????????????
        return sb.toString();
    }

}
