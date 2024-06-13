package com.muen.gamelink.ui.success

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import com.muen.gamelink.databinding.ActivitySuccessBinding
import com.muen.gamelink.game.util.LinkUtil
import com.muen.gamelink.music.SoundPlayManager
import com.muen.gamelink.ui.BaseActivity
import com.muen.gamelink.ui.game.LinkActivity
import com.muen.gamelink.ui.level.LevelActivity
import com.muen.gamelink.ui.success.vm.SuccessVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuccessActivity : BaseActivity<ActivitySuccessBinding>() {
    private val viewModel by viewModels<SuccessVM>()

    private val stars: ArrayList<ImageView> = arrayListOf()

    override fun onCreateViewBinding(): ActivitySuccessBinding {
        return ActivitySuccessBinding.inflate(layoutInflater)
    }

    override fun initData() {
        super.initData()
        //获取数据
        val intent = this.intent
        val bundle = intent.extras!!
        viewModel.level = bundle.getParcelable("level")!!
        viewModel.serialClick = bundle.getInt("serial_click")
        viewModel.gameEnd()
    }

    override fun initView() {
        super.initView()
        stars.add(viewBinding.starLeft)
        stars.add(viewBinding.starMiddle)
        stars.add(viewBinding.starRight)
        //设置关卡数据
        viewBinding.levelText.text = "第" + viewModel.level.levelId + "关"
        //设置星星
        val starSize: Int = viewModel.level.levelState
        for (i in 1..starSize) {
            stars[i - 1].visibility = View.VISIBLE
        }
        //设置时间
        viewBinding.timeText.text = viewModel.level.levelTime.toString() + "秒"
        //设置分数
        viewBinding.scoreText.text =
            LinkUtil.getScoreByTime(viewModel.level.levelTime!!).toString() + "分"
        //设置连击
        viewBinding.batterText.text = (viewModel.serialClick.toString() + "次")

    }

    override fun initListener() {
        super.initListener()
        viewBinding.btnMenu.setOnClickListener {
            //播放点击音效
            SoundPlayManager.getInstance(baseContext).play(3)
            //关卡菜单按钮
            jumpToActivity(0)
        }

        viewBinding.btnRefresh.setOnClickListener {
            //播放点击音效
            SoundPlayManager.getInstance(baseContext).play(3)
            //重新加载按钮
            jumpToActivity(1)
        }

        viewBinding.btnNext.setOnClickListener {
            //播放点击音效
            SoundPlayManager.getInstance(baseContext).play(3)
            //下一关按钮
            jumpToActivity(2)
        }
    }

    /**
     * 跳转界面
     * @param flag
     */
    private fun jumpToActivity(flag: Int) {
        if (flag == 0) {
            viewModel.selectLevelsByMode(viewModel.level.levelMode){
                //跳转界面
                val intent = Intent(this, LevelActivity::class.java)
                //加入数据
                val bundle = Bundle()
                //加入关卡模式数据
                bundle.putString("mode", "简单")
                //加入关卡数据
                bundle.putParcelableArrayList(
                    "levels",
                    it as java.util.ArrayList<out Parcelable?>
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
            bundle.putParcelable("level", viewModel.level)
            intent.putExtras(bundle)
            startActivity(intent)
        } else {
            viewModel.selectLevelById(viewModel.level.id + 1){
                //跳转界面
                val intent = Intent(this, LinkActivity::class.java)
                //加入数据
                val bundle = Bundle()
                //加入关卡数据
                bundle.putParcelable("level", it)
                intent.putExtras(bundle)
                //跳转
                startActivity(intent)
            }
        }
    }


}