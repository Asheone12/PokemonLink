package com.muen.gamelink.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.muen.gamelink.databinding.ActivitySuccessBinding
import com.muen.gamelink.game.constant.Constant
import com.muen.gamelink.game.util.LinkUtil
import com.muen.gamelink.music.SoundPlayManager
import com.muen.gamelink.source.local.dao.LevelDao
import com.muen.gamelink.source.local.db.GameDB
import com.muen.gamelink.source.local.entity.TLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SuccessActivity : BaseActivity<ActivitySuccessBinding>() {
    //关卡
    private lateinit var level: TLevel
    private lateinit var levelDao: LevelDao
    private lateinit var mContext:Context
    private val stars: ArrayList<ImageView> = arrayListOf()
    private var serialClick: Int = 0

    override fun onCreateViewBinding(): ActivitySuccessBinding {
        return ActivitySuccessBinding.inflate(layoutInflater)
    }

    override fun initData() {
        super.initData()
        mContext = this
        levelDao = GameDB.getDatabase(this).levelDao()
        //获取数据
        val intent = this.intent
        val bundle = intent.extras!!
        level = bundle.getParcelable("level")!!
        serialClick = bundle.getInt("serial_click")
    }

    override fun initView() {
        super.initView()
        stars.add(viewBinding.starLeft)
        stars.add(viewBinding.starMiddle)
        stars.add(viewBinding.starRight)
        //设置关卡数据
        viewBinding.levelText.text = "第" + level.levelId + "关"
        //设置星星
        val starSize: Int = level.levelState
        for (i in 1..starSize) {
            stars[i - 1].visibility = View.VISIBLE
        }
        //设置时间
        viewBinding.timeText.text = level.levelTime.toString() + "秒"
        //设置分数
        viewBinding.scoreText.text =
            LinkUtil.getScoreByTime(level.levelTime!!).toString() + "分"
        //设置连击
        viewBinding.batterText.text = (serialClick.toString() + "次")

    }

    override fun initListener() {
        super.initListener()
        viewBinding.btnMenu.setOnClickListener {
            //播放点击音效
            SoundPlayManager.getInstance(baseContext).play(3)
            //关卡菜单按钮
            Log.d(Constant.TAG, "关卡菜单按钮")
            jumpToActivity(0)
        }

        viewBinding.btnRefresh.setOnClickListener {
            //播放点击音效
            SoundPlayManager.getInstance(baseContext).play(3)
            //重新加载按钮
            Log.d(Constant.TAG, "重新加载按钮")
            jumpToActivity(1)
        }

        viewBinding.btnNext.setOnClickListener {
            //播放点击音效
            SoundPlayManager.getInstance(baseContext).play(3)
            //下一关按钮
            Log.d(Constant.TAG, "下一个关卡按钮")
            jumpToActivity(2)
        }
    }

    /**
     * 跳转界面
     * @param flag
     */
    private fun jumpToActivity(flag: Int) {
        if (flag == 0) {
            lifecycleScope.launch(Dispatchers.IO) {
                val levelList = levelDao.selectLevelByMode(level.levelMode)
                //跳转界面
                val intent = Intent(mContext, LevelActivity::class.java)
                //加入数据
                val bundle = Bundle()
                //加入关卡模式数据
                bundle.putString("mode", "简单")
                //加入关卡数据
                bundle.putParcelableArrayList(
                    "levels",
                    levelList as java.util.ArrayList<out Parcelable?>
                )
                intent.putExtras(bundle)
                startActivity(intent)
            }

        } else if (flag == 1) {
            //跳转界面
            val intent = Intent(this, LinkActivity::class.java)
            //加入数据
            val bundle = Bundle()
            //加入关卡数据
            bundle.putParcelable("level", level)
            intent.putExtras(bundle)
            startActivity(intent)
        } else {
            lifecycleScope.launch(Dispatchers.IO) {
                val nextLevel = levelDao.selectLevelById(level.id + 1)[0]
                //跳转界面
                val intent = Intent(mContext, LinkActivity::class.java)
                //加入数据
                val bundle = Bundle()
                //加入关卡数据
                bundle.putParcelable("level", nextLevel)
                intent.putExtras(bundle)
                //跳转
                startActivity(intent)
            }

        }
    }


}