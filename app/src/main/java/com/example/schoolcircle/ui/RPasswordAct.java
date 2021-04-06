package com.example.schoolcircle.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.User;
import com.example.schoolcircle.utils.ACache;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class RPasswordAct extends BaseActivity implements View.OnClickListener {
    private EditText et_rpwd,rpwd1,rpwd2;
    private Button bt_qrxg;
    private ImageView r1,r2;


    @Override
    public int intiLayout() {
        return R.layout.act_rpwd;
    }

    @Override
    public void initView() {
        et_rpwd = (EditText) findViewById(R.id.et_rpwd);
        rpwd1 = (EditText) findViewById(R.id.rpwd1);
        rpwd2 = (EditText) findViewById(R.id.rpwd2);
        bt_qrxg = (Button) findViewById(R.id.bt_qrxg);

        r1 = (ImageView) findViewById(R.id.r1);
        r2 = (ImageView) findViewById(R.id.r2);

        bt_qrxg.setOnClickListener(this);


    }

    @Override
    public void initData() {

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_qrxg:
                if(bt_qrxg.getText().equals("确定输入")){
                yanz();}
                else {
                    xgpwd();
                }
                break;


        }

    }

    private void yanz() {
        String pwd = et_rpwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入原密码", Toast.LENGTH_SHORT).show();
            return;
        }
        yz(userbean.account,pwd);

    }

    private void yz(String account1, final String pwd1) {
        BmobQuery<User> u =new BmobQuery<>();
        u.addWhereEqualTo("account",account1);
        u.findObjects(RPasswordAct.this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> listu) {
                if(listu.get(0).pwd.equals(pwd1)){
                    r1.setVisibility(View.VISIBLE);
                    rpwd1.setVisibility(View.VISIBLE);
                    r2.setVisibility(View.VISIBLE);
                    rpwd2.setVisibility(View.VISIBLE);

                    bt_qrxg.setText("确定修改");


                }
                else {
                    toast("密码错误，请重新输入");
                    et_rpwd.setText("");
                    return;
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

    private void xgpwd() {
        String pwd = rpwd1.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        String once = rpwd2.getText().toString().trim();
        if (TextUtils.isEmpty(once)) {
            Toast.makeText(this, "请再次输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pwd.equals(once)) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            rpwd1.setText("");
            rpwd2.setText("");
            return;
        }
        if(pwd.equals(userbean.pwd)){
            Toast.makeText(this, "密码与原密码一致，请重新输入或返回", Toast.LENGTH_SHORT).show();
            rpwd1.setText("");
            rpwd2.setText("");
            return;
        }

        cz(userbean.account,pwd);



    }

    //查询数据库验证操作
    private void cz(final String account, final String pwd) {
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account", account);
//        bmobQuery.addWhereEqualTo("pwd", ukey);
        bmobQuery.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list1) {
                if (list1.size()>0){
                    for (int i = 0; i < list1.size(); i++) {
                        User user = list1.get(i);
                        user.pwd=pwd;
                        user.update(RPasswordAct.this);
                        Toast.makeText(RPasswordAct.this, "修改成功，请重新登录", Toast.LENGTH_SHORT).show();
                        ACache.get(RPasswordAct.this).clear();
                        RPasswordAct.this.finish();
                        jumpAct(LoginActivity.class);

                    }
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });


    }
}
