package com.mredrock.cyxbs.discover.map.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.mredrock.cyxbs.common.config.DISCOVER_MAP
import com.mredrock.cyxbs.common.service.ServiceManager
import com.mredrock.cyxbs.common.service.account.IAccountService
import com.mredrock.cyxbs.common.ui.BaseViewModelActivity
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.ui.fragment.AllPictureFragment
import com.mredrock.cyxbs.discover.map.ui.fragment.FavoriteEditFragment
import com.mredrock.cyxbs.discover.map.ui.fragment.MainFragment
import com.mredrock.cyxbs.discover.map.util.KeyboardController
import com.mredrock.cyxbs.discover.map.viewmodel.MapViewModel
import kotlinx.android.synthetic.main.map_activity_map.*

/**
 * 单activity模式，所有fragment在此activity下，能拿到同一个viewModel实例
 * Fragment不能继承BaseViewModelFragment，因为获得的viewModel是不同实例，必须：
 * ViewModelProvider(requireActivity()).get(MapViewModel::class.java)来获得实例
 */


@Route(path = DISCOVER_MAP)
class MapActivity : BaseViewModelActivity<MapViewModel>() {
    override val isFragmentActivity = false
    override val viewModelClass = MapViewModel::class.java
    private val fragmentManager = supportFragmentManager
    private val mainFragment = MainFragment()
    private val favoriteEditFragment = FavoriteEditFragment()
    private val allPictureFragment = AllPictureFragment()

    @SuppressLint("SetJavaScriptEnabled", "ObsoleteSdkInt")
    override fun onStart() {
        super.onStart()
        val userState = ServiceManager.getService(IAccountService::class.java).getVerifyService()
        if (!userState.isLogin()) {
            //这里只是模拟一下登录，如果有并发需求，自己设计
            Thread {
                userState.login(this, "2019211135", "226312")
            }.start()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_activity_map)
        //初始化viewModel
        viewModel.init()
        fragmentManager.beginTransaction().add(R.id.map_fl_main_fragment, mainFragment).show(mainFragment).commit()


        //控制收藏页面是否显示
        viewModel.fragmentFavoriteEditIsShowing.observe(
                this@MapActivity,
                Observer<Boolean> { t ->
                    if (t == true) {
                        val transaction = fragmentManager.beginTransaction()
                        transaction.setCustomAnimations(R.animator.map_slide_from_right, R.animator.map_slide_to_left, R.animator.map_slide_from_left, R.animator.map_slide_to_right)
                        transaction.hide(mainFragment)
                        if (!favoriteEditFragment.isAdded) {
                            transaction.add(R.id.map_fl_main_fragment, favoriteEditFragment)
                        }
                        transaction
                                .show(favoriteEditFragment)
                                .addToBackStack("favorite_edit")
                                .commit()
                    } else {
                        //隐藏键盘再返回，防止发生布局变形
                        KeyboardController.hideInputKeyboard(this, map_fl_main_fragment)
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(R.animator.map_slide_from_left, R.animator.map_slide_to_right, R.animator.map_slide_from_right, R.animator.map_slide_to_left)
                                .hide(favoriteEditFragment)
                                .show(mainFragment)
                                .commit()
                        fragmentManager.popBackStack()

                    }
                }
        )

        //控制全部图片页面是否显示
        viewModel.fragmentAllPictureIsShowing.observe(
                this@MapActivity,
                Observer<Boolean> { t ->
                    if (t == true) {
                        val transaction = fragmentManager.beginTransaction()
                        transaction.setCustomAnimations(R.animator.map_slide_from_right, R.animator.map_slide_to_left, R.animator.map_slide_from_left, R.animator.map_slide_to_right)
                        transaction.hide(mainFragment)
                        if (!allPictureFragment.isAdded) {
                            transaction.add(R.id.map_fl_main_fragment, allPictureFragment)
                        }
                        transaction
                                .show(allPictureFragment)
                                .addToBackStack("all_picture")
                                .commit()
                    } else {
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(R.animator.map_slide_from_left, R.animator.map_slide_to_right, R.animator.map_slide_from_right, R.animator.map_slide_to_left)
                                .hide(allPictureFragment)
                                .show(mainFragment)
                                .commit()
                        fragmentManager.popBackStack()

                    }
                }
        )

    }

    override fun onBackPressed() {
        if (mainFragment.childFragmentManager.backStackEntryCount != 0) {
            mainFragment.closeSearchFragment()
            return
        }
        super.onBackPressed()
    }

}
