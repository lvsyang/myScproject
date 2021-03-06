package com.example.schoolcircle.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.CircleBean;
import com.example.schoolcircle.bean.MTopic;
import com.example.schoolcircle.utils.SimpleAdapter;
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
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Administrator on 2020/1/1.
 */

public class PubCircleAct extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.spiner)
    Spinner spiner;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.recycler_img)
    RecyclerView recyclerImg;
    @BindView(R.id.addtopic)
    Button addtopic;
    @BindView(R.id.xstopic)
    TextView xstopic;
    private TextView xstopicss;


    private List<String> pahtList=new ArrayList<>();
    private SimpleAdapter<String> adapter;
//    private static final String[] m={"?????????","?????????","???????????????","???????????????","???????????????"};
//    private static final String[] m={"?????????","?????????","???????????????","???????????????"};
private static final String[] m={"?????????","?????????","?????????"};

    private ArrayAdapter<String> sadapter;
    private String type=m[0];

    private AlertDialog alertDialog;


    private SensitiveWord sensitiveWord;

    @Override
    public int intiLayout() {
        return R.layout.act_pub;
    }
    private static <E> void swap(List<E> list,int index1,int index2) {
        //?????????????????????
        E e=list.get(index1);
        //?????????
        list.set(index1, list.get(index2));
        list.set(index2, e);
    }


    @Override
    public void initView() {

        tvTitle.setText("??????");
        tvRight.setText("??????");
        ivBack.setVisibility(View.VISIBLE);
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


        pahtList.add("s");
        recyclerImg.setLayoutManager(new GridLayoutManager(this,3));
        adapter = new SimpleAdapter<>(R.layout.item_photo, pahtList, new SimpleAdapter.ConVert<String>() {
            @Override
            public void convert(BaseViewHolder helper, final String path) {
                ImageView view = helper.getView(R.id.iv);
                if (path.equals("s")){

                    Glide.with(PubCircleAct.this).load(R.drawable.add).into(view);
                    helper.getView(R.id.iv_delete).setVisibility(View.GONE);
                    helper.getView(R.id.iv).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (pahtList.size()==7){
                                Toast.makeText(PubCircleAct.this,"?????????6???",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Phoenix.with()
                                    .theme(PhoenixOption.THEME_DEFAULT)// ??????
                                    .fileType(MimeType.ofImage())//??????????????????????????????????????????????????????
                                    .maxPickNumber(7-pahtList.size())// ??????????????????
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
                                    .start(PubCircleAct.this, PhoenixOption.TYPE_PICK_MEDIA, 100);
                        }
                    });
                }else {
                    Glide.with(PubCircleAct.this).load(path).into(view);
                    helper.getView(R.id.iv_delete).setVisibility(View.VISIBLE);
                    helper.getView(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pahtList.remove(path);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }


            }
        });
        recyclerImg.setAdapter(adapter);

        //??????????????????ArrayAdapter????????????
        sadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);

        //???????????????????????????
        sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //???adapter ?????????spinner???
        spiner.setAdapter(sadapter);

        //????????????Spinner????????????
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               type=m[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //???????????????
        spiner.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensitiveWord=new SensitiveWord();
        sensitiveWord.InitializationWork();
    }

    @Override
    public void initData() {
        inittopic();
//        txstopic();

    }

    @Override
    protected void onResume() {
        super.onResume();
        txstopic();
    }



    private void txstopic() {
        BmobQuery<MTopic> mtc=new BmobQuery<>();
        mtc.addWhereEqualTo("account",userbean.account);
        mtc.findObjects(this, new FindListener<MTopic>() {
            @Override
            public void onSuccess(List<MTopic> listmtc) {
                if(listmtc.size()>0){
                    xstopic.setText(listmtc.get(0).content);
                }
//                else {toast("11211");}
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void inittopic() {
        MTopic mTopic=new MTopic();
        mTopic.account=userbean.account;
        mTopic.content="";
        mTopic.save(PubCircleAct.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<String> strings = new ArrayList<>();
        List<MediaEntity> result = Phoenix.result(data);
        for (int i = 0; i < result.size(); i++) {
            strings.add(result.get(i).getLocalPath());
        }

        Luban.with(this)
                .load(strings)                                   // ??????????????????????????????
                .ignoreBy(100)                                  // ??????????????????????????????
                // ?????????????????????????????????
                .setCompressListener(new OnCompressListener() { //????????????
                    @Override
                    public void onStart() {
                        // TODO ???????????????????????????????????????????????? loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO ??????????????????????????????????????????????????????
                        final BmobFile bmobFile=new BmobFile(file);
                        bmobFile.uploadblock(PubCircleAct.this, new UploadFileListener() {
                            @Override
                            public void onSuccess() {


                                    pahtList.add(bmobFile.getUrl().toString());
                                    swap(pahtList,pahtList.size()-2,pahtList.size()-1);

                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });

                        // ????????????????????????

                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO ????????????????????????????????????
                    }
                }).launch();    //????????????


    }

    @OnClick({R.id.iv_back, R.id.tv_right,R.id.addtopic,R.id.retopics})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                deltopic();
                finish();
                break;
            case R.id.tv_right:
                String s = etContent.getText().toString();
                if (TextUtils.isEmpty(s)){
                    toast("?????????");
                    return;
                }
                String imgs="";
                if (pahtList.size()>1){
                    pahtList.remove("s");
                    for (int i = 0; i < pahtList.size(); i++) {
                        imgs+=pahtList.get(i)+"&";
                    }
                    imgs=imgs.substring(0,imgs.length()-1);
                }
                pub(s,imgs);
                deltopic();
                break;
            case R.id.addtopic:
                jumpAct(TopicSelectAct.class);
                break;
            case R.id.retopics:

                AlertDialog.Builder builder2=new AlertDialog.Builder(PubCircleAct.this);
                builder2.setTitle("????????????????????????");
                builder2.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PubCircleAct.this.alertDialog.dismiss();
                    }
                });
                builder2.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        retop();
                        xstopic.setText("");
                        PubCircleAct.this.alertDialog.dismiss();
                    }
                });
                this.alertDialog = builder2.create();
                this.alertDialog.show();

                break;


        }
    }

    private void retop() {
        BmobQuery<MTopic> bm=new BmobQuery<>();
        bm.addWhereEqualTo("account",userbean.account);
        bm.findObjects(this, new FindListener<MTopic>() {
            @Override
            public void onSuccess(List<MTopic> listmm) {
                if(listmm.size()>0){
                    MTopic mmm=new MTopic();
                    mmm=listmm.get(0);
                    mmm.content="";
                    mmm.update(PubCircleAct.this);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void deltopic() {
        BmobQuery<MTopic> mTopicBmobQuery=new BmobQuery<>();
        mTopicBmobQuery.addWhereEqualTo("account",userbean.account);
        mTopicBmobQuery.findObjects(this, new FindListener<MTopic>() {
            @Override
            public void onSuccess(List<MTopic> listmt) {
                if(listmt.size()>0){
                    for(int i=0;i<listmt.size();i++) {
                        MTopic mt = new MTopic();
                        mt = listmt.get(i);
                        mt.delete(PubCircleAct.this);
                    }}
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void pub(String content,String imgs){
        CircleBean circleBean=new CircleBean();
        circleBean.account=userbean.account;//???????????????????????????
        content = sensitiveWord.filterInfo(content);//?????????????????????
        circleBean.content=content;


//        circleBean.topic="";

//        if(content.contains("??????")||content.contains("??????")||content.contains("??????")||content.contains("??????")||content.contains("??????")||content.contains("??????")){
//            circleBean.topic+="??????"+"&";
//        }
//        if(content.contains("??????")||content.contains("??????")||content.contains("?????????")||content.contains("??????")||content.contains("??????")||content.contains("??????")||content.contains("??????")||content.contains("??????")||content.contains("NBA")||content.contains("CBA")||content.contains("??????")){
//            circleBean.topic+="??????"+"&";
//        }
//        if(content.contains("?????????")||content.contains("????????????")||content.contains("??????")||content.contains("??????")||content.contains("??????")||content.contains("??????")||content.contains("????????????")||content.contains("????????????")||content.contains("??????")){
//            circleBean.topic+="??????"+"&";
//        }
//
//        if(content.contains("??????")||content.contains("??????")||content.contains("?????????")||content.contains("??????")||content.contains("??????")||content.contains("?????????")){
//            circleBean.topic+="??????"+"&";
//        }
//
//        if(content.contains("??????")||content.contains("??????")||content.contains("??????")||content.contains("??????????????????")||content.contains("??????")||content.contains("??????")){
//            circleBean.topic+="??????"+"&";
//        }
//
//        if(content.contains("??????")||content.contains("??????")||content.contains("??????")||content.contains("??????")||content.contains("????????????")||content.contains("?????????")){
//            circleBean.topic+="??????"+"&";
//        }



        circleBean.saleflag="0";




        circleBean.type=type;//??????????????????
        if (!TextUtils.isEmpty(userbean.nick)){//??????????????????????????????
            circleBean.nick=userbean.nick;
        }
        if (userbean.img!=null){
            circleBean.head=userbean.img.getUrl();
        }
        circleBean.imgs=imgs;
        circleBean.topic=xstopic.getText().toString().trim();
        circleBean.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                toast("????????????");
                finish();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    /**
     * ??????????????? ?????????   -- ?????????????????????????????????
     * ???????????????????????????&????????????http://blog.csdn.net/hubiao_0618/article/details/45076871
     *
     * @author hubiao
     * @version 0.1
     * @CreateDate 2015???4???16??? 15:28:32
     */
    public class SensitiveWord {
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
        public SensitiveWord(String fileName) {
            this.fileName = fileName;
        }

        /**
         * @param replceStr  ???????????????????????????
         * @param replceSize ??????????????????
         */
        public SensitiveWord(String replceStr, int replceSize) {
            this.replceStr = fileName;
            this.replceSize = replceSize;
        }

        public SensitiveWord() {
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
