package com.test.mvvm.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.mvvm.MainActivity
import com.test.mvvm.repository.Test1Repository

class ViewModelTest2() : ViewModel() {
    var dataList = MutableLiveData<MutableList<TestData>>()
    // val tempList = mutableListOf<TestData>()

    init{
        getAll()
    }

//    fun addItem(testData: TestData){
//        tempList.add(testData)
//        dataList.value = tempList
//    }

    fun getAll(){
        // Repository로 부터 데이터를 가져와 설정
        dataList.value = Test1Repository.getAll(MainActivity.mainActivity)
    }
}

class TestData(var idx:Int, var data1:String, var data2:String)