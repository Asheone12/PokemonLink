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
import com.muen.gamelink.R
import com.muen.gamelink.databinding.ActivityMainBinding
import com.muen.gamelink.game.constant.Constant
import com.muen.gamelink.model.Item
import com.muen.gamelink.model.Level
import com.muen.gamelink.model.User
import com.muen.gamelink.music.BgmManager
import com.muen.gamelink.music.SoundPlayManager
import com.muen.gamelink.ui.fragment.HelpFragment
import com.muen.gamelink.ui.fragment.SettingFragment
import com.muen.gamelink.ui.fragment.StoreFragment
import com.muen.gamelink.util.PxUtil
import org.litepal.LitePal

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private lateinit var mBroadcastReceiver: BroadcastReceiver

    override fun onCreateViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initData() {
        super.initData()
        //提前加载资源，不然的话，资源没有加载好，会没有声音
        SoundPlayManager.getInstance(this)

        //初始化数据库 LitePal
        LitePal.initialize(this)
        val db = LitePal.getDatabase()

        //向数据库装入数据
        initSQLite()

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
            //查询简单模式的数据
            val levels: List<Level> =
                LitePal.where("levelMode == ?", "1").find(Level::class.java)
            Log.d(Constant.TAG, levels.size.toString() + "")
            //依次查询每一个内容
            for (level in levels) {
                Log.d(Constant.TAG, level.toString())
            }
            //跳转界面
            val intentEasy = Intent(this, LevelActivity::class.java)
            //加入数据
            val bundleEasy = Bundle()
            //加入关卡模式数据
            bundleEasy.putString("mode", "简单")
            //加入关卡数据
            bundleEasy.putParcelableArrayList("levels", levels as ArrayList<out Parcelable>)
            intentEasy.putExtras(bundleEasy)
            //跳转
            startActivity(intentEasy)
        }

        viewBinding.mainModeNormal.setOnClickListener {
            Log.d(Constant.TAG, "普通模式按钮")
            //播放点击音效
            SoundPlayManager.getInstance(baseContext).play(3)
            //查询简单模式的数据
            val levels: List<Level> =
                LitePal.where("levelMode == ?", "2").find(Level::class.java)
            Log.d(Constant.TAG, levels.size.toString() + "")
            //依次查询每一个内容
            for (level in levels) {
                Log.d(Constant.TAG, level.toString())
            }
            //跳转界面
            val intentNormal = Intent(this, LevelActivity::class.java)
            //加入数据
            val bundleNormal = Bundle()
            //加入关卡模式数据
            bundleNormal.putString("mode", "简单")
            //加入关卡数据
            bundleNormal.putParcelableArrayList(
                "levels",
                levels as java.util.ArrayList<out Parcelable?>
            )
            intentNormal.putExtras(bundleNormal)
            //跳转
            startActivity(intentNormal)
        }

        viewBinding.mainModeHard.setOnClickListener {
            Log.d(Constant.TAG, "困难模式按钮")
            //播放点击音效
            SoundPlayManager.getInstance(baseContext).play(3)
            //查询简单模式的数据
            val levels: List<Level> =
                LitePal.where("levelMode == ?", "3").find(Level::class.java)
            Log.d(Constant.TAG, levels.size.toString() + "")
            //依次查询每一个内容
            for (level in levels) {
                Log.d(Constant.TAG, level.toString())
            }
            //跳转界面
            val intentHard = Intent(this, LevelActivity::class.java)
            //加入数据
            val bundleHard = Bundle()
            //加入关卡模式数据
            bundleHard.putString("mode", "简单")
            //加入关卡数据
            bundleHard.putParcelableArrayList(
                "levels",
                levels as java.util.ArrayList<out Parcelable?>
            )
            intentHard.putExtras(bundleHard)
            //跳转
            startActivity(intentHard)
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
     * 初始化数据库
     */
    private fun initSQLite() {
        //查找当前数据库的内容
        val users: List<User> = LitePal.findAll(User::class.java)
        val levels: List<Level> = LitePal.findAll(Level::class.java)
        val props: List<Item> = LitePal.findAll(Item::class.java)

        //如果用户数据为空，装入数据
        if (users.isEmpty()) {
            val user = User()
            user.setUserMoney(1000)
            user.setUserBackground(0)
            user.save()
        }

        //如果关卡数据为空，装入数据
        if (levels.isEmpty()) {
            //简单模式
            for (i in 1..40) {
                val level = Level()
                //设置关卡号
                level.setLevelId(i)
                //设置关卡模式
                level.setLevelMode('1')
                //设置关卡的闯关状态
                if (i == 1) {
                    level.setLevelState('4')
                } else {
                    level.setLevelState('0')
                }
                //设置关卡的闯关时间
                level.setLevelTime(0f)

                //插入
                level.save()
            }

            //普通模式
            for (i in 1..40) {
                val level = Level()
                //设置关卡号
                level.setLevelId(i)
                //设置关卡模式
                level.setLevelMode('2')
                //设置关卡的闯关状态
                if (i == 1) {
                    level.setLevelState('4')
                } else {
                    level.setLevelState('0')
                }
                //设置关卡的闯关时间
                level.setLevelTime(0f)

                //插入
                level.save()
            }

            //困难模式
            for (i in 1..40) {
                val level = Level()
                //设置关卡号
                level.setLevelId(i)
                //设置关卡模式
                level.setLevelMode('3')
                //设置关卡的闯关状态
                if (i == 1) {
                    level.setLevelState('4')
                } else {
                    level.setLevelState('0')
                }
                //设置关卡的闯关时间
                level.setLevelTime(0f)

                //插入
                level.save()
            }
        }

        //如果道具数据为空，装入数据
        if (props.isEmpty()) {
            //1.装入拳头道具
            val prop_fight = Item()
            prop_fight.setItemType('1')
            prop_fight.setItemNumber(9)
            prop_fight.setItemPrice(10)
            prop_fight.save()

            //2.装入炸弹道具
            val prop_bomb = Item()
            prop_bomb.setItemType('2')
            prop_bomb.setItemNumber(9)
            prop_bomb.setItemPrice(10)
            prop_bomb.save()

            //3.装入刷新道具
            val prop_refresh = Item()
            prop_refresh.setItemType('3')
            prop_refresh.setItemNumber(9)
            prop_refresh.setItemPrice(10)
            prop_refresh.save()
        }
    }


}