package com.muen.gamelink.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.muen.gamelink.source.local.entity.TLevel
import kotlinx.coroutines.flow.Flow

@Dao
interface LevelDao {
    /**
     * 查询所有关卡
     */
    @Query("select * from T_Level")
    fun loadLevels(): Flow<List<TLevel>>

    /**
     * 根据id查询对应关卡
     */
    @Query("select * from T_Level where id = :id")
    fun selectLevelById(id: Int): Flow<List<TLevel>>

    /**
     * 根据mode查询对应关卡
     */
    @Query("select * from T_Level where levelMode = :mode")
    fun selectLevelByMode(mode: Int): Flow<List<TLevel>>

    /**
     * 添加关卡
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLevel(vararg level: TLevel)

    /**
     * 更新关卡
     */
    @Update
    suspend fun updateLevel(level:TLevel)

    /**
     * 更新关卡时间
     */
    @Query("update T_Level set levelTime = :time where id = :id")
    suspend fun updateLevelTimeById(time: Float,id: Int)

    /**
     * 更新关卡模式
     */
    @Query("update T_Level set levelMode = :mode where id = :id")
    suspend fun updateLevelModeById(mode: Int,id: Int)

    /**
     * 更新关卡通关状态
     */
    @Query("update T_Level set levelState = :state where id = :id")
    suspend fun updateLevelStateById(state: Int,id: Int)
}