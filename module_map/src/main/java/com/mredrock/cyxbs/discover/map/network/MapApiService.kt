package com.mredrock.cyxbs.discover.map.network

import com.mredrock.cyxbs.common.bean.RedrockApiWrapper
import com.mredrock.cyxbs.discover.map.bean.MapInfo
import com.mredrock.cyxbs.discover.map.bean.PlaceDetails
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST

/**
 *@author zhangzhe
 *@date 2020/8/8
 *@description 掌邮地图网络请求，需要用getApiService获得而不是getCommonApiService（如果有token）
 */

internal interface MapApiService {
    @POST("/map/map_info")
    fun getMapInfo(): Observable<RedrockApiWrapper<MapInfo>>

    @POST("/app/token/place_details")
    fun getPlaceDetails(@Field("place_id") placeId: Int): Observable<RedrockApiWrapper<PlaceDetails>>
}