package com.jacob.viewdraghelper.lession7;


import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 需要初始化
 * ScreenUtils
 * Created by hanj on 14-9-25.
 */
public class ScreenUtils {
    private static int screenW;
    private static int screenH;
    private static float screenDensity;
    private static Context context;

    public static void initScreen(Context context1) {
        context = context1;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        screenW = displayMetrics.widthPixels;
        screenH = displayMetrics.heightPixels;
        screenDensity = displayMetrics.density;
    }

    public static int getScreenW() {
        return screenW;
    }

    public static int getScreenH() {
        return screenH;
    }

    public static float getScreenDensity() {
        return screenDensity;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        return (int) (dpValue * getScreenDensity() + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(float pxValue) {
        return (int) (pxValue / getScreenDensity() + 0.5f);
    }

    /**
     * 获取手机状态栏高度 px
     *
     * @return
     */
    public static int getStatusBarHeight() {
        try {
            int result = 0;
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
            return result;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }

    }

    public static void full(boolean enable, Activity activity) {
        if (enable) {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            activity.getWindow().setAttributes(lp);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams attr = activity.getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().setAttributes(attr);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    public static float getScreenRate() {

        float H = screenH;
        float W = screenW;
        return (H / W);
    }

    public static float getCameraPreviewRate() {

        float H = screenH - getStatusBarHeight() - dp2px(44);
        float W = screenW;
        return (H / W);
    }

    public static boolean isInputShow(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm.hideSoftInputFromWindow(editText.getWindowToken(), 0)) {
            return true;
        } else {
            return false;
        }
    }

    public static void showInput(EditText editText) {
        InputMethodManager m = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideSystemInput(Activity activity) {
        try {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        } catch (Exception e) {

        }
    }

    public static void customEdgeEffect(Context context, int brandColor) {
        //glow
        try {
            int glowDrawableId = context.getResources().getIdentifier("overscroll_glow", "drawable", "android");
            Drawable androidGlow = context.getResources().getDrawable(glowDrawableId);
            androidGlow.setColorFilter(brandColor, PorterDuff.Mode.MULTIPLY);
            //edge
            int edgeDrawableId = context.getResources().getIdentifier("overscroll_edge", "drawable", "android");
            Drawable androidEdge = context.getResources().getDrawable(edgeDrawableId);
            androidEdge.setColorFilter(brandColor, PorterDuff.Mode.MULTIPLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
