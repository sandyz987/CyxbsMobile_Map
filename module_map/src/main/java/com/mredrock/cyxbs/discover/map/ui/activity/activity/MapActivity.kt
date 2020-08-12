package com.mredrock.cyxbs.discover.map.ui.activity.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.mredrock.cyxbs.common.config.DISCOVER_MAP
import com.mredrock.cyxbs.common.service.ServiceManager
import com.mredrock.cyxbs.common.service.account.IAccountService
import com.mredrock.cyxbs.common.ui.BaseActivity
import com.mredrock.cyxbs.common.ui.BaseViewModelActivity
import com.mredrock.cyxbs.common.utils.extensions.setFullScreen
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.ui.activity.fragment.MainFragment
import com.mredrock.cyxbs.discover.map.ui.activity.fragment.inner.MapViewFragment
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
    val fragmentManager = supportFragmentManager

    @SuppressLint("SetJavaScriptEnabled", "ObsoleteSdkInt")
    override fun onStart() {
        super.onStart()
        val userState = ServiceManager.getService(IAccountService::class.java).getVerifyService()
        if (!userState.isLogin()) {
            //这里只是模拟一下登录，如果有并发需求，自己设计
//            Thread {
//                userState.login(this, "你的学号", "你的后6位")
//            }.start()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_activity_map)
        //初始化viewModel
        viewModel.init()
        fragmentManager.beginTransaction().add(R.id.map_fl_main_fragment, MainFragment()).commit()

    }
}
