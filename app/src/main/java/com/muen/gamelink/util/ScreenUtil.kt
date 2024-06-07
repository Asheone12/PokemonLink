package com.muen.gamelink.util

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager

object ScreenUtil {
    /**
     * 获取屏幕的宽度
     * @param context
     * @return
     */
    fun getScreenWidth(context: Context): Int {
        //窗口管理者
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        //存储尺寸的
        val displayMetrics = DisplayMetrics()
        assert(windowManager != null)
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    /**
     * 获取屏幕的高度
     * @param context
     * @return
     */
    fun getScreenHeight(context: Context): Int {
        //窗口管理者
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        //存储尺寸的
        val displayMetrics = DisplayMetrics()
        assert(windowManager != null)
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    /**
     * 获取StatusBar状态栏的高度
     * @param context
     * @return
     */
    fun getStateBarHeight(context: Context): Int {
        var statusHeight = -1
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val `object` = clazz.newInstance()
            val height = clazz.getField("status_bar_height")[`object`].toString().toInt()
            statusHeight = context.resources.getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return statusHeight
    }

    /**
     * 判断设置是否有NavigationBar导航栏
     * @param context
     * @return
     */
    private fun checkDeviceHasNavigationBar(context: Context): Boolean {
        var hasNavigationBar = false
        val rs = context.resources
        val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id)
        }
        try {
            val systemPropertiesClass = Class.forName("android.os.SystemProperties")
            val m = systemPropertiesClass.getMethod("get", String::class.java)
            val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
            if ("1" == navBarOverride) {
                hasNavigationBar = false
                Log.d("muen", "手机没有导航栏")
            } else if ("0" == navBarOverride) {
                hasNavigationBar = true
                Log.d("muen", "手机有导航栏")
            }
        } catch (e: Exception) {
        }
        return hasNavigationBar
    }

    /**
     * 返回NavigationBar导航栏的高度
     * @param context
     * @return
     */
    fun getNavigationBarHeight(context: Context): Int {
        var height = 0

        //如果存在
        if (checkDeviceHasNavigationBar(context)) {
            val resources = context.resources
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            height = resources.getDimensionPixelSize(resourceId)
            Log.v("muen", "Navi height:$height")
        }
        return height
    }
}