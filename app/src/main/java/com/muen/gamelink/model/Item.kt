package com.muen.gamelink.model

import org.litepal.crud.LitePalSupport

/**
 * 道具
 */
class Item : LitePalSupport() {
    //道具的种类 1：拳头 2：炸弹 3：刷新
    private var itemType = 0.toChar()

    //道具的的数量
    private var itemNumber = 0

    //道具的价格
    private var itemPrice = 0

    //setter，getter方法
    fun getItemType(): Char {
        return itemType
    }

    fun setItemType(itemType: Char) {
        this.itemType = itemType
    }

    fun getItemNumber(): Int {
        return itemNumber
    }

    fun setItemNumber(itemNumber: Int) {
        this.itemNumber = itemNumber
    }

    fun getItemPrice(): Int {
        return itemPrice
    }

    fun setItemPrice(itemPrice: Int) {
        this.itemPrice = itemPrice
    }
}