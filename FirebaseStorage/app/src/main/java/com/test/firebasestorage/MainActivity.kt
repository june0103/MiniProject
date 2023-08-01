package com.test.firebasestorage

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.test.firebasestorage.databinding.ActivityMainBinding
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    // 확인 권한 목록
    val permissionList = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_MEDIA_LOCATION,
        Manifest.permission.INTERNET
    )

    // 앨범 액티비티를 실행하기 위한 런처
    lateinit var albumLauncher : ActivityResultLauncher<Intent>

    val fileName = "image/test.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        requestPermissions(permissionList,0)

        // 런처 생성
        val contract1 = ActivityResultContracts.StartActivityForResult()
        albumLauncher = registerForActivityResult(contract1){

            if(it?.resultCode == RESULT_OK){
                // firbase storage에 접근
                val storage = FirebaseStorage.getInstance()
                // 프로젝트에 적용할 때는 파일이름은 파일경로는 고정으로하고 다음과 같은 방식으로 적용하자
//                val fileName = "image/test_${System.currentTimeMillis()}.jpg"

                // 파일에 접근할 수 있는 객체
                val fileRef = storage.reference.child(fileName)
                // 파일 업로드 (URI객채)
                fileRef.putFile(it.data?.data!!).addOnCompleteListener{
                    Snackbar.make(activityMainBinding.root,"업로드가 완료되었습니다",Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        activityMainBinding.run {
            button.setOnClickListener {
                val newIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                newIntent.setType("image/*")
                val mimeType = arrayOf("image/*")
                newIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
                albumLauncher.launch(newIntent)
            }

            button2.setOnClickListener {
                val storage = FirebaseStorage.getInstance()
                val fileRef = storage.reference.child(fileName)

                // 데이터를 가져올 수 있는 경로를 가져온다.
                fileRef.downloadUrl.addOnCompleteListener {
                    thread {
                        // 파일에 접근할 수 있는 경로를 이용해 URL 객체를 생성한다.
                        val url = URL(it.result.toString())
                        // 접속한다.
                        val httpURLConnection = url.openConnection() as HttpURLConnection
                        // 이미지 객체를 생성한다.
                        val bitmap = BitmapFactory.decodeStream(httpURLConnection.inputStream)

                        runOnUiThread {
                            imageView.setImageBitmap(bitmap)
                        }
                    }
                }
            }

            button3.setOnClickListener {
                // 이미지 수정은 이미지 업로드와 동일
                // 다른 이미지를 같은 파일명으로 업로드하면 덮어 쓴다
                val newIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                newIntent.setType("image/*")
                val mimeType = arrayOf("image/*")
                newIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
                albumLauncher.launch(newIntent)
            }

            button4.setOnClickListener {
                val storage = FirebaseStorage.getInstance()
                val fileRef = storage.reference.child(fileName)
                // 파일 삭제
                fileRef.delete().addOnCompleteListener {
                    Snackbar.make(activityMainBinding.root,"삭제하였습니다",Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}