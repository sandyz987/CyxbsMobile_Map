package com.mredrock.cyxbs.discover.map.bean

import com.google.gson.annotations.SerializedName

/**
 *@author zhangzhe
 *@date 2020/8/8
 *@description 主要接口，用来获取地图的底图，基本信息（包括各个地点的坐标等），完全对应接口文档
 */

data class MapInfo (
        @SerializedName("hot_word")
        var hotWord: String,
        @SerializedName("place_list")
        var placeList: List<PlaceItem>,
        @SerializedName("map_url")
        var mapUrl: String,
        @SerializedName("map_width")
        var mapWidth: String,
        @SerializedName("map_height")
        var mapHeight: String
)