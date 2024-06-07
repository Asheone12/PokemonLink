package com.muen.gamelink.util

import android.content.Context

object PxUtil {
    /**
     * 得到设备的密度
     */
    fun getScreenDensity(context: Context): Float {
        return context.resources.displayMetrics.density
    }

    /**
     * 将传递的 整型dp 值转化为 px
     * @param dp
     * @param context
     * @return
     */
    fun dpToPx(dp: Int, context: Context): Int {
        return (dp * getScreenDensity(context)).toInt()
    }

    /**
     * 将传递的 浮点型dp 值转化为 px
     * @param dp
     * @param context
     * @return
     */
    fun dpToPx(dp: Float, context: Context): Int {
        return (dp * getScreenDensity(context)).toInt()
    }

    /**
     * 将传递的 整型px 值转化为 dp
     * @param px
     * @param context
     * @return
     */
    fun pxToDp(px: Int, context: Context): Float {
        return px / getScreenDensity(context)
    }

    /**
     * 将传递的 浮点型px 值转化为 dp
     * @param px
     * @param context
     * @return
     */
    fun pxToDp(px: Float, context: Context): Float {
        return px / getScreenDensity(context)
    }

    /**
     * 将传递的 sp 值转化为 px
     * @param sp
     * @param context
     * @return
     */
    fun spToPx(sp: Int, context: Context): Int {
        return (sp * getScreenDensity(context)).toInt()
    }

    /**
     * 将传递的 px 值 转化为 sp
     * @param px
     * @param context
     * @return
     */
    fun pxToSp(px: Int, context: Context): Int {
        return (px / getScreenDensity(context)).toInt()
    }
}