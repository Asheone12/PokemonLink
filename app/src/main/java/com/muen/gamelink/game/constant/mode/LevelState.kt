package com.muen.gamelink.game.constant.mode

/**
 * 关卡状态
 */
enum class LevelState(
    val value: Int
) {
    LEVEL_STATE_NO(0),        // 没有闯关
    LEVEL_STATE_ONE(1),       // 一星
    LEVEL_STATE_TWO(2),       // 二星
    LEVEL_STATE_THREE(3),     // 三星
    LEVEL_STATE_DOING(4);     // 闯关中

    companion object {
        //根据值得到状态
        fun getState(value: Int): LevelState {
            var levelState = LEVEL_STATE_NO
            when (value) {
                0 -> levelState = LEVEL_STATE_NO
                1 -> levelState = LEVEL_STATE_ONE
                2 -> levelState = LEVEL_STATE_TWO
                3 -> levelState = LEVEL_STATE_THREE
                4 -> levelState = LEVEL_STATE_DOING
            }
            return levelState
        }
    }
}
