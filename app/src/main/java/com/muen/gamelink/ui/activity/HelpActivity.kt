package com.muen.gamelink.ui.activity

import com.muen.gamelink.databinding.ActivityHelpBinding
import com.muen.gamelink.music.SoundPlayManager
import com.muen.gamelink.ui.BaseActivity

class HelpActivity : BaseActivity<ActivityHelpBinding>() {
    override fun onCreateViewBinding(): ActivityHelpBinding {
        return ActivityHelpBinding.inflate(layoutInflater)
    }

    override fun initListener() {
        super.initListener()
        viewBinding.mainKnow.setOnClickListener {
            SoundPlayManager.getInstance(this).play(3)
            finish()
        }
    }
}