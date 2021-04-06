package com.example.schoolcircle.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.CircleBean;
import com.example.schoolcircle.utils.SimpleAdapter;
import com.guoxiaoxing.phoenix.core.PhoenixOption;
import com.guoxiaoxing.phoenix.core.listener.ImageLoader;
import com.guoxiaoxing.phoenix.core.model.MediaEntity;
import com.guoxiaoxing.phoenix.core.model.MimeType;
import com.guoxiaoxing.phoenix.picker.Phoenix;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Administrator on 2020/1/1.
 */

public class PubGuanWangCircleAct extends BaseActivity {
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
    RecyclerView recyclerImg;  @BindView(R.id.ll_type)
    LinearLayout ll_type;
    private List<String> pahtList=new ArrayList<>();
    private SimpleAdapter<String> adapter;

    private ArrayAdapter<String> sadapter;


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

                    Glide.with(PubGuanWangCircleAct.this).load(R.drawable.add).into(view);
                    helper.getView(R.id.iv_delete).setVisibility(View.GONE);
                    helper.getView(R.id.iv).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (pahtList.size()==7){
                                Toast.makeText(PubGuanWangCircleAct.this,"最多选6张",Toast.LENGTH_SHORT).show();
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
                                    .start(PubGuanWangCircleAct.this, PhoenixOption.TYPE_PICK_MEDIA, 100);
                        }
                    });
                }else {
                    Glide.with(PubGuanWangCircleAct.this).load(path).into(view);
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

        //设置默认值
        ll_type.setVisibility(View.GONE);
    }

    @Override
    public void initData() {

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
                        bmobFile.uploadblock(PubGuanWangCircleAct.this, new UploadFileListener() {
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

    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
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
                break;
        }
    }
    private void pub(String content,String imgs){
        CircleBean circleBean=new CircleBean();
        circleBean.account=userbean.account;
        circleBean.content=content;
        circleBean.type="官方信息区";

        if (!TextUtils.isEmpty(userbean.nick)){
            circleBean.nick=userbean.nick;
        }
        if (userbean.img!=null){
            circleBean.head=userbean.img.getUrl();
        }

        circleBean.imgs=imgs;
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
}
