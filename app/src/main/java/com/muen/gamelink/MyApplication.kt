package com.muen.gamelink

import android.app.Application
import org.litepal.LitePal

class MyApplication:Application(){
    override fun onCreate() {
        super.onCreate()
        LitePal.initialize(this)
    }
}