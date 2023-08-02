package com.test.mvvm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.test.mvvm.databinding.ActivityMainBinding
import com.test.mvvm.databinding.RowBinding
import com.test.mvvm.vm.ViewModelTest1
import com.test.mvvm.vm.ViewModelTest2

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    // ViewModel
    lateinit var viewModelTest1: ViewModelTest1
    lateinit var viewModelTest2: ViewModelTest2

    companion object{
        lateinit var mainActivity: MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        mainActivity = this

        // viewModel 객체를 받아온다
        // ViewModelProvider는 뷰모델 객체를 생성하고 관리하는 클래스
        // owner는 뷰의 생명주기를 관리하는 인터페이스로 주로 액티비티나 프래그먼트가 해당역할을 한다
        viewModelTest1 = ViewModelProvider(this)[ViewModelTest1::class.java]
        viewModelTest2 = ViewModelProvider(this)[ViewModelTest2::class.java]

        activityMainBinding.run {

            buttonMain.setOnClickListener {
                val newIntent = Intent(this@MainActivity, AddActivity::class.java)
                startActivity(newIntent)
            }

            recyclerViewMain.run {
                adapter = MainRecyclerAdapter()
                layoutManager = LinearLayoutManager(this@MainActivity)
                addItemDecoration(MaterialDividerItemDecoration(this@MainActivity, MaterialDividerItemDecoration.VERTICAL))
            }

            // 데이터가 변경될때 마다 리사이클러뷰를 업데이트하는 옵저버 등록
            viewModelTest2.run {
                dataList.observe(this@MainActivity){
                    recyclerViewMain.adapter?.notifyDataSetChanged()
                }
            }

        }
    }

    inner class MainRecyclerAdapter : RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>(){
        inner class MainViewHolder(rowBinding: RowBinding) : RecyclerView.ViewHolder(rowBinding.root){
            var textViewRow:TextView

            init{
                textViewRow = rowBinding.textViewRow

                rowBinding.root.setOnClickListener {
                    val newIntent = Intent(this@MainActivity, ResultActivity::class.java)

                    // 값을 가지고 있는 객체 추출
                    val t1 = viewModelTest2.dataList.value?.get(adapterPosition)

                    newIntent.putExtra("testIdx",t1?.idx)

//                    // viewModel 객체에 새로운 값을 설정
//                    viewModelTest1.data1.value = t1?.data1
//                    viewModelTest1.data2.value = t1?.data2


                    startActivity(newIntent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val rowBinding = RowBinding.inflate(layoutInflater)
            val mainViewHolder = MainViewHolder(rowBinding)

            rowBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return mainViewHolder
        }

        override fun getItemCount(): Int {
            return viewModelTest2.dataList.value?.size!!
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            holder.textViewRow.text = viewModelTest2.dataList.value?.get(position)?.data1
        }
    }

    override fun onResume() {
        super.onResume()

        // viewModel 에 있는 모든 데이터를 가져오는 메서드
        viewModelTest2.getAll()
    }
}