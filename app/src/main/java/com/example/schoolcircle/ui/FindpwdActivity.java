package com.example.schoolcircle.ui;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class FindpwdActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_account,et_ukey,et_pwd,et_pwd_once;
    private Button bt_yzkey,bt_qrcz;
    private LinearLayout et_xs1,et_xs2;
    private ImageView xs1,xs2;
    private String d;
    public String e;


    @Override
    public int intiLayout() {
        return R.layout.act_findpwd;
    }

    @Override
    public void initView() {

        et_account = (EditText) findViewById(R.id.et_account);
        et_ukey = (EditText) findViewById(R.id.et_ukey);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_pwd_once = (EditText) findViewById(R.id.et_pwd_once);

        bt_yzkey = (Button) findViewById(R.id.bt_yzkey);
        bt_qrcz = (Button) findViewById(R.id.bt_qrcz);

        xs1 = (ImageView) findViewById(R.id.xs1);
        xs2 = (ImageView) findViewById(R.id.xs2);

        bt_yzkey.setOnClickListener(this);
        bt_qrcz.setOnClickListener(this);




    }

    @Override
    public void initData() {

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_yzkey:
                yanz();
                break;
            case R.id.bt_qrcz:
                chongz();
                break;

//            case R.id.et_choosegender:
//                submit();
//                break;

        }

    }



    //验证密钥过程
    private void yanz() {
        String account = et_account.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
            return;
        }
        String ukey = et_ukey.getText().toString().trim();
        if (TextUtils.isEmpty(ukey)) {
            Toast.makeText(this, "请输入密钥", Toast.LENGTH_SHORT).show();
            return;
        }

        yz(account,ukey);
    }

    //查询数据库验证操作
    private void yz(final String name, final String ukey) {

        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account", name);
        bmobQuery.addWhereEqualTo("ukey", ukey);

        bmobQuery.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                Log.e("COUNT", list.size() + "---");
                if (list.size() == 0) {
                    Toast.makeText(FindpwdActivity.this, "账号或密钥不正确", Toast.LENGTH_SHORT).show();
                } else {
                    User user = list.get(0);

                    d=user.fh;
                    e=user.pwd;


                    if(d.equals("0")) {

                        Toast.makeText(FindpwdActivity.this, "验证成功,请在下方重设密码" , Toast.LENGTH_SHORT).show();
                        xs1.setVisibility(View.VISIBLE);
                        et_pwd.setVisibility(View.VISIBLE);
                        xs2.setVisibility(View.VISIBLE);
                        et_pwd_once.setVisibility(View.VISIBLE);

                        bt_qrcz.setVisibility(View.VISIBLE);
                    }
                    else {
                        Toast.makeText(FindpwdActivity.this, "您已被封号，请联系管理员", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(FindpwdActivity.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void chongz() {
        String pwd = et_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        String once = et_pwd_once.getText().toString().trim();
        if (TextUtils.isEmpty(once)) {
            Toast.makeText(this, "请再次输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pwd.equals(once)) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            et_pwd.setText("");
            et_pwd_once.setText("");
            return;
        }
        if(pwd.equals(e)){
            Toast.makeText(this, "密码与原密码一致，请重新输入或返回", Toast.LENGTH_SHORT).show();
            et_pwd.setText("");
            et_pwd_once.setText("");
            return;
        }

        String account = et_account.getText().toString().trim();


        cz(account,pwd);


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
                        user.update(FindpwdActivity.this);

                        finish();
                    }
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });


    }

}
