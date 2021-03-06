package com.example.schoolcircle.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.APingLun;
import com.example.schoolcircle.bean.CircleBean;
import com.example.schoolcircle.bean.PingLun;
import com.example.schoolcircle.bean.User;
import com.example.schoolcircle.bean.Zan;
import com.example.schoolcircle.utils.ACache;
import com.guoxiaoxing.phoenix.compress.picture.internal.PictureCompressor;
import com.guoxiaoxing.phoenix.core.PhoenixOption;
import com.guoxiaoxing.phoenix.core.listener.ImageLoader;
import com.guoxiaoxing.phoenix.core.model.MediaEntity;
import com.guoxiaoxing.phoenix.core.model.MimeType;
import com.guoxiaoxing.phoenix.picker.Phoenix;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Administrator on 2019/4/7.
 */

public class UserInfoAct extends BaseActivity {

    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.tv_stuac)
    TextView tvStuac;
    @BindView(R.id.tv_stname)
    TextView tvStname;
    @BindView(R.id.tv_nick)
    EditText tvNick;


    private String compressPath;

    private User user;

    private String nick_name;
    private String strgender="???";
    private SensitiveWordnk sensitiveWordnk;

private String fl="1";



    @Override
    public int intiLayout() {
        return R.layout.act_user_info;
    }

    @Override
    public void initView() {

        Phoenix.config()
                .imageLoader(new ImageLoader() {
                    @Override
                    public void loadImage(Context mContext, ImageView imageView
                            , String imagePath, int type) {
                        Glide.with(mContext)
                                .load(imagePath)
                                .into(imageView);
                    }
                });



            getuserinfo();

    }

    @Override
    public void initData() {
//        toast(userbean.img.getFilename()+"w");

//        if(userbean.img.getFilename().equals(""))

    }



    private void getuserinfo() {

        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account", userbean.account);

        bmobQuery.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                Log.e("COUNT", list.size() + "---");
                user = list.get(0);


                RadioButton r1 = (RadioButton)findViewById(R.id.radio0);
                RadioButton r2 = (RadioButton)findViewById(R.id.radio1);
                if(user.gender.equals("???")){
                    r1.setChecked(true);
                }
                else {
                    r2.setChecked(true);
                }


                tvAccount.append(user.account);
                tvStname.append(user.stuname);
                tvStuac.append(user.stuaccount);


                tvNick.append(user.nick+"");



                if (user.img != null) {
                    Glide.with(UserInfoAct.this).load(user.img.getUrl()).into(ivHead);
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(UserInfoAct.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iscf(String nick_namea) {
        BmobQuery<User> isu=new BmobQuery<>();
        isu.addWhereNotEqualTo("account",userbean.account);
        isu.addWhereEqualTo("nick",nick_namea);
        isu.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> listcf) {
                if(listcf.size()>0){
                    toast("???????????????");
                    return;
                }
                else {
                    if (TextUtils.isEmpty(compressPath)&&fl.equals("1")&&userbean.imgflag.equals("0")) {


                        user.nick=nick_name;
                        user.gender=strgender;

                        user.update(UserInfoAct.this, new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                toast("????????????");
                                update("",nick_name);

                                ACache.get(UserInfoAct.this).put("userbean", user);
                                finish();
                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });


                    }
                    else if(TextUtils.isEmpty(compressPath)&&fl.equals("1")&&!TextUtils.isEmpty(userbean.img.toString().trim())){
                        user.nick=nick_name;
                        user.gender=strgender;

                        user.update(UserInfoAct.this, new UpdateListener() {
                            @Override
                            public void onSuccess() {
                            toast("????????????");
                                update(userbean.img.getUrl(),nick_name);

                                ACache.get(UserInfoAct.this).put("userbean", user);
                                finish();
                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });

                    }
                    else {
                        final BmobFile bmobFile = new BmobFile(new File(compressPath));
                        bmobFile.uploadblock(UserInfoAct.this, new UploadFileListener() {
                            @Override
                            public void onSuccess() {
                                String url = bmobFile.getUrl();
                                Log.e("url",url);


                                user.nick=nick_name;

                                user.img = bmobFile;
                                user.gender=strgender;
                                user.update(UserInfoAct.this, new UpdateListener() {
                                    @Override
                                    public void onSuccess() {
                                        update(bmobFile.getUrl(),nick_name);
                                    toast("????????????");
                                        ACache.get(UserInfoAct.this).put("userbean", user);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

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


    @OnClick({R.id.iv_head, R.id.tv_up, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_head:
                Phoenix.with()
                        .theme(PhoenixOption.THEME_DEFAULT)// ??????
                        .fileType(MimeType.ofImage())//??????????????????????????????????????????????????????
                        .maxPickNumber(1)// ??????????????????
                        .minPickNumber(0)// ??????????????????
                        .spanCount(4)// ??????????????????
                        .enablePreview(true)// ??????????????????
                        .enableCamera(true)// ??????????????????
                        .enableAnimation(true)// ??????????????????????????????
                        .enableCompress(true)// ??????????????????
                        .compressPictureFilterSize(1024)//??????kb????????????????????????
                        .compressVideoFilterSize(2018)//??????kb????????????????????????
                        .thumbnailHeight(160)// ????????????????????????
                        .thumbnailWidth(160)// ????????????????????????
                        .enableClickSound(false)// ????????????????????????

                        .mediaFilterSize(10000)//????????????kb???????????????/??????????????????0??????????????????
                        //????????????Activity???????????????Activity???????????????Fragment???????????????Fragment
                        .start(UserInfoAct.this, PhoenixOption.TYPE_PICK_MEDIA, 100);
                fl="0";
                if(userbean.imgflag.equals("0")){
                user.imgflag="1";
                user.update(this);
                ACache.get(UserInfoAct.this).put("userbean", user);
                }
                break;
            case R.id.tv_up:

                sensitiveWordnk=new SensitiveWordnk();
                sensitiveWordnk.InitializationWork();
                nick_name=tvNick.getText().toString();
                nick_name=sensitiveWordnk.filterInfo(nick_name);
                if (TextUtils.isEmpty(nick_name)) {
//                    toast("?????????");
                    nick_name="";


                }
                iscf(nick_name);


                break;
        }
  }



    private void update(final String head, final String nick) {

        BmobQuery<CircleBean> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account",userbean.account);
        bmobQuery.findObjects(this, new FindListener<CircleBean>() {
            @Override
            public void onSuccess(List<CircleBean> list1) {
               if (list1.size()>0){
                   for (int i = 0; i < list1.size(); i++) {
                       CircleBean circleBean = list1.get(i);
                       circleBean.head=head;
                       circleBean.nick=nick;
//                       circleBean.gender=strgender;
                       circleBean.update(UserInfoAct.this);
                   }
               }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

/////////////////////////?????????????????????
        BmobQuery<PingLun> pl=new BmobQuery<>();
        pl.addWhereEqualTo("account",userbean.account);
        pl.findObjects(this, new FindListener<PingLun>() {
            @Override
            public void onSuccess(List<PingLun> listpl) {
                if (listpl.size()>0){
                    for (int i = 0; i < listpl.size(); i++) {
                        PingLun pingLun = listpl.get(i);
                        pingLun.head=head;
                        pingLun.nick=nick;
//                       circleBean.gender=strgender;
                        pingLun.update(UserInfoAct.this);
                    }
                }

            }

            @Override
            public void onError(int i, String s) {

            }
        });
/////////////////////////////??????????????????????????????
        BmobQuery<Zan> zan=new BmobQuery<>();
        zan.addWhereEqualTo("account",userbean.account);
        zan.findObjects(this, new FindListener<Zan>() {
            @Override
            public void onSuccess(List<Zan> listzan) {
                if (listzan.size()>0){
                    for (int i = 0; i < listzan.size(); i++) {
                        Zan zzz = listzan.get(i);
                        zzz.head=head;
                        zzz.nick=nick;
//                       circleBean.gender=strgender;
                        zzz.update(UserInfoAct.this);
                    }
                }

            }

            @Override
            public void onError(int i, String s) {

            }
        });


        /////////////////////////////???????????????????????????????????????
        BmobQuery<APingLun> apl=new BmobQuery<>();
        apl.addWhereEqualTo("iaccount",userbean.account);
        apl.findObjects(this, new FindListener<APingLun>() {
            @Override
            public void onSuccess(List<APingLun> listapl) {
                if (listapl.size()>0){
                    for (int i = 0; i < listapl.size(); i++) {
                        APingLun zapl = listapl.get(i);

                        zapl.head=head;
                        zapl.nick=nick;
                        zapl.update(UserInfoAct.this);
                    }
                }

            }

            @Override
            public void onError(int i, String s) {

            }
        });





    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<MediaEntity> result = Phoenix.result(data);
        MediaEntity entity = result.get(0);
        String localPath = entity.getLocalPath();
        Glide.with(UserInfoAct.this).load(localPath).into(ivHead);
        File file = new File(localPath);
        try {
            File compressFIle = PictureCompressor.with(UserInfoAct.this)
                    .savePath(UserInfoAct.this.getCacheDir().getAbsolutePath())
                    .load(file)
                    .get();
            if (compressFIle != null) {
                compressPath = compressFIle.getAbsolutePath();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * ??????????????? ?????????   -- ?????????????????????????????????
     * ???????????????????????????&????????????http://blog.csdn.net/hubiao_0618/article/details/45076871
     *
     * @author hubiao
     * @version 0.1
     * @CreateDate 2015???4???16??? 15:28:32
     */
    public class SensitiveWordnk {
        private StringBuilder replaceAll;//?????????
        private String encoding = "UTF-8";
        private String replceStr = "*";
        private int replceSize = 500;
        private String fileName = "CensorWords.txt";
        private List<String> arrayList;
        public Set<String> sensitiveWordSet;//?????????????????????????????????????????????
        public List<String> sensitiveWordList;//?????????????????????????????????????????????????????????

        /**
         * ?????????????????????src???resource????????????????????????CensorWords.txt
         *
         * @param fileName ???????????????(?????????)
         */
        public SensitiveWordnk(String fileName) {
            this.fileName = fileName;
        }

        /**
         * @param replceStr  ???????????????????????????
         * @param replceSize ??????????????????
         */
        public SensitiveWordnk(String replceStr, int replceSize) {
            this.replceStr = fileName;
            this.replceSize = replceSize;
        }

        public SensitiveWordnk() {
        }

        /*
         * @param str ?????????????????????
         * @return ??????????????????
         */
        public String filterInfo(String str) {
            sensitiveWordSet = new HashSet<String>();
            sensitiveWordList = new ArrayList<>();
            StringBuilder buffer = new StringBuilder(str);
            HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>(arrayList.size());
            String temp;
            for (int x = 0; x < arrayList.size(); x++) {
                temp = arrayList.get(x);
                int findIndexSize = 0;
                for (int start = -1; (start = buffer.indexOf(temp, findIndexSize)) > -1; ) {
                    //System.out.println("###replace="+temp);
                    findIndexSize = start + temp.length();//??????????????????????????????
                    Integer mapStart = hash.get(start);//????????????
                    if (mapStart == null || (mapStart != null && findIndexSize > mapStart))//??????1??????????????????map
                    {
                        hash.put(start, findIndexSize);
                        //System.out.println("###????????????"+buffer.substring(start, findIndexSize));
                    }
                }
            }
            Collection<Integer> values = hash.keySet();
            for (Integer startIndex : values) {
                Integer endIndex = hash.get(startIndex);
                //??????????????????????????????????????????????????????
                String sensitive = buffer.substring(startIndex, endIndex);
                //System.out.println("###????????????"+sensitive);
                if (!sensitive.contains("*")) {//????????????????????????
                    sensitiveWordSet.add(sensitive);
                    sensitiveWordList.add(sensitive);
                }
                buffer.replace(startIndex, endIndex, replaceAll.substring(0, endIndex - startIndex));
            }
            hash.clear();
            return buffer.toString();
        }

        /**
         * ?????????????????????
         */
        public void InitializationWork() {
            replaceAll = new StringBuilder(replceSize);
            for (int x = 0; x < replceSize; x++) {
                replaceAll.append(replceStr);
            }
            //????????????
            arrayList = new ArrayList<String>();
            InputStreamReader read = null;
            BufferedReader bufferedReader = null;
            try {
                //read = new InputStreamReader(SensitiveWord.class.getClassLoader().getResourceAsStream(fileName),encoding);
                //bufferedReader = new BufferedReader(read);
            /*for(String txt = null;(txt = bufferedReader.readLine()) != null;){
                if(!arrayList.contains(txt))
                    arrayList.add(txt);
            }*/

                //????????????
                String[] a = initAssets();
                for (int i = 0; i < a.length; i++) {
                    if (!arrayList.contains(a[i]))
                        arrayList.add(a[i]);
                }

            } finally {
                try {
                    if (null != bufferedReader)
                        bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (null != read)
                        read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /*
     * ?????? ???String ????????????
     *
     * @return
     */
    public String[] initAssets() {
        try {
            //???????????????
            InputStream inputStream = getAssets().open("CensorWords");//????????????????????????txt ??????????????????

            //??????????????????
            String str = getString(inputStream);
            //???????????? ?????????????????????
            String[] arr = str.split("\n");
            return arr;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * ??????????????????
     *
     * @param inputStream
     * @return
     */
    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        //?????????????????????
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            //??????????????????
            while ((line = reader.readLine()) != null) {
                //???????????????????????????
                sb.append(line);
                //????????????
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //???????????????????????????
        return sb.toString();
    }



}
