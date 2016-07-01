package com.jacob.viewdraghelper.lession7;

import android.os.Parcelable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fcm
 * @Create at 2013-8-27 下午2:48:34
 * @Version 1.0
 * <p>
 * Features draft description.
 * 主要功能介绍
 * </p>
 */
public class MyPagerAdapter extends PagerAdapterX {
    private ArrayList<View> mPageViews = new ArrayList<View>();

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        // TODO Auto-generated method stub  
        ((ViewPagerX) arg0).removeView(mPageViews.get(arg1 % mPageViews.size()));
    }

    @Override
    public Object instantiateItem(View arg0, int arg1) {
        // TODO Auto-generated method stub  
        ((ViewPagerX) arg0).addView(mPageViews.get(arg1 % mPageViews.size()));

//        arg0.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				return  mViewPager.dispatchTouchEvent(event);  
//			}
//		});

        return mPageViews.get(arg1 % mPageViews.size());
    }

//    public float getPageWidth(int position) {
//        return (float) 0.9;
//    }

    public void setData(List<View> mImageList) {
        // TODO Auto-generated method stub
        this.mPageViews = (ArrayList<View>) mImageList;
    }

}
