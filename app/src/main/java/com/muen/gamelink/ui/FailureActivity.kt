package com.muen.gamelink.ui

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import com.muen.gamelink.databinding.ActivityFailureBinding
import com.muen.gamelink.game.constant.Constant
import com.muen.gamelink.model.Level
import com.muen.gamelink.music.SoundPlayManager
import org.litepal.LitePal
import java.lang.String
import kotlin.Int

class FailureActivity : BaseActivity<ActivityFailureBinding>() {
    //level
    private lateinit var level: Level

    override fun onCreateViewBinding(): ActivityFailureBinding {
        return ActivityFailureBinding.inflate(layoutInflater)
    }

    override fun initData() {
        super.initData()
        //获取数据
        val intent = this.intent
        val bundle = intent.extras!!
        level = bundle.getParcelable("level")!!
    }

    override fun initView() {
        super.initView()

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
            //查询对应模式的数据
            val levels: List<Level> =
                LitePal.where("levelMode == ?", String.valueOf(level.getLevelMode())).find(
                    Level::class.java
                )
            //依次查询每一个内容
            for (level in levels) {
                Log.d(Constant.TAG, level.toString())
            }

            //跳转界面
            val intent = Intent(this, LevelActivity::class.java)
            //加入数据
            val bundle = Bundle()
            //加入关卡模式数据
            bundle.putString("mode", "简单")
            //加入关卡数据
            bundle.putParcelableArrayList("levels", levels as ArrayList<out Parcelable?>)
            intent.putExtras(bundle)
            startActivity(intent)
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