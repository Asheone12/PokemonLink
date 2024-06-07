package com.muen.gamelink.ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Color
import android.graphics.RectF
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.BounceInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.widget.RelativeLayout
import android.widget.Toast
import com.muen.gamelink.R
import com.muen.gamelink.databinding.ActivityLinkBinding
import com.muen.gamelink.game.constant.Constant
import com.muen.gamelink.game.constant.LinkConstant
import com.muen.gamelink.game.constant.mode.ItemMode
import com.muen.gamelink.game.entity.LinkInfo
import com.muen.gamelink.game.manager.LinkManager
import com.muen.gamelink.game.util.AnimalSearchUtil.canMatchTwoAnimalWithTwoBreak
import com.muen.gamelink.game.util.LinkUtil
import com.muen.gamelink.game.view.AnimalView
import com.muen.gamelink.model.Item
import com.muen.gamelink.model.Level
import com.muen.gamelink.model.User
import com.muen.gamelink.music.SoundPlayManager
import com.muen.gamelink.ui.fragment.PauseFragment
import com.muen.gamelink.util.PxUtil
import com.muen.gamelink.util.ScreenUtil
import org.litepal.LitePal
import tyrantgit.explosionfield.ExplosionField
import java.lang.String
import kotlin.Float
import kotlin.Int
import kotlin.arrayOf
import kotlin.math.atan
import kotlin.math.sqrt


class LinkActivity : BaseActivity<ActivityLinkBinding>(), LinkManager.LinkGame {
    //屏幕宽度,高度
    private var screenWidth = 0
    private var screenHeight = 0

    //信息布局的bottom
    private var messageBottom = 0

    //当前关卡模型数据
    private lateinit var level: Level

    //用户
    private lateinit var user: User

    //道具
    private lateinit var items: List<Item>

    //存储点的信息集合
    private lateinit var linkInfo: LinkInfo

    //记录金币的变量
    private var money = 0

    //记录拳头道具的数量
    private var fightNum = 0

    //记录炸弹道具的数量
    private var bombNum = 0

    //记录刷新道具的数量
    private var refreshNum = 0

    //粉碎视图
    private var explosionField: ExplosionField? = null

    override fun onCreateViewBinding(): ActivityLinkBinding {
        return ActivityLinkBinding.inflate(layoutInflater)
    }

