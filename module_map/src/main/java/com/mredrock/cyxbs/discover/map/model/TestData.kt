package com.mredrock.cyxbs.discover.map.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mredrock.cyxbs.common.bean.RedrockApiStatus
import com.mredrock.cyxbs.common.bean.RedrockApiWrapper
import com.mredrock.cyxbs.discover.map.bean.MapInfo
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe

/**
 *@author zhangzhe
 *@date 2020/8/11
 *@description 生成测试数据
 */


object TestData {
    /**
     * 直接转成数据类返回
     */
    fun getMapInfo(): Observable<RedrockApiWrapper<MapInfo>> {
        val gson = Gson()
        return Observable.create {
            gson.fromJson<RedrockApiWrapper<MapInfo>>(mapInfoString, object : TypeToken<RedrockApiWrapper<MapInfo>>() {}.type)
        }
    }

}

/**
 * 返回的字符串
 */
const val mapInfoString = "{\n" +
        "    \"status\":200\n" +
        "    \"info\":\"success\"\n" +
        "    \"data\":" +
        "       {\n" +
        "        \"hot_word\": \"腾飞门\",\n" +
        "        \"place_list\": [\n" +
        "            {\n" +
        "                \"place_name\": \"重邮腾飞门\",\n" +
        "                \"place_id\": 1,\n" +
        "                \"building_list\": [\n" +
        "                    {\n" +
        "                        \"building_left\": 1361,\n" +
        "                        \"building_right\": 1770,\n" +
        "                        \"building_top\": 8513,\n" +
        "                        \"building_bottom\": 8744\n" +
        "                    }\n" +
        "                ],\n" +
        "                \"place_center_x\": 1555,\n" +
        "                \"place_center_y\": 8647,\n" +
        "                \"tag_left\": 1781,\n" +
        "                \"tag_right\": 2015,\n" +
        "                \"tag_top\": 8667,\n" +
        "                \"tag_bottom\": 8764\n" +
        "            },\n" +
        "            {\n" +
        "                \"place_name\": \"中心食堂\",\n" +
        "                \"place_id\": 2,\n" +
        "                \"building_list\": [\n" +
        "                    {\n" +
        "                        \"building_left\": 3387,\n" +
        "                        \"building_right\": 3719,\n" +
        "                        \"building_top\": 7393,\n" +
        "                        \"building_bottom\": 7627\n" +
        "                    }\n" +
        "                ],\n" +
        "                \"place_center_x\": 3548,\n" +
        "                \"place_center_y\": 7516,\n" +
        "                \"tag_left\": 3425,\n" +
        "                \"tag_right\": 3547,\n" +
        "                \"tag_top\": 7309,\n" +
        "                \"tag_bottom\": 7370\n" +
        "            }" +
        "        ],\n" +
        "        \"map_url\": \"./xxxxx/xxxx/xxx.png\",\n" +
        "        \"map_width\": 7447,\n" +
        "        \"map_height\": 13255,\n" +
        "        \"map_background_color\": \"#96ECBB\"\n" +
        "       }" +
        "}"