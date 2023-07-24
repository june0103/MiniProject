package com.test.material05_floatingactionbuttons

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.material05_floatingactionbuttons.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.run {
            floatingActionButton.setOnClickListener{
                textView.text = "플로팅액션버튼을 눌렀습니다"

                // 플로팅버튼2가 보인다면 가리고, 안보이면 나타내고
                if(floatingActionButton2.isShown){
                    floatingActionButton2.hide()
                } else{
                    floatingActionButton2.show()
                }

                // 플로팅버튼3이 펼져져있다면 접고, 접혀있으면 펼치고
                if(floatingActionButton3.isExtended){
                    floatingActionButton3.shrink()
                } else{
                    floatingActionButton3.extend()
                }

            }
        }
    }
}