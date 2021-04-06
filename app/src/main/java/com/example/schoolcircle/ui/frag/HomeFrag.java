package com.example.schoolcircle.ui.frag;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.schoolcircle.R;
import com.example.schoolcircle.base.BaseFragment;
import com.example.schoolcircle.bean.GongGao;
import com.example.schoolcircle.ui.GongGaoDetialAct;
import com.example.schoolcircle.ui.PubCircleAct;
import com.example.schoolcircle.ui.SearchAct;
import com.example.schoolcircle.utils.MyFragmentPagerAdapter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by Administrator on 2019/3/22.
 */
//首页
public class HomeFrag extends BaseFragment {

//    @BindView(R.id.tv_title)
//    TextView tvTitle;
    @BindView(R.id.iv_pub)
    ImageView iv_pub;

    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.homesearch)
    LinearLayout homesearch;
    private List<String> titles = new ArrayList<>();
    private List<BaseFragment> fragments = new ArrayList<>();
    private List<String> imageUrl=new ArrayList<>();
    private List<String> strings=new ArrayList<>();



//    private MyListener listener;
//    //自定义接口
//    public interface MyListener{
//        void message(String s);
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        //拿到和当前碎片相关联的activity
//        listener = (MyListener) getActivity();
//    }




    //设置xml
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate( R.layout.frg_home, container,false);
    }

    @Override
    public void initData() {
        super.initData();
        //recyclerview绑定adapter
//        tvTitle.setText("首页");
        iv_pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpAct(PubCircleAct.class);
            }
        });
        homesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpAct(SearchAct.class);
            }
        });
        titles.add("热门区");
        titles.add("关注区");
        titles.add("转发区");
        titles.add("专心学");
        titles.add("随心聊");
        titles.add("任性卖");
        titles.add("官方信息区");


        fragments.add(CircleFrag.newInstance("热门区"));
        fragments.add( CircleFrag.newInstance("关注区"));
        fragments.add( CircleFrag.newInstance("转发区"));
        fragments.add( CircleFrag.newInstance("专心学"));
        fragments.add( CircleFrag.newInstance("随心聊"));
        fragments.add( CircleFrag.newInstance("任性卖"));
        fragments.add( CircleFrag.newInstance("官方信息区"));

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragments, titles);
        vp.setAdapter(adapter);
        tab.setupWithViewPager(vp);
        getGongGao();
    }

   private void getGongGao(){
       BmobQuery<GongGao> bmobQuery = new BmobQuery<>();
       bmobQuery.order("-createdAt");
       bmobQuery.findObjects(getActivity(), new FindListener<GongGao>() {
           @Override
           public void onSuccess(List<GongGao> list1) {
               for (int i = 0; i < list1.size(); i++) {
                   GongGao gongGao = list1.get(i);
                   strings.add(gongGao.content);
                   imageUrl.add(gongGao.img);
               }
                startBanner();




           }

           @Override
           public void onError(int i, String s) {

           }
       });
   }
    private void startBanner() {
        //设置banner样式(显示圆形指示器)
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置指示器位置（指示器居右）
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(imageUrl);
        //设置banner动画效果
//        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(strings);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2000);
        //banner设置方法全部调用完毕时最后调用

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {//position是当前的数组下标
//                Toast.makeText(mActivity, " "+strings.get(position), Toast.LENGTH_SHORT).show();

//                listener.message(strings.get(position));

                Intent intent = new Intent(getActivity(),GongGaoDetialAct.class);
                intent.putExtra("www",strings.get(position));
                startActivity(intent);//启动Activity


            }
        });

        banner.start();
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //Glide 加载图片简单用法
            Glide.with(context).load(path).into(imageView);
        }
    }












}
