package com.test.mini02_boardproject02

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.mini02_boardproject02.databinding.FragmentPostModifyBinding

class PostModifyFragment : Fragment() {

    lateinit var fragmentPostModifyBinding: FragmentPostModifyBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentPostModifyBinding = FragmentPostModifyBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentPostModifyBinding.run {
            toolbarPostModify.run {
                title = "글수정"
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.POST_MODIFY_FRAGMENT)
                }
                inflateMenu(R.menu.menu_post_modify)

                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.item_post_modify_camera -> {

                        }
                        R.id.item_post_modify_album -> {

                        }
                        R.id.item_post_modify_done -> {
                            // 입력한 내용을 가져온다.
                            val subject = textInputEditTextPostModifySubject.text.toString()
                            val text = textInputEditTextPostModifyText.text.toString()

                            if(subject.isEmpty()){
                                val builder = MaterialAlertDialogBuilder(mainActivity)
                                builder.setTitle("제목 입력 오류")
                                builder.setMessage("제목을 입력해주세요")
                                builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                                    mainActivity.showSoftInput(textInputEditTextPostModifySubject)
                                }
                                builder.show()
                                return@setOnMenuItemClickListener true
                            }

                            if(text.isEmpty()){
                                val builder = MaterialAlertDialogBuilder(mainActivity)
                                builder.setTitle("내용 입력 오류")
                                builder.setMessage("내용을 입력해주세요")
                                builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                                    mainActivity.showSoftInput(textInputEditTextPostModifyText)
                                }
                                builder.show()
                                return@setOnMenuItemClickListener true
                            }
                            mainActivity.removeFragment(MainActivity.POST_MODIFY_FRAGMENT)
                        }
                    }
                    true
                }
            }
        }

        return fragmentPostModifyBinding.root
    }

}