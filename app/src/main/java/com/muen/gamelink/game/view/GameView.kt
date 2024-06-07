package com.muen.gamelink.game.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.widget.RelativeLayout
import com.muen.gamelink.game.entity.AnimalPoint
import com.muen.gamelink.game.entity.LinkInfo
import com.muen.gamelink.game.util.CustomPaint
import com.muen.gamelink.game.util.LinkUtil

class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    //点的信息
    private var linkInfo: LinkInfo? = null

    //画笔
    private var customPaint: CustomPaint? = null

    private val TAG = "GameView"

    init {
        setWillNotDraw(false)
    }

    fun getLinkInfo(): LinkInfo? {
        return linkInfo
    }

    fun setLinkInfo(linkInfo: LinkInfo?) {
        this.linkInfo = linkInfo
        //初始化画笔
        customPaint = CustomPaint()
        invalidate()
    }

    //重绘
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (linkInfo != null) {
            Log.d(TAG, "刷新界面")

            //获取点数据
            val points: List<AnimalPoint>? = linkInfo!!.getPoints()

            //判断linkInfo是否有数据
            if (points!!.isNotEmpty()) {

                //画路径
                for (i in 0 until points.size - 1) {
                    //转换坐标
                    val realPoint1: AnimalPoint = LinkUtil.getRealAnimalPoint(
                        points[i],
                        context
                    )
                    val realPoint2: AnimalPoint = LinkUtil.getRealAnimalPoint(
                        points[i + 1],
                        context
                    )

                    //测试
                    Log.d(TAG, points[i].toString() + " " + realPoint1.toString())
                    Log.d(TAG, points[i + 1].toString() + " " + realPoint2.toString())

                    //画闪电
                    customPaint?.drawLighting(
                        realPoint1.x.toFloat(),
                        realPoint1.y.toFloat(),
                        realPoint2.x.toFloat(),
                        realPoint2.y.toFloat(),
                        5,
                        canvas
                    )
                    customPaint?.drawLightingBold(
                        realPoint1.x.toFloat(),
                        realPoint1.y.toFloat(),
                        realPoint2.x.toFloat(),
                        realPoint2.y.toFloat(),
                        5,
                        canvas
                    )

                }
            }
        }

    }
}