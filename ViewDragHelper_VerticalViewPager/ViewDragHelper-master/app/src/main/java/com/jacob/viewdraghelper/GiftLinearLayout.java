package com.jacob.viewdraghelper;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzhili on 16/2/16.
 */
public class GiftLinearLayout extends LinearLayout {


    private Context context;
    private RelativeLayout relative1, relative2;
    private float startMargin = 0;
    private Path mPath;
    private Paint paint;
    private List<Gift> gifts;

    public GiftLinearLayout(Context context) {
        super(context);

        initData(context);
    }


    public GiftLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    public GiftLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
    }

    public GiftLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initData(context);
    }

    private void initData(Context context) {

        this.context = context;
        setOrientation(VERTICAL);
        relative1 = new RelativeLayout(context);
        relative2 = new RelativeLayout(context);
        LinearLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
        LinearLayout.LayoutParams layoutParams2 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);

        relative1.setBackgroundColor(Color.RED);
        relative2.setBackgroundColor(Color.BLUE);

        addView(relative1, layoutParams);
        addView(relative2, layoutParams2);

        setWillNotDraw(false);
        gifts = new ArrayList<Gift>();

        postDelayed(new Runnable() {
            @Override
            public void run() {
                addGift(new Gift(1, 3, 1000));
                postDelayed(this, 5000);
            }
        }, 1000);

    }

    public void addGift(Gift gift) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_barrage_gift, this, false);
        startAnimation(view, gift);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = measureWidth(widthMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec);
        // 计算自定义的ViewGroup中所有子控件的大小
