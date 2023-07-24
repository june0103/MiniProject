package com.test.material03_elevation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// elevation : UI 요소에 그림자를 줘서 공중에 떠있는 듯한 효과
// elevation 설정을 했는데도 그림자가 나타나지 않을 경우
// 1. translationZ 에 elevation과 동일하게 설정
// 2. 배경색이 투명색인 경우 1번을 설정해도 효과가 나타나지 않아 배경색을 설정해준다

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}