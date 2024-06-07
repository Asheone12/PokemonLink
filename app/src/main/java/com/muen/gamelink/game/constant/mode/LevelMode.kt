package com.muen.gamelink.game.constant.mode

/**
 * 关卡模式
 */
enum class LevelMode(val value: Char) {
    //简单模式
    LEVEL_MODE_EASY('1'),  //普通模式
    LEVEL_MODE_NORMAL('2'),  //困难模式
    LEVEL_MODE_HARD('3')
}