package com.test.mvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.test.mvvm.databinding.ActivityAddBinding
import com.test.mvvm.repository.Test1Repository
import com.test.mvvm.vm.TestData
import com.test.mvvm.vm.ViewModelTest2

class AddActivity : AppCompatActivity() {

    lateinit var activityAddBinding: ActivityAddBinding

    lateinit var viewModelTest2: ViewModelTest2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityAddBinding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(activityAddBinding.root)

        // ViewModel을 받아온다
        viewModelTest2 = ViewModelProvider(MainActivity.mainActivity)[ViewModelTest2::class.java]

        activityAddBinding.run {
            buttonAdd.setOnClickListener {
                val data1 = editTextAddData1.text.toString()
                val data2 = editTextAddData2.text.toString()

                val t1 = TestData(0, data1, data2)

                // viewModel객체의 리스트에 담아준다
                // viewModelTest2.dataList.value?.add(t1)
                // viewModelTest2.addItem(t1)

                // 데이터베이스 저장
                Test1Repository.addData(this@AddActivity, t1)

                finish()
            }
        }
    }
}