package com.muen.gamelink.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.muen.gamelink.R
import com.muen.gamelink.game.constant.mode.LevelState
import java.lang.String
import kotlin.Int
import kotlin.intArrayOf

class MyImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    companion object {
        val resources = intArrayOf(
            R.drawable.level_undo,  //关卡没有被打开
            R.drawable.level_did_one,  //关卡闯关成功 一星
            R.drawable.level_did_two,  //关卡闯关成功 二星
            R.drawable.level_did_three,  //关卡闯关成功 三星
            R.drawable.level_doing //关卡正在被闯
        )
    }

    /**
     * 设置背景颜色
     */
    fun changeLevelState(levelState: LevelState) {
        //切换到对应状态的图片
        setBackgroundResource(MyImageView.resources[String.valueOf(levelState.value).toInt()])
    }
}