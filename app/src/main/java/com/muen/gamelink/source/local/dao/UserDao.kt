package com.muen.gamelink.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.muen.gamelink.source.local.entity.TUser
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    /**
     * 查询所有用户
     */
    @Query("select * from T_User")
    fun loadUsers(): Flow<List<TUser>>

    /**
     * 查询指定用户
     */
    @Query("select * from T_User where userId = :userId")
    fun selectUserById(userId: String): Flow<List<TUser>>

    /**
     * 添加用户
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(vararg user: TUser)

    /**
     * 更新用户金钱
     */
    @Query("update T_User set userMoney = :money where userId = :id")
    suspend fun updateUserMoneyById(money: Int,id: String)

    /**
     * 更新用户背景
     */
    @Query("update T_User set userBackground = :background where userId = :id")
    suspend fun updateUserBackgroundById(background: Int,id: String)
}