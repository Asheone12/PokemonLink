package com.muen.gamelink.ui.fragment.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muen.gamelink.game.constant.mode.ItemMode
import com.muen.gamelink.source.local.dao.ItemDao
import com.muen.gamelink.source.local.dao.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StoreVM @Inject constructor(private val itemDao: ItemDao,private val userDao: UserDao): ViewModel() {
    //存储数据
    var userMoney = 0
    var fightMoney = 0
    var fightNum = 0
    var bombMoney = 0
    var bombNum = 0
    var refreshMoney = 0
    var refreshNum = 0
    val userChangeResult = MutableLiveData(false)
    val itemChangeResult = MutableLiveData(false)

    /**
     * 加载用户的数据
     */
    fun loadUserData(){
        viewModelScope.launch(Dispatchers.IO) {
            //查询用户数据
            userDao.loadUsers().collect{
                userMoney = it[0].userMoney
                withContext(Dispatchers.Main){
                    userChangeResult.value = true
                }
            }
        }
    }

    /**
     * 加载道具的数据
     */
    fun loadItemData(){
        viewModelScope.launch(Dispatchers.IO) {
            //查询道具数据
            itemDao.loadItems().collect{
                for(item in it){
                    when(item.itemType){
                        ItemMode.ITEM_FIGHT.value -> {
                            //拳头道具
                            fightMoney = item.itemPrice
                            fightNum = item.itemNumber
                        }
                        ItemMode.ITEM_BOMB.value -> {
                            //炸弹道具
                            bombMoney = item.itemPrice
                            bombNum = item.itemNumber
                        }
                        else -> {
                            //刷新道具
                            refreshMoney = item.itemPrice
                            refreshNum = item.itemNumber
                        }
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