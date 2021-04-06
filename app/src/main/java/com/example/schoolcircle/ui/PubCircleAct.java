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
//    private static final String[] m={"热门区","关注区","学习交流区","发呆娱乐区","二手市场区"};
//    private static final String[] m={"热门区","学习区","发呆娱乐区","二手市场区"};
private static final String[] m={"专心学","随心聊","任性卖"};

    private ArrayAdapter<String> sadapter;
    private String type=m[0];

    private AlertDialog alertDialog;


    private SensitiveWord sensitiveWord;

    @Override
    public int intiLayout() {
        return R.layout.act_pub;
    }
    private static <E> void swap(List<E> list,int index1,int index2) {
        //定义第三方变量
        E e=list.get(index1);
        //交换值
        list.set(index1, list.get(index2));
        list.set(index2, e);
    }


    @Override
    public void initView() {

        tvTitle.setText("发布");
        tvRight.setText("确定");
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
                                Toast.makeText(PubCircleAct.this,"最多选6张",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Phoenix.with()
                                    .theme(PhoenixOption.THEME_DEFAULT)// 主题
                                    .fileType(MimeType.ofImage())//显示的文件类型图片、视频、图片和视频
                                    .maxPickNumber(7-pahtList.size())// 最大选择数量
                                    .minPickNumber(0)// 最小选择数量
                                    .spanCount(4)// 每行显示个数
                                    .enablePreview(true)// 是否开启预览
                                    .enableCamera(true)// 是否开启拍照
                                    .enableAnimation(true)// 选择界面图片点击效果
                                    .enableCompress(true)// 是否开启压缩
                                    .compressPictureFilterSize(1024)//多少kb以下的图片不压缩
                                    .compressVideoFilterSize(2018)//多少kb以下的视频不压缩
                                    .thumbnailHeight(160)// 选择界面图片高度
                                    .thumbnailWidth(160)// 选择界面图片宽度
                                    .enableClickSound(false)// 是否开启点击声音

                                    .mediaFilterSize(10000)//显示多少kb以下的图片/视频，默认为0，表示不限制
                                    //如果是在Activity里使用就传Activity，如果是在Fragment里使用就传Fragment
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

        //将可选内容与ArrayAdapter连接起来
        sadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);

        //设置下拉列表的风格
        sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spiner.setAdapter(sadapter);

        //添加事件Spinner事件监听
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               type=m[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //设置默认值
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
                .load(strings)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
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

                        // 在视图中显示图片

                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();    //启动压缩


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
                    toast("请输入");
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
                builder2.setTitle("确定重选话题吗？");
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PubCircleAct.this.alertDialog.dismiss();
                    }
                });
                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
        circleBean.account=userbean.account;//获取动态发布者账号
        content = sensitiveWord.filterInfo(content);//进行敏感词过滤
        circleBean.content=content;


//        circleBean.topic="";

