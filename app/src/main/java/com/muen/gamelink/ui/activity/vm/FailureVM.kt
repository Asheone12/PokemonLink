package com.muen.gamelink.ui.activity.vm

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
class FailureVM @Inject constructor(
    private val levelDao: LevelDao
) : ViewModel() {
    //关卡
    lateinit var level: TLevel
    fun selectLevelsByMode(mode: Int, handler: (List<TLevel>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val levels = levelDao.selectLevelByMode(mode)
            //切换到主线程
            withContext(Dispatchers.Main) {
                handler.invoke(levels)
            }

        }
    }

}