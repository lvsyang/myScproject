package com.example.schoolcircle.ui;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;

public class AnswerAct extends BaseActivity {

    @Override
    public int intiLayout() {

        final Dialog dialog = new Dialog(AnswerAct.this,R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(AnswerAct.this).inflate(R.layout.item_answerpl,null);
        return 0;

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
