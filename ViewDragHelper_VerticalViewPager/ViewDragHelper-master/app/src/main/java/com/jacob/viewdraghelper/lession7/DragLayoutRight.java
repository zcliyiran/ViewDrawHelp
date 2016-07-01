package com.jacob.viewdraghelper.lession7;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzhili on 16/3/3.
 */
public class DragLayoutRight extends FrameLayout {

    private Context context;
    private int mHeight;
    private int mWidth;
    private ViewDragHelper viewDragHelper;

    private View dragBgVIew;
    private View dragFroundVIew;
    private int mSwipeRange;
    private int mVerticalSwipeRange;
    private boolean isCanVerticalDrag;
    private int STATE_VERTICAL_DRAG = 0;
    private int STATE_HORIZONTAL_DRAG = 1;
    private int STATE_NO = -2;
    private int STATE_OPEN = 3;
    private int STATE_CLOSE = 2;
    private int STATE_DRAGING = 4;
    private int orientationState = STATE_NO;
    private int openState = 3;
    private VerticalHorizontalScroll verticalScroll;
    private int topMargin;
    private int leftMargin;
    private boolean isOnce = true;
    private List<RecyclerView> allRecyclerView;
    private GestureDetector gestureDetector;
    private boolean isHoriaontalSmoothScroll;

    private int initHegiht;

    private ViewDragHelper.Callback callback;
    private Boolean isHasCorrectSize = false;
    private int bottom;
    private int judgeTime, judgeVertical, judgeHorizontal;
    private int screenH, actionBarHeight;
    private Runnable setCorrectSizeRunnable;


    public DragLayoutRight(Context context) {
        super(context);

        initData(context);
    }

    public DragLayoutRight(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    public DragLayoutRight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
    }

    boolean isToVerticalDrag;

    private void initData(final Context context) {
        this.context = context;
        setWillNotDraw(false);
        allRecyclerView = new ArrayList<>();
        callback = new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //mEdgeTrackerView禁止直接移动
                return child == dragFroundVIew || child == dragBgVIew;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
//                Log.d("DragLayoutRight", "scrolldistance:" + "left:" + left);
//                Log.d("DragLayoutRight", "scrolldistance:" + "dx:" + dx);
                leftMargin = dragFroundVIew.getLeft();
                topMargin = dragBgVIew.getTop();
//                Log.d("DragLayoutRight1", "dragFroundVIew.getTop():" + dragFroundVIew.getTop());
//                Log.d("DragLayoutRight1", "dragFroundVIew.getLeft():" + dragFroundVIew.getLeft());
//                Log.d("DragLayoutRight1", "dragBgVIew.getLeft():" + dragBgVIew.getLeft());
//                Log.d("DragLayoutRight1", "dragBgVIew.getTop():" + dragBgVIew.getTop());
//                Log.d("DragLayoutRight1", "orientationState.getTop():" + orientationState);

                if (orientationState == STATE_VERTICAL_DRAG) {
                    return 0;
                }
                if (child == dragBgVIew) {
//                    if (left < -mSwipeRange) {
//                        left = -mSwipeRange;
//                    } else if (left > 0) {
                    setLayoutMarginLeft(left, dx);
                    left = 0;
//                    }
                } else if (child == dragFroundVIew) {
                    if (left < mWidth - mSwipeRange) {
                        left = mWidth - mSwipeRange;
                    } else if (left > mWidth) {
                        left = mWidth;
                    }
                }

                if (judgeTime == 1) {
                    return 0;
                }
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
//                Log.d("DragLayoutRight", "scrolldistance:" + "top:" + top);
//                Log.d("DragLayoutRight", "scrolldistance:" + "dy:" + dy);

                leftMargin = dragFroundVIew.getLeft();
                topMargin = dragBgVIew.getTop();

                if (orientationState == STATE_HORIZONTAL_DRAG || !isCanVerticalDrag) {
                    return 0;
                }
                openState = STATE_DRAGING;
                if (top > mVerticalSwipeRange) {
                    top = mVerticalSwipeRange;
                } else if (top < -mVerticalSwipeRange) {
                    top = -mVerticalSwipeRange;
                }
//                Log.d("DragLayoutRight", "dragBgVIew.getTop():" + dragBgVIew.getTop());
//                Log.d("DragLayoutRight", "dragBgVIew.getTop():" + top);
                return top;

            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);

