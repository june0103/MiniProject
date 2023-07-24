package com.test.material08_carousel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.FullScreenCarouselStrategy
import com.google.android.material.carousel.HeroCarouselStrategy
import com.google.android.material.carousel.MultiBrowseCarouselStrategy
import com.test.material08_carousel.databinding.ActivityMainBinding
import com.test.material08_carousel.databinding.RowBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    val imageRes = arrayOf(
        R.drawable.image_1 ,R.drawable.image_2 ,R.drawable.image_3 ,R.drawable.image_4 ,
        R.drawable.image_5 ,R.drawable.image_6 ,R.drawable.image_7 ,R.drawable.image_8 ,
        R.drawable.image_9 ,R.drawable.image_10
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.run {
            recycleView.run {
                adapter = RecyclerViewAdapter()
                val carouselLayoutManager = CarouselLayoutManager()
                layoutManager = carouselLayoutManager
//                layoutManager = CarouselLayoutManager(MultiBrowseCarouselStrategy())
//                layoutManager = CarouselLayoutManager(HeroCarouselStrategy())
//                layoutManager = CarouselLayoutManager(FullScreenCarouselStrategy())

            }

        }

    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderClass>() {
        inner class ViewHolderClass(rowBinding: RowBinding) : RecyclerView.ViewHolder(rowBinding.root){

            var carouselImageView : ImageView

            init{
                carouselImageView = rowBinding.carouselImageView

                rowBinding.root.setOnClickListener {
                    activityMainBinding.imageView.setImageResource(imageRes[adapterPosition])
                }

            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            val rowBinding = RowBinding.inflate(layoutInflater)
            val viewHolderClass = ViewHolderClass(rowBinding)

            return viewHolderClass
        }

        override fun getItemCount(): Int {
            return imageRes.size
        }

        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
            holder.carouselImageView.setImageResource(imageRes[position])
        }
    }

}