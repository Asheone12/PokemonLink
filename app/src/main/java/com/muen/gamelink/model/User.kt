package com.muen.gamelink.model

import org.litepal.crud.LitePalSupport

/**
 * 用户
 */
class User : LitePalSupport() {
    //用户持有的金币数
    private var userMoney = 0

    //当前用户使用的游戏背景
    private var userBackground = 0

    //setter,getter方法
    fun getUserMoney(): Int {
        return userMoney
    }

    fun setUserMoney(userMoney: Int) {
        this.userMoney = userMoney
    }

    fun getUserBackground(): Int {
        return userBackground
    }

    fun setUserBackground(userBackground: Int) {
        this.userBackground = userBackground
    }
}