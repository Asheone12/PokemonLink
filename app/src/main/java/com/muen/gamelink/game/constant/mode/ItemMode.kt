package com.muen.gamelink.game.constant.mode

/**
 * 道具种类
 */
enum class ItemMode(
    val value: Char
) {

    ITEM_FIGHT('1'),    //简单模式
    ITEM_BOMB('2'),     //普通模式
    ITEM_REFRESH('3')   //困难模式
}