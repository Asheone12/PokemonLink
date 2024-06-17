package com.muen.gamelink.game.manager

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import com.muen.gamelink.R
import com.muen.gamelink.game.constant.Constant
import com.muen.gamelink.game.constant.LinkConstant
import com.muen.gamelink.game.entity.AnimalPoint
import com.muen.gamelink.game.util.LinkUtil
import com.muen.gamelink.game.view.AnimalView
import com.muen.gamelink.music.BgmManager
import com.muen.gamelink.music.SoundPlayManager
import com.muen.gamelink.source.local.entity.TLevel
import com.muen.gamelink.ui.activity.FailureActivity
import com.muen.gamelink.ui.activity.SuccessActivity
import com.muen.gamelink.util.PxUtil
import tyrantgit.explosionfield.ExplosionField
import java.util.Timer
import java.util.TimerTask

/**
 * 使用单例模式
 * 游戏管理者，管理关于游戏的一切
 */
object LinkManager {

    //掌管AnimalView分布规则（布局）
    private var board: Array<IntArray> = arrayOf()

    //掌管游戏时间
    private var timer: Timer? = null
    private var time: Float = LinkConstant.TIME

    //水平方向偏移间距
    private var padding_hor = 0

    //竖直方向偏移间距
    private var padding_ver = 0

    //存储所有AnimalView
    private var animals: ArrayList<AnimalView> = ArrayList()

    //保存上一个触摸的AnimalView
    private var lastAnimal: AnimalView? = null

    //AnimalView的大小
    private var animal_size = 0

    //是否暂停
    private var isPause = false

    //监听者
    private var listener: LinkGame? = null

