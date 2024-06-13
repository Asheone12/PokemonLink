package com.muen.gamelink.ui.main.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.muen.gamelink.databinding.FragmentSettingBinding
import com.muen.gamelink.game.constant.Constant
import com.muen.gamelink.music.BgmManager
import com.muen.gamelink.music.SoundPlayManager
import com.muen.gamelink.ui.BaseFragment


class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        super.initView()
        //处理事件
        val progressMusic = BgmManager.getInstance(requireContext()).getBackgroundVolume() * 100
        viewBinding.seekBarMusic.progress = progressMusic.toInt()

        val progressEffect = SoundPlayManager.getInstance(requireContext()).getVoice() * 100
        viewBinding.seekBarEffect.progress = progressEffect.toInt()

    }

    override fun initListener() {
        super.initListener()
        viewBinding.seekBarMusic.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d(Constant.TAG, "bgm当前进度：$progress")
                BgmManager.getInstance(requireContext()).setBackgroundVolume((progress / 100.0).toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        viewBinding.seekBarEffect.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d(Constant.TAG, "effect当前进度：$progress")
                SoundPlayManager.getInstance(requireContext()).setVoice((progress / 100.0).toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        viewBinding.settingFinish.setOnClickListener {
            SoundPlayManager.getInstance(requireContext()).play(3)
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.remove(this@SettingFragment)
            transaction.commit()
        }

    }

}