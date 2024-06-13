package com.muen.gamelink.ui.level

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.Toast
import com.muen.gamelink.R
import com.muen.gamelink.databinding.ActivityLevelBinding
import com.muen.gamelink.game.constant.Constant
import com.muen.gamelink.game.constant.mode.LevelState
import com.muen.gamelink.music.SoundPlayManager
import com.muen.gamelink.source.local.entity.TLevel
import com.muen.gamelink.ui.game.LinkActivity
import com.muen.gamelink.ui.main.MainActivity
import com.muen.gamelink.ui.BaseActivity
import com.muen.gamelink.util.PxUtil
import com.muen.gamelink.util.ScreenUtil
import com.muen.gamelink.widget.MyImageView

class LevelActivity : BaseActivity<ActivityLevelBinding>() {
    //屏幕宽度
    private var screenWidth = 0

    //记录屏幕当前的偏移程度
    var offset = 0

    //关卡模式数据
    private var mode: String? = null

    //关卡数据
    private var levels: List<TLevel>? = null

    //确定总页数
    private var pager = 0

    override fun onCreateViewBinding(): ActivityLevelBinding {
        return ActivityLevelBinding.inflate(layoutInflater)
    }

    override fun initData() {
        super.initData()
        //获取数据
        val intent = this.intent
        val bundle = intent.extras!!
        mode = bundle.getString("mode")
        levels = bundle.getParcelableArrayList("levels")
    }

    override fun initView() {
        super.initView()
        //屏幕的宽高
        screenWidth = ScreenUtil.getScreenWidth(applicationContext)
        Log.d(Constant.TAG, "屏幕宽度：$screenWidth")
        initLevel()
    }

    override fun initListener() {
        super.initListener()
        viewBinding.pagerBack.setOnClickListener {
            Log.d(Constant.TAG, "返回按钮")
            startActivity(Intent(this@LevelActivity, MainActivity::class.java))
            //自定义动画
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right)
        }

        viewBinding.pagerUp.setOnClickListener {
            Log.d(Constant.TAG, "上一页")
            //左滑
            scrollLevelsOfDirection(-1)
        }

        viewBinding.pagerDown.setOnClickListener {
            Log.d(Constant.TAG, "下一页")
            //右滑
            scrollLevelsOfDirection(1)
        }
    }

    /**
     * 加载关卡
     */
    private fun initLevel() {
        viewBinding.levelRoot.post {
            kotlin.run {
                //循环展示
                for (i in levels!!.indices) {
                    //确定页数
                    pager = i / Constant.LEVEL_PAGER_COUNT
                    viewBinding.pagerText.text = "1/" + (pager + 1)
                    //确定在当前页数的第几行
                    val pagerRow = i % Constant.LEVEL_PAGER_COUNT / Constant.LEVEL_ROW_COUNT
                    //确定在当前页数的第几列
                    val pagerCol = i % Constant.LEVEL_PAGER_COUNT % Constant.LEVEL_ROW_COUNT
                    //边距
                    val levelPadding: Int = (screenWidth - Constant.LEVEL_ROW_COUNT *
                            PxUtil.dpToPx(Constant.LEVEL_SIZE, applicationContext)) /
                            (Constant.LEVEL_ROW_COUNT + 1)

                    //创建视图
                    val myImageView = MyImageView(applicationContext)
                    myImageView.changeLevelState(LevelState.getState(levels!![i].levelState))

                    //设置id
                    myImageView.id = i

                    //布局参数
                    val layoutParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                        PxUtil.dpToPx(Constant.LEVEL_SIZE, applicationContext),
                        PxUtil.dpToPx(Constant.LEVEL_SIZE, applicationContext)
                    )

                    //添加约束
                    layoutParams.leftMargin =
                        screenWidth * pager + levelPadding + (levelPadding + PxUtil.dpToPx(
                            Constant.LEVEL_SIZE,
                            applicationContext
                        )) * pagerCol
                    layoutParams.topMargin = ScreenUtil.getStateBarHeight(applicationContext) +
                            PxUtil.dpToPx(Constant.LEVEL_TOP, applicationContext) +
                            levelPadding + (levelPadding + PxUtil.dpToPx(
                        Constant.LEVEL_SIZE,
                        applicationContext
                    )) * pagerRow

                    //最后一位需要添加右边距
                    if (i == levels!!.size - 1) {
                        layoutParams.rightMargin = levelPadding
                    }

                    //添加控件到容器中
                    viewBinding.levelRoot.addView(myImageView, layoutParams)

                    //点击事件
                    myImageView.setOnClickListener { v -> //播放点击音效
                        SoundPlayManager.getInstance(baseContext).play(3)
                        Log.d(Constant.TAG, "关卡${+ v.id},状态${levels!![v.id].levelState}" )

                        //判断是否可以进入该关卡
                        if (LevelState.getState(levels!![v.id].levelState) != LevelState.LEVEL_STATE_NO) {
                            jumpToLinkActivity(levels!![v.id])
                        } else {
                            Toast.makeText(
                                this@LevelActivity,
                                "当前关卡不可进入",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            }
        }

    }

    /**
     * 界面跳转
     */
    private fun jumpToLinkActivity(level: TLevel?) {
        //跳转界面
        val intent = Intent(this, LinkActivity::class.java)
        //加入数据
        val bundle = Bundle()
        //加入关卡数据
        bundle.putParcelable("level", level)
        intent.putExtras(bundle)
        //跳转
        startActivity(intent)
    }

    /**
     * 左右滑动一个屏幕关卡视图
     * @param direction 1:右滑 -1:左滑
     * @return 返回值表示是否滑动
     */
    fun scrollLevelsOfDirection(direction: Int): Boolean {
        if (direction == 1 && offset == pager * screenWidth || direction == -1 && offset == 0) {
            //如果当前向右滑动 且 当前已经处于最后一页 或
            //如果当前向左滑动 且 当前已经处于第一页
            return false
        } else if (direction == 1 && offset == (pager - 1) * screenWidth) {
            //如果当前向右滑动 且 滑动后处于最后一页
            //右边的按钮设置不可用
            viewBinding.pagerDown.isEnabled = false
            viewBinding.pagerDown.setBackgroundResource(R.drawable.level_page_down_enable)
        } else if (direction == -1 && offset == screenWidth) {
            //如果当前向左滑动 且 滑动后处于第一页
            //左边的按钮设置不可用
            viewBinding.pagerUp.isEnabled = false
            viewBinding.pagerUp.setBackgroundResource(R.drawable.level_page_up_enable)
        } else {
            //恢复
            viewBinding.pagerUp.isEnabled = true
            viewBinding.pagerUp.setBackgroundResource(R.drawable.level_page_up)
            viewBinding.pagerDown.setEnabled(true)
            viewBinding.pagerDown.setBackgroundResource(R.drawable.level_page_down)
        }

        //滑动视图
        viewBinding.levelPager.smoothScrollTo(offset + screenWidth * direction, 0)

        //修改偏移值
        offset += screenWidth * direction

        //修改显示内容
        viewBinding.pagerText.text = (offset / screenWidth + 1).toString() + "/" + (pager + 1)
        return true
    }

}