    override fun initData() {
        super.initData()
        //获取数据
        val intent = this.intent
        val bundle = intent.extras!!
        level = bundle.getParcelable("level")!!

        Log.d(Constant.TAG, "--------")
        Log.d(Constant.TAG, String.valueOf(level))

        //查询用户数据
        val users: List<User> = LitePal.findAll(User::class.java)
        user = users[0]
        money = user.getUserMoney()

        //查询道具数据
        items = LitePal.findAll(Item::class.java)
        for (item in items) {
            if (item.getItemType() == ItemMode.ITEM_FIGHT.value) {
                //拳头道具
                fightNum = item.getItemNumber()
                Log.d(Constant.TAG, "查询的消除道具数量：$fightNum")
            } else if (item.getItemType() == ItemMode.ITEM_BOMB.value) {
                //炸弹道具
                bombNum = item.getItemNumber()
                Log.d(Constant.TAG, "查询的炸弹道具数量：$bombNum")
            } else {
                //刷新道具
                refreshNum = item.getItemNumber()
                Log.d(Constant.TAG, "查询的刷新道具数量：$refreshNum")
            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        super.initView()
        screenWidth = ScreenUtil.getScreenWidth(applicationContext)
        screenHeight = ScreenUtil.getScreenHeight(applicationContext)

        linkInfo = LinkInfo()
        //创建分碎视图的类
        explosionField = ExplosionField.attach2Window(this)

        viewBinding.messageShow.setPadding(
            0,
            ScreenUtil.getStateBarHeight(this) + PxUtil.dpToPx(5, this),
            0,
            0
        )

        //设置当前进度
        viewBinding.timeShow.progress = LinkConstant.TIME

        //设置位置
        val layoutParams = RelativeLayout.LayoutParams(
            PxUtil.dpToPx(120, this),
            PxUtil.dpToPx(120, this)
        )
        layoutParams.setMargins(
            PxUtil.dpToPx(-50, this),
            ScreenUtil.getStateBarHeight(this) - PxUtil.dpToPx(20, this),
            0, 0
        )

        //设置角度
        val angle1 = Math.toDegrees(atan(sqrt(44.0) / 10)).toInt()
        val angle2 = Math.toDegrees(atan(sqrt(95.0) / 10)).toInt()
        viewBinding.timeShow.startAngle = 270 + angle1
        viewBinding.timeShow.endAngle = 540 - angle2

        //设置进度颜色以及当前进度值以及总的进度值
        viewBinding.timeShow.progress = 90f
        viewBinding.timeShow.total_progress = 90f
        viewBinding.timeShow.progress_paint.color = Color.parseColor("#c2c2c2")

        //设置游戏主题内容布局
        viewBinding.timeShow.layoutParams = layoutParams
        viewBinding.timeShow.post {
            kotlin.run {
                messageBottom = viewBinding.timeShow.bottom

                val paramsLinkLayout = viewBinding.linkLayout.layoutParams
                paramsLinkLayout.height = screenHeight - messageBottom
                viewBinding.linkLayout.layoutParams = paramsLinkLayout

                viewBinding.linkLayout.setOnTouchListener { _, event -> //获取触摸点相对于布局的坐标
                    val x = event.x.toInt()
                    val y = event.y.toInt()
                    //触摸事件
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        for (animal in LinkManager.getAnimals()) {
                            //获取AnimalView实例的rect
                            val rectF = RectF(
                                animal.left.toFloat(),
                                animal.top.toFloat(),
                                animal.right.toFloat(),
                                animal.bottom.toFloat()
                            )

                            //判断是否包含
                            if (rectF.contains(
                                    x.toFloat(),
                                    y.toFloat()
                                ) && animal.visibility == View.VISIBLE
                            ) {
                                //获取上一次触摸的AnimalView
                                val lastAnimal = LinkManager.getLastAnimal()
                                //如果触摸的是石头直接结束
                                if (animal.getFlag() == -1) {
                                    //播放无法点击音效
                                    SoundPlayManager.getInstance(baseContext).play(5)
                                    break
                                }
                                //如果不是第一次触摸 且 触摸的不是同一个点
                                if (lastAnimal != null && lastAnimal != animal) {
                                    Log.d(
                                        Constant.TAG,
                                        "$lastAnimal $animal"
                                    )
                                    //如果两者的图片相同，且两者可以连接
                                    if (animal.getFlag() == lastAnimal.getFlag() &&
                                        canMatchTwoAnimalWithTwoBreak(
                                            LinkManager.getBoard(),
                                            lastAnimal.getPoint()!!,
                                            animal.getPoint()!!,
                                            linkInfo
                                        )
                                    ) {
                                        //播放无法点击音效
                                        SoundPlayManager.getInstance(baseContext).play(3)
                                        //当前点改变背景和动画
                                        animal.changeAnimalBackground(LinkConstant.ANIMAL_SELECT_BG)
                                        animationOnSelectAnimal(animal)
                                        //画线
                                        viewBinding.linkLayout.setLinkInfo(linkInfo)
                                        //设置所有的宝可梦不可以点击
                                        viewBinding.linkLayout.isEnabled = false
                                        //延迟操作
                                        Handler().postDelayed({ //播放消除音效
                                            SoundPlayManager.getInstance(baseContext).play(4)
                                            //修改模板
                                            LinkManager.getBoard()[lastAnimal.getPoint()!!.x][lastAnimal.getPoint()!!.y] =
                                                0
                                            LinkManager.getBoard()[animal.getPoint()!!.x][animal.getPoint()!!.y] =
                                                0
                                            //粉碎
                                            explosionField?.explode(lastAnimal)
                                            explosionField?.explode(animal)
                                            //隐藏
                                            lastAnimal.visibility = View.INVISIBLE
                                            lastAnimal.clearAnimation()
                                            animal.visibility = View.INVISIBLE
                                            animal.clearAnimation()
                                            //上一个点置空
                                            LinkManager.setLastAnimal(null)
                                            //去线
                                            viewBinding.linkLayout.setLinkInfo(null)
                                            //获得金币
                                            money += 2
                                            viewBinding.linkMoneyText.text = money.toString()
                                            //设置所有的宝可梦可以点击
                                            viewBinding.linkLayout.isEnabled = true
                                        }, 500)

                                    } else {
                                        //点击的两个图片不可以相连接
                                        //播放点击音效
                                        SoundPlayManager.getInstance(baseContext).play(3)

                                        //上一个点恢复原样
                                        lastAnimal.changeAnimalBackground(LinkConstant.ANIMAL_BG)
                                        if (lastAnimal.animation != null) {
                                            //清楚所有动画
                                            lastAnimal.clearAnimation()
                                        }

                                        //设置当前点的背景颜色和动画
                                        animal.changeAnimalBackground(LinkConstant.ANIMAL_SELECT_BG)
                                        animationOnSelectAnimal(animal)

                                        //将当前点作为选中点
                                        LinkManager.setLastAnimal(animal)
                                    }
                                } else if (lastAnimal == null) {
                                    //播放点击音效
                                    SoundPlayManager.getInstance(baseContext).play(3)

                                    //第一次触摸 当前点改变背景和动画
                                    animal.changeAnimalBackground(LinkConstant.ANIMAL_SELECT_BG)
                                    animationOnSelectAnimal(animal)

                                    //将当前点作为选中点
                                    LinkManager.setLastAnimal(animal)
                                }
                                break
                            }
                        }
                    }
                    true
                }

                //开始游戏
                LinkManager.startGame(
                    applicationContext,
                    viewBinding.linkLayout,
                    screenWidth,
                    screenHeight - messageBottom - ScreenUtil.getNavigationBarHeight(
                        applicationContext
                    ),
                    level.getLevelId(),
                    level.getLevelMode()
                )

                //设置监听者
                LinkManager.setListener(this@LinkActivity)
            }
        }

        //手动调整道具的排列
        val tempProp = arrayOf(
            viewBinding.itemFight,
            viewBinding.itemBomb,
            viewBinding.itemRefresh,
            viewBinding.linkPause
        )
        viewBinding.linkItems.post {

            //控制道具的大小
            val propSize = PxUtil.dpToPx(55, baseContext)

            //计算间距
            val padding: Int = (viewBinding.linkItems.width - propSize * 4) / 5

            //依次设置位置
            for (i in tempProp.indices) {
                //设置约束
                val rlLayoutParams = RelativeLayout.LayoutParams(
                    propSize,
                    propSize
                )
                rlLayoutParams.setMargins(
                    padding + (padding + propSize) * i,
                    0,
                    padding + (padding + propSize) * i + propSize,
                    0
                )
                //重新设置给道具视图
                tempProp[i].layoutParams = rlLayoutParams
            }
        }
        //设置金币
        viewBinding.linkMoneyText.text = money.toString()
        //设置道具数量
        viewBinding.itemFight.count = fightNum
        viewBinding.itemBomb.count = bombNum
        viewBinding.itemRefresh.count = refreshNum

    }

    override fun initListener() {
        super.initListener()
        viewBinding.itemFight.setOnClickListener {
            Log.d(Constant.TAG, "拳头道具")

            if (fightNum > 0) {
                //随机消除一对可以消除的AnimalView
                LinkManager.fightGame(this@LinkActivity)

                //数量减1
                fightNum--
                viewBinding.itemFight.count = fightNum

                //数据库处理
                val values = ContentValues()
                values.put("itemNumber", fightNum)
                LitePal.update(Item::class.java, values, 1)
            } else {
                Toast.makeText(this, "道具已经用完", Toast.LENGTH_SHORT).show()
            }
        }

        viewBinding.itemBomb.setOnClickListener {
            Log.d(Constant.TAG, "炸弹道具")

            if (bombNum > 0) {
                //随机消除某一种所有的AnimalView
                LinkManager.bombGame(this@LinkActivity)

                //数量减1
                bombNum--
                viewBinding.itemBomb.count = bombNum
                Log.d(Constant.TAG, "数量：$bombNum")

                //数据库处理
                val values = ContentValues()
                values.put("itemNumber", bombNum)
                LitePal.update(Item::class.java, values, 2)
            } else {
                Toast.makeText(this, "道具已经用完", Toast.LENGTH_SHORT).show()
            }
        }

        viewBinding.itemRefresh.setOnClickListener {
            Log.d(Constant.TAG, "刷新道具")

            if (refreshNum > 0) {
                //刷新游戏
                LinkManager.refreshGame(
                    applicationContext,
                    viewBinding.linkLayout,
                    screenWidth,
                    screenHeight - messageBottom - ScreenUtil.getNavigationBarHeight(
                        applicationContext
                    ),
                    level.getLevelId(),
                    level.getLevelMode(),
                    this@LinkActivity
                )

                //数量减1
                refreshNum--
                viewBinding.itemRefresh.count = refreshNum

                //数据库处理
                val values = ContentValues()
                values.put("itemNumber", refreshNum)
                LitePal.update(Item::class.java, values, 3)
            } else {
                Toast.makeText(this, "道具已经用完", Toast.LENGTH_SHORT).show()
            }
        }

        viewBinding.linkPause.setOnClickListener {
            Log.d(Constant.TAG, "暂停")
            //暂停游戏
            //1.定时器暂停
            LinkManager.pauseGame()
            //2.添加一个fragment
            val transaction = supportFragmentManager.beginTransaction()
            val pause = PauseFragment()
            val bundle = Bundle()
            bundle.putParcelable("level", level)
            pause.arguments = bundle
            transaction.replace(R.id.root_link, pause, "pause")
            transaction.commit()
        }
    }

    override fun onPause() {
        super.onPause()
        LinkManager.pauseGame()
    }

    override fun onResume() {
        super.onResume()
        //开启游戏
        if (LinkManager.isPause()) {
            LinkManager.pauseGame()
        }
    }

    override fun onTimeChanged(time: Float) {
        //如果时间小于0
        if (time <= 0.0) {
            LinkManager.pauseGame()
            LinkManager.endGame(this, level, time)
        } else {
            //保留小数后一位
            viewBinding.timeShow.progress = time
        }

        //如果board全部清除了
        if (LinkUtil.getBoardState()) {
            //结束游戏
            LinkManager.pauseGame()
            level.setLevelTime((LinkConstant.TIME - time))
            level.setLevelState(LinkUtil.getStarByTime(time.toInt()))
            LinkManager.endGame(this, level, time)

            //关卡结算
            level.update(level.getId().toLong())

            //下一关判断
            val nextLevel: Level =
                LitePal.find(Level::class.java, (level.getId() + 1).toLong())
            if (nextLevel.getLevelState() == '0') {
                nextLevel.setLevelState('4')
                nextLevel.update(level.getId() + 1L)
            }

            //金币道具清算
            user.setUserMoney(money)
            user.update(1)
        }
    }

    /**
     * 选中的AnimalView动画
     * @param animal
     */
    private fun animationOnSelectAnimal(animal: AnimalView) {
        //缩放动画
        val scaleAnimation = ScaleAnimation(
            1.0f, 1.05f,
            1.0f, 1.05f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        scaleAnimation.duration = 100
        scaleAnimation.repeatCount = 0
        scaleAnimation.fillAfter = true

        //旋转动画
        val rotateAnimation = RotateAnimation(
            -20f,
            20f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotateAnimation.duration = 500
        rotateAnimation.startOffset = 100
        rotateAnimation.repeatCount = Animation.INFINITE
        rotateAnimation.repeatMode = Animation.REVERSE
        rotateAnimation.interpolator = BounceInterpolator()

        //组合动画
        val animationSet = AnimationSet(true)
        animationSet.addAnimation(scaleAnimation)
        animationSet.addAnimation(rotateAnimation)

        //开启动画
        animal.startAnimation(animationSet)
        animationSet.startNow()
    }

}