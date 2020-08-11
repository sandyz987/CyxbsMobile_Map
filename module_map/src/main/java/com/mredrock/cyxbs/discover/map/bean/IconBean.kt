package com.mredrock.cyxbs.discover.map.bean

/**
 * 添加icon的数据类，与MapLayout搭配使用
 */
data class IconBean(
        val id: Int,
        val sx: Float,
        val sy: Float,
        val leftX: Float,
        val rightX: Float,
        val leftY: Float,
        val rightY: Float
)
