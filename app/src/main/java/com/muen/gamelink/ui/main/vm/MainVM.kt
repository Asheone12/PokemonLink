package com.muen.gamelink.ui.main.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muen.gamelink.source.local.dao.ItemDao
import com.muen.gamelink.source.local.dao.LevelDao
import com.muen.gamelink.source.local.dao.UserDao
import com.muen.gamelink.source.local.entity.TItem
import com.muen.gamelink.source.local.entity.TLevel
import com.muen.gamelink.source.local.entity.TUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val userDao: UserDao,
    private val levelDao: LevelDao,
    private val itemDao: ItemDao
) : ViewModel() {
    /**
     * 初始化数据库
     */
    fun initDatabase() {
        loadUsers()
        loadLevels()
        loadItems()
    }

    private fun loadUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            //如果用户数据为空，装入数据
            userDao.loadUsers().collect {
                if (it.isEmpty()) {
                    userDao.insertUser(TUser("1001", 1000, 0))
                }
            }
        }
    }

    private fun loadLevels() {
        viewModelScope.launch(Dispatchers.IO) {
            //如果关卡数据为空，装入数据
            levelDao.loadLevels().collect {
                if (it.isEmpty()) {
                    //简单模式
                    for (i in 1..40) {
                        if (i == 1) {
                            levelDao.insertLevel(TLevel(i, 0f, 1, 4))
                        } else {
                            levelDao.insertLevel(TLevel(i, 0f, 1, 0))
                        }
                    }
                    //普通模式
                    for (i in 1..40) {
                        if (i == 1) {
                            levelDao.insertLevel(TLevel(i, 0f, 2, 4))
                        } else {
                            levelDao.insertLevel(TLevel(i, 0f, 2, 0))
                        }
                    }
                    //困难模式
                    for (i in 1..40) {
                        if (i == 1) {
                            levelDao.insertLevel(TLevel(i, 0f, 3, 4))
                        } else {
                            levelDao.insertLevel(TLevel(i, 0f, 3, 0))
                        }
                    }
                }
            }
        }
    }

    private fun loadItems() {
        viewModelScope.launch(Dispatchers.IO) {
            //如果道具数据为空，装入数据
            itemDao.loadItems().collect {
                if (it.isEmpty()) {
                    //1.装入拳头道具
                    itemDao.insertItem(TItem(1, 9, 10))
                    //2.装入炸弹道具
                    itemDao.insertItem(TItem(2, 9, 10))
                    //3.装入刷新道具
                    itemDao.insertItem(TItem(3, 9, 10))
                }
            }
        }
    }


    fun selectLevelData(mode: Int, handler: (List<TLevel>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            levelDao.selectLevelByMode(mode).collect {
                //切换到主线程
                withContext(Dispatchers.Main) {
                    handler.invoke(it)
                }
            }

        }
    }
}