package com.test.mini02_boardproject02

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.mini02_boardproject02.databinding.FragmentJoinBinding
import com.test.mini02_boardproject02.vm.UserViewModel

class JoinFragment : Fragment() {

    lateinit var fragmentJoinBinding : FragmentJoinBinding
    lateinit var mainActivity: MainActivity

    lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        fragmentJoinBinding = FragmentJoinBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        userViewModel = ViewModelProvider(mainActivity)[UserViewModel::class.java]
        userViewModel.run {
            userId.observe(mainActivity){
                fragmentJoinBinding.textInputEditTextJoinUserId.setText(it)
            }
            userPw.observe(mainActivity){
                fragmentJoinBinding.textInputEditTextJoinUserPw.setText(it)
            }
            userPw2.observe(mainActivity){
                fragmentJoinBinding.textInputEditTextJoinUserPw2.setText(it)
            }
        }

        fragmentJoinBinding.run {
            // toolbar
            toolbarJoin.run {
                title = "회원가입"
                // 백 버튼 아이콘
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.JOIN_FRAGMENT)
                }
            }

            buttonJoinNext.setOnClickListener {
                next()
            }

            // 비밀번호 확인 입력요소
            textInputEditTextJoinUserPw2.run {
                setOnEditorActionListener { textView, i, keyEvent ->
                    next()
                    true
                }
            }

            // 사용자 아이디에 포커스
            mainActivity.showSoftInput(textInputEditTextJoinUserId)
        }

        return fragmentJoinBinding.root
    }

    // 다음 버튼, 비밀번호 확인 입력 요소에서 엔터키를 눌렀을 경우
    fun next(){
        fragmentJoinBinding.run {
            val joinUserId = textInputEditTextJoinUserId.text.toString()
            val joinUserPw = textInputEditTextJoinUserPw.text.toString()
            val joinUserPw2 = textInputEditTextJoinUserPw2.text.toString()

            if(joinUserId.isEmpty()) {
                val builder = MaterialAlertDialogBuilder(mainActivity)
                builder.setTitle("로그인 오류")
                builder.setMessage("아이디를 입력해주세요")
                builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->

                    mainActivity.showSoftInput(textInputEditTextJoinUserId)

                }
                builder.show()
                return
            }

            if(joinUserPw.isEmpty()) {
                val builder = MaterialAlertDialogBuilder(mainActivity)
                builder.setTitle("비밀번호 오류")
                builder.setMessage("비밀번호를 입력해주세요")
                builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->

                    mainActivity.showSoftInput(textInputEditTextJoinUserPw)

                }
                builder.show()
                return
            }

            if(joinUserPw2.isEmpty()) {
                val builder = MaterialAlertDialogBuilder(mainActivity)
                builder.setTitle("비밀번호 오류")
                builder.setMessage("비밀번호를 입력해주세요")
                builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->

                    mainActivity.showSoftInput(textInputEditTextJoinUserPw2)

                }
                builder.show()
                return
            }

            if(joinUserPw != joinUserPw2){
                val builder = MaterialAlertDialogBuilder(mainActivity)
                builder.setTitle("비빌번호 오류")
                builder.setMessage("비밀번호가 일치하지 않습니다.")
                builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    textInputEditTextJoinUserPw.setText("")
                    textInputEditTextJoinUserPw2.setText("")
                    mainActivity.showSoftInput(textInputEditTextJoinUserPw)
                }
                builder.show()
                return
            }

            val newBundle = Bundle()
            newBundle.putString("joinUserId",joinUserId)
            newBundle.putString("joinUserPw",joinUserPw)
            mainActivity.replaceFragment(MainActivity.ADD_USER_INFO_FRAGMENT,true,newBundle)
        }

    }

    override fun onResume() {
        super.onResume()
        userViewModel.reset()
    }

}