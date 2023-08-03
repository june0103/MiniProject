package com.test.mini02_boardproject02

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.test.mini02_boardproject02.databinding.FragmentLoginBinding
import com.test.mini02_boardproject02.repository.UserRepository
import com.test.mini02_boardproject02.vm.UserViewModel

class LoginFragment : Fragment() {

    lateinit var fragmentLoginBinding : FragmentLoginBinding
    lateinit var mainActivity: MainActivity

    lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        userViewModel = ViewModelProvider(mainActivity)[UserViewModel::class.java]
        userViewModel.run {
            userId.observe(mainActivity){
                fragmentLoginBinding.textInputEditTextLoginUserId.setText(it)
            }
            userPw.observe(mainActivity){
                fragmentLoginBinding.textInputEditTextLoginUserPw.setText(it)
            }
        }

        fragmentLoginBinding.run {
            // toolbar
            toolbarLogin.run {
                title = "로그인"
            }

            buttonLoginJoin.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.JOIN_FRAGMENT,true,null)
            }

            buttonLoginSubmit.setOnClickListener {

                loginSubmit()
            }

            // 비빌번호 입력요소
            textInputEditTextLoginUserPw.run{
                setOnEditorActionListener { textView, i, keyEvent ->
                    loginSubmit()
                    true
                }
            }
        }



        return fragmentLoginBinding.root
    }

    // 로그인버튼, 비밀번호 입력요소에서 엔터키를 누를 때
    fun loginSubmit(){
        fragmentLoginBinding.run {
            // 입력한 내용 가져오기
            val loginUserId = textInputEditTextLoginUserId.text.toString()
            val loginUserPw = textInputEditTextLoginUserPw.text.toString()

            if(loginUserId.isEmpty()) {
                val builder = MaterialAlertDialogBuilder(mainActivity)
                builder.setTitle("로그인 오류")
                builder.setMessage("아이디를 입력해주세요")
                builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->

                    mainActivity.showSoftInput(textInputEditTextLoginUserId)

                }
                builder.show()
                return
            }

            if(loginUserPw.isEmpty()) {
                val builder = MaterialAlertDialogBuilder(mainActivity)
                builder.setTitle("로그인 오류")
                builder.setMessage("비밀번호를 입력해주세요")
                builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->

                    mainActivity.showSoftInput(textInputEditTextLoginUserPw)

                }
                builder.show()
                return
            }

            UserRepository.getUserInfoByUserId(loginUserId){
                //  가져온 데이터가 없다면
                if(it.result.exists() == false){
                    val builder = MaterialAlertDialogBuilder(mainActivity)
                    builder.setTitle("로그인 오류")
                    builder.setMessage("존재하지 않는 아이디 입니다")
                    builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                        textInputEditTextLoginUserId.setText("")
                        textInputEditTextLoginUserPw.setText("")
                        mainActivity.showSoftInput(textInputEditTextLoginUserId)
                    }
                    builder.show()
                }
                // 가져온 데이터가 있다면
                else{
                    for(c1 in it.result.children){
                        // 가져온 데이터에서 비밀번호 추출
                        val userPw = c1.child("userPw").value as String

                        // 입력비밀번호와 가져온 비밀번호가 다르다면
                        if(loginUserPw != userPw){
                            val builder = MaterialAlertDialogBuilder(mainActivity)
                            builder.setTitle("로그인 오류")
                            builder.setMessage("잘못된 비밀번호 입니다")
                            builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                                textInputEditTextLoginUserPw.setText("")
                                mainActivity.showSoftInput(textInputEditTextLoginUserPw)
                            }
                            builder.show()
                        }
                        // 비밀번호가 같다면
                        else{

                            // 로그인한 사용자 정보를 가져온다
                            val userIdx = c1.child("userIdx").value as Long
                            val userId = c1.child("userId").value as String
                            val userPw = c1.child("userPw").value as String
                            val userNickname = c1.child("userNickname").value as String
                            val userAge = c1.child("userAge").value as Long
                            val hobby1 = c1.child("hobby1").value as Boolean
                            val hobby2 = c1.child("hobby2").value as Boolean
                            val hobby3 = c1.child("hobby3").value as Boolean
                            val hobby4 = c1.child("hobby4").value as Boolean
                            val hobby5 = c1.child("hobby5").value as Boolean
                            val hobby6 = c1.child("hobby6").value as Boolean

                            mainActivity.loginUserClass = UserClass(userIdx, userId, userPw, userNickname, userAge, hobby1, hobby2, hobby3, hobby4, hobby5, hobby6)
                            Snackbar.make(fragmentLoginBinding.root, "로그인 되었습니다", Snackbar.LENGTH_SHORT).show()

                            mainActivity.replaceFragment(MainActivity.BOARD_MAIN_FRAGMENT,false,null)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        userViewModel.reset()
    }
}