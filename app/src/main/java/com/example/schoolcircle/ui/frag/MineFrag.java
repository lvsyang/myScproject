package com.example.schoolcircle.ui.frag;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseFragment;
import com.example.schoolcircle.bean.GuanZhu;
import com.example.schoolcircle.bean.MsgRead;
import com.example.schoolcircle.bean.User;
import com.example.schoolcircle.ui.LoginActivity;
import com.example.schoolcircle.ui.MyFensiAct;
import com.example.schoolcircle.ui.MyGuanzAct;
import com.example.schoolcircle.ui.MyJiaoYiAct;
import com.example.schoolcircle.ui.MyLaheiAct;
import com.example.schoolcircle.ui.MyMsgAct;
import com.example.schoolcircle.ui.MyPubAct;
import com.example.schoolcircle.ui.PubCircleAct;
import com.example.schoolcircle.ui.Setting;
import com.example.schoolcircle.ui.UserInfoAct;
import com.example.schoolcircle.utils.ACache;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Administrator on 2019/3/22.
 */
//我的页面
public class MineFrag extends BaseFragment {

    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    Unbinder unbinder;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right1)
    ImageView tv_right1;
    @BindView(R.id.tv_right2)
    TextView tv_right2;
    @BindView(R.id.my_gender)
    ImageView my_gender;
    @BindView(R.id.tv_gz_num)
    TextView tv_gz_num;
    @BindView(R.id.tv_fs_num)
    TextView tv_fs_num;
    Unbinder unbinder1;
    private User userbean;


    //设置xml
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {

        return inflater.inflate(R.layout.frg_mine, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        //控件设置数据
        userbean = (User) ACache.get(mActivity).getAsObject("userbean");
        getmsg();

        tvAccount.setText(userbean.account);
        tvNick.setText("昵称:" + (TextUtils.isEmpty(userbean.nick) ? "无" : userbean.nick));
        if (userbean.img != null) {
            Glide.with(mActivity).load(userbean.img.getUrl()).into(ivHead);
        }
        getFenSiNum();
        getRealFenSiNum();
        if(userbean.gender.equals("男")){

            my_gender.setImageResource(R.drawable.man);
        }
        if(userbean.gender.equals("女")){
            my_gender.setImageResource(R.drawable.girl);
        }
    }

    @Override
    public void initData() {
        super.initData();
        tvTitle.setText("我的");
        tv_right1.setVisibility(View.VISIBLE);

        userbean = (User) ACache.get(mActivity).getAsObject("userbean");
        getmsg();





    }

    private void getmsg() {               //获取当前未读信息数
        BmobQuery<MsgRead> m=new BmobQuery<>();
        m.addWhereEqualTo("account",userbean.account);
        m.findObjects(getActivity(), new FindListener<MsgRead>() {
            @Override
            public void onSuccess(List<MsgRead> listm) {
                if(listm.size()>0){
                    int j=0;
                    for (int i=0;i<listm.size();i++){

                        j=j+listm.get(i).countread;
                    }
                    tv_right2.setText(""+j);
                }
                else {
                    tv_right2.setText("0");
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void getFenSiNum(){
        BmobQuery<GuanZhu> bmobQuery=new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account",userbean.account);
        bmobQuery.findObjects(getActivity(), new FindListener<GuanZhu>() {
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
        bmobQuery.addWhereEqualTo("u_id",userbean.account);
        bmobQuery.findObjects(getActivity(), new FindListener<GuanZhu>() {
            @Override
            public void onSuccess(List<GuanZhu> list) {
                tv_fs_num.setText(list.size()+"");
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }




    @OnClick({R.id.ll_info, R.id.ll_pub, R.id.ll_my_pub,R.id.ll_lahei,R.id.ll_set, R.id.ll_exit,R.id.tv_gz_num,R.id.tv_fs_num,R.id.ll_jiaoyi,R.id.tv_right1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_info:
                jumpAct(UserInfoAct.class);
                break;
            case R.id.ll_pub:
                jumpAct(PubCircleAct.class);

                break;
            case R.id.ll_my_pub:
                jumpAct(MyPubAct.class);
                break;
            case R.id.ll_lahei:
                jumpAct(MyLaheiAct.class);
                break;
            case R.id.ll_set:
                jumpAct(Setting.class);
                break;
            case R.id.ll_exit:
                ACache.get(mActivity).clear();
                mActivity.finish();
                jumpAct(LoginActivity.class);
                break;
            case R.id.tv_gz_num:
                jumpAct(MyGuanzAct.class);
                break;
            case R.id.tv_fs_num:
                jumpAct(MyFensiAct.class);
                break;
            case R.id.ll_jiaoyi:
                jumpAct(MyJiaoYiAct.class);
                break;
            case R.id.tv_right1:
                jumpAct(MyMsgAct.class);
                delmsg();
//                Toast.makeText(mActivity, "要顺便把你没看的点开哟", Toast.LENGTH_SHORT).show();
                break;


        }
    }

//    private Context mContext;
//    private void getsss() {
//        BmobPushManager bmobPushManager = new BmobPushManager(mContext);
//        BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
//        bmobPushManager.pushMessageAll("消息内容", new PushListener() {
//
//            @Override
//            public void onSuccess() {
//                Log.d("bmob", "客户端收到推送内容：");
//
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//
//            }
//        });
//    }

    private void delmsg() {
        final BmobQuery<MsgRead> msgRead=new BmobQuery<>();
        msgRead.addWhereEqualTo("account",userbean.account);
        msgRead.addWhereNotEqualTo("mtype","PingLun");
        msgRead.findObjects(getActivity(), new FindListener<MsgRead>() {
            MsgRead msg=new MsgRead();
            @Override
            public void onSuccess(List<MsgRead> listmsg) {
                if(listmsg.size()!=0){
                    for(int i=0;i<listmsg.size();i++){
                        msg=listmsg.get(i);
                        msg.delete(getActivity());
                    }
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


}
