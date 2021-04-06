package com.example.schoolcircle.ui;

import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.utils.ThemeChangeUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class Setting extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.switch1)
    Switch aSwitch;
    @BindView(R.id.switch2)
    Switch bSwitch;
    @BindView(R.id.rpwd)
    TextView rpwd;

//    @BindView(R.id.ilogout)
//    TextView ilogout;

    //实现Android白天/夜间模式的关键类
    private UiModeManager mUiModeManager = null;




    @Override
    public int intiLayout() {
        return R.layout.act_setting;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        ThemeChangeUtil.changeTheme(this);
        super.onCreate(savedInstanceState);



        mUiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);



        //获取当前是夜间模式还是白天模式
        int currentNightMode = getResources().getConfiguration().uiMode  & Configuration.UI_MODE_NIGHT_MASK;
        if(currentNightMode==32){
            aSwitch.setChecked(true);
        }else {
            aSwitch.setChecked(false);
        }
        if(aSwitch.getCurrentTextColor()==111);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    mUiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);





                }else{
                    mUiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);

                }



            }
        });




//        bSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    Calendar calendar = Calendar.getInstance();//获取当前时间
//                    int hour = calendar.get(Calendar.HOUR_OF_DAY);//获取当前时间中的小时
//
//                    if(hour>=22||hour<=6) {
//                        mUiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);//定时进入夜间模式
//                    }else {
//                        mUiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
//                    }
//
//
//                }else{
//                    Toast.makeText(Setting.this, "已关闭", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });


        rpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpAct(RPasswordAct.class);
            }
        });


    }



    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
