package com.sty.pinch.image.viewpager;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sty.pinch.image.viewpager.view.PinchImageView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TextView tvPagerIndicator;

    private MyPagerAdapter mPagerAdapter;
    private List<Integer> mDataList;
    private LinkedList<PinchImageView> mViewCache = new LinkedList<>();

    private int mCurSelectIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createData();
        initView();
    }

    private void createData(){
        mDataList = new ArrayList<>();
        mDataList.add(R.drawable.meng1);
        mDataList.add(R.drawable.meng2);
        mDataList.add(R.drawable.meng3);
        mDataList.add(R.drawable.meng4);
        mDataList.add(R.drawable.meng5);
    }

    private void initView(){
        viewPager = findViewById(R.id.vp_view_pager);
        tvPagerIndicator = findViewById(R.id.tv_pager_indicator);

        mPagerAdapter = new MyPagerAdapter(mDataList);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mCurSelectIndex = position;
                setPageNumber();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private class MyPagerAdapter extends PagerAdapter{
        private List<Integer> dataList; //图片资源Id

        public MyPagerAdapter(List<Integer> dataList){
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PinchImageView piv;
            if(mViewCache.size() > 0){
                piv = mViewCache.remove();
                piv.reset();
            }else {
                piv = new PinchImageView(MainActivity.this);
            }
            Glide.with(MainActivity.this)
                    .load(mDataList.get(position))
                    .into(piv);
            container.addView(piv);

            return piv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            PinchImageView piv = (PinchImageView) object;
            container.removeView(piv);
            mViewCache.add(piv);
        }

        public void addImage(int resourceId){
            dataList.add(resourceId);
            notifyDataSetChanged();
        }

        public void removeImage(int resourceId){
            for(Integer integer : dataList){
                if(integer == resourceId){
                    dataList.remove(integer);
                    notifyDataSetChanged();
                }
            }
        }
    }

    private void setPageNumber(){
        tvPagerIndicator.setText((mCurSelectIndex + 1) + "/" + mDataList.size());
    }
}
