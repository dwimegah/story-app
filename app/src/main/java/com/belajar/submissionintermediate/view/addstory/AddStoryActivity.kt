package com.belajar.submissionintermediate.view.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.belajar.submissionintermediate.databinding.ActivityAddStoryBinding
import com.belajar.submissionintermediate.utils.reduceFileImage
import com.belajar.submissionintermediate.utils.uriToFile
import com.belajar.submissionintermediate.view.ViewModelFactory
import com.belajar.submissionintermediate.view.main.MainActivity
import com.belajar.submissionintermediate.view.main.MainViewModel
import com.belajar.submissionintermediate.view.welcome.WelcomeActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val storyViewModel by viewModels<AddStoryViewModel>()
    private lateinit var binding: ActivityAddStoryBinding

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        binding.buttonCamera.setOnClickListener {
            startCameraX()
        }
        binding.buttonGallery.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose a Picture")
            launcherIntentGallery.launch(chooser)
        }
        binding.buttonAdd.setOnClickListener {
            uploadImage()
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            val myFile = uriToFile(it, this)
            getFile = myFile

            binding.imageStory.setImageURI(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private var getFile: File? = null

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Tidak mendapat akses", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding.imageStory.setImageURI(selectedImg)
        }
    }

    private fun uploadImage() {
        viewModel.getSession().observe(this) { it ->
            if (!it.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                if (getFile != null) {
                    val file = reduceFileImage(getFile as File)
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
                    val description = binding.textDescription.text.toString()
                        .toRequestBody("text/plain".toMediaType())
                    val imageMultipart: MultipartBody.Part =
                        MultipartBody.Part.createFormData("photo", file.name, requestImageFile)
                    if (description.toString().isNotEmpty()) {
                        storyViewModel.postStory(it.token, imageMultipart, description)
                        storyViewModel.story.observe(this) {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Toast.makeText(this, "Input Description", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this, "Input Image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        const val REQUEST_CODE_PERMISSIONS = 10
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}