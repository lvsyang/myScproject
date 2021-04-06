package com.example.schoolcircle.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2020/1/1.
 */

public class User extends BmobObject {
    public String account;
    public String pwd;
    public String nick;
    public String userlimit;
    public String gender;
    public BmobFile img;
    public String fh;
    public String ukey;
    public String logtime;
    public String imgflag;
    public String stuname;
    public String stuverify;
    public String stuaccount;


}
