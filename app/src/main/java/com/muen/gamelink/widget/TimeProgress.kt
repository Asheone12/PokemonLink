package com.muen.gamelink.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.muen.gamelink.R
import com.muen.gamelink.util.PxUtil.dpToPx
import com.muen.gamelink.util.PxUtil.spToPx
import java.text.DecimalFormat

class TimeProgress @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    //进度背景的画笔
    private lateinit var progressBgPaint: Paint

    //进度的画笔
    lateinit var progressPaint: Paint

    //进度值的画笔
    private lateinit var progressValuePaint: Paint

    //视图的布局
    private lateinit var layout: RelativeLayout

    //进度值
    private var mProgress = 0f

    //总的进度值
    var totalProgress = 100f

    //开始的角度
    var startAngle = 0

    //结束的角度
    var endAngle = 360

    //字体的大小
    private var textSize = 14

    //字体的颜色
    private var textColor = "#ffffff"

    //绘制字体的左间距
    private var textLeftPadding = 0

    //绘制字体的上间距
    private var textTopPadding = 0

    init {
        //解析属性
        if (attrs != null) {
            //1.获取所有自定义属性值的集合
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgress)

            //2.依次解析
            mProgress = typedArray.getFloat(
                R.styleable.CircleProgress_progress,
                mProgress
            )
            setProgress(mProgress)
            totalProgress = typedArray.getFloat(
                R.styleable.CircleProgress_total_progress,
                totalProgress
            )
            startAngle = typedArray.getInteger(
                R.styleable.CircleProgress_startAngle,
                startAngle
            )
            endAngle = typedArray.getInteger(
                R.styleable.CircleProgress_endAngle,
                endAngle
            )
            textSize = typedArray.getInteger(
                R.styleable.CircleProgress_textSize,
                textSize
            )
            val tempTextColor = typedArray.getString(
                R.styleable.CircleProgress_textColor
            )
            if (tempTextColor != null) {
                textColor = tempTextColor
            }
            textLeftPadding = typedArray.getInteger(
                R.styleable.CircleProgress_textLeftPadding,
                0
            )
            textTopPadding = typedArray.getInteger(
                R.styleable.CircleProgress_textTopPadding,
                0
            )

            //3.释放资源
            typedArray.recycle()
        }

        initPaint()
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        //1.确定绘制的矩形区域
        @SuppressLint("DrawAllocation") val rectF = RectF(
            layout.left.toFloat(),
            layout.top.toFloat(),
            layout.right.toFloat(),
            layout.bottom
                .toFloat()
        )

        //2.绘制背景
        canvas!!.drawArc(
            rectF, 0f,
            360f,
            false,
            progressBgPaint
        )

        //3.绘制进度
        canvas.drawArc(
            rectF,
            startAngle.toFloat(),
            (endAngle - startAngle) * ((totalProgress - mProgress) / totalProgress),
            false,
            progressPaint
        )

        //4.绘制进度值
        val text = DecimalFormat("##0.0").format(mProgress.toDouble()) + "秒"

        //4.1计算文本宽度
        val textWidth = progressValuePaint.measureText(text)

        //4.2获取字体fontMetrics
        val fontMetrics = progressValuePaint.fontMetrics

        //4.3计算文本高度
        val textHeight = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom

        //4.4绘制需要的字体
        canvas.drawText(
            text,
            dpToPx(textLeftPadding, context) + pivotX - textWidth / 2,
            dpToPx(textTopPadding, context) + pivotY + textHeight,
            progressValuePaint
        )
    }

    private fun initPaint() {
        //进度的背景画笔
        progressBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        progressBgPaint.color = Color.rgb(153, 223, 159)
        progressBgPaint.strokeWidth = dpToPx(10, context).toFloat()
        progressBgPaint.style = Paint.Style.STROKE
        progressBgPaint.strokeJoin = Paint.Join.ROUND
        progressBgPaint.strokeCap = Paint.Cap.ROUND

        //进度的画笔
        progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        progressPaint.color = Color.argb(180, 34, 52, 68)
        progressPaint.strokeWidth = spToPx(10, context).toFloat()
        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeJoin = Paint.Join.ROUND
        progressPaint.strokeCap = Paint.Cap.ROUND

        //进度值的画笔
        progressValuePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        progressValuePaint.color = Color.parseColor(textColor)
        progressValuePaint.textSize = dpToPx(textSize, context).toFloat()
        progressValuePaint.typeface = Typeface.createFromAsset(
            context.assets,
            "fonts/造字工房乐真体.ttf"
        )

        //加载视图
        val inflate = inflate(context, R.layout.circle_progress, this)
        layout = inflate.findViewById(R.id.root)
    }

    fun getProgress(): Float {
        return this.mProgress
    }

    fun setProgress(progress: Float) {
        this.mProgress = progress

        //刷新，重绘
        invalidate()
    }
}