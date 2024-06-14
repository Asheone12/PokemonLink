package com.muen.gamelink.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import com.muen.gamelink.R
import com.muen.gamelink.databinding.ActivityMainBinding
import com.muen.gamelink.game.constant.Constant
import com.muen.gamelink.music.BgmManager
import com.muen.gamelink.music.SoundPlayManager
import com.muen.gamelink.ui.BaseActivity
import com.muen.gamelink.ui.fragment.HelpFragment
import com.muen.gamelink.ui.fragment.SettingFragment
import com.muen.gamelink.ui.fragment.StoreFragment
import com.muen.gamelink.ui.activity.vm.MainVM
import com.muen.gamelink.util.PxUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel by viewModels<MainVM>()
    private lateinit var mBroadcastReceiver: BroadcastReceiver

    override fun onCreateViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initData() {
        super.initData()
        //提前加载资源，不然的话，资源没有加载好，会没有声音
        SoundPlayManager.getInstance(this)
        //向数据库装入数据
        viewModel.initDatabase()
        //播放音乐
        playMusic()
        //广播接受者
        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action
                if (!TextUtils.isEmpty(action)) {
                    when (action) {
                        Intent.ACTION_SCREEN_OFF -> {
                            Log.d(Constant.TAG, "屏幕关闭，变黑")
                            if (BgmManager.getInstance(baseContext).isBackgroundMusicPlaying()) {
                                Log.d(Constant.TAG, "正在播放音乐，关闭")
                                //暂停播放
                                BgmManager.getInstance(baseContext).pauseBackgroundMusic()
                            }
                        }

                        Intent.ACTION_SCREEN_ON -> Log.d(Constant.TAG, "屏幕开启，变亮")
                        Intent.ACTION_USER_PRESENT -> Log.d(Constant.TAG, "解锁成功")
                        else -> {}
                    }
                }
            }
        }
        registerReceiver(mBroadcastReceiver, IntentFilter(Intent.ACTION_SCREEN_OFF))
        registerReceiver(mBroadcastReceiver, IntentFilter(Intent.ACTION_SCREEN_ON))
        registerReceiver(mBroadcastReceiver, IntentFilter(Intent.ACTION_USER_PRESENT))

    }

    override fun initView() {
        super.initView()
        //设置模式按钮的drawableLeft
        setDrawableLeft(viewBinding.mainModeEasy, R.drawable.main_mode_easy)
        setDrawableLeft(viewBinding.mainModeNormal, R.drawable.main_mode_normal)
        setDrawableLeft(viewBinding.mainModeHard, R.drawable.main_mode_hard)
    }

    override fun initListener() {
        super.initListener()
        //简单模式
        viewBinding.mainModeEasy.setOnClickListener {
            //播放点击音效
            SoundPlayManager.getInstance(baseContext).play(3)
            //查询简单模式的数据
            viewModel.selectLevelData(1){
                //跳转界面
                val intentEasy = Intent(this, LevelActivity::class.java)
                //加入数据
                val bundleEasy = Bundle()
                //加入关卡模式数据
                bundleEasy.putString("mode", "简单")
                //加入关卡数据
                bundleEasy.putParcelableArrayList("levels", it as ArrayList<out Parcelable>)
                intentEasy.putExtras(bundleEasy)
                //跳转
                startActivity(intentEasy)
            }

        }
        //普通模式
        viewBinding.mainModeNormal.setOnClickListener {
            //播放点击音效
            SoundPlayManager.getInstance(baseContext).play(3)
            //查询普通模式的数据
            viewModel.selectLevelData(2){
                //跳转界面
                val intentNormal = Intent(this, LevelActivity::class.java)
                //加入数据
                val bundleNormal = Bundle()
                //加入关卡模式数据
                bundleNormal.putString("mode", "普通")
                //加入关卡数据
                bundleNormal.putParcelableArrayList("levels", it as ArrayList<out Parcelable>)
                intentNormal.putExtras(bundleNormal)
                //跳转
                startActivity(intentNormal)
            }
        }
        //困难模式
        viewBinding.mainModeHard.setOnClickListener {
            //播放点击音效
            SoundPlayManager.getInstance(baseContext).play(3)
            //查询困难模式的数据
            viewModel.selectLevelData(3){
                //跳转界面
                val intentNormal = Intent(this, LevelActivity::class.java)
                //加入数据
                val bundleNormal = Bundle()
                //加入关卡模式数据
                bundleNormal.putString("mode", "困难")
                //加入关卡数据
                bundleNormal.putParcelableArrayList("levels", it as ArrayList<out Parcelable>)
                intentNormal.putExtras(bundleNormal)
                //跳转
                startActivity(intentNormal)
            }
        }
        //设置
        viewBinding.mainSetting.setOnClickListener {
            //播放点击音效
            SoundPlayManager.getInstance(baseContext).play(3)
            //fragment事务
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            //添加一个fragment
            val setting = SettingFragment()
            transaction.replace(R.id.root_main, setting, "setting")
            transaction.commit()
        }
        //帮助
        viewBinding.mainHelp.setOnClickListener {
            //播放点击音效
            SoundPlayManager.getInstance(baseContext).play(3)
            //fragment事务
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            //添加一个fragment
            val help = HelpFragment()
            transaction.replace(R.id.root_main, help, "help")
            transaction.commit()
        }
        //商店
        viewBinding.mainStore.setOnClickListener {
            //播放点击音效
            SoundPlayManager.getInstance(baseContext).play(3)
            //fragment事务
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            Log.d(Constant.TAG, "商店按钮")
            //添加一个fragment
            val store = StoreFragment()
            transaction.replace(R.id.root_main, store, "store")
            transaction.commit()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mBroadcastReceiver)
    }

    /**
     * 用给定资源设置指定按钮的drawableLeft
     */
    private fun setDrawableLeft(btn: Button, resId: Int) {
        //获取指定的drawable
        val drawable = resources.getDrawable(resId)
        //设置其drawable的左上右下
        drawable.setBounds(
            PxUtil.dpToPx(20, this),
            PxUtil.dpToPx(2, this),
            PxUtil.dpToPx(60, this),
            PxUtil.dpToPx(42, this)
        )
        //设置放在控件的左上右下
        btn.setCompoundDrawables(drawable, null, null, null)
    }

    /**
     * 播放背景音乐
     */
    private fun playMusic() {
        //判断是否正在播放
        if (!BgmManager.getInstance(this).isBackgroundMusicPlaying()) {
            //播放
            BgmManager.getInstance(this).playBackgroundMusic(
                R.raw.bg_music,
                true
            )
        }
    }

}