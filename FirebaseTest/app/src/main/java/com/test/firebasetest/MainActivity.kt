package com.test.firebasetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.test.firebasetest.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(activityMainBinding.root)

        activityMainBinding.run {
            button.setOnClickListener {
                val t1 = TestData(100,"문자열1",true)
                val t2 = TestData(200,"문자열2",false)
                val t3 = TestData(300,"문자열3",true)

                // firebase 객체 생성
                val database = FirebaseDatabase.getInstance()
                // TestData에 접근. 있다면 접근하지만 없다면 생성된다
                val testDataRef = database.getReference("TestData")

                // 저장
                testDataRef.push().setValue(t1)
                testDataRef.push().setValue(t2)
                testDataRef.push().setValue(t3)
            }

            button2.setOnClickListener {
                thread{
                    // firebase 객체 생성
                    val database = FirebaseDatabase.getInstance()
                    // TestData에 접근
                    val testDataRef = database.getReference("TestData")

                    // 전부 가져오기
                    testDataRef.get().addOnCompleteListener {
                        textView.text = ""
                        // 가져온 데이터의 수 만큼 반복
                        for(a1 in it.result.children){
                            val data1 = a1.child("data1").value as Long
                            val data2 = a1.child("data2").value as String
                            val data3 = a1.child("data3").value as Boolean

                            textView.append("data1 : ${data1}\n")
                            textView.append("data2 : ${data2}\n")
                            textView.append("data3 : ${data3}\n")
                        }
                    }
                }
            }

            button3.setOnClickListener {
                // firebase 객체 생성
                val database = FirebaseDatabase.getInstance()
                // TestData에 접근
                val testDataRef = database.getReference("TestData")

                // data1 이 200인 데이터만 불러오기
                // orderByChild("data1") : testDataRef안에 있는 객체 안의 데이터 이름 설정
                // equalTo : 같은것
                // firebase database의 규칙에 .indexOn 설정을 해줘야 한다.

                // equalTo : 같은 것
                // endAt : 지정한 값보다 작거나 같은 것
                // endBefore : 지정한 값보다 작은 것
                // startAt : 지정한 값보다 크거나 같은 것
                // startAfter : 지정한 값보다 큰것
                testDataRef.orderByChild("data1").equalTo(200.0).get().addOnCompleteListener {
                    textView.text = ""
                    // 가져온 데이터의 수 만큼 반복
                    for(a1 in it.result.children){
                        val data1 = a1.child("data1").value as Long
                        val data2 = a1.child("data2").value as String
                        val data3 = a1.child("data3").value as Boolean

                        textView.append("data1 : ${data1}\n")
                        textView.append("data2 : ${data2}\n")
                        textView.append("data3 : ${data3}\n")
                    }
                }
            }

            button4.setOnClickListener {
                // firebase 객체 생성
                val database = FirebaseDatabase.getInstance()
                // TestData에 접근
                val testDataRef = database.getReference("TestData")

                testDataRef.orderByChild("data1").equalTo(200.0).get().addOnCompleteListener {
                    for(a1 in it.result.children){
                        // data2에 새로운 데이터 설정
                        a1.ref.child("data2").setValue("새로운 문자열")
                    }
                }
            }

            button5.setOnClickListener {
                val database = FirebaseDatabase.getInstance()
                val testDataRef = database.getReference("TestData")

                // data1이 100인 데이터를 가져온다.
                testDataRef.orderByChild("data1").equalTo(100.0).get().addOnCompleteListener {
                    for (a1 in it.result.children) {
                        // 해당 데이터를 삭제한다.
                        a1.ref.removeValue()
                    }
                }
            }
            
        }
    }
}

data class TestData(var data1:Long, var data2:String, var data3:Boolean)