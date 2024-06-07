package com.muen.gamelink.game.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.muen.gamelink.game.constant.LinkConstant
import com.muen.gamelink.game.entity.AnimalPoint

class AnimalView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    //图片的标志代号，用来判断两个图片是否相等
    private var flag = 0

    //图片的坐标
    private lateinit var point: AnimalPoint

    fun initAnimal(flag: Int, point: AnimalPoint) {
        this.flag = flag
        this.point = point
    }

    fun initAnimal(resId: Int, flag: Int, point: AnimalPoint) {
        this.setBackgroundResource(LinkConstant.ANIMAL_BG)
        this.setImageResource(resId)
        this.flag = flag
        this.point = point
    }

    //选中时的背景
    fun changeAnimalBackground(resId: Int) {
        setBackgroundResource(resId)
    }

    fun getFlag(): Int {
        return flag
    }

    fun getPoint(): AnimalPoint? {
        return point
    }
}