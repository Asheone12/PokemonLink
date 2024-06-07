package com.muen.gamelink.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class MyTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    /**
     * 重写设置字体方法
     */
    override fun setTypeface(tf: Typeface?) {
        val newTf = Typeface.createFromAsset(context.assets, "fonts/造字工房乐真体.ttf")
        super.setTypeface(newTf)
    }
}