    @SuppressLint("HandlerLeak")
    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            //判断消息来源
            if (msg.what == Constant.TIMER) {
                //时间减少
                time -= 0.1f

                //设置文本
                listener!!.onTimeChanged(time)
            }
        }
    }

    /**
     * 开始游戏
     * @param context
     * @param layout
     * @param width
     * @param level_id
     * @param level_mode
     */
    fun startGame(
        context: Context,
        layout: RelativeLayout,
        width: Int,
        height: Int,
        levelId: Int,
        levelMode: Int
    ) {
        //清楚上一次游戏的痕迹
        clearLastGame()

        //产生二维数组布局模板
        setBoard(LinkUtil.loadLevelWithIdAndMode(levelId, levelMode))

        //界面布局
        addAnimalViewToLayout(context, layout, width, height)

        //开启定时器
        startTimer(time)

        //判断需不需要提示有岩石存在
        hintRock(context)
    }

    //判断需不需要岩石
    private fun hintRock(context: Context) {
        var flag = 0
        for (i in getBoard().indices) {
            for (j in getBoard()[0].indices) {
                if (getBoard()[i][j] < 0) {
                    //加载布局
                    val inflate = LayoutInflater.from(context)
                        .inflate(R.layout.toast_layout, null) as LinearLayout
                    //创建Toast
                    val toast = Toast(context)
                    //设置位置
                    toast.setGravity(Gravity.FILL_VERTICAL or Gravity.FILL_HORIZONTAL, 0, 0)
                    //设置持续时间
                    toast.duration = Toast.LENGTH_SHORT
                    //添加
                    toast.setView(inflate)
                    //显示
                    toast.show()
                    flag = 1
                    break
                }
            }
            if (flag == 1) {
                break
            }
        }
    }

    //清楚上一次游戏的痕迹
    private fun clearLastGame() {
        board = emptyArray()
        time = LinkConstant.TIME
        padding_hor = 0
        padding_ver = 0
        animals.clear()
        lastAnimal = null
        animal_size = 0
        isPause = false
    }

    //在给定的布局上添加AnimalView
    private fun addAnimalViewToLayout(
        context: Context,
        layout: RelativeLayout,
        width: Int,
        height: Int
    ) {
        //随机加载AnimalView的显示图片
        val resources: List<Int> = LinkUtil.loadPictureResourceWithBoard(getBoard())

        //横竖方向的个数
        val row_animal_num = getBoard().size
        val col_animal_num = getBoard()[0].size

        //根据数量动态设置AnimalView的大小
        Log.d(Constant.TAG, "行数：$row_animal_num 列数：$col_animal_num")

        //循环找到适合的大小
        for (size in LinkConstant.ANIMAL_SIZE downTo 10) {

            //如果宽度高度都满足条件
            if (size * col_animal_num < PxUtil.pxToDp(width, context) &&
                size * row_animal_num < PxUtil.pxToDp(height, context)
            ) {
                animal_size = size
                break
            }
        }

        //计算两边的间距
        padding_hor = (width - col_animal_num * PxUtil.dpToPx(animal_size, context)) / 2
        padding_ver = (height - row_animal_num * PxUtil.dpToPx(animal_size, context)) / 2
        Log.d(Constant.TAG, "width：" + PxUtil.pxToDp(width, context))
        Log.d(Constant.TAG, "height：" + PxUtil.pxToDp(height, context))
        Log.d(Constant.TAG, "width-sum：" + col_animal_num * animal_size)
        Log.d(Constant.TAG, "height-sum：" + row_animal_num * animal_size)
        Log.d(Constant.TAG, "水平间距：" + PxUtil.pxToDp(padding_hor, context))
        Log.d(Constant.TAG, "垂直间距：" + PxUtil.pxToDp(padding_ver, context))

        //依次添加到布局中
        for (i in 0 until row_animal_num) {
            for (j in 0 until col_animal_num) {
                //判断当前位置是否需要显示内容
                var animal: AnimalView
                if (getBoard()[i][j] == 0) {
                    animal = AnimalView(context)
                    animal.initAnimal(getBoard()[i][j], AnimalPoint(i, j))
                    animal.visibility = View.INVISIBLE
                } else {
                    //创建一个AnimalView
                    animal = AnimalView(context)
                    animal.initAnimal(
                        if (getBoard()[i][j] > 0) LinkConstant.ANIMAL_RESOURCE[resources[getBoard()[i][j] - 1]] else LinkConstant.ANIMAL_WOOD,
                        getBoard()[i][j],
                        AnimalPoint(i, j)
                    )
                }

                //创建布局约束
                val layoutParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                    PxUtil.dpToPx(animal_size, context),
                    PxUtil.dpToPx(animal_size, context)
                )

                //左上间距
                layoutParams.leftMargin = padding_hor + PxUtil.dpToPx(animal_size, context) * j
                layoutParams.topMargin = padding_ver + PxUtil.dpToPx(animal_size, context) * i

                //设置内间距
                animal.setPadding(
                    PxUtil.dpToPx(LinkConstant.ANIMAL_PADDING, context),
                    PxUtil.dpToPx(LinkConstant.ANIMAL_PADDING, context),
                    PxUtil.dpToPx(LinkConstant.ANIMAL_PADDING, context),
                    PxUtil.dpToPx(LinkConstant.ANIMAL_PADDING, context)
                )

                //添加视图
                layout.addView(animal, layoutParams)

                //保存该视图
                if (animal.getFlag() != 0) {
                    animals.add(animal)
                }
            }
        }
    }

    //开启定时器
    private fun startTimer(time: Float) {
        //取消之前的定时器
        if (timer != null) {
            stopTimer()
        }

        //以游戏时间开启定时器
        timer = Timer()

        //启动定时器每隔一秒，发送一次消息
        timer?.schedule(object : TimerTask() {
            override fun run() {
                handler.sendEmptyMessage(Constant.TIMER)
            }
        }, 0, 100)
    }

    //关闭定时器
    private fun stopTimer() {
        if (timer != null) {
            timer?.cancel()
            timer = null
        }
    }

    /**
     * 拳头道具的功能实现
     */
    fun fightGame(link_activity: Activity?) {
        //1.产生一对消除的点
        val doubleRemove: Array<AnimalPoint?> = LinkUtil.getDoubleRemove()
        Log.d(Constant.TAG, "第一个点：" + doubleRemove[0]?.x + " " + doubleRemove[0]?.y)
        Log.d(Constant.TAG, "第二个点：" + doubleRemove[1]?.x + " " + doubleRemove[1]?.y)

        //2.board修改
        board[doubleRemove[0]!!.x][doubleRemove[0]!!.y] = 0
        board[doubleRemove[1]!!.x][doubleRemove[1]!!.y] = 0

        //3.播放消除音效以及粉碎
        //SoundPlayManager.getInstance(mContext).play(4)
        //粉碎、
        val explosionField: ExplosionField = ExplosionField.attach2Window(link_activity)

        //4.AnimalView隐藏
        for (animal in animals) {
            if ((animal.getPoint()?.x === doubleRemove[0]?.x
                        && animal.getPoint()?.y === doubleRemove[0]?.y)
                || animal.getPoint()?.x === doubleRemove[1]?.x
                && animal.getPoint()?.y === doubleRemove[1]?.y
            ) {
                //恢复背景颜色和清除动画
                if (animal.animation != null) {
                    animal.changeAnimalBackground(LinkConstant.ANIMAL_BG)
                    animal.clearAnimation()
                }

                //粉碎
                explosionField.explode(animal)

                //隐藏
                animal.visibility = View.INVISIBLE
            }
        }
    }

    /**
     * 炸弹道具的功能实现
     */
    fun bombGame(link_activity: Activity?) {
        //1.随机产生一个待消除的
        val random: Int = LinkUtil.getExistAnimal()
        Log.d(Constant.TAG, "消除$random")

        //2.board修改
        for (i in board.indices) {
            for (j in board[0].indices) {
                if (board[i][j] == random) {
                    board[i][j] = 0
                }
            }
        }

        //3.播放消除音效以及粉碎
        //SoundPlayManager.getInstance(mContext!!).play(4)
        //粉碎、
        val explosionField: ExplosionField = ExplosionField.attach2Window(link_activity)

        //4.AnimalView隐藏
        for (animal in animals) {
            if (animal.getFlag() == random) {
                //恢复背景颜色和清除动画
                if (animal.animation != null) {
                    animal.changeAnimalBackground(LinkConstant.ANIMAL_BG)
                    animal.clearAnimation()
                }

                //粉碎
                explosionField.explode(animal)

                //隐藏
                animal.visibility = View.INVISIBLE
            }
        }
    }

    /**
     * 刷新道具的功能实现
     * @param context
     * @param layout
     * @param width
     * @param level_id
     * @param level_mode
     */
    fun refreshGame(
        context: Context,
        layout: RelativeLayout,
        width: Int,
        height: Int,
        level_id: Int,
        level_mode: Int,
        link_activity: Activity?
    ) {
        //0.播放消除音效以及粉碎
        //SoundPlayManager.getInstance(mContext!!).play(4)
        //粉碎、
        val explosionField: ExplosionField = ExplosionField.attach2Window(link_activity)

        //1.所以的AnimalView消失
        for (animal in animals) {
            //恢复背景颜色和清除动画
            if (animal.animation != null) {
                animal.changeAnimalBackground(LinkConstant.ANIMAL_BG)
                animal.clearAnimation()
            }

            //粉碎
            explosionField.explode(animal)

            //隐藏
            animal.visibility = View.INVISIBLE
        }
        Handler().postDelayed({ //2.移除所有的子视图
            layout.removeAllViews()

            //3.重新开始游戏
            startGame(context, layout, width, height, level_id, level_mode)
        }, 1500)
    }

    /**
     * 暂停游戏
     */
    fun pauseGame() {
        //判断是打开还是关闭
        if (!isPause) {
            stopTimer()
            //切换状态
            isPause = !isPause
        }
    }

    fun replayGame(){
        //判断是打开还是关闭
        if (isPause) {
            startTimer(time)
            //切换状态
            isPause = !isPause
        }
    }

    /**
     * 结束游戏
     * @param context
     * @param level
     * @param time
     */
    fun endGame(context: Context, level: TLevel?, time: Float) {
        if (time < 0.1) {
            Log.d(Constant.TAG, "失败啦")

            //界面跳转
            val intent = Intent(context, FailureActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("level", level)
            intent.putExtras(bundle)
            context.startActivity(intent)

            //暂停背景音乐
            BgmManager.getInstance(context).pauseBackgroundMusic()
            //播放失败音效
            SoundPlayManager.getInstance(context).play(2)

            //继续播放
            Handler().postDelayed({
                BgmManager.getInstance(context).resumeBackgroundMusic()
            }, 5000)
        } else {
            Log.d(Constant.TAG, "成功啦")
            val intent = Intent(context, SuccessActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("level", level)
            bundle.putInt("serial_click", LinkUtil.getSerialClick())
            intent.putExtras(bundle)
            context.startActivity(intent)

            //暂停背景音乐
            BgmManager.getInstance(context).pauseBackgroundMusic()
            //播放成功音效
            SoundPlayManager.getInstance(context).play(1)
            Handler().postDelayed({
                BgmManager.getInstance(context).resumeBackgroundMusic()
            }, 5000)
        }

        //自定义 从右向左滑动的效果
        //((Activity)context).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        // 自定义的淡入淡出动画效果
        (context as Activity).overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

        //清楚上一场游戏
        clearLastGame()
    }

    //接口
    interface LinkGame {
        //时间改变了
        fun onTimeChanged(time: Float)
    }

    //setter，getter方法
    fun getBoard(): Array<IntArray> {
        return board
    }

    fun setBoard(board: Array<IntArray>) {
        this.board = board
    }

    fun getTime(): Float {
        return time.toFloat()
    }

    fun setTime(time: Float) {
        this.time = time
    }

    fun getAnimals(): List<AnimalView> {
        return animals
    }

    fun setAnimals(animals: ArrayList<AnimalView>) {
        this.animals = animals
    }

    fun getLastAnimal(): AnimalView? {
        return lastAnimal
    }

    fun setLastAnimal(lastAnimal: AnimalView?) {
        this.lastAnimal = lastAnimal
    }

    fun getPaddingHor(): Int {
        return padding_hor
    }

    fun setPaddingHor(padding_hor: Int) {
        this.padding_hor = padding_hor
    }

    fun getPaddingVer(): Int {
        return padding_ver
    }

    fun setPadding_ver(padding_ver: Int) {
        this.padding_ver = padding_ver
    }

    fun getAnimalSize(): Int {
        return animal_size
    }

    fun setAnimal_size(animal_size: Int) {
        this.animal_size = animal_size
    }

    fun getListener(): LinkGame? {
        return listener
    }

    fun setListener(listener: LinkGame) {
        this.listener = listener
    }

    fun isPause(): Boolean {
        return isPause
    }

    fun setPause(pause: Boolean) {
        isPause = pause
    }
}