package com.test.mvvm.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.mvvm.MainActivity
import com.test.mvvm.repository.Test1Repository

class ViewModelTest1() : ViewModel() {

    var data1 = MutableLiveData<String>()
    var data2 = MutableLiveData<String>()

    fun getOne(testIdx:Int){
        val t1 = Test1Repository.getOne(MainActivity.mainActivity, testIdx)

        data1.value = t1.data1
        data2.value = t1.data2
    }
}