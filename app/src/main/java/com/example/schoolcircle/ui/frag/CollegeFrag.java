package com.example.schoolcircle.ui.frag;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseFragment;
import com.example.schoolcircle.bean.User;
import com.example.schoolcircle.ui.STimeTableAct;
import com.example.schoolcircle.utils.ACache;

import butterknife.BindView;
import butterknife.OnClick;

public class CollegeFrag extends BaseFragment {
    @BindView(R.id.timetable)
    TextView timetable;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private User userbean;


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frg_college,container,false);
    }


    @Override
    public void initData() {
        super.initData();
        tvTitle.setText("我的校园");
//        tv_right1.setVisibility(View.VISIBLE);
//
        userbean = (User) ACache.get(mActivity).getAsObject("userbean");
//        getmsg();


    }












    @OnClick({R.id.timetable})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.timetable:
                jumpAct(STimeTableAct.class);
                break;


        }
    }
}
