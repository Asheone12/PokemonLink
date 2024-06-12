package com.muen.gamelink.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 用户
 */
@Entity(tableName = "T_User")
data class TUser(
    @PrimaryKey val userId: String,
    var userMoney: Int = 0,
    var userBackground: Int = 0,
)