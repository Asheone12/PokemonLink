package com.muen.gamelink.ui.activity.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muen.gamelink.game.constant.mode.ItemMode
import com.muen.gamelink.source.local.dao.ItemDao
import com.muen.gamelink.source.local.dao.LevelDao
import com.muen.gamelink.source.local.dao.UserDao
import com.muen.gamelink.source.local.entity.TLevel
import com.muen.gamelink.source.local.entity.TUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LinkVM @Inject constructor(
    private val userDao: UserDao,
    private val levelDao: LevelDao,
    private val itemDao: ItemDao
) : ViewModel() {
    //当前关卡模型数据
    lateinit var level: TLevel
    //用户
    lateinit var user: TUser

    //记录金币的变量
    var money = 0
    //记录拳头道具的数量
    var fightNum = 0
    //记录炸弹道具的数量
    var bombNum = 0
    //记录刷新道具的数量
    var refreshNum = 0

    val moneyChangeResult = MutableLiveData(false)
    val itemChangeResult = MutableLiveData(false)

    fun loadUsers(){
        viewModelScope.launch(Dispatchers.IO) {
            //查询用户数据
            userDao.loadUsers().collect {
                user = it[0]
                money = user.userMoney
                withContext(Dispatchers.Main){
                    moneyChangeResult.value = true
                }
            }
        }
    }

    fun loadItems(){
        viewModelScope.launch(Dispatchers.IO) {
            itemDao.loadItems().collect{
                for (item in it) {
                    if (item.itemType == ItemMode.ITEM_FIGHT.value) {
                        //拳头道具
                        fightNum = item.itemNumber
                    } else if (item.itemType == ItemMode.ITEM_BOMB.value) {
                        //炸弹道具
                        bombNum = item.itemNumber
                    } else {
                        //刷新道具
                        refreshNum = item.itemNumber
                    }
                }
                withContext(Dispatchers.Main){
                    itemChangeResult.value = true
                }
            }
        }
    }

    /**
     * 修改金钱
     */
    fun changeUserMoney(money:Int){
        //刷新用户数据
        viewModelScope.launch(Dispatchers.IO) {
            userDao.updateUserMoneyById(money,"1001")
        }
    }

    /**
     * 修改指定道具数量
     */
    fun changeItemCount(type:Int,itemCount:Int){
        viewModelScope.launch(Dispatchers.IO) {
            itemDao.updateItemNumberByType(itemCount,type)
        }
    }

}