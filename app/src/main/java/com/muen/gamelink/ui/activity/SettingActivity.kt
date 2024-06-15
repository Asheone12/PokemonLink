package com.muen.gamelink.ui.activity

import android.util.Log
import android.widget.SeekBar
import com.muen.gamelink.databinding.ActivitySettingBinding
import com.muen.gamelink.game.constant.Constant
import com.muen.gamelink.music.BgmManager
import com.muen.gamelink.music.SoundPlayManager
import com.muen.gamelink.ui.BaseActivity

class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    override fun onCreateViewBinding(): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        //处理事件
        val progressMusic = BgmManager.getInstance(this).getBackgroundVolume() * 100
        viewBinding.seekBarMusic.progress = progressMusic.toInt()

        val progressEffect = SoundPlayManager.getInstance(this).getVoice() * 100
        viewBinding.seekBarEffect.progress = progressEffect.toInt()

    }

    override fun initListener() {
        super.initListener()
        viewBinding.seekBarMusic.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d(Constant.TAG, "bgm当前进度：$progress")
                BgmManager.getInstance(this@SettingActivity)
                    .setBackgroundVolume((progress / 100.0).toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        viewBinding.seekBarEffect.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d(Constant.TAG, "effect当前进度：$progress")
                SoundPlayManager.getInstance(this@SettingActivity)
                    .setVoice((progress / 100.0).toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        viewBinding.settingFinish.setOnClickListener {
            SoundPlayManager.getInstance(this).play(3)
            finish()
        }
    }

}