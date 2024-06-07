package com.muen.gamelink.game.util

import android.content.Context
import android.util.Log
import com.muen.gamelink.game.constant.LinkConstant
import com.muen.gamelink.game.constant.mode.LevelMode
import com.muen.gamelink.game.entity.AnimalPoint
import com.muen.gamelink.game.manager.LinkManager
import com.muen.gamelink.game.view.AnimalView
import com.muen.gamelink.util.PxUtil
import java.util.Random

object LinkUtil {
    private val TAG = "LinkUtil"

    /**
     * 设置所有的宝可梦的响应状态
     * @param status
     */
    fun setBoardsStatus(status: Boolean) {
        //获取所有宝可梦视图
        val manager: LinkManager = LinkManager.instance
        val animals: List<AnimalView> = manager.getAnimals()
        for (i in animals.indices) {
            animals[i].setEnabled(status)
        }
    }

    /**
     * 返回一个布局中可以消除的两个AnimalView
     * @return
     */
    fun getDoubleRemove(): Array<AnimalPoint?> {
        //获得模板
        val board: Array<IntArray> = LinkManager.instance.getBoard()

        //存储两个相对坐标
        var point1: AnimalPoint? = null
        var point2: AnimalPoint? = null

        //找到两个点
        while (point1 == null && point2 == null) {
            //寻找第一个点
            for (i in 1 until board.size - 1) {
                for (j in 1 until board[0].size - 1) {
                    //产生第一个点
                    point1 = AnimalPoint(i, j)
                    for (p in 1 until board.size - 1) {
                        for (q in 1 until board[0].size - 1) {
                            //产生第二个点
                            point2 = AnimalPoint(p, q)
                            Log.d(TAG, "第一个点：" + point1.x + " " + point1.y)
                            Log.d(TAG, "第二个点：" + point2.x + " " + point2.y)

                            //如果两个点不是同一个点
                            if (point1.x !== point2.x || point1.y !== point2.y) {
                                //并且该两点图案相同 且不为0
                                if (board[point1.x][point1.y] == board[point2.x][point2.y]
                                    && board[point1.x][point1.y] != 0
                                ) {
                                    //并且可以被消除
                                    if (AnimalSearchUtil.canMatchTwoAnimalWithTwoBreak(
                                            board,
                                            point1,
                                            point2,
                                            null
                                        )
                                    ) {
                                        return arrayOf(point1, point2)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return arrayOf(point1, point2)
    }

    /**
     * 返回一个布局存在的AnimalView
     * @return
     */
    fun getExistAnimal(): Int {
        //获取布局
        val board: Array<IntArray> = LinkManager.instance.getBoard()

        //产生随机数
        var random = 0
        while (!getBoardState()) {
            //产生一个随机数
            random = Random().nextInt(getMaxData(board)) + 1
            Log.d(TAG, "测试消除$random")

            //判断该布局中是否有
            for (i in board.indices) {
                for (j in board[0].indices) {
                    if (board[i][j] == random) {
                        return random
                    }
                }
            }
        }
        return random
    }

    /**
     * 判断是否全部消除
     * @return
     */
    fun getBoardState(): Boolean {
        //获取布局
        val board: Array<IntArray> = LinkManager.instance.getBoard() ?: return false

        //bug处理

        //判断状态
        for (i in board.indices) {
            for (j in board[0].indices) {
                if (board[i][j] > 0) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * 根据游戏时间获取相关星级评价
     * @param time
     * @return
     */
    fun getStarByTime(time: Int): Char {
        return if (time <= 40) {
            '1'
        } else if (time <= 60) {
            '2'
        } else {
            '3'
        }
    }

    /**
     * 根据游戏时间获取相关分数评价
     * @param time
     * @return
     */
    fun getScoreByTime(time: Float): Float {
        return (LinkConstant.TIME - time) * LinkConstant.BASE_SCORE / LinkConstant.TIME
    }

    /**
     * 获取连击数
     */
    fun getSerialClick(): Int {
        //获取布局
        val board: Array<IntArray> = LinkManager.instance.getBoard()
        return (board.size - 2) * (board[0].size - 2) / 2
    }

    /**
     * 索引坐标->真实坐标
     * @param point
     * @param context
     * @return
     */
    fun getRealAnimalPoint(point: AnimalPoint, context: Context): AnimalPoint {
        val manager: LinkManager = LinkManager.instance
        return AnimalPoint(
            manager.getPaddingHor() + PxUtil.dpToPx(
                manager.getAnimalSize(),
                context
            ) / 2 + point.y * PxUtil.dpToPx(manager.getAnimalSize(), context),
            manager.getPaddingVer() + PxUtil.dpToPx(
                manager.getAnimalSize(),
                context
            ) / 2 + point.x * PxUtil.dpToPx(manager.getAnimalSize(), context)
        )
    }

    /**
     * 加载给定关卡号和难度的布局
     * @param level_id
     * @param level_mode
     * @return
     */
    fun loadLevelWithIdAndMode(levelId: Int, levelMode: Char): Array<IntArray> {

        //1.先判断是什么类型的关卡
        val BOARD: Array<Array<IntArray>> =
            if (levelMode == LevelMode.LEVEL_MODE_EASY.value) {
                LinkConstant.BOARD_EASY
            } else if (levelMode == LevelMode.LEVEL_MODE_NORMAL.value) {
                LinkConstant.BOARD_NORMAL
            } else {
                LinkConstant.BOARD_HARD
            }

        //2.获取需要加载的资源数量
        val resourceID = Random().nextInt(BOARD.size)

        //3.获取随机产生的模板
        val board = BOARD[resourceID]
        Log.d(TAG, "加载的board$resourceID")

        //4.拷贝模板
        val row = board.size
        val col = board[0].size
        val clone = Array(row) { IntArray(col) }
        for (i in 0 until row) {
            System.arraycopy(board[i], 0, clone[i], 0, col)
        }
        return clone
    }

    /**
     * 加载所需的数量的图片
     * @param board
     * @return
     */
    fun loadPictureResourceWithBoard(board: Array<IntArray>): List<Int> {
        //初始化存储集合
        val list: MutableList<Int> = ArrayList()

        //求出二维数组中的最大值
        val max = getMaxData(board)

        //随机加载图片
        val random = Random()
        while (list.size != max) {
            //产生指定范围内的随机数
            val randomInt = random.nextInt(LinkConstant.ANIMAL_RESOURCE.size)

            //重复的随机数不再加入
            var flag = 0
            for (integer in list) {
                if (integer == randomInt) flag = 1
            }

            //满足条件的加入
            if (flag == 0) list.add(randomInt)
        }
        return list
    }

    /**
     * 求解二维矩阵的最大值
     * @param matrix
     * @return
     */
    fun getMaxData(matrix: Array<IntArray>): Int {
        val row = matrix.size
        val col = matrix[0].size
        val data = IntArray(row * col)
        var index = 0
        for (i in 0 until row) {
            for (j in 0 until col) {
                data[index++] = matrix[i][j]
            }
        }

        //除开特殊情况
        return if (data.size == 0) {
            0
        } else getMaxDataBySort(data)
    }

    /**
     * 返回数组中第k小的数值
     * @param data
     * @return
     */
    private fun getMaxDataBySort(data: IntArray): Int {
        //排序
        mergeSort(data, 0, data.size - 1)
        return data[data.size - 1]
    }

    /**
     * 归并排序 递归
     * @param nums
     * @param start
     * @param end
     */
    private fun mergeSort(nums: IntArray, start: Int, end: Int) {
        //只要划分的区间长度仍然不为1
        if (start != end) {
            val middle = (start + end) / 2

            //分
            mergeSort(nums, start, middle)
            mergeSort(nums, middle + 1, end)

            //治
            merge(nums, start, end, middle)
        }
    }

    /**
     * 归并
     * @param nums
     * @param start
     * @param end
     * @param middle
     */
    private fun merge(nums: IntArray, start: Int, end: Int, middle: Int) {
        //模拟第一个序列的头指针
        var i = start

        //模拟第二个序列的头指针
        var j = middle + 1

        //模拟临时数组的头指针
        var t = 0

        //创建临时数组
        val temp = IntArray(end - start + 1)

        //从头比较两个序列，小的放入临时数组temp
        while (i <= middle && j <= end) {
            //比较大小
            if (nums[i] < nums[j]) {
                //前一个序列
                temp[t++] = nums[i++]
            } else {
                //后一个序列
                temp[t++] = nums[j++]
            }
        }

        //单独处理没有处理完的第一个序列
        while (i <= middle) {
            temp[t++] = nums[i++]
        }

        //单独处理没有处理完的第二个序列
        while (j <= end) {
            temp[t++] = nums[j++]
        }

        //将临时数组的值赋值到原数组
        if (temp.size >= 0) System.arraycopy(temp, 0, nums, start, temp.size)
    }
}