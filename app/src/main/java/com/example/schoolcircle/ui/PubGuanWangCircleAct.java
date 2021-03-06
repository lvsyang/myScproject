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

                    Glide.with(PubGuanWangCircleAct.this).load(R.drawable.add).into(view);
                    helper.getView(R.id.iv_delete).setVisibility(View.GONE);
                    helper.getView(R.id.iv).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (pahtList.size()==7){
                                Toast.makeText(PubGuanWangCircleAct.this,"?????????6???",Toast.LENGTH_SHORT).show();
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

        //??????????????????ArrayAdapter????????????

        //???????????????
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

                        // ????????????????????????

                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO ????????????????????????????????????
                    }
                }).launch();    //????????????


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
                break;
        }
    }
    private void pub(String content,String imgs){
        CircleBean circleBean=new CircleBean();
        circleBean.account=userbean.account;
        circleBean.content=content;
        circleBean.type="???????????????";

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
                toast("????????????");
                finish();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}