//        measureChildren(widthMeasureSpec, heightMeasureSpec);
//         设置自定义的控件MyViewGroup的大小
        setMeasuredDimension(measureWidth, measureHeight);
    }

    private int measureWidth(int pWidthMeasureSpec) {
        int result = 0;
        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸

        switch (widthMode) {
            /**
             * mode共有三种情况，取值分别为MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
             * MeasureSpec.AT_MOST。
             *
             *
             * MeasureSpec.EXACTLY是精确尺寸，
             * 当我们将控件的layout_width或layout_height指定为具体数值时如andorid
             * :layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
             *
             *
             * MeasureSpec.AT_MOST是最大尺寸，
             * 当控件的layout_width或layout_height指定为WRAP_CONTENT时
             * ，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
             * 。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
             *
             *
             * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，
             * 通过measure方法传入的模式。
             */
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }

    private int measureHeight(int pHeightMeasureSpec) {
        int result = 0;

        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }
        return result;
    }

    private void startAnimation(final View view, Gift gift) {
        final RelativeLayout imageGift = (RelativeLayout) view.findViewById(R.id.rl_gift_picture);
        imageGift.setVisibility(INVISIBLE);
        if (relative2.getChildCount() == 0) {
            startAnimation(relative2, view, imageGift, gift);
        } else if (relative1.getChildCount() == 0) {
            startAnimation(relative1, view, imageGift, gift);
        }
    }

    private void startAnimation(final RelativeLayout relativeLayoutParent, View view, final RelativeLayout imageGift, final Gift gift) {
        final TextView tvMount = (TextView) view.findViewById(R.id.tvMount);
        relativeLayoutParent.addView(view);
        relativeLayoutParent.startAnimation(new ViewShowAnimation(700));
        postDelayed(new Runnable() {
            @Override
            public void run() {
                imageGift.startAnimation(new GiftViewAnimation(700));
                imageGift.setVisibility(VISIBLE);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (gift.getCurShowMount() <= gift.getMount()) {
                            tvMount.setText("X" + gift.getCurShowMount());
                            gift.setCurShowMount(gift.getCurShowMount() + 1);
                            tvMount.startAnimation(new TextChangeAnimation(500, tvMount));
                            postDelayed(this, gift.getPerShowTime());
                        }
                    }
                }, 720);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        relativeLayoutParent.startAnimation(new ViewDismissAnimation(700));
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                removeAndShowNext(relativeLayoutParent, gift);
                            }
                        }, 720);
                    }
                }, gift.getMount() * gift.getPerShowTime() + gift.getPerShowTime());
            }
        }, 720);
    }

    private void removeAndShowNext(RelativeLayout relativeLayoutParent, Gift gift) {

        relativeLayoutParent.removeAllViews();
        gifts.remove(gift);
        if (gifts.size() > 0) {
            addGift(new Gift(1, 2, 1000));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        super.onLayout(changed, l, t, r, b);
        int mTotalHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            // 获取在onMeasure中计算的视图尺寸
            int measureHeight = childView.getMeasuredHeight();
            int measuredWidth = childView.getMeasuredWidth();
            childView.layout(5, 5 + mTotalHeight, measuredWidth - 5, mTotalHeight
                    + measureHeight - 5);
            mTotalHeight += measureHeight;

        }

    }

    public class EaseInQuartInterpolator implements TimeInterpolator {

        @Override
        public float getInterpolation(float input) {
            return input * input * input * input;
        }

    }

    class GiftViewAnimation extends Animation {
        private PathMeasure pathMeasure;
        private int animationTime;

        public GiftViewAnimation(int animationTime) {
            this.animationTime = animationTime;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            setDuration(animationTime);
            setFillAfter(true);
            setInterpolator(new LinearInterpolator());
            Path mPath = new Path();
            mPath.moveTo(-1000, 0);
            mPath.lineTo(50, 0);
            mPath.lineTo(0, 0);
            pathMeasure = new PathMeasure(mPath, false);
//            GiftLinearLayout.this.invalidate();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
//            final Matrix matrix = t.getMatrix();
//            matrix.setScale(interpolatedTime, interpolatedTime);
//            //通过坐标变换，把参考点（0,0）移动到View中间
//            matrix.preTranslate(-mCenterX, -mCenterY);
//            //动画完成后再移回来
//            matrix.postTranslate(mCenterX, mCenterY);
//            float y = getInterpolation3(10 * interpolatedTime);
            final Matrix matrix = t.getMatrix();
//            matrix.setScale(interpolatedTime, interpolatedTime);
//            camera.save();
//            camera.translate(0.0f, 0.0f, (1300 - 1300.0f * interpolatedTime));
//            camera.rotateY(360 * interpolatedTime);
//            camera.getMatrix(matrix);
//            matrix.preTranslate(-mCenterX, -mCenterY);
//            matrix.postTranslate(mCenterX, mCenterY);
//            camera.restore();
            pathMeasure.getMatrix(pathMeasure.getLength() * getEaseOutQuartInterpolation(interpolatedTime), matrix, PathMeasure.POSITION_MATRIX_FLAG);
//            matrix.preTranslate(mCenterX, mCenterX);
//            matrix.postTranslate(50,50);
//            matrix.postTranslate(300 * interpolatedTime, y);
        }
    }

    class ViewShowAnimation extends Animation {

        private PathMeasure pathMeasure;
        private int animationTime;

        public ViewShowAnimation(int animationTime) {
            this.animationTime = animationTime;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            setDuration(animationTime);
            setFillAfter(true);
            setInterpolator(new LinearInterpolator());
            Path mPath = new Path();
            mPath.moveTo(-1000, 0);
            mPath.lineTo(0, 0);
            pathMeasure = new PathMeasure(mPath, false);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final Matrix matrix = t.getMatrix();
            pathMeasure.getMatrix(pathMeasure.getLength() * interpolatedTime, matrix, PathMeasure.POSITION_MATRIX_FLAG);
        }
    }

    class ViewDismissAnimation extends Animation {

        private PathMeasure pathMeasure;
        private int animationTime;

        public ViewDismissAnimation(int animationTime) {
            this.animationTime = animationTime;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            setDuration(animationTime);
            setFillAfter(true);
            setInterpolator(new LinearInterpolator());
            Path mPath = new Path();
            mPath.moveTo(0, 0);
            mPath.lineTo(0, -500);
            pathMeasure = new PathMeasure(mPath, false);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final Matrix matrix = t.getMatrix();
            pathMeasure.getMatrix(pathMeasure.getLength() * interpolatedTime, matrix, PathMeasure.POSITION_MATRIX_FLAG);
            interpolatedTime *= 2;
            float alpha = 1 - interpolatedTime;
            if (alpha < 0) {
                alpha = 0;
            }
            t.setAlpha(alpha);
        }

    }

    class TextChangeAnimation extends Animation {

        private PathMeasure pathMeasure;
        private int animationTime;
        private TextView textView;

        public TextChangeAnimation(int animationTime, TextView textView) {
            this.animationTime = animationTime;
            this.textView = textView;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            setDuration(animationTime);
            setFillAfter(true);
            setInterpolator(new LinearInterpolator());
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float scale = getTextScaleInterpolation(interpolatedTime);
            textView.setScaleX(scale);
            textView.setScaleY(scale);
        }

    }

    private static final float s = 1.70158f * 1.525f;

    public float getTextScaleInterpolation(float input) {
        if ((input *= 2) < 1.0f) {
            return 0.5f * (input * input * ((s + 1) * input - s));
        }

        input -= 2;
        return 0.5f * (input * input * ((s + 1) * input + s) + 2);
    }

    private static final float t = 1.70158f;

    public float getGiftPictureInterpolation(float input) {
        input -= 1;
        return input * input * ((t + 1) * input + t) + 1;
    }
    public float getEaseInQuartInterpolation(float input) {
        return input*input*input*input;
    }

    public float getEaseOutQuartInterpolation(float input) {
        input -= 1;
        return 1 - input*input*input*input;
    }
}
