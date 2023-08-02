package com.test.mvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.test.mvvm.databinding.ActivityResultBinding
import com.test.mvvm.vm.ViewModelTest1
import com.test.mvvm.vm.ViewModelTest2

class ResultActivity : AppCompatActivity() {

    lateinit var activityResultBinding: ActivityResultBinding

    lateinit var viewModelTest1: ViewModelTest1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityResultBinding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(activityResultBinding.root)

        // ViewModel 객체를 가져온다
        viewModelTest1 = ViewModelProvider(this)[ViewModelTest1::class.java]

        activityResultBinding.run {
            // viewModel 객체가 가지고있는 프로퍼티에 대한 감시자를 설정
            viewModelTest1.run {
                data1.observe(this@ResultActivity){
                    textViewResultData1.text = it
                }
                data2.observe(this@ResultActivity){
                    textViewResultData2.text = it
                }
            }

            // 데이터 가져오기
            viewModelTest1.getOne(intent.getIntExtra("testIdx", 0))

            // viewModel 객체에 새로운 값을 설정
//            viewModelTest2.data1.value = "새로운 문자열1"
//            viewModelTest2.data2.value = "새로운 문자열2"
        }
    }
}