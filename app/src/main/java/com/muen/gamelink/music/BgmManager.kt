package com.muen.gamelink.music

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.muen.gamelink.util.SingletonHolder

class BgmManager private constructor(context: Context){
    //带参单例
    companion object :SingletonHolder<BgmManager,Context>(::BgmManager)

    private var mContext: Context
    private val TAG = "bgm"
    private var mLeftVolume = 0.5f
    private var mRightVolume = 0.5f
    private var mBgmMediaPlayer: MediaPlayer? = null
    private var mIsPaused = false
    private var mCurrentPath = 0

    init {
        mContext = context
        initData()
    }

    // 数据初始化
    private fun initData() {
        mLeftVolume = 0.5f
        mRightVolume = 0.5f
        mBgmMediaPlayer = null
        mIsPaused = false
        mCurrentPath = 0
    }

    /**
     * 根据path路径播放背景音乐
     *
     * @param path
     * :assets中的音频路径
     * @param isLoop
     * :是否循环播放
     */
    fun playBackgroundMusic(path: Int, isLoop: Boolean) {
        if (mCurrentPath == 0) {
            // 这是第一次播放背景音乐--- it is the first time to play background music
            // 或者是执行end()方法后，重新被叫---or end() was called
            mBgmMediaPlayer = createMediaPlayerFromPath(path)
            mCurrentPath = path
        } else {
            if (mCurrentPath != path) {
                // 播放一个新的背景音乐--- play new background music
                // 释放旧的资源并生成一个新的----release old resource and create a new one
                if (mBgmMediaPlayer != null) {
                    mBgmMediaPlayer?.release()
                }
                mBgmMediaPlayer = createMediaPlayerFromPath(path)
                // 记录这个路径---record the path
                mCurrentPath = path
            }
        }
        if (mBgmMediaPlayer == null) {
            Log.e(TAG, "playBackgroundMusic: background media player is null")
        } else {
            // 若果音乐正在播放或已近中断，停止它---if the music is playing or paused, stop it
            mBgmMediaPlayer?.stop()
            mBgmMediaPlayer?.isLooping = isLoop
            try {
                mBgmMediaPlayer?.prepare()
                mBgmMediaPlayer?.seekTo(0)
                mBgmMediaPlayer?.start()
                mIsPaused = false
            } catch (e: Exception) {
                Log.e(TAG, "playBackgroundMusic: error state")
            }
        }
    }

    /**
     * 停止播放背景音乐
     */
    fun stopBackgroundMusic() {
        if (mBgmMediaPlayer != null) {
            mBgmMediaPlayer?.stop()
            // should set the state, if not , the following sequence will be
            // error
            // play -> pause -> stop -> resume
            mIsPaused = false
        }
    }

    /**
     * 暂停播放背景音乐
     */
    fun pauseBackgroundMusic() {
        if (mBgmMediaPlayer != null
            && mBgmMediaPlayer!!.isPlaying
        ) {
            mBgmMediaPlayer?.pause()
            mIsPaused = true
        }
    }

    /**
     * 继续播放背景音乐
     */
    fun resumeBackgroundMusic() {
        if (mBgmMediaPlayer != null && mIsPaused) {
            mBgmMediaPlayer?.start()
            mIsPaused = false
        }
    }

    /**
     * 重新播放背景音乐
     */
    fun rewindBackgroundMusic() {
        if (mBgmMediaPlayer != null) {
            mBgmMediaPlayer?.stop()
            try {
                mBgmMediaPlayer?.prepare()
                mBgmMediaPlayer?.seekTo(0)
                mBgmMediaPlayer?.start()
                mIsPaused = false
            } catch (e: Exception) {
                Log.e(TAG, "rewindBackgroundMusic: error state")
            }
        }
    }

    /**
     * 判断背景音乐是否正在播放
     *
     * @return：返回的boolean值代表是否正在播放
     */
    fun isBackgroundMusicPlaying(): Boolean {
        val ret: Boolean = if (mBgmMediaPlayer == null) {
            false
        } else {
            mBgmMediaPlayer!!.isPlaying
        }
        return ret
    }

    /**
     * 结束背景音乐，并释放资源
     */
    fun end() {
        if (mBgmMediaPlayer != null) {
            mBgmMediaPlayer?.release()
        }
        // 重新“初始化数据”
        initData()
    }

    /**
     * 得到背景音乐的“音量”
     *
     * @return
     */
    fun getBackgroundVolume(): Float {
        return if (this.mBgmMediaPlayer != null) {
            (mLeftVolume + mRightVolume) / 2
        } else {
            0.0f
        }
    }

    /**
     * 设置背景音乐的音量
     *
     * @param volume
     * ：设置播放的音量，float类型
     */
    fun setBackgroundVolume(volume: Float) {
        mRightVolume = volume
        mLeftVolume = mRightVolume
        if (this.mBgmMediaPlayer != null) {
            this.mBgmMediaPlayer?.setVolume(
                mLeftVolume,
                mRightVolume
            )
        }
    }

    /**
     * create mediaplayer for music
     *
     * @param path
     * the path relative to assets
     * @return
     */
    private fun createMediaPlayerFromPath(path: Int): MediaPlayer {
        val mediaPlayer = MediaPlayer.create(mContext, path)
        mediaPlayer.setVolume(mLeftVolume, mRightVolume)
        return mediaPlayer
    }
}