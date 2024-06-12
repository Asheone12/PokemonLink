package com.muen.gamelink.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.muen.gamelink.source.local.entity.TItem

@Dao
interface ItemDao {
    /**
     * 查询所有道具
     */
    @Query("select * from T_Item")
    fun loadItems(): List<TItem>

    /**
     * 根据道具类型查询对应道具
     */
    @Query("select * from T_Item where itemType = :type")
    fun selectItemByType(type: String): List<TItem>

    /**
     * 添加道具
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(vararg item: TItem)

    /**
     * 更新道具数量
     */
    @Query("update T_Item set itemNumber = :number where itemType = :type")
    suspend fun updateItemNumberByType(number: Int,type: Int)

    /**
     * 更新道具价格
     */
    @Query("update T_Item set itemPrice = :price where itemType = :type")
    suspend fun updateItemPriceByType(price: Int,type: Int)
}