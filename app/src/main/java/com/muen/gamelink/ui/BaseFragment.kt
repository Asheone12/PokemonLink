package com.muen.gamelink.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding> : Fragment() {
    lateinit var viewBinding: T
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = onCreateViewBinding(inflater, container, savedInstanceState)
        return viewBinding.root
    }

    /**
     * 创建ViewBinding
     */
    abstract fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): T

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        afterViewCreated(view, savedInstanceState)
        initData()
        initView()
        initListener()
    }

    @CallSuper
    open fun afterViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    @CallSuper
    open fun initView() {
    }

    @CallSuper
    open fun initData() {
        observerData()
    }

    /**
     * 绑定viewModel数据方法
     */
    open fun observerData() {}

    @CallSuper
    open fun initListener() {
    }
}