package com.example.schoolcircle.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolcircle.MainActivity;
import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.User;
import com.example.schoolcircle.utils.ACache;
import com.example.schoolcircle.utils.Code;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class LoginActivity extends BaseActivity implements View.OnClickListener {


    //初始化控件
    private EditText et_account;
    private EditText et_pwd;
    private Button tv_reg;
    private Button bt_Login;
    private RadioButton rb1;
    private RadioButton rb2;
    private TextView tv,tv_findpwd;
    private SeekBar seekBar;
    private String a,b,c;
    private String d,e;
    private ImageView iv_showCode;
    private String realCode;
    private EditText et_phoneCode;



    @Override
    public int intiLayout() {
        return R.layout.act_login;
    }
    //查找控件
    @Override
    public void initView() {

        iv_showCode=(ImageView) findViewById(R.id.iv_showCode);
        et_phoneCode=(EditText) findViewById(R.id.et_phoneCodes);
        et_account = (EditText) findViewById(R.id.et_account);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        tv_reg = (Button) findViewById(R.id.tv_reg);
        bt_Login = (Button) findViewById(R.id.bt_Login);
        rb1 =  findViewById(R.id.rb1);
        rb2 =  findViewById(R.id.rb2);
        tv_findpwd= findViewById(R.id.tv_findpwd);


        iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
        iv_showCode.setOnClickListener(this);

        bt_Login.setOnClickListener(this);
        tv_reg.setOnClickListener(this);
        tv_findpwd.setOnClickListener(this);
        iv_showCode.setOnClickListener(this);




    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_Login:
                submit();
                break;
            case R.id.tv_reg:
                //跳转注册页面
                startActivity(new Intent(LoginActivity.this, RegActivity.class));
                break;
            case R.id.tv_findpwd:

                startActivity(new Intent(LoginActivity.this, FindpwdActivity.class));
                break;
            case R.id.iv_showCode:
                //验证码
                iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                Log.v(TAG,"realCode"+realCode);
                break;
        }
    }
    //登录操作
    private void submit() {
        // validate
//        if(userbean.fh.equals("1")){
//            Toast.makeText(this, "登录失败，你已被封号，请联系管理员", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//        else {



            String account = et_account.getText().toString().trim();
            if (TextUtils.isEmpty(account)) {
                Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
                return;
            }

            String pwd = et_pwd.getText().toString().trim();
            if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                return;
            }

        String phoneCode = et_phoneCode.getText().toString().toLowerCase();
        if (phoneCode.equals(realCode)) {
//            Toast.makeText(LoginActivity.this, "验证码正确", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LoginActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();

            et_account.setText("");
            et_pwd.setText("");
            et_phoneCode.setText("");
            iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
            realCode = Code.getInstance().getCode().toLowerCase();
            Log.v(TAG,"realCode"+realCode);
            return;
        }




            if (rb1.isChecked()) {
                a = account;
                b = pwd;
//                setContentView(R.layout.act_yz);
                tv = (TextView) findViewById(R.id.tv);
//                seekBar = (SeekBar) findViewById(R.id.sb);
//                seekBar.setOnSeekBarChangeListener(this);
                login(a, b);


//            login(account, pwd);

//            if (tv.getText().equals("完成验证")){
//            }
            }

            if (rb2.isChecked()) {

                a = account;
                b = pwd;
                c = "1";
                loginmanger(a, b, c);


//            if (account.equals("admin")&&pwd.equals("123456")){
//                toast("登录成功");
//
//                ACache.get(LoginActivity.this).put("islogin", "true");
//                ACache.get(LoginActivity.this).put("type", "1");
//                Intent intent = new Intent(LoginActivity.this,MangerMainActivity.class);
//                startActivity(intent);
//                finish();
//
//            }else {
//                toast("管理员账号密码不正确");
//            }
            }


//        }
    }

    //查询管理员数据库登录操作
    private void loginmanger(final String name, final String pwd,final String userlimit) {
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account", name);
        bmobQuery.addWhereEqualTo("pwd", pwd);
        bmobQuery.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                Log.e("COUNT", list.size() + "---");

                if (list.size() == 0) {
                    Toast.makeText(LoginActivity.this, "账号或密码不正确", Toast.LENGTH_SHORT).show();
                    et_account.setText("");
                    et_pwd.setText("");
                    et_phoneCode.setText("");
                    iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
                    realCode = Code.getInstance().getCode().toLowerCase();
                    Log.v(TAG,"realCode"+realCode);
                    return;


                } else {
                    User user = list.get(0);

                        if (user.userlimit.equals("1")||user.userlimit.equals("0")) {

                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            ACache.get(LoginActivity.this).put("userbean", list.get(0));
                            ACache.get(LoginActivity.this).put("uact", list.get(0).account);
                            ACache.get(LoginActivity.this).put("upwd", list.get(0).pwd);
                            ACache.get(LoginActivity.this).put("islogin", "true");
                            ACache.get(LoginActivity.this).put("type", "1");

                            startActivity(new Intent(LoginActivity.this, MangerMainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "登录失败，您不是管理员", Toast.LENGTH_SHORT).show();
                        }



                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(LoginActivity.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //查询数据库登录操作
    private void login(final String name, final String pwd) {

        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account", name);
        bmobQuery.addWhereEqualTo("pwd", pwd);

        bmobQuery.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                Log.e("COUNT", list.size() + "---");
                if (list.size() == 0) {
                    Toast.makeText(LoginActivity.this, "账号或密码不正确", Toast.LENGTH_SHORT).show();
                    et_account.setText("");
                    et_pwd.setText("");
                    et_phoneCode.setText("");
                    iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
                    realCode = Code.getInstance().getCode().toLowerCase();
                    Log.v(TAG,"realCode"+realCode);
                    return;

                } else {
                    User user = list.get(0);
                    d=user.fh;
                    e=user.stuverify;


                    if(d.equals("0")) {
                        if(e.equals("0")){
                            Toast.makeText(LoginActivity.this, "您还未通过审核，请联系管理员", Toast.LENGTH_SHORT).show();
                            ACache.get(LoginActivity.this).clear();

                        }
                        else {




                        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(System.currentTimeMillis());
                        String logtime = ft.format(date);
                        user.logtime=logtime;
                        user.update(LoginActivity.this);

                        Toast.makeText(LoginActivity.this, "登录成功" , Toast.LENGTH_SHORT).show();
                        ACache.get(LoginActivity.this).put("userbean", list.get(0));
                        ACache.get(LoginActivity.this).put("uact", list.get(0).account);
                        ACache.get(LoginActivity.this).put("upwd", list.get(0).pwd);
                        ACache.get(LoginActivity.this).put("islogin", "true");
                        ACache.get(LoginActivity.this).put("type", "0");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "您已被封号，请联系管理员", Toast.LENGTH_SHORT).show();
                        ACache.get(LoginActivity.this).clear();

                    }



                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(LoginActivity.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });




    }


//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.act_yz);
//
//
//    }

//    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        if (seekBar.getProgress() == seekBar.getMax()) {
//            tv.setVisibility(View.VISIBLE);
//            tv.setTextColor(Color.WHITE);
//            tv.setText("完成验证");
//
//            login(a, b);
//
//        } else {
//            tv.setVisibility(View.INVISIBLE);
//        }
//
//    }
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//        if (seekBar.getProgress() != seekBar.getMax()) {
//            seekBar.setProgress(0);
//            tv.setVisibility(View.VISIBLE);
//            tv.setTextColor(Color.GRAY);
//            tv.setText("请按住滑块，拖动到最右边");
//        }
//
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//
//    }
}
