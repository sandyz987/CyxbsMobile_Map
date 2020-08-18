package com.mredrock.cyxbs.discover.map.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.mredrock.cyxbs.common.bean.isSuccessful
import com.mredrock.cyxbs.common.network.ApiGenerator
import com.mredrock.cyxbs.common.utils.extensions.doOnErrorWithDefaultErrorHandler
import com.mredrock.cyxbs.common.utils.extensions.safeSubscribeBy
import com.mredrock.cyxbs.common.utils.extensions.setSchedulers
import com.mredrock.cyxbs.common.viewmodel.BaseViewModel
import com.mredrock.cyxbs.discover.map.BuildConfig
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.bean.*
import com.mredrock.cyxbs.discover.map.model.DataSet
import com.mredrock.cyxbs.discover.map.network.MapApiService
import com.mredrock.cyxbs.discover.map.widget.ProgressDialog
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 *@author zhangzhe
 *@date 2020/8/8
 *@description 由于掌邮地图模块采用单activity多fragment模式，所以所有fragment都共用本viewModel
 */

class MapViewModel : BaseViewModel() {
    private lateinit var mapApiService: MapApiService

    //网络请求得到的数据(地图基本信息接口)，如何使用：在要使用的activity或者fragment观察即可
    val mapInfo = MutableLiveData<MapInfo>()

    //搜索框下方按钮内容
    val buttonInfo = MutableLiveData<ButtonInfo>()

    //喜欢列表内容
    val collectList = MutableLiveData<MutableList<FavoritePlace>>()

    //地点详细弹出框要显示的内容（由PlaceDetailBottomSheetFragment观察）
    val placeDetails = MutableLiveData<PlaceDetails>()

    //是否正在显示收藏页面
    val fragmentFavoriteEditIsShowing = MutableLiveData(false)

    //是否正在显示全部图片界面
    val fragmentAllPictureIsShowing = MutableLiveData(false)

    //详细页面正在显示的地点id
    var showingPlaceId = "-1"

    //是否显示bottomSheet，用于监听并隐藏
    val bottomSheetIsShowing = MutableLiveData(false)

    //是否在动画中
    val isAnimation = MutableLiveData(false)

    //搜索框的文字，只用在搜索界面fragment观察本变量即可实现搜索
    val searchText = MutableLiveData("")

    //搜索结果
    val searchResult = ObservableArrayList<PlaceItem>()

    //显示的地点id
    val showSomeIconsId = MutableLiveData<MutableList<String>>()

    //用于通知mainFragment关闭搜索框
    val closeSearchFragment = MutableLiveData(false)

    //网络请求失败，使用本地缓存
    val loadFail = MutableLiveData(false)

    //是否锁定
    val isLock = MutableLiveData(false)

    //是否点击了标签或收藏
    val isClickSymbol = MutableLiveData(false)

    //通知收藏列表关闭
    val dismissPopUpWindow = MutableLiveData(false)


    fun init() {

        /**
         * 初始化网络请求
         */
        ApiGenerator.registerNetSettings(1234, { builder ->
            builder.baseUrl("https://cyxbsmobile.redrock.team/wxapi/magipoke-stumap/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        }, { builder ->
            builder.apply {
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(logging)
                }
            }
        }, true)
        mapApiService = ApiGenerator.getApiService(1234, MapApiService::class.java)
        /**
         * 下载地图可以放在这里，但必须开线程！
         */
        mapApiService.getMapInfo()//网络请求替换为：apiService.getMapInfo()
                .setSchedulers()
                .doOnErrorWithDefaultErrorHandler {
                    toastEvent.value = R.string.map_network_connect_error
                    //使用缓存数据
                    val mapInfoStore = DataSet.getMapInfo()
                    if (mapInfoStore != null) {
                        mapInfo.postValue(mapInfoStore)
                    }
                    loadFail.postValue(true)
                    true
                }
                .safeSubscribeBy {
                    mapInfo.postValue(it.data)
                    DataSet.saveMapInfo(it.data)
                }.lifeCycle()
        mapApiService.getButtonInfo()
                .setSchedulers()
                .doOnErrorWithDefaultErrorHandler {
                    toastEvent.value = R.string.map_network_connect_error
                    //使用缓存数据
                    val buttonInfoStore = DataSet.getButtonInfo()
                    if (buttonInfoStore != null) {
                        buttonInfo.postValue(buttonInfoStore)
                    }
                    true
                }
                .safeSubscribeBy {
                    buttonInfo.postValue(it.data)
                    DataSet.saveButtonInfo(it.data)
                }.lifeCycle()

        refreshCollectList()

    }


