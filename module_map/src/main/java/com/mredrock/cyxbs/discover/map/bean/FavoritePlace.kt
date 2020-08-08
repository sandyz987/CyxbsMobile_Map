package com.mredrock.cyxbs.discover.map.bean

import com.google.gson.annotations.SerializedName

/**
 *@author zhangzhe
 *@date 2020/8/8
 *@description
 */

data class FavoritePlace(
        @SerializedName("place_nickname")
        var placeNickname: String,
        @SerializedName("place_id")
        var placeId: Int
)