                if (changedView == dragBgVIew) {
                    dragFroundVIew.offsetTopAndBottom(dy);
                } else if (changedView == dragFroundVIew) {
                    dragBgVIew.offsetTopAndBottom(dy);
                }
//
                invalidate();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                if (isCanVerticalDrag) {
                    return mVerticalSwipeRange;
                } else {
                    return 0;
                }

            }

            @Override
            public int getViewHorizontalDragRange(View child) {

                return mSwipeRange;
            }

            //手指释放的时候回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //平滑移动部分
                isToVerticalDrag = false;

                if (releasedChild.getTop() > mHeight / 3) {
                    if (verticalScroll != null) {
                        verticalScroll.goPrev();
//                        Log.d("DragLayoutRight", "goPreRelease");
                    }
                } else if (releasedChild.getBottom() < mHeight / 3 * 2) {
                    if (verticalScroll != null) {
                        verticalScroll.goNext();
//                        Log.d("DragLayoutRight", "goNextRelease");
                    }
                }

                int dragViewCenterXPx = mWidth - mSwipeRange + mSwipeRange / 2;
//                Log.d("DragLayoutRight", "dragViewCenterXPx:" + dragViewCenterXPx + "getLeft:" + dragFroundVIew.getLeft());
                if (dragFroundVIew.getLeft() > dragViewCenterXPx && !isHoriaontalSmoothScroll) {
                    startAnimationToLocationX(leftMargin, mWidth);
//                    Log.d("DragLayoutRight", "goright");
//                    close(true);
//                    leftMargin = mWidth;
                } else if (!isHoriaontalSmoothScroll) {
                    startAnimationToLocationX(leftMargin, 0);
//                    Log.d("DragLayoutRight", "goleft");
//                    open(true);
//                    leftMargin = 0;
                }

                startAnimationToLocationY(topMargin, 0);
//                orientationState = STATE_NO;
//                topMargin = 0;
            }

            @Override
            public void onViewDragStateChanged(int state) {
                switch (state) {
                    case ViewDragHelper.STATE_DRAGGING:  // 正在被拖动
                        break;
                    case ViewDragHelper.STATE_IDLE:  // view没有被拖拽或者 正在进行fling/snap
                        break;
                    case ViewDragHelper.STATE_SETTLING: // fling完毕后被放置到一个位置
                        break;
                }
                super.onViewDragStateChanged(state);
            }

            //在边界拖动时回调
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {

//                Log.i("DragLayoutRight", "edgeFlags:" + edgeFlags);
//                Log.i("DragLayoutRight", "pointerId:" + pointerId);
//                viewDragHelper.captureChildView(mDragView, pointerId);
            }
        };
        viewDragHelper = ViewDragHelper.create(this, 1.0f, callback);
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_RIGHT);

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                Log.d("GestureDetector", "velocityX:" + velocityX);
                if (Math.abs(velocityX) >= Math.abs(velocityY)) {
                    if (velocityX > 8000 && !isHoriaontalSmoothScroll && orientationState == STATE_HORIZONTAL_DRAG) {
                        startAnimationToLocationX(leftMargin, mWidth);
//                        Log.d("DragLayoutRight", "gorightFlying");
//                    close(true);
//                    leftMargin = mWidth;
                    } else if (velocityX < -8000 && !isHoriaontalSmoothScroll && orientationState == STATE_HORIZONTAL_DRAG) {
                        startAnimationToLocationX(leftMargin, 0);
//                        Log.d("DragLayoutRight", "goleftFlying");
                    }
                } else {
                    if (velocityY > 10000 && verticalScroll != null && topMargin <= mHeight / 3 && isCanVerticalDrag) {
                        verticalScroll.goPrev();
//                        Log.d("DragLayoutRight", "flyinggoPre");
                    } else if (velocityY < -10000 && verticalScroll != null && bottom >= mHeight / 3 * 2 && isCanVerticalDrag) {
                        verticalScroll.goNext();
//                        Log.d("DragLayoutRight", "flyinggoNext:Bottom" + bottom);
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

//                Log.d("DragLayoutRight", "onScroll" + "disx:" + distanceX + "disY" + distanceY + (orientationState == STATE_NO));
                float x = Math.abs(distanceX);
                float y = Math.abs(distanceY);
//                if (x > 200 || y > 200) {
//                    return true;
//                }
                judgeTime++;
                if (judgeTime == 1) {
                    return true;
                }
                if (x >= y && orientationState == STATE_NO) {
                    orientationState = STATE_HORIZONTAL_DRAG;
                } else if (orientationState == STATE_NO) {
                    orientationState = STATE_VERTICAL_DRAG;
                }

                return true;
            }

        });

    }


    private void open(boolean b) {

        if (b) {
            viewDragHelper.smoothSlideViewTo(dragFroundVIew, mWidth - mSwipeRange, 0);
            invalidate();
        } else {
            layout(true);
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                openState = STATE_OPEN;
            }
        }, 20);
    }

    private void close(boolean b) {

        if (b) {
            viewDragHelper.smoothSlideViewTo(dragFroundVIew, mWidth, 0);
            invalidate();
        } else {
            layout(false);
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                openState = STATE_CLOSE;
            }
        }, 20);
    }

    private void layout(boolean isToOpen) {
        if (isToOpen) {
            dragFroundVIew.layout(mWidth - mSwipeRange, 0, mWidth, mHeight);
        } else {
            dragFroundVIew.layout(mWidth, 0, mWidth + mSwipeRange, mHeight);
        }
    }

    private synchronized void startAnimationToLocationX(final int fromX, int toX) {

        if (toX == mWidth) {
//            Log.d("DragLayoutRight", "startCLose");
            isHoriaontalSmoothScroll = true;
            if (verticalScroll != null) {
                verticalScroll.goRight();
            }
        } else if (toX == 0) {
//            Log.d("DragLayoutRight", "startOpen");
            isHoriaontalSmoothScroll = true;
            if (verticalScroll != null) {
                verticalScroll.goLeft();
            }
        }

        final int dx = toX - fromX;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f);
        valueAnimator.setDuration(200);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isHoriaontalSmoothScroll = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float curTimePercent = animation.getCurrentPlayTime() / (float) animation.getDuration();
                float curYPercent = ((Float) animation.getAnimatedValue()).floatValue();
                leftMargin = (int) (fromX + dx * curYPercent);
                int right = (int) (fromX + dx * curYPercent + mSwipeRange);
