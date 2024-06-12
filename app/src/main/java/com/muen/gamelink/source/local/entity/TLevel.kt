package com.muen.gamelink.source.local.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 关卡
 */
@Entity(tableName = "T_Level")
data class TLevel(

    var levelId: Int,           //关卡号
    var levelTime: Float = 0f,       //闯关时间
    var levelMode: Int = 0,         //关卡模式 1：简单 2：普通 3：困难
    var levelState: Int = 0         //闯关状态 0：没有闯关 1：1星 2：2星 3：3星
): Parcelable {
    @PrimaryKey (autoGenerate = true)
    var id: Int = 0    //id

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readInt()
    ) {
        id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(levelId)
        parcel.writeFloat(levelTime)
        parcel.writeInt(levelMode)
        parcel.writeInt(levelState)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TLevel> {
        override fun createFromParcel(parcel: Parcel): TLevel {
            return TLevel(parcel)
        }

        override fun newArray(size: Int): Array<TLevel?> {
            return arrayOfNulls(size)
        }
    }
}