//        if(content.contains("学习")||content.contains("作业")||content.contains("做题")||content.contains("题目")||content.contains("试卷")||content.contains("考试")){
//            circleBean.topic+="学习"+"&";
//        }
//        if(content.contains("篮球")||content.contains("足球")||content.contains("乒乓球")||content.contains("跳绳")||content.contains("铅球")||content.contains("跑步")||content.contains("做操")||content.contains("滑冰")||content.contains("NBA")||content.contains("CBA")||content.contains("打球")){
//            circleBean.topic+="运动"+"&";
//        }
//        if(content.contains("打游戏")||content.contains("王者荣耀")||content.contains("电竞")||content.contains("游戏")||content.contains("排位")||content.contains("吃鸡")||content.contains("和平精英")||content.contains("绝地求生")||content.contains("王者")){
//            circleBean.topic+="游戏"+"&";
//        }
//
//        if(content.contains("房间")||content.contains("打扫")||content.contains("洗衣服")||content.contains("床单")||content.contains("生活")||content.contains("大扫除")){
//            circleBean.topic+="生活"+"&";
//        }
//
//        if(content.contains("校园")||content.contains("学校")||content.contains("岭师")||content.contains("岭南师范学院")||content.contains("老师")||content.contains("饭堂")){
//            circleBean.topic+="校园"+"&";
//        }
//
//        if(content.contains("美食")||content.contains("蛋糕")||content.contains("饭菜")||content.contains("水果")||content.contains("番茄炒蛋")||content.contains("红烧肉")){
//            circleBean.topic+="美食"+"&";
//        }



        circleBean.saleflag="0";




        circleBean.type=type;//获取指定类别
        if (!TextUtils.isEmpty(userbean.nick)){//如果有图片则上传图片
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
                toast("发布成功");
                finish();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    /**
     * 敏感词过滤 工具类   -- 【匹配度高，可以使用】
     * 《高效精准》敏感字&词过滤：http://blog.csdn.net/hubiao_0618/article/details/45076871
     *
     * @author hubiao
     * @version 0.1
     * @CreateDate 2015年4月16日 15:28:32
     */
    public class SensitiveWord {
        private StringBuilder replaceAll;//初始化
        private String encoding = "UTF-8";
        private String replceStr = "*";
        private int replceSize = 500;
        private String fileName = "CensorWords.txt";
        private List<String> arrayList;
        public Set<String> sensitiveWordSet;//包含的敏感词列表，过滤掉重复项
        public List<String> sensitiveWordList;//包含的敏感词列表，包括重复项，统计次数

        /**
         * 文件要求路径在src或resource下，默认文件名为CensorWords.txt
         *
         * @param fileName 词库文件名(含后缀)
         */
        public SensitiveWord(String fileName) {
            this.fileName = fileName;
        }

        /**
         * @param replceStr  敏感词被转换的字符
         * @param replceSize 初始转义容量
         */
        public SensitiveWord(String replceStr, int replceSize) {
            this.replceStr = fileName;
            this.replceSize = replceSize;
        }

        public SensitiveWord() {
        }

        /*
         * @param str 将要被过滤信息
         * @return 过滤后的信息
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
                    findIndexSize = start + temp.length();//从已找到的后面开始找
                    Integer mapStart = hash.get(start);//起始位置
                    if (mapStart == null || (mapStart != null && findIndexSize > mapStart))//满足1个，即可更新map
                    {
                        hash.put(start, findIndexSize);
                        //System.out.println("###敏感词："+buffer.substring(start, findIndexSize));
                    }
                }
            }
            Collection<Integer> values = hash.keySet();
            for (Integer startIndex : values) {
                Integer endIndex = hash.get(startIndex);
                //获取敏感词，并加入列表，用来统计数量
                String sensitive = buffer.substring(startIndex, endIndex);
                //System.out.println("###敏感词："+sensitive);
                if (!sensitive.contains("*")) {//添加敏感词到集合
                    sensitiveWordSet.add(sensitive);
                    sensitiveWordList.add(sensitive);
                }
                buffer.replace(startIndex, endIndex, replaceAll.substring(0, endIndex - startIndex));
            }
            hash.clear();
            return buffer.toString();
        }

        /**
         * 初始化敏感词库
         */
        public void InitializationWork() {
            replaceAll = new StringBuilder(replceSize);
            for (int x = 0; x < replceSize; x++) {
                replaceAll.append(replceStr);
            }
            //加载词库
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

                //改成这个
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
     * 返回 以String 数组形式
     *
     * @return
     */
    public String[] initAssets() {
        try {
            //获取输入流
            InputStream inputStream = getAssets().open("CensorWords");//这里的名字是你的txt 文本文件名称

            //获取学生名单
            String str = getString(inputStream);
            //字符分割 每行为一个学生
            String[] arr = str.split("\n");
            return arr;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件内容
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
        //创建字符缓冲流
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            //读取每行学生
            while ((line = reader.readLine()) != null) {
                //添加到字符缓冲流中
                sb.append(line);
                //一条一行
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回学生名单字符串
        return sb.toString();
    }


}
