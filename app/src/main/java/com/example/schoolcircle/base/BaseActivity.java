package com.example.schoolcircle.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.schoolcircle.bean.GuanZhu;
import com.example.schoolcircle.bean.User;
import com.example.schoolcircle.utils.ACache;
import com.example.schoolcircle.utils.Contans;

import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;

//import cn.bmob.v3.BmobInstallationManager;
//import cn.bmob.v3.InstallationListener;


//封装的activity
public abstract class BaseActivity extends AppCompatActivity {
//    /***是否显示标题栏*/
//    private  boolean isshowtitle = true;
//    /***是否显示标题栏*/
//    private  boolean isshowstate = true;
    /***封装toast对象**/
    private static Toast toast;
    /***获取TAG的activity名称**/
    protected final String TAG = this.getClass().getSimpleName();
    protected User userbean;
    protected GuanZhu gzinfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Contans.BombKey);



//        ActivityCollector.addActivity(this);


//        // 使用推送服务时的初始化操作
//        BmobInstallation.getCurrentInstallation(this).save();
//        // 启动推送服务
//        BmobPush.startWork(this);


//// 使用推送服务时的初始化操作
//        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
//            @Override
//            public void done(BmobInstallation bmobInstallation, BmobException e) {
//                if (e == null) {
//                    Logger.i(bmobInstallation.getObjectId() + "-" + bmobInstallation.getInstallationId());
//                } else {
//                    Logger.e(e.getMessage());
//                }
//            }
//        });
//// 启动推送服务
//        BmobPush.startWork(this);


//        MobSDK.init(this);



//        if(!isshowtitle){
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//        }
       userbean = (User) ACache.get(this).getAsObject("userbean");

        gzinfo = (GuanZhu) ACache.get(this).getAsObject("gzinfo");
//        if(isshowstate){
//            getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
//                    WindowManager.LayoutParams. FLAG_FULLSCREEN);
//        }
        //设置布局
        setContentView(intiLayout());
        ButterKnife.bind(this);
        //初始化控件
        initView();
        //设置数据
        initData();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        ActivityCollector.removeActivity(this);
//    }

    //页面跳转
    public void jumpAct(Class s){
        Intent intent = new Intent(this, s);
        startActivity(intent);
    } public void jumpAct(Class s, int res){
        Intent intent = new Intent(this, s);
        startActivityForResult(intent,res);
    }
    public void jumpAct(Class s, Bundle bundle){
        Intent intent = new Intent(this, s);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    /**
     * 设置布局
     *
     * @return
     */
    public abstract int intiLayout();
 
    /**
     * 初始化布局
     */
    public abstract void initView();
 
    /**
     * 设置数据
     */
    public abstract void initData();
 
    public void toast(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
    }
}
