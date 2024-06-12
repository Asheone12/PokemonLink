package com.muen.gamelink.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.muen.gamelink.databinding.FragmentPauseBinding
import com.muen.gamelink.game.constant.mode.LevelState
import com.muen.gamelink.game.manager.LinkManager
import com.muen.gamelink.music.SoundPlayManager
import com.muen.gamelink.source.local.dao.LevelDao
import com.muen.gamelink.source.local.db.GameDB
import com.muen.gamelink.source.local.entity.TLevel
import com.muen.gamelink.ui.LevelActivity
import com.muen.gamelink.ui.LinkActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PauseFragment : BaseFragment<FragmentPauseBinding>() {
    private lateinit var level: TLevel
    private lateinit var levelDao: LevelDao

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
        levelDao = GameDB.getDatabase(requireContext()).levelDao()
    }

    override fun initListener() {
        super.initListener()
        viewBinding.btnMenu.setOnClickListener {
            SoundPlayManager.getInstance(requireContext()).play(3)
            lifecycleScope.launch(Dispatchers.IO) {
                val levelList = levelDao.selectLevelByMode(level.levelMode)
                //跳转界面
                val intent = Intent(activity, LevelActivity::class.java)
                //加入数据
                val bundle = Bundle()
                //加入关卡模式数据
                bundle.putString("mode", "简单")
                //加入关卡数据
                bundle.putParcelableArrayList("levels", levelList as ArrayList<out Parcelable?>)
                intent.putExtras(bundle)
                startActivity(intent)
            }

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
            lifecycleScope.launch(Dispatchers.IO) {
                //下一关，加入关卡数据
                val nextLevel = levelDao.selectLevelById(level.id + 1)[0]
                //判断是否开启
                if(nextLevel.levelState != LevelState.LEVEL_STATE_NO.value){
                    //跳转界面
                    val intent = Intent(activity, LinkActivity::class.java)
                    //加入数据
                    val bundle = Bundle()
                    bundle.putParcelable("level", nextLevel)
                    intent.putExtras(bundle)
                    //跳转
                    startActivity(intent)
                }else{
                    //Toast.makeText(activity, "下一关还没有开启", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

}