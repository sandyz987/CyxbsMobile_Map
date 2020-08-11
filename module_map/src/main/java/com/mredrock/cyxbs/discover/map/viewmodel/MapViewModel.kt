package com.mredrock.cyxbs.discover.map.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.mredrock.cyxbs.common.BaseApp
import com.mredrock.cyxbs.common.utils.extensions.doOnErrorWithDefaultErrorHandler
import com.mredrock.cyxbs.common.utils.extensions.safeSubscribeBy
import com.mredrock.cyxbs.common.utils.extensions.setSchedulers
import com.mredrock.cyxbs.common.viewmodel.BaseViewModel
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.bean.ButtonInfo
import com.mredrock.cyxbs.discover.map.bean.MapInfo
import com.mredrock.cyxbs.discover.map.bean.PlaceDetails
import com.mredrock.cyxbs.discover.map.model.TestData
import com.mredrock.cyxbs.discover.map.widget.ProgressDialog
import io.reactivex.schedulers.Schedulers
import okhttp3.internal.notify

/**
 *@author zhangzhe
 *@date 2020/8/8
 *@description 由于掌邮地图模块采用单activity多fragment模式，所以所有fragment都共用本viewModel
 */

class MapViewModel : BaseViewModel() {

    //网络请求得到的数据(地图基本信息接口)，如何使用：在要使用的activity或者fragment观察即可
    val mapInfo = MutableLiveData<MapInfo>()

    //搜索框下方按钮内容
    val buttonInfo = MutableLiveData<ButtonInfo>()

    //地点详细弹出框要显示的内容（由PlaceDetailBottomSheetFragment观察）
    val placeDetails = MutableLiveData<PlaceDetails>()

    //在唯一的activity的onCreate调用，获取地图数据（地点list），下载地图应该在此处完成（就是文档上第一个接口）
    fun init() {
        //ProgressDialog.show(BaseApp.context,"提示","请稍后",false)//ProgressDialog.hide()
        //这里应该写的是网络请求，但是先用测试数据
        /**
         * 下载地图可以放在这里，但必须开线程！
         */
        TestData.getMapInfo()//网络请求替换为：apiService.getMapInfo()
                .setSchedulers()
                .doOnErrorWithDefaultErrorHandler {
                    toastEvent.value = R.string.map_network_connect_error
                    false
                }
                .safeSubscribeBy {
                    //it是请求返回的数据，以后的网络请求都照着这个模板写
                    mapInfo.value = it.data
                }.lifeCycle()
        TestData.getButtonInfo()//网络请求替换为：apiService.getMapInfo()
                .setSchedulers()
                .doOnErrorWithDefaultErrorHandler {
                    toastEvent.value = R.string.map_network_connect_error
                    false
                }
                .safeSubscribeBy {
                    buttonInfo.value = it.data
                }.lifeCycle()

    }





}