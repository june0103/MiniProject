package com.test.mini02_boardproject02

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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