package com.muen.gamelink.util

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.util.Log

object StateUtil {
    /**
     * 判断是否在前台
     * @param context
     * @return
     */
    fun isBackground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses
        for (appProcess in appProcesses) {
            if (appProcess.processName == context.packageName) {
                return if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("muen", appProcess.processName)
                    true
                } else {
                    Log.i("muen", appProcess.processName)
                    false
                }
            }
        }
        return false
    }
}