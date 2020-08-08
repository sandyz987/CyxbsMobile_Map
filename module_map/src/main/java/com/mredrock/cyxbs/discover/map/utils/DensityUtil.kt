package com.mredrock.cyxbs.discover.map.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

object DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun getDisplayWidth(activity: Activity): Int {
        val metric = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metric)
        return metric.widthPixels // 屏幕宽度（像素）
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun getDisplayHeight(activity: Activity): Int {
        val metric = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metric)
        return metric.heightPixels - getStatusBarHeight(activity) // 屏幕宽度（像素）
    }

    fun getStatusBarHeight(activity: Activity): Int {
        var statusBarHeight = -1
        val resourceId =
                activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = activity.resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }


    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context 上下文
     * @param pxValue px value
     * @return sp
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale: Float = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context 上下文
     * @param spValue sp value
     * @return px
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale: Float = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }
}