package com.muen.gamelink.game.constant

import com.muen.gamelink.R

object LinkConstant {
    /**
     * 连连看图标大小
     */
    var ANIMAL_SIZE = 70

    /**
     * 连连看AnimalView的内间距
     */
    var ANIMAL_PADDING = 8

    /**
     * 连连看图标
     */
    var ANIMAL_RESOURCE = intArrayOf(
        R.drawable.animal_0, R.drawable.animal_1,
        R.drawable.animal_2, R.drawable.animal_3,
        R.drawable.animal_4, R.drawable.animal_5,
        R.drawable.animal_6, R.drawable.animal_7,
        R.drawable.animal_8, R.drawable.animal_9,
        R.drawable.animal_10, R.drawable.animal_11,
        R.drawable.animal_12, R.drawable.animal_13,
        R.drawable.animal_14, R.drawable.animal_15,
        R.drawable.animal_16
    )

    //木头图标
    var ANIMAL_WOOD: Int = R.drawable.animal_no

    /**
     * 连连看图标背景
     */
    var ANIMAL_BG: Int = R.drawable.animal_bg1
    var ANIMAL_SELECT_BG: Int = R.drawable.animal_select_bg1

    /**
     * 连连看默认的时间
     */
    var TIME = 90f

    /**
     * 基础分数
     */
    var BASE_SCORE = 500

    /**
     * 连连看测试模板
     * 0：空白
     * 1~∞：神奇宝贝图片
     */
    var board_test1 = arrayOf(
        intArrayOf(0, 0, 0, 0, 0, 0),
        intArrayOf(0, 1, 2, 3, 4, 0),
        intArrayOf(0, 2, 4, 1, 4, 0),
        intArrayOf(0, 3, 1, 3, 2, 0),
        intArrayOf(0, 1, 4, 2, 3, 0),
        intArrayOf(0, 0, 0, 0, 0, 0)
    )

    var board_test2 = arrayOf(
        intArrayOf(0, 0, 0, 0, 0, 0),
        intArrayOf(0, 1, 0, 3, 4, 0),
        intArrayOf(0, 2, 0, 1, 4, 0),
        intArrayOf(0, 3, 1, 3, 2, 0),
        intArrayOf(0, 1, 5, 2, 3, 0),
        intArrayOf(0, 0, 0, 0, 0, 0)
    )

    var board_test3 = arrayOf(
        intArrayOf(0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 3, 4, 0),
        intArrayOf(0, 0, 4, 1, 4, 0),
        intArrayOf(0, 3, 1, 3, 2, 0),
        intArrayOf(0, 0, 4, 2, 3, 0),
        intArrayOf(0, 0, 0, 0, 0, 0)
    )

    /**
     * 连连看正式模板-简单
     */
    var BOARD_EASY = arrayOf(
        LinkBoard.board_easy_1,
        LinkBoard.board_easy_2,
        LinkBoard.board_easy_3,
        LinkBoard.board_easy_4,
        LinkBoard.board_easy_5,
        LinkBoard.board_easy_6,
        LinkBoard.board_easy_7,
        LinkBoard.board_easy_8,
        LinkBoard.board_easy_9,
        LinkBoard.board_easy_10,
        LinkBoard.board_easy_11,
        LinkBoard.board_easy_12,
        LinkBoard.board_easy_13
    )

    /**
     * 连连看正式模板-普通
     */
    var BOARD_NORMAL = arrayOf(
        LinkBoard.board_normal_1,
        LinkBoard.board_normal_2,
        LinkBoard.board_normal_3,
        LinkBoard.board_normal_4,
        LinkBoard.board_normal_5
    )

    /**
     * 连连看正式模板-困难
     */
    var BOARD_HARD = arrayOf(
        LinkBoard.board_hard_1,
        LinkBoard.board_hard_2,
        LinkBoard.board_hard_3
    )
}