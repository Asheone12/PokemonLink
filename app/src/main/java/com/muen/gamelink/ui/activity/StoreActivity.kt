package com.muen.gamelink.ui.activity

import androidx.activity.viewModels
import com.muen.gamelink.databinding.ActivityStoreBinding
import com.muen.gamelink.music.SoundPlayManager
import com.muen.gamelink.ui.BaseActivity
import com.muen.gamelink.ui.activity.vm.StoreVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreActivity : BaseActivity<ActivityStoreBinding>() {
    private val viewModel by viewModels<StoreVM>()
    override fun onCreateViewBinding(): ActivityStoreBinding {
        return ActivityStoreBinding.inflate(layoutInflater)
    }

    override fun initData() {
        super.initData()
        viewModel.loadUserData()
        viewModel.loadItemData()
    }

    override fun initListener() {
        super.initListener()
        viewBinding.storeFight.setOnClickListener {
            //购买拳头道具
            viewModel.userMoney = viewModel.userMoney - viewModel.fightMoney
            viewModel.fightNum = viewModel.fightNum + 1
            viewModel.changeItemCount(1,viewModel.fightNum)
            viewModel.changeUserMoney(viewModel.userMoney)
        }

        viewBinding.storeBomb.setOnClickListener {
            SoundPlayManager.getInstance(this).play(3)
            //购买炸弹道具
            viewModel.userMoney = viewModel.userMoney - viewModel.bombMoney
            viewModel.bombNum = viewModel.bombNum + 1
            viewModel.changeItemCount(2,viewModel.bombNum)
            viewModel.changeUserMoney(viewModel.userMoney)
        }

        viewBinding.storeRefresh.setOnClickListener {
            SoundPlayManager.getInstance(this).play(3)
            //购买刷新道具
            viewModel.userMoney = viewModel.userMoney - viewModel.refreshMoney
            viewModel.refreshNum = viewModel.refreshNum + 1
            viewModel.changeItemCount(3,viewModel.refreshNum)
            viewModel.changeUserMoney(viewModel.userMoney)
        }

        //移除该视图
        viewBinding.storeDelete.setOnClickListener {
            SoundPlayManager.getInstance(this).play(3)
            finish()
        }

    }

    override fun observerData() {
        super.observerData()
        viewModel.userChangeResult.observe(this) {
            viewBinding.storeUserMoney.text = viewModel.userMoney.toString()
        }
        viewModel.itemChangeResult.observe(this) {
            viewBinding.propFight.setCount(viewModel.fightNum)
            viewBinding.propBomb.setCount(viewModel.bombNum)
            viewBinding.propRefresh.setCount(viewModel.refreshNum)
        }
    }
}