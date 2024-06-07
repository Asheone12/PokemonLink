package com.muen.gamelink.ui

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.muen.gamelink.databinding.ActivitySuccessBinding
import com.muen.gamelink.game.constant.Constant
import com.muen.gamelink.game.util.LinkUtil
import com.muen.gamelink.model.Level
import com.muen.gamelink.music.SoundPlayManager
import org.litepal.LitePal
import java.lang.String
import kotlin.Int

class SuccessActivity : BaseActivity<ActivitySuccessBinding>() {
    //关卡
    private lateinit var level: Level
    private val stars: ArrayList<ImageView> = arrayListOf()
    private var serialClick: Int = 0

    override fun onCreateViewBinding(): ActivitySuccessBinding {
        return ActivitySuccessBinding.inflate(layoutInflater)
    }

    override fun initData() {
        super.initData()
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
        viewBinding.levelText.text = "第" + level.getLevelId() + "关"
        //设置星星
        val starSize: Int = level.getLevelState().minus('0') ?: 0
        for (i in 1..starSize) {
            stars[i - 1].visibility = View.VISIBLE
        }
        //设置时间
        viewBinding.timeText.text = level.getLevelTime().toString() + "秒"
        //设置分数
        viewBinding.scoreText.text =
            LinkUtil.getScoreByTime(level.getLevelTime()!!).toString() + "分"
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
            //查询对应模式的数据
            val levels: List<Level> =
                LitePal.where("levelMode == ?", String.valueOf(level.getLevelMode())).find(
                    Level::class.java
                )
            Log.d(Constant.TAG, levels.size.toString() + "")
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
            bundle.putParcelableArrayList(
                "levels",
                levels as java.util.ArrayList<out Parcelable?>
            )
            intent.putExtras(bundle)
            startActivity(intent)
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
            //下一关
            //跳转界面
            val intent = Intent(this, LinkActivity::class.java)
            //加入数据
            val bundle = Bundle()
            //加入关卡数据
            val nextLevel: Level =
                LitePal.find(Level::class.java, (level.getId() + 1).toLong())
            bundle.putParcelable("level", nextLevel)
            intent.putExtras(bundle)
            //跳转
            startActivity(intent)
        }
    }


}