package com.muen.gamelink.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.muen.gamelink.databinding.FragmentStoreBinding
import com.muen.gamelink.music.SoundPlayManager
import com.muen.gamelink.ui.BaseFragment
import com.muen.gamelink.ui.fragment.vm.StoreVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreFragment : BaseFragment<FragmentStoreBinding>() {
    private val viewModel by viewModels<StoreVM>()

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentStoreBinding {
        return FragmentStoreBinding.inflate(inflater, container, false)
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
            SoundPlayManager.getInstance(requireContext()).play(3)
            //购买炸弹道具
            viewModel.userMoney = viewModel.userMoney - viewModel.bombMoney
            viewModel.bombNum = viewModel.bombNum + 1
            viewModel.changeItemCount(2,viewModel.bombNum)
            viewModel.changeUserMoney(viewModel.userMoney)
        }

        viewBinding.storeRefresh.setOnClickListener {
            SoundPlayManager.getInstance(requireContext()).play(3)
            //购买刷新道具
            viewModel.userMoney = viewModel.userMoney - viewModel.refreshMoney
            viewModel.refreshNum = viewModel.refreshNum + 1
            viewModel.changeItemCount(3,viewModel.refreshNum)
            viewModel.changeUserMoney(viewModel.userMoney)
        }

        //移除该视图
        viewBinding.storeDelete.setOnClickListener {
            SoundPlayManager.getInstance(requireContext()).play(3)
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.remove(this@StoreFragment)
            transaction.commit()
        }

    }

    override fun observerData() {
        super.observerData()
        viewModel.userChangeResult.observe(this){
            viewBinding.storeUserMoney.text = viewModel.userMoney.toString()
        }
        viewModel.itemChangeResult.observe(this){
            viewBinding.propFight.count = viewModel.fightNum
            viewBinding.propBomb.count = viewModel.bombNum
            viewBinding.propRefresh.count = viewModel.refreshNum
        }
    }

}