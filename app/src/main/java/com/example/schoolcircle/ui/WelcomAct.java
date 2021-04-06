package com.example.schoolcircle.ui;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.view.WindowManager;

import com.example.schoolcircle.MainActivity;
import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.User;
import com.example.schoolcircle.utils.ACache;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


//欢迎页面
public class WelcomAct extends BaseActivity {
    private UiModeManager mUiModeManager = null;


    @Override
    public int intiLayout() {

        mUiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        int currentNightMode = getResources().getConfiguration().uiMode  & Configuration.UI_MODE_NIGHT_MASK;
        if(currentNightMode==32){
            return R.layout.act_wel;
        }else {
            return R.layout.act_welsun;
        }

    }

    @Override
    public void initView() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                String islogin = ACache.get(WelcomAct.this).getAsString("islogin");
                if (TextUtils.isEmpty(islogin)){

                    //跳转登录
                    Intent intent = new Intent(WelcomAct.this,LoginActivity.class);
                    startActivity(intent);
                }else {
               String type = ACache.get(WelcomAct.this).getAsString("type");
               String uact = ACache.get(WelcomAct.this).getAsString("uact");
               String upwd = ACache.get(WelcomAct.this).getAsString("upwd");

               if (type.equals("0")){
//                   Intent intent = new Intent(WelcomAct.this,MainActivity.class);
//                   startActivity(intent);
                   isrigt(uact,upwd);
               }else {
//              Intent intent = new Intent(WelcomAct.this,MangerMainActivity.class);
//                   startActivity(intent);

                   isrigtmanager(uact,upwd);

               }



                }

                finish();
            }
        },2000);
    }

    private void isrigtmanager(String uact,final String upwdm) {
        BmobQuery<User> u=new BmobQuery<>();
        u.addWhereEqualTo("account",uact);
        u.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> listu) {
                if(listu.get(0).pwd.equals(upwdm)){
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(System.currentTimeMillis());
                    String logtime = ft.format(date);
                    listu.get(0).logtime=logtime;
                    listu.get(0).update(WelcomAct.this);

                    Intent intent = new Intent(WelcomAct.this,MangerMainActivity.class);
                    startActivity(intent);

                }
                else {



                    //跳转登录
                    Intent intent = new Intent(WelcomAct.this,LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

    private void isrigt(String uact, final String upwd) {
        BmobQuery<User> u=new BmobQuery<>();
        u.addWhereEqualTo("account",uact);
        u.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> listu) {
                if(listu.get(0).pwd.equals(upwd)&&listu.get(0).fh.equals("0")){

                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(System.currentTimeMillis());
                    String logtime = ft.format(date);
                    listu.get(0).logtime=logtime;
                    listu.get(0).update(WelcomAct.this);

                    Intent intent = new Intent(WelcomAct.this,MainActivity.class);
                    startActivity(intent);

                }
                else {
                    //跳转登录
                    Intent intent = new Intent(WelcomAct.this,LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

    @Override
    public void initData() {

    }




}
