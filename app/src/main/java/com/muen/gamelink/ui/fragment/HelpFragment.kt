package com.muen.gamelink.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.muen.gamelink.databinding.FragmentHelpBinding
import com.muen.gamelink.music.SoundPlayManager
import com.muen.gamelink.ui.base.BaseFragment

class HelpFragment : BaseFragment<FragmentHelpBinding>() {
    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHelpBinding {
        return FragmentHelpBinding.inflate(inflater, container, false)
    }

    override fun initListener() {
        super.initListener()
        viewBinding.mainKnow.setOnClickListener {
            SoundPlayManager.getInstance(requireContext()).play(3)

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.remove(this@HelpFragment)
            transaction.commit()
        }
    }
}