package com.muen.gamelink.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.muen.gamelink.R
import com.muen.gamelink.util.PxUtil.dpToPx
import com.muen.gamelink.util.PxUtil.spToPx

class GameItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    //存储物品的图片描述
    private var showImg: ImageView

    //物品的图片
    private var srcResource = 0

    //物品图片的背景样式
    private var imgResource = 0

    //存储物品的数量描述
    private var showCount: TextView

    //物品数量的背景样式
    private var countResource = 0

    //存储物品的数量
    private var count = 0

    //绘制物品数量的画笔
    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        //画笔操作
        paint.textSize = spToPx(14, getContext()).toFloat()
        paint.typeface = Typeface.DEFAULT_BOLD
        paint.color = Color.WHITE

        //加载视图
        val inflate = inflate(getContext(), R.layout.number_item, this)
        showImg = inflate.findViewById<ImageView>(R.id.show_img)
        showCount = inflate.findViewById<TextView>(R.id.show_count)

        //解析自定义属性
        if (attrs != null) {
            //获取自定义属性值的集合
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumberOfItem)

            //解析单个资源
            srcResource = typedArray.getResourceId(
                R.styleable.NumberOfItem_srcResource,
                R.drawable.link_prop_fight
            )
            showImg.setImageResource(srcResource)
            imgResource = typedArray.getResourceId(
                R.styleable.NumberOfItem_imgResource,
                R.drawable.img_shape
            )
            showImg.setBackgroundResource(imgResource)
            countResource = typedArray.getResourceId(
                R.styleable.NumberOfItem_countResource,
                R.drawable.count_shape
            )
            showCount.setBackgroundResource(countResource)

            //释放资源
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //获取宽-测量规则的模式和大小
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        //获取高-测量规则的模式和大小
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        //设置wrap_content的默认宽 / 高值
        //默认宽/高的设定并无固定依据,根据需要灵活设置
        val mWidth = dpToPx(45, context)
        val mHeight = dpToPx(45, context)

        //当布局参数设置为wrap_content时，设置默认值
        //宽,高任意一个布局参数为= wrap_content时，都设置默认值
        if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT && layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mWidth, mHeight)
        } else if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mWidth, heightSize)
        } else if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(widthSize, mHeight)
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        //需要绘制的文本
        val text = count.toString()

        //计算文本宽度
        val textWidth = paint.measureText(text)

        //获取字体fontMetrics
        val fontMetrics = paint.fontMetrics

        //计算文本高度
        val textHeight = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom

        //获取绘制的位置
        val pivotX: Int = showCount.left + (showCount.right - showCount.left) / 2
        val pivotY: Int = showCount.top + (showCount.bottom - showCount.top) / 2

        //绘制需要的字体
        canvas!!.drawText(
            text,
            pivotX - textWidth / 2,
            pivotY + textHeight,
            paint
        )
    }

    fun getCount(): Int {
        return count
    }

    fun setCount(count: Int) {
        this.count = count
        //刷新界面
        invalidate()
    }
}