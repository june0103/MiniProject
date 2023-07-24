package com.test.material04_button

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.material04_button.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.run {
            button.setOnClickListener {
                textView.text = "첫 번째 버튼을 눌렀습니다."
            }
            toggleGroup.run{
                // 체크 상태를 설정한다.
                check(R.id.buttonToggle1)
                check(R.id.buttonToggle3)
                // 체크 해제를 설정한다.
                uncheck(R.id.buttonToggle2)

                // 리스너
                addOnButtonCheckedListener { group, checkedId, isChecked ->
                    textView.text = ""
                    when(checkedId){
                        R.id.buttonToggle1 -> {
                            if(isChecked){
                                textView.append("토글 버튼1이 체크 되었습니다\n")
                            } else{
                                textView.append("토글 버튼1이 체크 해제되었습니다\n")
                            }
                        }
                        R.id.buttonToggle2 -> {
                            textView.append("토글 버튼2이 선택되어 있습니다\n")
                        }
                        R.id.buttonToggle3 -> {
                            textView.append("토글 버튼3이 선택되어 있습니다\n")
                        }
                    }
                }
            }

            toggleGroup2.run {
                check(R.id.buttonToggle5)
            }

            button6.setOnClickListener {
                textView.text = ""

                for(id in toggleGroup.checkedButtonIds){

                    when(id){
                        R.id.buttonToggle1 -> {
                            textView.append("토글 버튼1이 선택되어 있습니다\n")
                        }
                        R.id.buttonToggle2 -> {
                            textView.append("토글 버튼2이 선택되어 있습니다\n")
                        }
                        R.id.buttonToggle3 -> {
                            textView.append("토글 버튼3이 선택되어 있습니다\n")
                        }
                    }
                }

                // single selection
                when(toggleGroup2.checkedButtonId){
                    R.id.buttonToggle4 -> {
                        textView.append("토글 버튼 4가 선택되어 있습니다\n")
                    }
                    R.id.buttonToggle5 -> {
                        textView.append("토글 버튼 5가 선택되어 있습니다\n")
                    }
                    R.id.buttonToggle6 -> {
                        textView.append("코글 버튼 6이 선택되어 있습니다\n")
                    }
                }
            }
        }
    }
}