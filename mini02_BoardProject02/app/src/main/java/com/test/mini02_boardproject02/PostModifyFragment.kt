package com.test.mini02_boardproject02

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.mini02_boardproject02.databinding.FragmentPostModifyBinding
import com.test.mini02_boardproject02.repository.PostRepository
import com.test.mini02_boardproject02.vm.PostViewModel
import java.io.File

class PostModifyFragment : Fragment() {

    lateinit var fragmentPostModifyBinding: FragmentPostModifyBinding
    lateinit var mainActivity: MainActivity

    // 게시글 번호
    var readPostIdx = 0L

    lateinit var postViewModel: PostViewModel

    // 업로드할 이미지 Uri
    var uploadUri: Uri? = null

    // 카메라 런처
    lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    // 새로운 이미지를 설정한 적이 있는지
    var isSelectNewImage = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentPostModifyBinding = FragmentPostModifyBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        readPostIdx = arguments?.getLong("readPostIdx")!!

        // 카메라 세팅
        cameraLauncher = cameraSetting(fragmentPostModifyBinding.imageViewPostModify)
        // 앨범 설정
        albumLauncher = albumSetting(fragmentPostModifyBinding.imageViewPostModify)

        postViewModel = ViewModelProvider(mainActivity)[PostViewModel::class.java]
        postViewModel.run {
            postSubject.observe(mainActivity) {
                fragmentPostModifyBinding.textInputEditTextPostModifySubject.setText(it)
            }
            postText.observe(mainActivity) {
                fragmentPostModifyBinding.textInputEditTextPostModifyText.setText(it)
            }
            postImage.observe(mainActivity) {
                fragmentPostModifyBinding.imageViewPostModify.visibility = View.VISIBLE
                fragmentPostModifyBinding.imageViewPostModify.setImageBitmap(it)
            }
        }

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
                            val newIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                            // 촬영될 사진이 저장될 파일 이름
                            val fileName = "/temp_upload.jpg"
                            // 경로
                            val filePath = mainActivity.getExternalFilesDir(null).toString()
                            // 경로 + 파일 이름
                            val picPath = "${filePath}/${fileName}"

                            // 사진이 저장될 경로를 관리할 Uri 객체를 만들어준다
                            // 업로드할 때 사용할 Uri
                            val file = File(picPath)
                            uploadUri = FileProvider.getUriForFile(mainActivity,"com.test.mini02_boardproject02.file_provider", file)

