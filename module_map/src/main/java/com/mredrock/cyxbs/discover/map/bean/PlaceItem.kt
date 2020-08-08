package com.mredrock.cyxbs.discover.map.bean

import com.google.gson.annotations.SerializedName

/**
 *@author zhangzhe
 *@date 2020/8/8
 *@description 单个地点的基本信息
 */

/**
 * "place_name":"风雨操场"
 * "place_id":36
 * "building_x":4300
 * "building_y":6200
 * "building_r":200
 * "tag_x":4500
 * "tag_y":6300
 * "tag_r":200
 */

data class PlaceItem(
        @SerializedName("place_name")
        var placeName: String,
        @SerializedName("place_id")
        var placeId: String,
        @SerializedName("building_x")
        var buildingX: String,
        @SerializedName("building_y")
        var buildingY: String,
        @SerializedName("building_r")
        var buildingR: String,
        @SerializedName("tag_x")
        var tagX: String,
        @SerializedName("tag_y")
        var tagY: String,
        @SerializedName("tag_r")
        var tagR: String
)