//                Log.d("DragLayoutRight", "curYPercent:" + curYPercent + "right:" + right + "swipRange:" + mSwipeRange);
                dragFroundVIew.layout(leftMargin, 0, right, mHeight);
            }
        });
        valueAnimator.start();

    }

    private void startAnimationToLocationY(final int fromY, int toY) {

        final int dy = toY - fromY;

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f);
        valueAnimator.setDuration(200);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float curTimePercent = animation.getCurrentPlayTime() / (float) animation.getDuration();
                float curYPercent = ((Float) animation.getAnimatedValue()).floatValue();
                topMargin = (int) (fromY + dy * curYPercent);
                dragFroundVIew.layout(leftMargin, topMargin, leftMargin + mWidth, topMargin + mHeight);
                dragBgVIew.layout(0, topMargin, mWidth, topMargin + mHeight);
//                if (curYPercent == 1) {
//                    if (leftMargin < 5) {
//                        openState = STATE_OPEN;
//                    } else {
//                        openState = STATE_CLOSE;
//                    }
//                }
            }
        });
        valueAnimator.start();

    }


    private void setLayoutMarginLeftAndWidth() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) dragFroundVIew.getLayoutParams();
        layoutParams.width = mWidth;
        dragFroundVIew.setLayoutParams(layoutParams);
    }

    private void setLayoutMarginLeft(int left, int dx) {
        int toLeft = dragFroundVIew.getLeft() + left;
//        Log.d("DragLayoutRight", "dx:" + dx + "left:" + left);
        if (toLeft > mWidth) {
            dx = mWidth - dragFroundVIew.getLeft();
        } else if (toLeft < mWidth - mSwipeRange) {
            dx = mWidth - mSwipeRange - dragFroundVIew.getLeft();
        }

        dragFroundVIew.offsetLeftAndRight(dx);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        open(false);
//        Log.d("DragLayoutRight", "player_mHeight:" + mHeight + changed);
//        Log.d("DragLayoutRight", "player_mHeight:" + top + ":" + bottom);
        layoutNowLocation();

        if (isOnce) {
            getAlLRecyclerView(allRecyclerView, this);
            initHegiht = bottom - top;
            isOnce = false;
        }
//        Log.d("DragLayoutRight", "l:" + left);
//        Log.d("DragLayoutRight", "t:" + top);
//        Log.d("DragLayoutRight", "r:" + right);
//        Log.d("DragLayoutRight", "b:" + bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        Log.d("DragLayoutRight", "player_onSizeChanged");
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();

        mSwipeRange = dragFroundVIew.getMeasuredWidth();
        mVerticalSwipeRange = mHeight;

//        Log.d("DragLayoutRight", "w:" + w);
//        Log.d("DragLayoutRight", "h:" + h);
//        Log.d("DragLayoutRight", "oldw:" + oldw);
//        Log.d("DragLayoutRight", "oldh:" + oldh);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        if (getTouchRecyclerView(allRecyclerView, event) != null) {

//            Log.d("DragLayoutRight", "intercept1:" + "false");
            return false;
        }
        boolean shouldInterceptTouchEvent = false;
        try {
            shouldInterceptTouchEvent = viewDragHelper.shouldInterceptTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.d("DragLayoutRight", "intercept2:" + shouldInterceptTouchEvent);
        return shouldInterceptTouchEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        viewDragHelper.processTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
//                Log.d("DragLayoutRight", "orientationState = STATE_NO");
                orientationState = STATE_NO;
                judgeTime = 0;
                judgeHorizontal = 0;
                judgeVertical = 0;
                break;
        }
        return true;
    }

    private void layoutNowLocation() {
//        Log.d("DragLayoutRight", "ijkPlayer" + (ijkVideoView == null));
//        dragBgVIew.layout(0, topMargin + 1, mWidth, topMargin + mHeight + 1);

        //若不减一 刚进直播ijkView会被压扁并且反应不过来 可以通过状态栏的隐藏状态刷新,若减一则始终被压扁(此处采用第二种情况)
//        Log.d("DragLayoutRight", "screenH:" + screenH);
//        if (ijkVideoView != null && ijkVideoView.getMeasuredHeight() < screenH - ScreenUtils.dp2px(130)) {
//            isAllowFlatten = true;
//            postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    isAllowFlatten = false;
//                }
//            }, 500);
//        }
//            Log.d("DragLayoutRight", "ijkView_small");
//            dragBgVIew.layout(0, topMargin - 1, mWidth, topMargin + mHeight);
////            ijkVideoView.layout(0, 0, mWidth, ScreenUtils.getScreenH());
////            ijkVideoView.setMinimumHeight(screenH);
////            float scale = (float) screenH / ijkVideoView.getMeasuredHeight();
////            ijkVideoView.startAnimation(new ScaleY(ijkVideoView, scale));
////            Log.d("DragLayoutRight", "scale:" + scale);
////            Log.d("DragLayoutRight", "ijkVideoView:" + ijkVideoView.getHeight() + ":" + ijkVideoView.getMeasuredHeight() + "-" + dragBgVIew.getHeight() + ":" + dragBgVIew.getMeasuredHeight());
//        } else {
//            Log.d("DragLayoutRight", "ijkView_big");
//        Log.d("DragLayoutRight", "ijkView_disallow:" + ijkVideoView.getMeasuredHeight());
//        if (ijkVideoView != null && ijkVideoView.getMeasuredHeight() >= screenH - actionBarHeight) {
//            isHasCorrectSize = true;
//        } else if (ijkVideoView != null && !ScreenUtils.isInputShow(activity, editText)) {
//            isHasCorrectSize = false;
//        }
//        Log.d("DragLayoutRight", "ijkView_disallow:" + isHasCorrectSize);
//        if (isHasCorrectSize) {
        dragBgVIew.layout(0, topMargin, mWidth, topMargin + mHeight);
//        } else {
//            dragBgVIew.layout(0, -1, mWidth, topMargin + mHeight);
////            if (ijkVideoView != null &&) {
////                isHasCorrectSize = true;
////            }
////            Log.d("DragLayoutRight", "ijkView_allow");
//        }
//        }
        dragFroundVIew.layout(leftMargin, topMargin, leftMargin + mWidth, topMargin + mHeight);
    }

    private synchronized boolean getIsHasCorrectSize(Boolean isReturnTure) {
        if (isReturnTure) {
            isHasCorrectSize = true;
        }
        return isHasCorrectSize;
    }

    boolean isResetSize;

    public void resetSize() {

        post(new Runnable() {
            @Override
            public void run() {
//                Log.d("DragLayoutRight", "resetSize,time");
                isResetSize = true;
                if (mHeight >= screenH - actionBarHeight && !isHasCorrectSize) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            dragBgVIew.layout(0, topMargin - 1, mWidth, topMargin + mHeight);
                        }
                    });
//                        isHasCorrectSize = true;
//                        return;
                }
                if (!isHasCorrectSize) {
                    postDelayed(this, 50);
                }
                isResetSize = false;
            }
        });

        setCorrectSizeRunnable = new Runnable() {
            @Override
            public void run() {
//                Log.d("DragLayoutRight", "isHasCorrectSize:" + getIsHasCorrectSize(false));
                if (mHeight >= screenH - actionBarHeight) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mHeight >= screenH - actionBarHeight) {
                                getIsHasCorrectSize(true);
                            } else {
                                postDelayed(setCorrectSizeRunnable, 50);
                            }

                        }
                    }, 100);
                } else {
                    postDelayed(this, 50);
                }
            }
        };
        postDelayed(setCorrectSizeRunnable, 8 * 1000);