                            newIntent.putExtra(MediaStore.EXTRA_OUTPUT, uploadUri)
                            cameraLauncher.launch(newIntent)
                        }
                        R.id.item_post_modify_album -> {
                            val newIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            newIntent.setType("image/*")
                            // 선택할 파일의 타입을 지정(안드로이드  OS가 이미지에 대한 사전 작업을 할 수 있도록)
                            val mimeType = arrayOf("image/*")
                            newIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
                            albumLauncher.launch(newIntent)
                        }
                        R.id.item_post_modify_done -> {
                            // 입력한 내용을 가져온다.
                            val subject = textInputEditTextPostModifySubject.text.toString()
                            val text = textInputEditTextPostModifyText.text.toString()

                            if(subject.isEmpty()){
                                val builder = MaterialAlertDialogBuilder(mainActivity)
                                builder.setTitle("제목 입력 오류")
                                builder.setMessage("제목을 입력해주세요")
                                builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                                    mainActivity.showSoftInput(textInputEditTextPostModifySubject)
                                }
                                builder.show()
                                return@setOnMenuItemClickListener true
                            }

                            if(text.isEmpty()){
                                val builder = MaterialAlertDialogBuilder(mainActivity)
                                builder.setTitle("내용 입력 오류")
                                builder.setMessage("내용을 입력해주세요")
                                builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                                    mainActivity.showSoftInput(textInputEditTextPostModifyText)
                                }
                                builder.show()
                                return@setOnMenuItemClickListener true
                            }

                            val fileName = if(isSelectNewImage == false){
                                "none"
                            } else{
                                if(postViewModel.postFileName.value == "none"){
                                    "image/img_${System.currentTimeMillis()}.jpg"
                                } else{
                                    postViewModel.postFileName.value
                                }
                            }
                            val postDataClass = PostDataClass(readPostIdx, 0, subject, text, "", fileName!!, mainActivity.loginUserClass.userIdx)
                            PostRepository.modifyPost(postDataClass, isSelectNewImage){
                                // 새롭게 선택한 이미지가 있다면
                                if(isSelectNewImage == true){
                                    PostRepository.uploadImage(fileName, uploadUri!! ){
                                        mainActivity.removeFragment(MainActivity.POST_MODIFY_FRAGMENT)
                                    }
                                } else{
                                    mainActivity.removeFragment(MainActivity.POST_MODIFY_FRAGMENT)
                                }
                            }
                        }
                    }
                    true
                }
            }
        }
        return fragmentPostModifyBinding.root
    }

    // 카메라 관련 설정
    fun cameraSetting(previewImageView: ImageView) : ActivityResultLauncher<Intent>{
        val cameraContract = ActivityResultContracts.StartActivityForResult()
        cameraLauncher = registerForActivityResult(cameraContract){
            if(it?.resultCode == AppCompatActivity.RESULT_OK){
                // 새로운 이미지 선택 여부값 설정
                isSelectNewImage = true

                // Uri를 이용해 이미지에 접근하여 Bitmap 객체로 생성
                val bitmap = BitmapFactory.decodeFile(uploadUri?.path)

                // 이미지 크기조정
                // 이미지의 축소/확대 비율
                val ratio = 1024.0 / bitmap.width
                // 세로 길이를 구한다.
                val targetHeight = (bitmap.height * ratio).toInt()
                // 크기를 조정한 Bitmap을 생성한다.
                val bitmap2 = Bitmap.createScaledBitmap(bitmap, 1024, targetHeight, false)

                // 회전 각도를 가져온다
                val degree = getDegree(uploadUri!!)

                // 회전 이미지 생성
                val matrix = Matrix()
                matrix.postRotate(degree.toFloat())
                val bitmap3 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.width, bitmap2.height, matrix, false)
                previewImageView.setImageBitmap(bitmap3)
            }
        }
        return cameraLauncher
    }

    // 이미지 파일에 기록되어 있는 회전 정보를 가져온다.
    fun getDegree(uri:Uri) : Int{

        var exifInterface: ExifInterface? = null

        // 사진 파일로 부터 tag 정보를 관리하는 객체를 추출한다.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val photoUri = MediaStore.setRequireOriginal(uri)
            // 스트림을 추출한다.
            val inputStream = mainActivity.contentResolver.openInputStream(photoUri)
            // ExifInterface 정보를 읽엉돈다.
            exifInterface = ExifInterface(inputStream!!)
        } else {
            exifInterface = ExifInterface(uri.path!!)
        }

        var degree = 0
        if(exifInterface != null){
            // 각도 값을 가지고온다.
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)

            when(orientation){
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        }
        return degree
    }

    // 앨범 관련 설정
    fun albumSetting(previewImageView: ImageView) : ActivityResultLauncher<Intent>{
        val albumContract = ActivityResultContracts.StartActivityForResult()
        val albumLauncher = registerForActivityResult(albumContract){

            if(it.resultCode == AppCompatActivity.RESULT_OK){
                // 선택한 이미지에 접근할 수 있는 Uri객체 추출
                if(it.data?.data != null){
                    // 새로운 이미지 선택 여부값 설정
                    isSelectNewImage = true

                    uploadUri = it.data?.data

                    if(uploadUri != null){
                        // 안드로이드 10 (Q) 이상이라면...
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                            // 이미지를 생성할 수 있는 디코더를 생성한다.
                            val source = ImageDecoder.createSource(mainActivity.contentResolver, uploadUri!!)
                            // Bitmap객체를 생성한다.
                            val bitmap = ImageDecoder.decodeBitmap(source)

                            previewImageView.setImageBitmap(bitmap)
                        } else{
                            // 컨텐츠 프로바이더를 통해 이미지 데이터 정보를 가져온다.
                            val cursor = mainActivity.contentResolver.query(uploadUri!!, null, null, null, null)
                            if(cursor != null) {
                                cursor.moveToNext()

                                // 이미지의 경로를 가져온다.
                                val idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                                val source = cursor.getString(idx)

                                // 이미지를 생성하여 보여준다.
                                val bitmap = BitmapFactory.decodeFile(source)
                                previewImageView.setImageBitmap(bitmap)
                            }
                        }
                    }
                }
            }
        }
        return albumLauncher
    }
}