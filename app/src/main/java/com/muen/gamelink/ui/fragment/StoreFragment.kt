package com.muen.gamelink.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.muen.gamelink.databinding.FragmentStoreBinding
import com.muen.gamelink.game.constant.Constant
import com.muen.gamelink.game.constant.mode.ItemMode
import com.muen.gamelink.model.Item
import com.muen.gamelink.model.User
import com.muen.gamelink.music.SoundPlayManager
import org.litepal.LitePal

class StoreFragment : BaseFragment<FragmentStoreBinding>() {
    //存储数据
    private var user_money = 0
    private var fight_money = 0
    private var fight_num = 0
    private var bomb_money = 0
    private var bomb_num = 0
    private var refresh_money = 0
    private var refresh_num = 0

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentStoreBinding {
        return FragmentStoreBinding.inflate(inflater, container, false)
    }

    override fun initData() {
        super.initData()

        //查询用户数据
        val users: List<User> = LitePal.findAll(User::class.java)
        val user: User = users[0]
        //存储用户数据
        user_money = user.getUserMoney()
        //查询道具数据
        val items: List<Item> = LitePal.findAll(Item::class.java)
        for (item in items) {
            if (item.getItemType() == ItemMode.ITEM_FIGHT.value) {
                //拳头道具
                fight_money = item.getItemPrice()
                fight_num = item.getItemNumber()
            } else if (item.getItemType() == ItemMode.ITEM_BOMB.value) {
                //炸弹道具
                bomb_money = item.getItemPrice()
                bomb_num = item.getItemNumber()
            } else {
                //刷新道具
                refresh_money = item.getItemPrice()
                refresh_num = item.getItemNumber()
            }
        }
    }

    override fun initView() {
        super.initView()
        //显示道具数量的item
        viewBinding.propFight.count = fight_num
        viewBinding.propBomb.count = bomb_num
        viewBinding.propRefresh.count = refresh_num

        //显示道具价格的item
        viewBinding.storeUserMoney.text = user_money.toString()
        viewBinding.storeFightMoney.text = fight_money.toString()
        viewBinding.storeBombMoney.text = bomb_money.toString()
        viewBinding.storeRefreshMoney.text = refresh_money.toString()

    }

    override fun initListener() {
        super.initListener()
        viewBinding.storeFight.setOnClickListener {
            Log.d(Constant.TAG, "购买拳头")
            refreshSQLite(ItemMode.ITEM_FIGHT)
        }

        viewBinding.storeBomb.setOnClickListener {
            SoundPlayManager.getInstance(requireContext()).play(3)
            Log.d(Constant.TAG, "购买炸弹")
            refreshSQLite(ItemMode.ITEM_BOMB)
        }

        viewBinding.storeRefresh.setOnClickListener {
            SoundPlayManager.getInstance(requireContext()).play(3)
            Log.d(Constant.TAG, "购买刷新")
            refreshSQLite(ItemMode.ITEM_REFRESH)
        }

        //移除该视图
        viewBinding.storeDelete.setOnClickListener {
            SoundPlayManager.getInstance(requireContext()).play(3)
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.remove(this@StoreFragment)
            transaction.commit()
        }

    }

    /**
     * 刷新数据库的内容
     * @param mode
     */
    private fun refreshSQLite(mode: ItemMode) {
        //道具对象
        val item = Item()
        when (mode) {
            ItemMode.ITEM_FIGHT -> {
                user_money -= fight_money
                fight_num++
                item.setItemNumber(fight_num)
                item.update(1)

                //道具购买提示
                Toast.makeText(
                    context,
                    "成功购买一个锤子道具，消耗" + fight_money + "金币",
                    Toast.LENGTH_SHORT
                ).show()
            }

            ItemMode.ITEM_BOMB -> {
                user_money -= bomb_money
                bomb_num++
                item.setItemNumber(bomb_num)
                item.update(2)

                //道具购买提示
                Toast.makeText(
                    context,
                    "成功购买一个炸弹道具，消耗" + bomb_money + "金币",
                    Toast.LENGTH_SHORT
                ).show()
            }

            ItemMode.ITEM_REFRESH -> {
                user_money -= refresh_money
                refresh_num++
                item.setItemNumber(refresh_num)
                item.update(3)

                //道具购买提示
                Toast.makeText(
                    context,
                    "成功购买一个重排道具，消耗" + refresh_money + "金币",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        //刷新用户数据
        val user = User()
        user.setUserMoney(user_money)
        user.update(1)

        //重新设置金币
        viewBinding.storeUserMoney.text = user_money.toString()

        //找到显示道具数量的文本
        viewBinding.propFight.count = fight_num
        viewBinding.propBomb.count = bomb_num
        viewBinding.propRefresh.count = refresh_num
    }

}