//        while (!isHasCorrectSize) {
//            dragBgVIew.layout(0, -1, mWidth, initHegiht);
//            if (ijkVideoView != null && ijkVideoView.getMeasuredHeight() >= screenH - actionBarHeight) {
//                isHasCorrectSize = true;
//            }
//        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(null);
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        switch (getChildCount()) {
            case 0:
                dragBgVIew = new RelativeLayout(context);
                addView(dragBgVIew);
                dragFroundVIew = new RelativeLayout(context);
                addView(dragFroundVIew);
                break;
            case 1:
                dragBgVIew = getChildAt(0);
                dragFroundVIew = new RelativeLayout(context);
                addView(dragFroundVIew);
                break;
            default:
                dragBgVIew = getChildAt(0);
                dragFroundVIew = getChildAt(1);
                break;
        }

        screenH = ScreenUtils.getScreenH();
        actionBarHeight = ScreenUtils.dp2px(150);
//
//        Log.d("DragLayoutRight", "getChildCount():" + getChildCount());
//        ViewGroup viewGroup = ((ViewGroup) dragBgVIew);
//        for (int i = 0; i < viewGroup.getChildCount(); i++) {
//            if (viewGroup.getChildAt(i) instanceof IjkVideoView) {
//                ijkVideoView = (IjkVideoView) viewGroup.getChildAt(i);
//                return;
//            }
//        }
    }

    private void getAlLRecyclerView(List<RecyclerView> mRecyclerViews, ViewGroup parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            if (child instanceof RecyclerView) {
                mRecyclerViews.add((RecyclerView) child);
            } else if (child instanceof ViewGroup) {
                getAlLRecyclerView(mRecyclerViews, (ViewGroup) child);
            }
        }
    }

    private RecyclerView getTouchRecyclerView(List<RecyclerView> mRecyclerViews, MotionEvent ev) {
        if (mRecyclerViews == null || mRecyclerViews.size() == 0) {
            return null;
        }
        Rect mRect = new Rect();
        for (RecyclerView v : mRecyclerViews) {
            v.getHitRect(mRect);
            if (mRect.contains((int) ev.getX(), (int) ev.getY())) {
                return v;
            }
        }
        return null;
    }

    public interface VerticalHorizontalScroll {

        void goNext();

        void goPrev();

        void goRight();

        void goLeft();
    }

    public VerticalHorizontalScroll getVerticalScroll() {
        return verticalScroll;
    }

    public void setVerticalScroll(VerticalHorizontalScroll verticalScroll) {
        this.verticalScroll = verticalScroll;
    }

    public boolean isCanVerticalDrag() {
        return isCanVerticalDrag;
    }

    public void setIsCanVerticalDrag(boolean isCanVerticalDrag) {
        this.isCanVerticalDrag = isCanVerticalDrag;
    }

    //    public class ScaleY extends Animation {
//
//        private View view;
//        private float scale;
//
//        public ScaleY(View view, float scale) {
//            this.view = view;
//            this.scale = scale;
//        }
//
//        @Override
//        public void initialize(int width, int height, int parentWidth, int parentHeight) {
//            super.initialize(width, height, parentWidth, parentHeight);
//            setFillAfter(true);
//            setDuration(0);
//        }
//
//        @Override
//        protected void applyTransformation(float interpolatedTime, Transformation t) {
//            super.applyTransformation(interpolatedTime, t);
//            Matrix matrix = t.getMatrix();
//            matrix.postScale(1, scale, 0, 0);
//        }
//    }

//    public boolean isHasSurficeView() {
//        return hasSurficeView;
//    }
//
//    public void setHasSurficeView(boolean hasSurficeView) {
//        this.hasSurficeView = hasSurficeView;
//    }
}
