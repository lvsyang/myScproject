package com.example.schoolcircle.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.example.schoolcircle.bean.GongGao;
import com.guoxiaoxing.phoenix.compress.picture.internal.PictureCompressor;
import com.guoxiaoxing.phoenix.core.PhoenixOption;
import com.guoxiaoxing.phoenix.core.listener.ImageLoader;
import com.guoxiaoxing.phoenix.core.model.MediaEntity;
import com.guoxiaoxing.phoenix.core.model.MimeType;
import com.guoxiaoxing.phoenix.picker.Phoenix;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2020/3/17.
 */

public class PubGongGaoAct extends BaseActivity {
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_right;
    private ImageView ivadd;
    private EditText et_content,et_msg;
    private String compressPath;
    private String img;
    private String soundName = "";

    private int currentChooseTime = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_gonggao);
        initView();
    }

    @Override
    public int intiLayout() {
        return R.layout.act_gonggao;
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
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_back.setVisibility(View.VISIBLE);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("发布公告");
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        tv_right.setText("发布");
        ivadd = (ImageView) findViewById(R.id.ivadd);
        ivadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Phoenix.with()
                        .theme(PhoenixOption.THEME_DEFAULT)// 主题
                        .fileType(MimeType.ofImage())//显示的文件类型图片、视频、图片和视频
                        .maxPickNumber(1)// 最大选择数量
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
                        .start(PubGongGaoAct.this, PhoenixOption.TYPE_PICK_MEDIA, 100);
            }
        });
        et_content = (EditText) findViewById(R.id.et_content);
        et_msg = (EditText) findViewById(R.id.et_msg);

    }

    @Override
    public void initData() {

    }

    private void submit() {
        // validate
        String content = et_content.getText().toString().trim();
        String msg = et_msg.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入", Toast.LENGTH_SHORT).show();
            return;
        }  if (TextUtils.isEmpty(img)) {
            Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
            return;
        }

        GongGao gongGao=new GongGao();
        gongGao.content=content;
        gongGao.msg=msg;
        gongGao.img=img;
        gongGao.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                toast("发布成功");

//                MobPushLocalNotification localNotification = new MobPushLocalNotification();
//
//                localNotification.setTitle("本地通知标题");
//
//                localNotification.setContent("本地通知内容");
//
//                localNotification.setNotificationId(本地通知ID);
//
//
//                MobPush.addLocalNotification(localNotification);


//                SimulateRequest.sendPush(1, et_content.getText().toString().trim(), 0, null, soundName, new MobPushCallback<Boolean>() {
//                    public void onCallback(Boolean result) {
////                        if (result) {
////                            new DialogShell(getContext()).autoDismissDialog(R.string.toast_notify, null, 2);
////                        } else if (!NetWorkHelper.netWorkCanUse(null)) {
////                            new DialogShell(getContext()).autoDismissDialog(R.string.error_network, null, 2);
////                        } else {
////                            new DialogShell(getContext()).autoDismissDialog(R.string.error_ukonw, null, 2);
////                        }
//                        toast("发布成功");
//                        finish();
//                    }
//                });



//                MobPushLocalNotification notification = new MobPushLocalNotification();
//                String appName = DeviceHelper.getInstance(getContext()).getAppName();
//                notification.setTitle(TextUtils.isEmpty(appName) ? getContext().getString(R.string.item_local) : appName);
//                notification.setContent(et_content.getText().toString().trim());
////				notification.setVoice(false);//可设置不进行声音提醒，默认声音、振动、指示灯
//                notification.setNotificationId(new Random().nextInt());
//                notification.setTimestamp(currentChooseTime * 60 * 1000 + System.currentTimeMillis());
//                notification.setNotifySound(soundName);
////                notification.notifyAll();
//
//                MobPush.addLocalNotification(notification);





            }

            @Override
            public void onFailure(int i, String s) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<MediaEntity> result = Phoenix.result(data);
        MediaEntity entity = result.get(0);
        String localPath = entity.getLocalPath();
        Glide.with(PubGongGaoAct.this).load(localPath).into(ivadd);
        File file = new File(localPath);
        try {
            File compressFIle = PictureCompressor.with(PubGongGaoAct.this)
                    .savePath(PubGongGaoAct.this.getCacheDir().getAbsolutePath())
                    .load(file)
                    .get();
            if (compressFIle != null) {
                compressPath = compressFIle.getAbsolutePath();

                final BmobFile bmobFile = new BmobFile(new File(compressPath));
                bmobFile.uploadblock(PubGongGaoAct.this, new UploadFileListener() {
                    @Override
                    public void onSuccess() {
                        img = bmobFile.getUrl();



                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
