package com.mredrock.cyxbs.discover.map.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mredrock.cyxbs.common.bean.RedrockApiWrapper
import com.mredrock.cyxbs.discover.map.bean.ButtonInfo
import com.mredrock.cyxbs.discover.map.bean.FavoritePlace
import com.mredrock.cyxbs.discover.map.bean.MapInfo
import com.mredrock.cyxbs.discover.map.bean.PlaceDetails
import io.reactivex.Observable

/**
 *@author zhangzhe
 *@date 2020/8/11
 *@description 生成测试数据，所有测试数据应写在这里
 */


object TestData {
    /**
     * 地图基本信息接口
     * 目前只返回 中心食堂id2 和 腾飞门id1
     */
    private val gson = Gson()
    fun getMapInfo() = Observable.create<RedrockApiWrapper<MapInfo>> {
        it.onNext(gson.fromJson(mapInfoString, object : TypeToken<RedrockApiWrapper<MapInfo>>() {}.type))
    }

    /**
     * 筛选接口
     * @param code 筛选条件
     * @return 返回list装int，符合条件的地点id
     * 现在暂时返回[1,2]，因为“地图基本信息”接口只返回了两个地点，测试用
     */
    fun getScreen(code: String) = Observable.create<RedrockApiWrapper<List<Int>>> {
        it.onNext(gson.fromJson(screenString, object : TypeToken<RedrockApiWrapper<List<Int>>>() {}.type))
    }

    /**
     * 获得收藏接口，{nickname：最爱去的食堂 id：2}  和  {nickname：漂亮的新校门 id：1}
     */
    fun getFavorite() = Observable.create<RedrockApiWrapper<List<FavoritePlace>>> {
        it.onNext(gson.fromJson(favoriteString, object : TypeToken<RedrockApiWrapper<List<FavoritePlace>>>() {}.type))
    }

    /**
     * 地点详细接口
     * 目前只返回 中心食堂id2 和 腾飞门id1
     * @param placeId 返回对应地点的地点详细
     */
    fun getPlaceDetails(placeId: Int): Observable<RedrockApiWrapper<PlaceDetails>> {
        val s = if (placeId == 1) place1DetailsString else place2DetailsString
        return Observable.create<RedrockApiWrapper<PlaceDetails>> { em ->
            em.onNext(gson.fromJson(s, object : TypeToken<RedrockApiWrapper<PlaceDetails>>() {}.type))
        }
    }

    fun getButtonInfo(): Observable<RedrockApiWrapper<ButtonInfo>> {
        return Observable.create<RedrockApiWrapper<ButtonInfo>> { em ->
            em.onNext(gson.fromJson(buttonInfoString, object : TypeToken<RedrockApiWrapper<ButtonInfo>>() {}.type))
        }
    }

}

/**
 * 接口的字符串
 */

const val buttonInfoString =
        "{\n" +
                "    \"status\":200,\n" +
                "    \"info\":\"success\",\n" +
                "    \"version\":\"1.0\",\n" +
                "    \"id\":0,\n" +
                "    \"data\":\n" +
                "    {\n" +
                "        \"button_info\":\n" +
                "        [\n" +
                "            {\n" +
                "                \"title\":\"新生报到处\",\n" +
                "                \"code\":\"新生报到点\",\n" +
                "                \"is_hot\":true\n" +
                "            },\n" +
                "            {\n" +
                "                \"title\":\"食堂\",\n" +
                "                \"code\":\"食堂\",\n" +
                "                \"is_hot\":false\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}"

//id = 1 的地点信息字符串
const val place1DetailsString =
        "{\n" +
                "    \"status\":200,\n" +
                "    \"info\":\"success\",\n" +
                "    \"version\":\"1.0\",\n" +
                "    \"id\":0,\n" +
                "    \"data\":\n" +
                "    {\n" +
                "        \"place_name\":\"崇文门\",\n" +
                "        \"place_attribute\":\n" +
                "        [\n" +
                "            \"校门\",\n" +
                "            \"标签\",\n" +
                "            \"标签\",\n" +
                "            \"标签\",\n" +
                "            \"标签\",\n" +
                "            \"标签\",\n" +
                "            \"标签\",\n" +
                "            \"标签\"\n" +
                "        ],\n" +
                "        \"tags\":\n" +
                "        [\n" +
                "            \"新生报到\",\n" +
                "            \"开门时间：6:00-23:00\"\n" +
                "        ],\n" +
                "        \"images\":\n" +
                "        [\n" +
                "            \"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3271944503,17290708&fm=26&gp=0.jpg\",\n" +
                "            \"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3271944503,17290708&fm=26&gp=0.jpg\",\n" +
                "            \"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3271944503,17290708&fm=26&gp=0.jpg\"\n" +
                "        ]\n" +
                "    }\n" +
                "}"

//id = 2 的地点信息字符串
const val place2DetailsString =
        "{\n" +
                "    \"status\":200,\n" +
                "    \"info\":\"success\",\n" +
                "    \"version\":\"1.0\",\n" +
                "    \"id\":0,\n" +
                "    \"data\":\n" +
                "    {\n" +
                "        \"place_name\":\"中心食堂\",\n" +
                "        \"place_attribute\":\n" +
                "        [\n" +
                "            \"食堂\",\n" +
                "            \"标签\"\n" +
                "        ],\n" +
                "        \"is_collected\":true,\n" +
                "        \"tags\":\n" +
                "        [\n" +
                "           \"芜湖\",\n" +
                "           \"芜湖\",\n" +
                "           \"芜湖\",\n" +
                "           \"芜湖\",\n" +
                "           \"芜湖\",\n" +
                "            \"新生报到\",\n" +
                "           \"大司音乐食堂\",\n" +
                "           \"芜湖\",\n" +
                "            \"开饭时间：7:00-20:00\"\n" +
                "        ],\n" +
                "        \"images\":\n" +
                "        [\n" +
                "            \"https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=982417989,4270285044&fm=26&gp=0.jpg\",\n" +
                "            \"https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3549813042,1783346451&fm=26&gp=0.jpg\",\n" +
                "            \"https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=591674105,1532042351&fm=15&gp=0.jpg\"\n" +
                "        ]\n" +
                "    }\n" +
                "}"

const val favoriteString =
        "{\n" +
                "    \"status\":200,\n" +
                "    \"info\":\"success\",\n" +
                "    \"id\":0,\n" +
                "    \"version\":\"1.0\",\n" +
                "    \"data\":\n" +
                "    [\n" +
                "        {\n" +
                "            \"place_nickname\":\"最爱去的食堂\",\n" +
                "            \"place_id\":2\n" +
                "        },\n" +
                "        {\n" +
                "            \"place_nickname\":\"漂亮的新校门\",\n" +
                "            \"place_id\":1\n" +
                "        }\n" +
                "    ]\n" +
                "}"

const val screenString =
        "{\n" +
                "    \"status\":200,\n" +
                "    \"info\":\"success\",\n" +
                "    \"id\":0,\n" +
                "    \"version\":\"1.0\",\n" +
                "    \"data\":\n" +
                "    [\n" +
                "        1,2\n" +
                "    ]\n" +
                "}"

const val mapInfoString = "{\n" +
        "    \"status\":200,\n" +
        "    \"info\":\"success\",\n" +
        "    \"version\":\"1\"," +
        "    \"id\":0," +
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