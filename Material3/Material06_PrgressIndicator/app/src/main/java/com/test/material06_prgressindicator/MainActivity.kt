package com.test.material06_prgressindicator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.material06_prgressindicator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.run {
            button.setOnClickListener {
                // 현재의 progress 값에 10을 더해준다.
//                progress1.progress = progress1.progress + 10
//                progress2.progress = progress2.progress + 10

                progress1.setProgressCompat(progress1.progress + 10, true)
                progress2.setProgressCompat(progress2.progress + 10, true)
                progress3.setProgressCompat(progress3.progress + 10, true)
                progress4.setProgressCompat(progress4.progress + 10, true)
            }
        }
    }
}