package com.test.material07_bottomsheet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.test.material07_bottomsheet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.run {

            // BottomSheet의 동작을 제어하는 객체
            val sheetBehavior = BottomSheetBehavior.from(include1.bottomSheet)

            // 사라지게한다
            sheetBehavior.isHideable = true
            sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            // BottomSheet가 접힌상태로 80dp만큼 튀어나오게
//            sheetBehavior.isHideable = false
//            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//            sheetBehavior.peekHeight = 80

            button.setOnClickListener {
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            button2.setOnClickListener {
                if(sheetBehavior.isHideable){
                    sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                } else {
                    sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }

            include1.run {
                buttonBottom1.setOnClickListener {
                    textView.text = "추가하기"
                    if(sheetBehavior.isHideable){
                        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    } else {
                        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
                buttonBottom2.setOnClickListener {
                    textView.text = "재밌어요"
                    if(sheetBehavior.isHideable){
                        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    } else {
                        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
                buttonBottom3.setOnClickListener {
                    textView.text = "좋아요"
                    if(sheetBehavior.isHideable){
                        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    } else {
                        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
                buttonBottom4.setOnClickListener {
                    textView.text = "추천"
                    if(sheetBehavior.isHideable){
                        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    } else {
                        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
            }
        }
    }
}