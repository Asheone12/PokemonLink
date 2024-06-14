package com.muen.gamelink.ui.game.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.muen.gamelink.databinding.FragmentPauseBinding
import com.muen.gamelink.game.constant.mode.LevelState
import com.muen.gamelink.game.manager.LinkManager
import com.muen.gamelink.music.SoundPlayManager
import com.muen.gamelink.ui.BaseFragment
import com.muen.gamelink.ui.game.LinkActivity
import com.muen.gamelink.ui.game.fragment.vm.PauseVM
import com.muen.gamelink.ui.level.LevelActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PauseFragment : BaseFragment<FragmentPauseBinding>() {
    private val viewModel by viewModels<PauseVM>()

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
        viewModel.level = requireArguments().getParcelable("level")!!
    }

    override fun initListener() {
        super.initListener()
        viewBinding.btnMenu.setOnClickListener {
            SoundPlayManager.getInstance(requireContext()).play(3)
            viewModel.selectLevelsByMode(viewModel.level.levelMode){
                //跳转界面
                val intent = Intent(activity, LevelActivity::class.java)
                //加入数据
                val bundle = Bundle()
                //加入关卡模式数据
                bundle.putString("mode", "简单")
                //加入关卡数据
                bundle.putParcelableArrayList("levels", it as ArrayList<out Parcelable?>)
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
            viewModel.selectLevelById(viewModel.level.id + 1){
                //判断是否开启
                if(it.levelState != LevelState.LEVEL_STATE_NO.value){
                    //跳转界面
                    val intent = Intent(activity, LinkActivity::class.java)
                    //加入数据
                    val bundle = Bundle()
                    bundle.putParcelable("level", it)
                    intent.putExtras(bundle)
                    //跳转
                    startActivity(intent)
                }else{
                    Toast.makeText(activity, "下一关还没有开启", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

}