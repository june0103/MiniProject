package com.test.mini02_boardproject02

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.transition.MaterialSharedAxis
import com.test.mini02_boardproject02.databinding.FragmentBoardMainBinding
import com.test.mini02_boardproject02.databinding.HeaderBoardMainBinding

class BoardMainFragment : Fragment() {

    lateinit var fragmentBoardMainBinding: FragmentBoardMainBinding
    lateinit var mainActivity: MainActivity

    var newFragment: Fragment? = null
    var oldFragment: Fragment? = null

    companion object {
        val POST_LIST_FRAGMENT = "PostListFragment"
        val MODIFY_USER_FRAGMENT = "ModifyUserFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        fragmentBoardMainBinding = FragmentBoardMainBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentBoardMainBinding.run {

            replaceFragment(POST_LIST_FRAGMENT, false, false, null)

            toolbarBoardMain.run {
                title = "게시판 메인"
                setNavigationIcon(R.drawable.menu_24px)
                setNavigationOnClickListener {
                    // 네비게이션 뷰
                    drawerLayoutBoardMain.open()
                }
            }

            // DrawerView
            navigationViewBoardMain.run {

                // 헤더설정
                val headerBoardMainBinding = HeaderBoardMainBinding.inflate(inflater)
                headerBoardMainBinding.textViewHeaderBoardMainNickName.text = "아무개님"
                addHeaderView(headerBoardMainBinding.root)

                // 항목 선택 동작 리스너
                setNavigationItemSelectedListener {

                    // 선택한 메뉴를 체크상태로 유지
                    // it.isChecked = true

                    // 선택한 메뉴 id로 분기
                    when (it.itemId) {
                        // 전체게시판
                        R.id.item_board_main_all -> {
                            replaceFragment(POST_LIST_FRAGMENT, false, false, null)
                            drawerLayoutBoardMain.close()
                        }
                        // 자유게시판
                        R.id.item_board_main_free -> {
                            replaceFragment(POST_LIST_FRAGMENT, false, false, null)
                            drawerLayoutBoardMain.close()
                        }
                        // 유머게시판
                        R.id.item_board_main_gag -> {
                            replaceFragment(POST_LIST_FRAGMENT, false, false, null)
                            drawerLayoutBoardMain.close()
                        }
                        // 질문게시판
                        R.id.item_board_main_qna -> {
                            replaceFragment(POST_LIST_FRAGMENT, false, false, null)
                            drawerLayoutBoardMain.close()
                        }
                        // 스포츠게시판
                        R.id.item_board_main_sports -> {
                            replaceFragment(POST_LIST_FRAGMENT, false, false, null)
                            drawerLayoutBoardMain.close()
                        }
                        // 사용자 정보 수정
                        R.id.item_board_main_user_info -> {
                            replaceFragment(MODIFY_USER_FRAGMENT,true, false,null)
                            drawerLayoutBoardMain.close()
                        }
                        // 로그아웃
                        R.id.item_board_main_logout -> {
                            mainActivity.replaceFragment(MainActivity.LOGIN_FRAGMENT, false, null)
                        }
                        // 회원탈퇴
                        R.id.item_board_main_sign_out -> {
                            mainActivity.replaceFragment(MainActivity.LOGIN_FRAGMENT, false, null)
                        }
                    }

                    // 닫아준다
//                    drawerLayoutBoardMain.close()

                    false
                }
            }
        }
        return fragmentBoardMainBinding.root
    }

    // 지정한 Fragment를 보여주는 메서드
    fun replaceFragment(name: String, addToBackStack: Boolean, animate: Boolean, bundle: Bundle?) {
        // Fragment 교체 상태로 설정한다.
        val fragmentTransaction = mainActivity.supportFragmentManager.beginTransaction()

        // newFragment 에 Fragment가 들어있으면 oldFragment에 넣어준다.
        if (newFragment != null) {
            oldFragment = newFragment
        }

        // 새로운 Fragment를 담을 변수
        newFragment = when (name) {
            POST_LIST_FRAGMENT -> PostListFragment()
            MODIFY_USER_FRAGMENT -> ModifyUserFragment()

            else -> Fragment()
        }

        newFragment?.arguments = bundle

        if (animate == true) {
            // 애니메이션 설정
            if (oldFragment != null) {
                oldFragment?.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
                oldFragment?.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
                oldFragment?.enterTransition = null
                oldFragment?.returnTransition = null
            }

            newFragment?.exitTransition = null
            newFragment?.reenterTransition = null
            newFragment?.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
            newFragment?.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        } else {
            if (oldFragment != null) {
                oldFragment?.exitTransition = null
                oldFragment?.reenterTransition = null
                oldFragment?.enterTransition = null
                oldFragment?.returnTransition = null
            }

            newFragment?.exitTransition = null
            newFragment?.reenterTransition = null
            newFragment?.enterTransition = null
            newFragment?.returnTransition = null
        }
        // Fragment를 교채한다.
        fragmentTransaction.replace(R.id.boardMainContainer, newFragment!!)

        if (addToBackStack == true) {
            // Fragment를 Backstack에 넣어 이전으로 돌아가는 기능이 동작할 수 있도록 한다.
            fragmentTransaction.addToBackStack(name)
        }

        // 교체 명령이 동작하도록 한다.
        fragmentTransaction.commit()
    }


    // Fragment를 BackStack에서 제거한다.
    fun removeFragment(name: String) {
        mainActivity.supportFragmentManager.popBackStack(
            name,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }
}