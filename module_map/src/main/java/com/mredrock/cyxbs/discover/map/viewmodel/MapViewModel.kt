package com.mredrock.cyxbs.discover.map.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.mredrock.cyxbs.common.BaseApp
import com.mredrock.cyxbs.common.network.ApiGenerator
import com.mredrock.cyxbs.common.utils.extensions.doOnErrorWithDefaultErrorHandler
import com.mredrock.cyxbs.common.utils.extensions.safeSubscribeBy
import com.mredrock.cyxbs.common.utils.extensions.setSchedulers
import com.mredrock.cyxbs.common.viewmodel.BaseViewModel
import com.mredrock.cyxbs.discover.map.BuildConfig
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.bean.*
import com.mredrock.cyxbs.discover.map.model.TestData
import com.mredrock.cyxbs.discover.map.network.MapApiService
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
    val favoriteList = MutableLiveData<MutableList<FavoritePlace>>()

    //地点详细弹出框要显示的内容（由PlaceDetailBottomSheetFragment观察）
    val placeDetails = MutableLiveData<PlaceDetails>()

    //是否正在显示收藏页面
    val fragmentFavoriteEditIsShowing = MutableLiveData<Boolean>(false)

    //是否正在显示全部图片界面
    val fragmentAllPictureIsShowing = MutableLiveData<Boolean>(false)

    //详细页面正在显示的地点id
    var showingPlaceId = -1

    //是否显示bottomSheet，用于监听并隐藏
    val bottomSheetIsShowing = MutableLiveData<Boolean>(false)

    //是否在动画中
    val isAnimation = MutableLiveData(false)

    //搜索框的文字，只用在搜索界面fragment观察本变量即可实现搜索
    val searchText = MutableLiveData<String>("")

    //搜索结果
    val searchResult = ObservableArrayList<PlaceItem>()

    //在唯一的activity的onCreate调用，获取地图数据（地点list），下载地图应该在此处完成（就是文档上第一个接口）
    fun init() {
        //ProgressDialog.show(BaseApp.context,"提示","请稍后",false)//ProgressDialog.hide()
        /**
         * 初始化网络请求
         */
        ApiGenerator.registerNetSettings(2019211135, { builder ->
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
        mapApiService = ApiGenerator.getApiService(2019211135, MapApiService::class.java)
        /**
         * 下载地图可以放在这里，但必须开线程！
         */
        mapApiService.getMapInfo()//网络请求替换为：apiService.getMapInfo()
                .setSchedulers()
                .doOnErrorWithDefaultErrorHandler {
                    toastEvent.value = R.string.map_network_connect_error
                    false
                }
                .safeSubscribeBy {
                    mapInfo.postValue(it.data)
                }.lifeCycle()
        TestData.getButtonInfo()
                .setSchedulers()
                .doOnErrorWithDefaultErrorHandler {
                    toastEvent.value = R.string.map_network_connect_error
                    false
                }
                .safeSubscribeBy {
                    buttonInfo.postValue(it.data)
                }.lifeCycle()

        //第一次先获得一次收藏列表
        getFavoriteList()

    }

    //当地图标签被点击，执行此网络请求，在对应的fragment观察数据即可
    fun showPlaceDetails(placeId: Int) {
        mapApiService.getPlaceDetails(placeId)
                .setSchedulers()
                .doOnErrorWithDefaultErrorHandler {
                    toastEvent.value = R.string.map_network_connect_error
                    false
                }
                .safeSubscribeBy {
                    showingPlaceId = placeId
                    placeDetails.postValue(it.data)
                    Toast.makeText(BaseApp.context, it.data.placeName, Toast.LENGTH_SHORT).show()
                    bottomSheetIsShowing.postValue(true)
                }.lifeCycle()
    }

    fun getFavoriteList() {
        TestData.getFavorite()
                .setSchedulers()
                .doOnErrorWithDefaultErrorHandler {
                    toastEvent.value = R.string.map_network_connect_error
                    false
                }
                .safeSubscribeBy {
                    favoriteList.postValue(it.data.toMutableList())
                }.lifeCycle()
    }


}