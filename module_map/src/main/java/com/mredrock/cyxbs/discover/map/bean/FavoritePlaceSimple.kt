package com.mredrock.cyxbs.discover.map.bean

import com.google.gson.annotations.SerializedName

/**
 *@author zhangzhe
 *@date 2020/8/18
 *@description
 */

data class FavoritePlaceSimple(
        @SerializedName("place_id")
        val placeIdList: MutableList<String>
)