package com.muen.gamelink.ui.failure

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.viewModels
import com.muen.gamelink.databinding.ActivityFailureBinding
import com.muen.gamelink.game.constant.Constant
import com.muen.gamelink.music.SoundPlayManager
import com.muen.gamelink.ui.BaseActivity
import com.muen.gamelink.ui.failure.vm.FailureVM
import com.muen.gamelink.ui.game.LinkActivity
import com.muen.gamelink.ui.level.LevelActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FailureActivity : BaseActivity<ActivityFailureBinding>() {
    private val viewModel by viewModels<FailureVM>()
    private lateinit var mContext: Context

    override fun onCreateViewBinding(): ActivityFailureBinding {
        return ActivityFailureBinding.inflate(layoutInflater)
    }

    override fun initData() {
        super.initData()
        mContext = this
        //获取数据
        val intent = this.intent
        val bundle = intent.extras!!
        viewModel.level = bundle.getParcelable("level")!!
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
            viewModel.selectLevelsByMode(viewModel.level.levelMode){
                //跳转界面
                val intent = Intent(mContext, LevelActivity::class.java)
                //加入数据
                val bundle = Bundle()
                //加入关卡模式数据
                bundle.putString("mode", "简单")
                //加入关卡数据
                bundle.putParcelableArrayList("levels", it as ArrayList<out Parcelable?>)
                intent.putExtras(bundle)
                startActivity(intent)
            }

        } else {
            //跳转界面
            val intent = Intent(this, LinkActivity::class.java)
            //加入数据
            val bundle = Bundle()
            //加入关卡数据
            bundle.putParcelable("level", viewModel.level)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

}