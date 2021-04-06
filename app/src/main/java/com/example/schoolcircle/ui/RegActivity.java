package com.example.schoolcircle.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


//注册页面
public class RegActivity extends BaseActivity implements View.OnClickListener {

//初始化控件
    private EditText et_account;
    private EditText et_pwd;
    private EditText et_pwd_once,et_uuk,et_stuacou,et_stuname;
    private Button bt_Login;
//    private String re_gender="男";
   private String strgender="男";




    @Override
    public int intiLayout() {
        return R.layout.act_reg;
    }

    @Override
    public void initView() {

        et_account = (EditText) findViewById(R.id.et_account);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_pwd_once = (EditText) findViewById(R.id.et_pwd_once);
        et_uuk= (EditText) findViewById(R.id.et_uuk);
        et_stuacou= (EditText) findViewById(R.id.et_stuacou);
        et_stuname= (EditText) findViewById(R.id.et_stuname);
        bt_Login = (Button) findViewById(R.id.bt_Login);


        bt_Login.setOnClickListener(this);
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
//            case R.id.et_choosegender:
//                submit();
//                break;

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RadioGroup sex = (RadioGroup) findViewById(R.id.radioGroup1);
        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton r = (RadioButton) findViewById(checkedId);
                strgender = r.getText().toString();
            }
        });

    }

    //注册操作
    private void submit() {
        // validate

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


        String uuk = et_uuk.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, "请输入密钥", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwd.equals(uuk)) {
            Toast.makeText(this, "密码与密钥不能相同", Toast.LENGTH_SHORT).show();
            et_pwd.setText("");
            et_pwd_once.setText("");
            et_uuk.setText("");
            return;
        }






        String account1 = et_account.getText().toString().trim();
        if(getContainChinese(account1).contains("1")||getContainChinesefuhao(account1).contains("1")){
            Toast.makeText(this, "请输入字母和数字，账号不允许有中文和符号", Toast.LENGTH_SHORT).show();
            return;

        }

        String stuacc=et_stuacou.getText().toString().trim();
        if (TextUtils.isEmpty(stuacc)) {
            Toast.makeText(this, "请输入学号", Toast.LENGTH_SHORT).show();
            return;
        }

        String stuna=et_stuname.getText().toString().trim();
        if (TextUtils.isEmpty(stuna)) {
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!getContainChinese(stuna).contains("1")){
            Toast.makeText(this, "姓名只能为中文", Toast.LENGTH_SHORT).show();
            return;

        }





            reg(account,pwd,strgender,uuk,stuacc,stuna);







    }


    public static String getContainChinese(String a) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(a);
        String result="";
        while (m.find()) {
            result="1";

        }
        return result;
    }

    public static String getContainChinesefuhao(String a) {
        Pattern p = Pattern.compile("[\\u3002\\uff1b\\uff0c\\uff1a\\u201c\\u201d\\uff08\\uff09\\u3001\\uff1f\\u300a\\u300b]");
        Matcher m = p.matcher(a);
        String result="";
        while (m.find()) {
            result="1";

        }
        return result;
    }



    //查询是否注册
    private void reg(final String name, final String pwd, final String strgender,final String uuk,final String stac,final String stna) {
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account",name);
        BmobQuery<User> bmobQuery01 = new BmobQuery<>();
        bmobQuery01.addWhereEqualTo("stuname",stna);
        BmobQuery<User> bmobQuery02 = new BmobQuery<>();
        bmobQuery02.addWhereEqualTo("stuaccount",stac);
        //或条件
        List<BmobQuery<User>> queries = new ArrayList<BmobQuery<User>>();
        queries.add(bmobQuery);
        queries.add(bmobQuery01);
        queries.add(bmobQuery02);
        BmobQuery<User> mainQuery = new BmobQuery<User>();
        mainQuery.or(queries);
        mainQuery.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                Log.e("COUNT",list.size()+"---");
                if (list.size()==0){
                    save(name,pwd,strgender,uuk,stac,stna);
                }else {
                    Toast.makeText(RegActivity.this, "账号或学号或姓名已存在"+list.size(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(RegActivity.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });

    }
//注册到数据库
    private void save(String name, String pwd,String strgender,String uuk,String sac,String sna) {
        User category = new User();
        category.account=(name);
        category.pwd=(pwd);
        category.userlimit="2";
        category.gender=(strgender);
        category.fh="0";
        category.imgflag="0";
        category.ukey=(uuk);
        category.stuaccount=sac;
        category.stuname=sna;
        category.stuverify="0";



        category.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegActivity.this, "注册成功,等待管理员审核", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(RegActivity.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }




}
