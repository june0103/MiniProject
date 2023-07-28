package com.test.mini02_boardproject02

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.mini02_boardproject02.databinding.FragmentPostWriteBinding

class PostWriteFragment : Fragment() {

    lateinit var fragmentPostWriteBinding: FragmentPostWriteBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        fragmentPostWriteBinding = FragmentPostWriteBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentPostWriteBinding.run {
            toolbarPostWrite.run {
                title = "글 작성"
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.POST_WRITE_FRAGMENT)
                }
                inflateMenu(R.menu.menu_post_write)
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.item_post_write_album -> {

                        }
                        R.id.item_post_write_camera -> {

                        }
                        R.id.item_post_write_done -> {
                                mainActivity.replaceFragment(MainActivity.POST_READ_FRAGMENT,true,null)
                        }
                    }
                    true
                }
            }
        }

        return fragmentPostWriteBinding.root
    }

}