    //当地图标签被点击，执行此网络请求，在对应的fragment观察数据即可
    fun showPlaceDetails(placeId: String) {
        mapApiService.getPlaceDetails(placeId)
                .setSchedulers()
                .doOnErrorWithDefaultErrorHandler {
                    toastEvent.value = R.string.map_network_connect_error
                    true
                }
                .safeSubscribeBy {
                    showingPlaceId = placeId
                    placeDetails.postValue(it.data)
                    bottomSheetIsShowing.postValue(true)
                }.lifeCycle()
    }

    fun refreshCollectList() {
        mapApiService.getCollect()
                .setSchedulers()
                .doOnErrorWithDefaultErrorHandler {
                    val list = DataSet.getCollect()
                    if (list != null) {
                        collectList.postValue(list)
                    }
                    if (collectList.value?.size == 0) {
                        toastEvent.value = R.string.map_favorite_empty
                    }
                    true
                }
                .safeSubscribeBy {

                    it.data.placeId.forEach { item ->
                        DataSet.addCollect(item)
                    }

                    collectList.value = DataSet.getCollect()

                    if (collectList.value?.size == 0) {
                        toastEvent.value = R.string.map_favorite_empty
                    }
                }.lifeCycle()
    }

    fun getSearchType(code: String) {
        mapApiService.getSearchType(code)
                .setSchedulers()
                .doOnErrorWithDefaultErrorHandler {
                    toastEvent.value = R.string.map_network_connect_error
                    true
                }
                .safeSubscribeBy {
                    showSomeIconsId.value = it.data
                }.lifeCycle()
    }

    fun addCollect(placeNickname: String, id: String) {
        var notExist = true
        if (collectList.value != null) {
            for (t: FavoritePlace in collectList.value!!) {
                if (t.placeId == id) {
                    notExist = false
                }
            }
        }

        if (notExist) {
            mapApiService.addCollect(id)
                    .setSchedulers()
                    .doOnErrorWithDefaultErrorHandler {
                        toastEvent.value = R.string.map_network_connect_error
                        ProgressDialog.hide()
                        true
                    }
                    .safeSubscribeBy {
                        if (it.isSuccessful) {
                            DataSet.addCollect(FavoritePlace(placeNickname, id))
                        } else {
                            toastEvent.value = R.string.map_network_connect_error
                        }
                        ProgressDialog.hide()
                        refreshCollectList()
                    }.lifeCycle()
        } else {
            DataSet.addCollect(FavoritePlace(placeNickname, id))
            ProgressDialog.hide()
            refreshCollectList()
        }


    }

    fun deleteCollect(id: String) {
        mapApiService.deleteCollect(id.toInt())
                .setSchedulers()
                .doOnErrorWithDefaultErrorHandler {
                    toastEvent.value = R.string.map_network_connect_error
                    ProgressDialog.hide()
                    true
                }
                .safeSubscribeBy {
                    if (it.isSuccessful) {
                        DataSet.deleteCollect(id)
                    } else {
                        toastEvent.value = R.string.map_network_connect_error
                    }
                    ProgressDialog.hide()
                    refreshCollectList()
                }.lifeCycle()

    }


}