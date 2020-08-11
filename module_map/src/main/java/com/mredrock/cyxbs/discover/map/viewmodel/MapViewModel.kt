package com.mredrock.cyxbs.discover.map.viewmodel

import com.mredrock.cyxbs.common.bean.RedrockApiWrapper
import com.mredrock.cyxbs.common.utils.extensions.safeSubscribeBy
import com.mredrock.cyxbs.common.utils.extensions.setSchedulers
import com.mredrock.cyxbs.common.viewmodel.BaseViewModel
import com.mredrock.cyxbs.discover.map.bean.MapInfo
import com.mredrock.cyxbs.discover.map.model.TestData
import io.reactivex.Observable

/**
 *@author zhangzhe
 *@date 2020/8/8
 *@description 由于掌邮地图模块采用单activity多fragment模式，所以所有fragment都共用本viewModel
 */

class MapViewModel : BaseViewModel() {

    lateinit var mapInfo: Observable<RedrockApiWrapper<MapInfo>>

    //在唯一的activity的onCreate调用，获取地图数据（地点list），下载地图应该在此处完成（就是文档上第一个接口）
    fun init() {


        //这里应该写的是网络请求，但是先用测试数据
        //mapInfo = apiService.getMapInfo(无参数)
        mapInfo = TestData.getMapInfo()
        mapInfo.setSchedulers().safeSubscribeBy {

        }
    }

}