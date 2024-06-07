package com.muen.gamelink.model

import android.os.Parcel
import android.os.Parcelable
import org.litepal.crud.LitePalSupport

/**
 * 关卡
 */
class Level : LitePalSupport(), Parcelable {
    //id
    private var id = 0

    //关卡号
    private var levelId = 0

    //闯关时间
    private var levelTime = 0f

    //关卡模式 1：简单 2：普通 3：困难
    private var levelMode = '0'

    //闯关状态 0：没有闯关 1：1星 2：2星 3：3星
    private var levelState = '0'

    //setter、getter方法
    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getLevelId(): Int {
        return levelId
    }

    fun setLevelId(levelId: Int) {
        this.levelId = levelId
    }

    fun getLevelTime(): Float {
        return levelTime
    }

    fun setLevelTime(levelTime: Float) {
        this.levelTime = levelTime
    }

    fun getLevelMode(): Char {
        return levelMode
    }

    fun setLevelMode(levelMode: Char) {
        this.levelMode = levelMode
    }

    fun getLevelState(): Char {
        return levelState
    }

    fun setLevelState(levelState: Char) {
        this.levelState = levelState
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(levelId)
        parcel.writeFloat(levelTime)
        parcel.writeCharArray(charArrayOf(levelMode, levelState))
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Level> {
        override fun createFromParcel(parcel: Parcel): Level {
            //必须按顺序读取
            val level = Level()
            level.id = parcel.readInt()
            level.levelId = parcel.readInt()
            level.levelTime = parcel.readFloat()
            val temp = CharArray(2)
            parcel.readCharArray(temp)
            level.levelMode = temp[0]
            level.levelState = temp[1]

            return level
        }

        override fun newArray(size: Int): Array<Level?> {
            return arrayOfNulls(size)
        }
    }
}