package com.test.mini02_boardproject02

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.mini02_boardproject02.databinding.FragmentModifyUserBinding

class ModifyUserFragment : Fragment() {

    lateinit var fragmentModifyUserBinding: FragmentModifyUserBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentModifyUserBinding = FragmentModifyUserBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentModifyUserBinding.run {
            buttonModifyUserAccept.setOnClickListener {
                // 입력한 내용 가져오기
                val modifyUserPw1 = textInputEditTextModifyUserPw.text.toString()
                val modifyUserPw2 = textInputEditTextModifyUserPw2.text.toString()
                val modifyUserNickName = textInputEditTextModifyUserNickName.text.toString()
                val modifyUserAge = textInputEditTextModifyUserAge.text.toString()

                if(modifyUserPw1.isNotEmpty() || modifyUserPw2.isNotEmpty()){
                    if(modifyUserPw1 != modifyUserPw2){
                        val builder = MaterialAlertDialogBuilder(mainActivity)
                        builder.setTitle("비밀번호 오류")
                        builder.setMessage("비밀번호가 다릅니다")
                        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->

                            mainActivity.showSoftInput(textInputEditTextModifyUserPw)

                        }
                        builder.show()
                        return@setOnClickListener
                    }
                }
                if(modifyUserNickName.isEmpty()){
                    val builder = MaterialAlertDialogBuilder(mainActivity)
                    builder.setTitle("닉네임 입력 오류")
                    builder.setMessage("닉네임을 입력해주세요")
                    builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->

                        mainActivity.showSoftInput(textInputEditTextModifyUserNickName)

                    }
                    builder.show()
                    return@setOnClickListener
                }
                if(modifyUserAge.isEmpty()){
                    val builder = MaterialAlertDialogBuilder(mainActivity)
                    builder.setTitle("나이 입력 오류")
                    builder.setMessage("나이를 입력해주세요")
                    builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->

                        mainActivity.showSoftInput(textInputEditTextModifyUserAge)

                    }
                    builder.show()
                    return@setOnClickListener
                }
            }
        }

        return fragmentModifyUserBinding.root
    }

}