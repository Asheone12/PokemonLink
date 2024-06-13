package com.muen.gamelink.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.muen.gamelink.R
import com.muen.gamelink.databinding.ActivityMainBinding
import com.muen.gamelink.game.constant.Constant
import com.muen.gamelink.music.BgmManager
import com.muen.gamelink.music.SoundPlayManager
import com.muen.gamelink.source.local.dao.ItemDao
import com.muen.gamelink.source.local.dao.LevelDao
import com.muen.gamelink.source.local.dao.UserDao
import com.muen.gamelink.source.local.db.GameDB
import com.muen.gamelink.source.local.entity.TItem
import com.muen.gamelink.source.local.entity.TLevel
import com.muen.gamelink.source.local.entity.TUser
import com.muen.gamelink.ui.base.BaseActivity
import com.muen.gamelink.ui.fragment.HelpFragment
import com.muen.gamelink.ui.fragment.SettingFragment
import com.muen.gamelink.ui.fragment.StoreFragment
import com.muen.gamelink.util.PxUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private lateinit var mBroadcastReceiver: BroadcastReceiver

    //查找当前数据库的内容
    private lateinit var itemDao:ItemDao
    private lateinit var levelDao:LevelDao
    private lateinit var userDao:UserDao

    private lateinit var mContext: Context

    override fun onCreateViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initData() {
        super.initData()
        mContext = this

        //提前加载资源，不然的话，资源没有加载好，会没有声音
        SoundPlayManager.getInstance(this)

        //向数据库装入数据
        initRoom()

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

        viewBinding.mainModeEasy.setOnClickListener {
            Log.d(Constant.TAG, "简单模式按钮")
            //播放点击音效
            SoundPlayManager.getInstance(baseContext).play(3)
            lifecycleScope.launch(Dispatchers.IO) {
                levelDao.selectLevelByMode(1).collect{
                    val levelList = it
                    //切换到主线程
                    withContext(Dispatchers.Main){
                        //跳转界面
                        val intentEasy = Intent(mContext, LevelActivity::class.java)
                        //加入数据
                        val bundleEasy = Bundle()
                        //加入关卡模式数据
                        bundleEasy.putString("mode", "简单")
                        //加入关卡数据
                        bundleEasy.putParcelableArrayList("levels", levelList as ArrayList<out Parcelable>)
                        intentEasy.putExtras(bundleEasy)
                        //跳转
                        startActivity(intentEasy)
                    }
                }

            }

        }

        viewBinding.mainModeNormal.setOnClickListener {
            Log.d(Constant.TAG, "普通模式按钮")
            //播放点击音效
            SoundPlayManager.getInstance(baseContext).play(3)
            //查询普通模式的数据
            lifecycleScope.launch(Dispatchers.IO) {
                levelDao.selectLevelByMode(2).collect{
                    val levelList = it
                    //切换到主线程
                    withContext(Dispatchers.Main){
                        //跳转界面
                        val intentNormal = Intent(mContext, LevelActivity::class.java)
                        //加入数据
                        val bundleNormal = Bundle()
                        //加入关卡模式数据
                        bundleNormal.putString("mode", "普通")
                        //加入关卡数据
                        bundleNormal.putParcelableArrayList("levels", levelList as ArrayList<out Parcelable>)
                        intentNormal.putExtras(bundleNormal)
                        //跳转
                        startActivity(intentNormal)
                    }
                }

            }
        }

        viewBinding.mainModeHard.setOnClickListener {
            Log.d(Constant.TAG, "困难模式按钮")
            //播放点击音效
            SoundPlayManager.getInstance(baseContext).play(3)
            //查询困难模式的数据
            lifecycleScope.launch(Dispatchers.IO) {
                levelDao.selectLevelByMode(3).collect{
                    val levelList = it
                    //切换到主线程
                    withContext(Dispatchers.Main){
                        //跳转界面
                        val intentNormal = Intent(mContext, LevelActivity::class.java)
                        //加入数据
                        val bundleNormal = Bundle()
                        //加入关卡模式数据
                        bundleNormal.putString("mode", "困难")
                        //加入关卡数据
                        bundleNormal.putParcelableArrayList("levels", levelList as ArrayList<out Parcelable>)
                        intentNormal.putExtras(bundleNormal)
                        //跳转
                        startActivity(intentNormal)
                    }

                }

            }
        }

        viewBinding.mainSetting.setOnClickListener {
            //播放点击音效
            SoundPlayManager.getInstance(baseContext).play(3)
            //fragment事务
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            Log.d(Constant.TAG, "设置按钮")
            //添加一个fragment
            val setting = SettingFragment()
            transaction.replace(R.id.root_main, setting, "setting")
            transaction.commit()
        }

        viewBinding.mainHelp.setOnClickListener {
            //播放点击音效
            SoundPlayManager.getInstance(baseContext).play(3)
            //fragment事务
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            Log.d(Constant.TAG, "帮助按钮")
            //添加一个fragment
            val help = HelpFragment()
            transaction.replace(R.id.root_main, help, "help")
            transaction.commit()
        }

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

    /**
     * 初始化room
     */
    private fun initRoom() {
        itemDao = GameDB.getDatabase(this).itemDao()
        levelDao = GameDB.getDatabase(this).levelDao()
        userDao =  GameDB.getDatabase(this).userDao()
        //如果用户数据为空，装入数据
        lifecycleScope.launch(Dispatchers.IO) {
            userDao.loadUsers().collect{
                if(it.isEmpty()){
                    userDao.insertUser(TUser("1001", 1000, 0))
                }
            }
        }

        //如果关卡数据为空，装入数据
        lifecycleScope.launch(Dispatchers.IO) {
            levelDao.loadLevels().collect{
                if(it.isEmpty()){
                    //简单模式
                    for (i in 1..40) {
                        if (i == 1) {
                            levelDao.insertLevel(TLevel(i,0f,1,4))
                        } else {
                            levelDao.insertLevel(TLevel(i,0f,1,0))
                        }
                    }

                    //普通模式
                    for (i in 1..40) {
                        if (i == 1) {
                            levelDao.insertLevel(TLevel(i,0f,2,4))
                        } else {
                            levelDao.insertLevel(TLevel(i,0f,2,0))
                        }
                    }

                    //困难模式
                    for (i in 1..40) {
                        if (i == 1) {
                            levelDao.insertLevel(TLevel(i,0f,3,4))
                        } else {
                            levelDao.insertLevel(TLevel(i,0f,3,0))
                        }
                    }
                }
            }

        }

        //如果道具数据为空，装入数据
        lifecycleScope.launch(Dispatchers.IO) {
            itemDao.loadItems().collect{
                if(it.isEmpty()){
                    //1.装入拳头道具
                    itemDao.insertItem(TItem(1,9,10))
                    //2.装入炸弹道具
                    itemDao.insertItem(TItem(2,9,10))
                    //3.装入刷新道具
                    itemDao.insertItem(TItem(3,9,10))
                }
            }
        }

    }


}