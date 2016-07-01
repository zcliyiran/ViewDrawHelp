package com.jacob.viewdraghelper.lession7;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jacob.viewdraghelper.R;

import java.util.ArrayList;

public class LessonSevenActivity extends AppCompatActivity {

    private DragLayoutRight drag;
    private ViewPagerX mViewPager1;
    private ArrayList<View> mImageList;
    private MyPagerAdapter mAdapter;
    private static final float MIN_SCALE = 0.75f;
    private RelativeLayout containor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//        drag.setVerticalScroll(new DragLayoutRight.VerticalHorizontalScroll() {
//            @Override
//            public void goNext() {
//
//                Toast.makeText(LessonSevenActivity.this, "goNext", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void goPrev() {
//                Toast.makeText(LessonSevenActivity.this, "goPrev", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void goRight() {
//
//                Toast.makeText(LessonSevenActivity.this, "goRight", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void goLeft() {
//
//                Toast.makeText(LessonSevenActivity.this, "goLeft", Toast.LENGTH_SHORT).show();
//            }
//        });

        mImageList = new ArrayList<View>();
        initView();
        mAdapter = new MyPagerAdapter();
        mAdapter.setData(mImageList);
        mViewPager1.setAdapter(mAdapter);

//        mViewPager1.setPageMargin(-100);
//        mViewPager1.setOffscreenPageLimit(8);
//        mViewPager1.setHorizontalFadingEdgeEnabled(false);
        //mViewPager1.setClipChildren(false);
        //containor.setClipChildren(false);
//        ((ViewGroup) mViewPager1.getParent()).setClipChildren(false);
//		((ViewGroup) containor.getParent()).setClipChildren(false);
        mViewPager1.setPageTransformer(true, new ViewPagerX.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();

                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0);

                } else if (position <= 0) { // [-1,0]
                    // Use the default slide transition when moving to the left page
                    view.setAlpha(1);
                    view.setTranslationY(0);
                    view.setScaleX(1);
                    view.setScaleY(1);

                } else if (position <= 1) { // (0,1]
                    // Fade the page out.
                    view.setAlpha(1 - position);

                    // Counteract the default slide transition
                    view.setTranslationY(pageWidth * -position);

                    // Scale the page down (between MIN_SCALE and 1)
                    float scaleFactor = MIN_SCALE
                            + (1 - MIN_SCALE) * (1 - Math.abs(position));
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0);
                }
            }
        });
        containor.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return mViewPager1.dispatchTouchEvent(event);
            }
        });
    }

    private void initView() {
        // TODO Auto-generated method stub
        containor = (RelativeLayout) findViewById(R.id.containor);
        int width = getWindow().getWindowManager().getDefaultDisplay()
                .getWidth() / 2;
        mViewPager1 = (ViewPagerX) findViewById(R.id.viewPager);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_pager, mViewPager1, false);
        drag = ((DragLayoutRight) view.findViewById(R.id.dlr));
        mImageList.add(view);

        View view2 = LayoutInflater.from(this).inflate(R.layout.layout_pager, mViewPager1, false);
        DragLayoutRight drag2 = ((DragLayoutRight) view2.findViewById(R.id.dlr));
        mImageList.add(view2);

        View view3 = LayoutInflater.from(this).inflate(R.layout.layout_pager, mViewPager1, false);
        DragLayoutRight drag3 = ((DragLayoutRight) view3.findViewById(R.id.dlr));
        mImageList.add(view3);

        View view4 = LayoutInflater.from(this).inflate(R.layout.layout_pager, mViewPager1, false);
        mImageList.add(view4);
    }

}
