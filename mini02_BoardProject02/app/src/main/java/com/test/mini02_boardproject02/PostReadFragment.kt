package com.test.mini02_boardproject02

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.test.mini02_boardproject02.databinding.FragmentPostReadBinding
import com.test.mini02_boardproject02.vm.PostViewModel

class PostReadFragment : Fragment() {

    lateinit var fragmentPostReadBinding: FragmentPostReadBinding
    lateinit var mainActivity: MainActivity

    lateinit var postViewModel: PostViewModel

    // 게시글 인덱스번호
    var readPostIdx = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentPostReadBinding = FragmentPostReadBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        postViewModel = ViewModelProvider(mainActivity)[PostViewModel::class.java]
        postViewModel.run {
            postSubject.observe(mainActivity){
                fragmentPostReadBinding.textInputEditTextPostReadSubject.setText(it)
            }
            postText.observe(mainActivity){
                fragmentPostReadBinding.textInputEditTextPostReadText.setText(it)
            }
            postNickname.observe(mainActivity){
                fragmentPostReadBinding.textInputEditTextPostReadNickName.setText(it)
            }
            postWriteDate.observe(mainActivity){
                fragmentPostReadBinding.textInputEditTextPostReadWriteDate.setText(it)
            }
            postFileName.observe(mainActivity){
                if(it == "none"){
                    fragmentPostReadBinding.imageViewPostRead.visibility = View.GONE
                }
            }
            postImage.observe(mainActivity){
                fragmentPostReadBinding.imageViewPostRead.setImageBitmap(it)
            }
        }

        fragmentPostReadBinding.run {
            toolbarPostRead.run {
                title = "글읽기"
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.POST_WRITE_FRAGMENT)
                    mainActivity.removeFragment(MainActivity.POST_READ_FRAGMENT)
                }
                inflateMenu(R.menu.menu_post_read)

                setOnMenuItemClickListener {

                    when(it.itemId){
                        R.id.item_post_read_modify -> {
                            // 수정 프래그먼트를 만들지않고 수정 하는 방법
//                            if(textInputEditTextPostReadSubject.isEnabled == false) {
//                                textInputEditTextPostReadSubject.isEnabled = true
//                                textInputEditTextPostReadText.isEnabled = true
//                            } else{
//                                textInputEditTextPostReadSubject.isEnabled = false
//                                textInputEditTextPostReadText.isEnabled = false
//                            }

                            mainActivity.replaceFragment(MainActivity.POST_MODIFY_FRAGMENT,true,null)
                        }
                        R.id.item_post_read_delete -> {
                            mainActivity.removeFragment(MainActivity.POST_WRITE_FRAGMENT)
                            mainActivity.removeFragment(MainActivity.POST_READ_FRAGMENT)
                        }
                    }

                    true
                }

                textInputEditTextPostReadSubject.run {
                    setTextColor(Color.BLACK)
                }
                textInputEditTextPostReadText.run {
                    setTextColor(Color.BLACK)
                }
                textInputEditTextPostReadNickName.run {
                    setTextColor(Color.BLACK)
                }
                textInputEditTextPostReadWriteDate.run {
                    setTextColor(Color.BLACK)
                }
            }
        }

        // 게시글 인덱스 번호
        readPostIdx = arguments?.getLong("readPostIdx")!!
        // 게시글 정보
        postViewModel.setPostReadData(readPostIdx.toDouble())

        return fragmentPostReadBinding.root
    }

}