package com.wj.sampleproject.repository

import com.wj.sampleproject.net.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * 体系相关数据仓库
 */
class SystemRepository : KoinComponent {

    /** 网络请求对象 */
    private val mWebService: WebService by inject()

    /**
     * 获取体系目录列表
     */
    suspend fun getSystemCategoryList() = withContext(Dispatchers.IO) {
        mWebService.getSystemCategoryList()
    }

    /**
     * 获取导航列表
     */
    suspend fun getNavigationList() = withContext(Dispatchers.IO) {
        mWebService.getNavigationList()
    }
}