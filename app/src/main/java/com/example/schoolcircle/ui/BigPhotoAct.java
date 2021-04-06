package com.example.schoolcircle.ui;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2020/1/3.
 */

public class BigPhotoAct extends BaseActivity {
    @BindView(R.id.banner)
    Banner banner;


    @Override
    public int intiLayout() {
        return R.layout.act_big_photo;
    }

    @Override
    public void initView() {
        Bundle extras = getIntent().getExtras();
        ArrayList<String> urls = (ArrayList<String>) extras.getSerializable("urls");
        initBanner(urls);
    }

    @Override
    public void initData() {

    }
    private void initBanner( ArrayList<String> urls) {
        banner = (Banner) findViewById(R.id.banner);
        //放图片地址的集合


        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(urls);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合

        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(false);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。

                //必须最后调用的方法，启动轮播图。
                .start();


    }

    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }

}
