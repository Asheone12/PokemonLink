package com.muen.gamelink.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.muen.gamelink.databinding.ActivityFailureBinding
import com.muen.gamelink.game.constant.Constant
import com.muen.gamelink.music.SoundPlayManager
import com.muen.gamelink.source.local.dao.LevelDao
import com.muen.gamelink.source.local.db.GameDB
import com.muen.gamelink.source.local.entity.TLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FailureActivity : BaseActivity<ActivityFailureBinding>() {
    //level
    private lateinit var level: TLevel
    private lateinit var levelDao: LevelDao
    private lateinit var mContext: Context

    override fun onCreateViewBinding(): ActivityFailureBinding {
        return ActivityFailureBinding.inflate(layoutInflater)
    }

    override fun initData() {
        super.initData()
        mContext = this
        levelDao = GameDB.getDatabase(this).levelDao()
        //获取数据
        val intent = this.intent
        val bundle = intent.extras!!
        level = bundle.getParcelable("level")!!
    }

    override fun initListener() {
        super.initListener()
        viewBinding.btnMenu.setOnClickListener {
            //播放点击音效
            SoundPlayManager.getInstance(baseContext).play(3)
            Log.d(Constant.TAG, "关卡菜单按钮被点击了")
            jumpToActivity(0)
        }

        viewBinding.btnRefresh.setOnClickListener {
            SoundPlayManager.getInstance(baseContext).play(3)
            Log.d(Constant.TAG, "刷新按钮被点击了")
            jumpToActivity(1)
        }

    }

    /**
     * 跳转界面
     */
    private fun jumpToActivity(flag: Int) {
        if (flag == 0) {
            lifecycleScope.launch(Dispatchers.IO) {
                levelDao.selectLevelByMode(level.levelMode).collect{
                    val levelList = it
                    //切换到主线程
                    withContext(Dispatchers.Main){
                        //跳转界面
                        val intent = Intent(mContext, LevelActivity::class.java)
                        //加入数据
                        val bundle = Bundle()
                        //加入关卡模式数据
                        bundle.putString("mode", "简单")
                        //加入关卡数据
                        bundle.putParcelableArrayList("levels", levelList as ArrayList<out Parcelable?>)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                }

            }

        } else {
            //跳转界面
            val intent = Intent(this, LinkActivity::class.java)
            //加入数据
            val bundle = Bundle()
            //加入关卡数据
            bundle.putParcelable("level", level)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

}