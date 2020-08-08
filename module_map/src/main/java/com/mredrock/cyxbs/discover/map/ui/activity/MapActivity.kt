package com.mredrock.cyxbs.discover.map.ui.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.mredrock.cyxbs.common.config.DISCOVER_MAP
import com.mredrock.cyxbs.common.service.ServiceManager
import com.mredrock.cyxbs.common.service.account.IAccountService
import com.mredrock.cyxbs.common.ui.BaseActivity
import com.mredrock.cyxbs.common.utils.extensions.setFullScreen
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.bean.IconBean
import com.mredrock.cyxbs.discover.map.utils.MapLayout
import kotlinx.android.synthetic.main.map_activity_map.*

@Route(path = DISCOVER_MAP)
class MapActivity : BaseActivity() {
    override val isFragmentActivity = false
    private lateinit var webView: WebView

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

        setFullScreen()

        map_layout.addIcon(IconBean(0, 2945f, 6526f, 2430f, 3500f, 6210f, 6830f))
        map_layout.addIcon(IconBean(1, 2830f, 7488f, 2430f, 3250f, 7245f, 7717f))
        map_layout.setMyOnIconClickListener(object : MapLayout.OnIconClickListener {
            override fun onIconClick(v: View) {
                val bean = v.tag as IconBean
                map_layout.focusToPoint(bean.sx, bean.sy)
            }

        })
    }

    override fun onStop() {
        webView.destroy()
        super.onStop()
    }
}
