package com.test.mini02_boardproject02

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.mini02_boardproject02.databinding.FragmentBoardMainBinding
import com.test.mini02_boardproject02.databinding.HeaderBoardMainBinding

class BoardMainFragment : Fragment() {

    lateinit var fragmentBoardMainBinding: FragmentBoardMainBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        fragmentBoardMainBinding = FragmentBoardMainBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentBoardMainBinding.run {
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
                    when(it.itemId) {
                        // 전체게시판
                        R.id.item_board_main_all -> {
                            textViewTest.text = "전체게시판"
                        }
                        // 자유게시판
                        R.id.item_board_main_free -> {
                            textViewTest.text = "자유게시판"
                        }
                        // 유머게시판
                        R.id.item_board_main_gag -> {
                            textViewTest.text = "유머게시판"
                        }
                        // 질문게시판
                        R.id.item_board_main_qna -> {
                            textViewTest.text = "질문게시판"
                        }
                        // 스포츠게시판
                        R.id.item_board_main_sports -> {
                            textViewTest.text = "스포츠게시판"
                        }
                        // 사용자 정보 수정
                        R.id.item_board_main_user_info -> {
                            mainActivity.replaceFragment(MainActivity.MODIFY_USER_FRAGMENT, true, null)
                        }
                        // 로그아웃
                        R.id.item_board_main_logout -> {
                            mainActivity.removeFragment(MainActivity.BOARD_MAIN_FRAGMENT)
                        }
                        // 회원탈퇴
                        R.id.item_board_main_sign_out -> {
                            mainActivity.removeFragment(MainActivity.BOARD_MAIN_FRAGMENT)
                        }
                    }

                    // 닫아준다
                    drawerLayoutBoardMain.close()

                    true
                }
            }
        }

        return fragmentBoardMainBinding.root
    }

}