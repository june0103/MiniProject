package com.test.material09_dialog

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.material09_dialog.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.run {
            button.setOnClickListener {
                val builder = AlertDialog.Builder(this@MainActivity).apply {
                    setTitle("Basic Alert Dialog")
                    setMessage("Basic Alert Dialog 입니다")
                    setNeutralButton("Neutral") { dialogInterface: DialogInterface, i: Int ->
                        textView.text = "Basic Alert Dialog - Neutral"
                    }
                    setPositiveButton("Positive") { dialogInterface: DialogInterface, i: Int ->
                        textView.text = "Basic Alert Dialog - positive"
                    }
                    setNegativeButton("Negative") { dialogInterface: DialogInterface, i: Int ->
                        textView.text = "Basic Alert Dialog - Negative"
                    }
                }
                builder.show()
            }

            button2.setOnClickListener {
                val builder = MaterialAlertDialogBuilder(this@MainActivity).apply {
                    setTitle("Material Alert Dialog")
                    setMessage("Material Alert Dialog 입니다")
                    setNeutralButton("Neutral") { dialogInterface: DialogInterface, i: Int ->
                        textView.text = "Material Alert Dialog - Neutral"
                    }
                    setPositiveButton("Positive") { dialogInterface: DialogInterface, i: Int ->
                        textView.text = "Material Alert Dialog - positive"
                    }
                    setNegativeButton("Negative") { dialogInterface: DialogInterface, i: Int ->
                        textView.text = "Material Alert Dialog - Negative"
                    }
                }
                builder.show()
            }
        }
    }
}
