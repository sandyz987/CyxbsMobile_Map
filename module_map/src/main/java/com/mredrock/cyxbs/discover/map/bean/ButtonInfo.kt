package com.mredrock.cyxbs.discover.map.bean

import com.google.gson.annotations.SerializedName

/**
 *@author zhangzhe
 *@date 2020/8/11
 *@description
 */

data class InfoItem(
        var title: String,
        var code: String,
        @SerializedName("is_hot")
        var isHot: Boolean
)

data class ButtonInfo(
        @SerializedName("button_info")
        var buttonInfo: List<InfoItem>
)