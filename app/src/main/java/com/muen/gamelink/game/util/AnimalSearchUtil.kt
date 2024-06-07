package com.muen.gamelink.game.util

import com.muen.gamelink.game.entity.AnimalPoint
import com.muen.gamelink.game.entity.LinkInfo

/**
 * 判断一个布局中的两个动物是否可以连接
 */
object AnimalSearchUtil {
    /**
     * 判断在给点的布局中，给定的两个位置，能否无转折点连接。
     * @param board
     * @param startPoint
     * @param endPoint
     * @param linkInfo
     * @return
     */
    private fun canMatchTwoAnimalWithNoBreak(
        board: Array<IntArray>,
        startPoint: AnimalPoint,
        endPoint: AnimalPoint,
        linkInfo: LinkInfo?
    ): Boolean {
        //如果不属于无转折连接，直接返回
        if (startPoint.x != endPoint.x && startPoint.y != endPoint.y) return false

        //记录两点之间的范围边界
        var min = 0
        var max = 0

        //如果两个点 x 坐标相同，则在竖直方向扫描
        if (startPoint.x === endPoint.x) {
            //竖直方向扫描的范围
            min = Math.min(startPoint.y, endPoint.y)
            max = Math.max(startPoint.y, endPoint.y)

            //扫描
            min++
            while (min < max) {
                if (board[startPoint.x][min] != 0) {
                    return false
                }
                min++
            }
        } else {
            //如果两个点 y 坐标相同，则在水平方向扫描

            //水平方向扫描的范围
            min = Math.min(startPoint.x, endPoint.x)
            max = Math.max(startPoint.x, endPoint.x)

            //扫描
            min++
            while (min < max) {
                if (board[min][startPoint.y] != 0) {
                    return false
                }
                min++
            }
        }

        //封装数据
        if (linkInfo != null) {
            linkInfo.setPoints(LinkInfo(startPoint, endPoint).getPoints())
        }
        return true
    }

    /**
     * 判断在给点的布局中，给定的两个位置，能否在有一个转折点的情况下连接。
     * @param board
     * @param startPoint
     * @param endPoint
     * @param linkInfo
     * @return
     */
    private fun canMatchTwoAnimalWithOneBreak(
        board: Array<IntArray>,
        startPoint: AnimalPoint,
        endPoint: AnimalPoint,
        linkInfo: LinkInfo?
    ): Boolean {
        //一个转折点，那么满足这个转折点的点只有两个。
        //起始，结束，两个转折点共计四个点组成一个长方形。

        //测试转折点1
        val breakPoint1 = AnimalPoint(startPoint.x, endPoint.y)
        if (board[breakPoint1.x][breakPoint1.y] == 0) {
            //转折点首先需要能通过，通过后就转化为判断两次无转折连接
            if (canMatchTwoAnimalWithNoBreak(board, startPoint, breakPoint1, null)
                && canMatchTwoAnimalWithNoBreak(board, breakPoint1, endPoint, null)
            ) {
                //封装数据
                if (linkInfo != null) {
                    linkInfo.setPoints(LinkInfo(startPoint, breakPoint1, endPoint).getPoints())
                }
                return true
            }
        }

        //测试转折点2
        val breakPoint2 = AnimalPoint(endPoint.x, startPoint.y)
        if (board[breakPoint2.x][breakPoint2.y] == 0) {
            //转折点首先需要能通过，通过后就转化为判断两次无转折连接
            if (canMatchTwoAnimalWithNoBreak(board, startPoint, breakPoint2, null)
                && canMatchTwoAnimalWithNoBreak(board, breakPoint2, endPoint, null)
            ) {
                //封装数据
                if (linkInfo != null) {
                    linkInfo.setPoints(LinkInfo(startPoint, breakPoint2, endPoint).getPoints())
                }
                return true
            }
        }
        return false
    }

