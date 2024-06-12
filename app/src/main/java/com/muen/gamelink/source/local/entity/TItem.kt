package com.muen.gamelink.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 道具
 */
@Entity(tableName = "T_Item")
data class TItem(
    @PrimaryKey var itemType: Int,  //道具的种类 1：拳头 2：炸弹 3：刷新
    var itemNumber: Int = 0,            //道具的的数量
    var itemPrice: Int = 0              //道具的价格
)