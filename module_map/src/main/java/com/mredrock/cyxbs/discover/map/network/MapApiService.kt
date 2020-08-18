package com.mredrock.cyxbs.discover.map.network

import com.mredrock.cyxbs.common.bean.RedrockApiStatus
import com.mredrock.cyxbs.common.bean.RedrockApiWrapper
import com.mredrock.cyxbs.discover.map.bean.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

/**
 *@author zhangzhe
 *@date 2020/8/8
 *@description 掌邮地图网络请求，需要用getApiService获得而不是getCommonApiService（如果有token）
 */

internal interface MapApiService {
    @GET("basic")
    fun getMapInfo(): Observable<RedrockApiWrapper<MapInfo>>

    @FormUrlEncoded
    @POST("detailsite")
    fun getPlaceDetails(@Field("place_id") placeId: String): Observable<RedrockApiWrapper<PlaceDetails>>

    @GET("button")
    fun getButtonInfo(): Observable<RedrockApiWrapper<ButtonInfo>>

    @FormUrlEncoded
    @POST("searchtype")
    fun getSearchType(@Field("code") code: String): Observable<RedrockApiWrapper<MutableList<String>>>

    @GET("rockmap/collect")
    fun getCollect(): Observable<RedrockApiWrapper<FavoritePlaceSimple>>

    @FormUrlEncoded
    @PATCH("rockmap/addkeep")
    fun addCollect(@Field("place_id") placeId: String): Observable<RedrockApiStatus>

    @Multipart
    @HTTP(method = "DELETE", path = "rockmap/deletekeep", hasBody = true)
    fun deleteCollect(@Part("place_id") placeId: Int): Observable<RedrockApiStatus>

    @Multipart
    @POST("rockmap/upload")
    fun uploadPicture(@Part photo: MultipartBody.Part, @PartMap params: Map<String, Int>): Observable<RedrockApiStatus>


}