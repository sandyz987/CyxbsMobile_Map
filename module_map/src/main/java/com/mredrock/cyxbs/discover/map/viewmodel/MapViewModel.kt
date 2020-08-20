package com.mredrock.cyxbs.discover.map.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mredrock.cyxbs.common.BaseApp
import com.mredrock.cyxbs.common.bean.isSuccessful
import com.mredrock.cyxbs.common.network.ApiGenerator
import com.mredrock.cyxbs.common.utils.extensions.doOnErrorWithDefaultErrorHandler
import com.mredrock.cyxbs.common.utils.extensions.safeSubscribeBy
import com.mredrock.cyxbs.common.utils.extensions.setSchedulers
import com.mredrock.cyxbs.common.viewmodel.BaseViewModel
import com.mredrock.cyxbs.discover.map.BuildConfig
import com.mredrock.cyxbs.discover.map.R
import com.mredrock.cyxbs.discover.map.bean.*
import com.mredrock.cyxbs.discover.map.component.MapToast
import com.mredrock.cyxbs.discover.map.model.DataSet
import com.mredrock.cyxbs.discover.map.network.MapApiService
import com.mredrock.cyxbs.discover.map.widget.MapDialogTips
import com.mredrock.cyxbs.discover.map.widget.OnSelectListenerTips
import com.mredrock.cyxbs.discover.map.widget.ProgressDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


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

    //地点详细弹出框要显示的内容
    val placeDetails = MutableLiveData<PlaceDetails>()

    //是否正在显示收藏页面
    val fragmentFavoriteEditIsShowing = MutableLiveData<Boolean>()

    //是否正在显示全部图片界面
    val fragmentAllPictureIsShowing = MutableLiveData<Boolean>()

    //详细页面正在显示的地点id
    var showingPlaceId = "-1"

    //是否显示bottomSheet，用于监听并隐藏
    val bottomSheetStatus = MutableLiveData<Int>()

    //是否在动画中
    val mapViewIsInAnimation = MutableLiveData<Boolean>()

    //搜索框的文字，只用在搜索界面fragment观察本变量即可实现搜索
    val searchText = MutableLiveData("")

    //搜索结果
    val searchResult = ObservableArrayList<PlaceItem>()

    //搜索记录
    var searchHistory = mutableListOf<String>()

    //当历史记录点击，通知输入框更新
    val searchHistoryString = MutableLiveData<String>("")

    //显示的地点id
    val showSomePlaceIconById = MutableLiveData<MutableList<String>>()

    //缩放到某一个地点id
    val showIconById = MutableLiveData<String>()

    //用于通知mainFragment关闭搜索框
    val closeSearchFragment = MutableLiveData<Boolean>()

    //网络请求失败，使用本地缓存
    val loadFail = MutableLiveData<Boolean>()

    //是否锁定
    val isLock = MutableLiveData<Boolean>(false)

    //是否点击了标签或收藏
    val isClickSymbol = MutableLiveData<Boolean>()

    //通知收藏列表关闭
    val showPopUpWindow = MutableLiveData<Boolean>()


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
                    MapToast.makeText(BaseApp.context, R.string.map_network_connect_error, Toast.LENGTH_SHORT).show()
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
                    MapToast.makeText(BaseApp.context, R.string.map_network_connect_error, Toast.LENGTH_SHORT).show()
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
    fun getPlaceDetails(placeId: String) {
        mapApiService.getPlaceDetails(placeId)
                .setSchedulers()
                .doOnErrorWithDefaultErrorHandler {
                    MapToast.makeText(BaseApp.context, R.string.map_network_connect_error, Toast.LENGTH_SHORT).show()

                    //有缓存则使用缓存
                    val t = DataSet.getPlaceDetails(placeId)
                    if (t != null) {
                        showingPlaceId = placeId
                        placeDetails.postValue(t)
                        bottomSheetStatus.postValue(BottomSheetBehavior.STATE_COLLAPSED)
                    } else {
                        bottomSheetStatus.postValue(BottomSheetBehavior.STATE_HIDDEN)
                    }

                    true
                }
                .safeSubscribeBy() {
                    showingPlaceId = placeId
                    placeDetails.postValue(it.data)
                    if (bottomSheetStatus.value == BottomSheetBehavior.STATE_HIDDEN) {
                        bottomSheetStatus.postValue(BottomSheetBehavior.STATE_COLLAPSED)
                    }
                    DataSet.savePlaceDetails(it.data, placeId)


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
                        MapToast.makeText(BaseApp.context, R.string.map_favorite_empty, Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                .safeSubscribeBy {

                    it.data.placeIdList.forEach { item ->
                        DataSet.addCollect(item)
                    }

                    collectList.value = DataSet.getCollect()

                    if (collectList.value?.size == 0) {
                        MapToast.makeText(BaseApp.context, R.string.map_favorite_empty, Toast.LENGTH_SHORT).show()
                    } else {
                        if (bottomSheetStatus.value == BottomSheetBehavior.STATE_HIDDEN || showPopUpWindow.value == true) {
                            showSomePlaceIconById.value = it.data.placeIdList
                        }
                    }
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
                        MapToast.makeText(BaseApp.context, R.string.map_network_connect_error, Toast.LENGTH_SHORT).show()
                        ProgressDialog.hide()
                        refreshCollectList()
                        true
                    }
                    .safeSubscribeBy {
                        if (it.isSuccessful) {
                            DataSet.addCollect(FavoritePlace(placeNickname, id))
                            MapToast.makeText(BaseApp.context, "收藏成功！", Toast.LENGTH_SHORT).show()
                        } else {
                            MapToast.makeText(BaseApp.context, R.string.map_network_connect_error, Toast.LENGTH_SHORT).show()
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
                    MapToast.makeText(BaseApp.context, R.string.map_network_connect_error, Toast.LENGTH_SHORT).show()
                    ProgressDialog.hide()
                    refreshCollectList()
                    true
                }
                .safeSubscribeBy {
                    if (it.isSuccessful) {
                        DataSet.deleteCollect(id)
                    } else {
                        MapToast.makeText(BaseApp.context, R.string.map_network_connect_error, Toast.LENGTH_SHORT).show()
                    }
                    ProgressDialog.hide()
                    refreshCollectList()
                }.lifeCycle()

    }

    fun notifySearchHistoryChange() {
        searchHistory = DataSet.getSearchHistory() ?: mutableListOf()
        searchHistory.reverse()
    }

    fun uploadPicture(imgPath: String?, context: Context) {
        if (imgPath == null) {
            return
        }
        val file = File(imgPath)
        val requestFile: RequestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val fileNameByTimeStamp: String = System.currentTimeMillis().toString() + ".jpg"
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("upload_picture", fileNameByTimeStamp, requestFile)
        val params: MutableMap<String, Int> = HashMap()
        params["place_id"] = showingPlaceId.toInt()

        mapApiService.uploadPicture(body, params)
                .setSchedulers()
                .doOnErrorWithDefaultErrorHandler {
                    ProgressDialog.hide()
                    MapToast.makeText(BaseApp.context, "上传失败，网络似乎有问题", Toast.LENGTH_LONG).show()
                    true
                }
                .safeSubscribeBy {
                    ProgressDialog.hide()
                    if (it.isSuccessful) {
                        MapDialogTips.show(context, "上传成功", "上传的照片审核通过就可以在这里看到啦~", object : OnSelectListenerTips {
                            override fun onPositive() {}
                        })
                    } else {
                        MapToast.makeText(BaseApp.context, "上传失败，服务器拒绝", Toast.LENGTH_LONG).show()

                    }
                }.lifeCycle()

    }
}


