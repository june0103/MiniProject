package com.test.mini02_boardproject02.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel() : ViewModel() {

    var userId = MutableLiveData<String>()
    var userPw = MutableLiveData<String>()
    var userPw2 = MutableLiveData<String>()
    var userNickname = MutableLiveData<String>()
    var userAge = MutableLiveData<Long>()
    var hobby1 = MutableLiveData<Boolean>()
    var hobby2 = MutableLiveData<Boolean>()
    var hobby3 = MutableLiveData<Boolean>()
    var hobby4 = MutableLiveData<Boolean>()
    var hobby5 = MutableLiveData<Boolean>()
    var hobby6 = MutableLiveData<Boolean>()
    var hobbyAll = MutableLiveData<Boolean>()

    fun reset(){
        userId.value = ""
        userPw.value = ""
        userPw2.value = ""
    }
}