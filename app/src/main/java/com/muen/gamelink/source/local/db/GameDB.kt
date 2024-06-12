package com.muen.gamelink.source.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.muen.gamelink.source.local.dao.ItemDao
import com.muen.gamelink.source.local.dao.LevelDao
import com.muen.gamelink.source.local.dao.UserDao
import com.muen.gamelink.source.local.entity.TItem
import com.muen.gamelink.source.local.entity.TLevel
import com.muen.gamelink.source.local.entity.TUser

@Database(version = 1, entities = [TItem::class,TLevel::class,TUser::class])
abstract class GameDB: RoomDatabase() {
    abstract fun itemDao():ItemDao
    abstract fun levelDao():LevelDao
    abstract fun userDao():UserDao

    companion object {
        @Volatile
        private var instance: GameDB? = null

        @Synchronized
        fun getDatabase(context: Context): GameDB {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context.applicationContext,
                GameDB::class.java,
                "game.db"
            )//.createFromAsset("database/game.db")        //如果需要手动添加数据库文件，加上这一句话
                .build().apply {
                    instance = this
                }
        }
    }
}