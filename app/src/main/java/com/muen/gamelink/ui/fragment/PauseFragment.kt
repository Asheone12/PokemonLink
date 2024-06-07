package com.muen.gamelink.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.muen.gamelink.databinding.FragmentPauseBinding
import com.muen.gamelink.game.constant.Constant
import com.muen.gamelink.game.constant.mode.LevelState
import com.muen.gamelink.game.manager.LinkManager
import com.muen.gamelink.model.Level
import com.muen.gamelink.music.SoundPlayManager
import com.muen.gamelink.ui.LevelActivity
import com.muen.gamelink.ui.LinkActivity
import org.litepal.LitePal
import java.lang.String

class PauseFragment : BaseFragment<FragmentPauseBinding>() {
    private lateinit var level: Level

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentPauseBinding {
        return FragmentPauseBinding.inflate(inflater, container, false)
    }

    override fun initData() {
        super.initData()
        //接收数据
        level = requireArguments().getParcelable("level")!!
    }

    override fun initListener() {
        super.initListener()
        viewBinding.btnMenu.setOnClickListener {
            SoundPlayManager.getInstance(requireContext()).play(3)
            //查询对应模式的数据
            val levels: List<Level> =
                LitePal.where("l_mode == ?", String.valueOf(level.getLevelMode())).find(
                    Level::class.java
                )
            //依次查询每一个内容
            for (xlLevel in levels) {
                Log.d(Constant.TAG, xlLevel.toString())
            }
            //跳转界面
            val intent = Intent(activity, LevelActivity::class.java)
            //加入数据
            val bundle = Bundle()
            //加入关卡模式数据
            bundle.putString("mode", "简单")
            //加入关卡数据
            bundle.putParcelableArrayList("levels", levels as ArrayList<out Parcelable?>)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        viewBinding.btnRefresh.setOnClickListener {
            //播放点击音效
            SoundPlayManager.getInstance(requireContext()).play(3)
            //回到游戏
            LinkManager.pauseGame()
            //移除自己
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.remove(this@PauseFragment)
            transaction.commit()
        }

        viewBinding.btnNext.setOnClickListener {
            //播放点击音效
            SoundPlayManager.getInstance(requireContext()).play(3)
            //下一关，加入关卡数据
            val nextLevel: Level =
                LitePal.find(Level::class.java, (level.getId() + 1).toLong())
            //判断是否开启
            if (nextLevel.getLevelState() != LevelState.LEVEL_STATE_NO.value) {
                //跳转界面
                val intent = Intent(activity, LinkActivity::class.java)
                //加入数据
                val bundle = Bundle()
                bundle.putParcelable("level", nextLevel)
                intent.putExtras(bundle)
                //跳转
                startActivity(intent)
            } else {
                Toast.makeText(activity, "下一关还没有开启", Toast.LENGTH_SHORT).show()
            }
        }
    }

}