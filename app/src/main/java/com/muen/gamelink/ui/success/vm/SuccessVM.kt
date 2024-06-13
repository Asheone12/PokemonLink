package com.muen.gamelink.ui.success.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muen.gamelink.source.local.dao.LevelDao
import com.muen.gamelink.source.local.entity.TLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SuccessVM @Inject constructor(
    private val levelDao: LevelDao
) : ViewModel() {
    //关卡
    lateinit var level: TLevel
    var serialClick: Int = 0
    fun selectLevelsByMode(mode: Int, handler: (List<TLevel>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            levelDao.selectLevelByMode(mode).collect {
                //切换到主线程
                withContext(Dispatchers.Main) {
                    handler.invoke(it)
                }
            }

        }
    }

    fun selectLevelById(id: Int, handler: (TLevel) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            levelDao.selectLevelById(id).collect {
                //切换到主线程
                withContext(Dispatchers.Main) {
                    handler.invoke(it[0])
                }
            }

        }
    }

    /**
     * 关卡结算
     */
    fun gameEnd(){
        viewModelScope.launch(Dispatchers.IO) {
            //关卡结算
            levelDao.updateLevel(level)
            //下一关判断
            levelDao.selectLevelById(level.id + 1).collect{
                val nextLevel = it[0]
                if(nextLevel.levelState == 0){
                    levelDao.updateLevelStateById(4,nextLevel.id)
                }
            }
        }
    }
}