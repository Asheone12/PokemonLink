package com.muen.gamelink.music

import android.content.Context
import android.media.SoundPool
import android.util.Log
import com.muen.gamelink.R
import com.muen.gamelink.util.SingletonHolder

class SoundPlayManager private constructor(context: Context) {
    //带参单例
    companion object : SingletonHolder<SoundPlayManager, Context>(::SoundPlayManager)

    private val TAG = "soundPlay"
    private var soundPool: SoundPool? = null
    private var mContext: Context

    //声音大小
    private var voice = 1f

    init {
        mContext = context
        if (soundPool == null) {
            Log.d(TAG, "初始化及时播放音频类")
            //设置同时播放流的最大数量
            soundPool = SoundPool.Builder().setMaxStreams(3).build()
            //加载音频文件
            soundPool?.load(mContext, R.raw.game_victory, 1) // 1 游戏闯关成功
            soundPool?.load(mContext, R.raw.game_default, 1) // 2 游戏闯关失败
            soundPool?.load(mContext, R.raw.game_click_btn, 1) // 3 点击按钮的音效
            soundPool?.load(mContext, R.raw.game_remove, 1) // 4 消除宝可梦的音效
            soundPool?.load(mContext, R.raw.game_un_click, 1) // 5 无法点击按钮的音效

        }
    }

    //播放指定音乐
    fun play(soundID: Int) {
        soundPool?.play(soundID, voice, voice, 0, 0, 1f)
    }

    //setter，getter
    fun getVoice(): Float {
        return voice
    }

    fun setVoice(voice: Float) {
        this.voice = voice
    }
}