package com.amin.quran.repositorys

import com.amin.quran.local.SureDao
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val dao: SureDao
) {

    //local
//    suspend fun insertOrderInfoInDatabase(data : PanelEntity) = dao.insertOrderInfo(data)
}