    /**
     * 判断在给点的布局中，给定的两个位置，能否只有两个转折点的情况下连接。
     * @param board
     * @param startPoint
     * @param endPoint
     * @param linkInfo
     * @return
     */
    fun canMatchTwoAnimalWithTwoBreak(
        board: Array<IntArray>,
        startPoint: AnimalPoint,
        endPoint: AnimalPoint,
        linkInfo: LinkInfo?
    ): Boolean {
        //判断是不是无转折连接
        if (canMatchTwoAnimalWithNoBreak(board, startPoint, endPoint, linkInfo)) {
            return true
        }

        //判断是不是一个转折连接
        if (canMatchTwoAnimalWithOneBreak(board, startPoint, endPoint, linkInfo)) {
            return true
        }

        //两个转折点的判断
        var i: Int
        /**
         * 水平向右扫描
         * A--->C
         * |
         * B<---
         */
        i = startPoint.y + 1
        while (i < board[startPoint.x].size) {

            //首先通过的位置不能有其它动物
            if (board[startPoint.x][i] == 0) {
                //找到C点
                val breakPoint = AnimalPoint(startPoint.x, i)

                //判断C点到B点能否在有一个转折点的情况下连接
                val temp_info = LinkInfo()
                if (canMatchTwoAnimalWithOneBreak(board, breakPoint, endPoint, temp_info)) {
                    //封装数据
                    if (linkInfo != null) {
                        linkInfo.setPoints(
                            LinkInfo(
                                startPoint,
                                breakPoint,
                                temp_info.getPoints().get(1),
                                endPoint
                            ).getPoints()
                        )
                    }

                    //返回
                    return true
                }

                //如果C到不了B继续向右扫描
            } else {
                //无法向右扫描
                break
            }
            i++
        }
        /**
         * 水平向左扫描
         * C<--- A
         * |
         * ---> B
         */
        i = startPoint.y - 1
        while (i >= 0) {

            //首先通过的位置不能有其它动物
            if (board[startPoint.x][i] == 0) {
                //找到C点
                val breakPoint = AnimalPoint(startPoint.x, i)

                //判断C点到B点能否在有一个转折点的情况下连接
                val temp_info = LinkInfo()
                if (canMatchTwoAnimalWithOneBreak(board, breakPoint, endPoint, temp_info)) {
                    //封装数据
                    if (linkInfo != null) {
                        linkInfo.setPoints(
                            LinkInfo(
                                startPoint,
                                breakPoint,
                                temp_info.getPoints().get(1),
                                endPoint
                            ).getPoints()
                        )
                    }

                    //返回
                    return true
                }

                //如果C到不了B继续向左扫描
            } else {
                //无法向左扫描
                break
            }
            i--
        }
        /**
         * 竖直向下扫描
         * A   B
         * |   |
         * C---
         */
        i = startPoint.x + 1
        while (i < board.size) {

            //首先通过的位置不能有其它动物
            if (board[i][startPoint.y] == 0) {
                //找到C点
                val breakPoint = AnimalPoint(i, startPoint.y)

                //判断C点到B点能否在有一个转折点的情况下连接
                val temp_info = LinkInfo()
                if (canMatchTwoAnimalWithOneBreak(board, breakPoint, endPoint, temp_info)) {
                    //封装数据
                    linkInfo?.setPoints(
                        LinkInfo(
                            startPoint,
                            breakPoint,
                            temp_info.getPoints().get(1),
                            endPoint
                        ).getPoints()
                    )

                    //返回
                    return true
                }

                //如果C到不了B继续向下扫描
            } else {
                //无法向下扫描
                break
            }
            i++
        }
        /**
         * 竖直向上扫描
         * C---
         * |   |
         * A   B
         */
        i = startPoint.x - 1
        while (i >= 0) {

            //首先通过的位置不能有其它动物
            if (board[i][startPoint.y] == 0) {
                //找到C点
                val breakPoint = AnimalPoint(i, startPoint.y)

                //判断C点到B点能否在有一个转折点的情况下连接
                val temp_info = LinkInfo()
                if (canMatchTwoAnimalWithOneBreak(board, breakPoint, endPoint, temp_info)) {
                    //封装数据
                    if (linkInfo != null) {
                        linkInfo.setPoints(
                            LinkInfo(
                                startPoint,
                                breakPoint,
                                temp_info.getPoints().get(1),
                                endPoint
                            ).getPoints()
                        )
                    }

                    //返回
                    return true
                }

                //如果C到不了B继续向上扫描
            } else {
                //无法向上扫描
                break
            }
            i--
        }
        return false
    }
}