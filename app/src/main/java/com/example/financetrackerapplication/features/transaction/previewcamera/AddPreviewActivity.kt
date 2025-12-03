package com.example.financetrackerapplication.features.transaction.previewcamera

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.Gravity
import android.view.ScaleGestureDetector
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.databinding.ActivityAddPreviewBinding
import com.example.financetrackerapplication.utils.FileUtils
import com.google.android.material.card.MaterialCardView

class AddPreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPreviewBinding

    private lateinit var camera: Camera
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private var cameraProvider: ProcessCameraProvider? = null

    private val requiredPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.CAMERA
        )
    } else {
        arrayOf(
            Manifest.permission.CAMERA
        )
    }

    // request
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permission ->
            val isGrantedAll = permission.all { it.value }
            if (isGrantedAll) {
                onPermissionGranted()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun onPermissionGranted() {
//        val latestImageUri = PostUtils.getLatestImageUri(this)
//
//        if (latestImageUri != null) {
//            binding.postImageOpenGalery.setImageURI(latestImageUri)
//        } else {
//            Toast.makeText(this, "Tidak ada gambar ditemukan", Toast.LENGTH_SHORT).show()
//        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissionLauncher.launch(requiredPermission)

        // back from edit
        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_countainer)
            if (currentFragment == null || currentFragment !is PreviewFragment) {
                // Kalau tidak ada fragment, atau fragment edit sudah hilang
                startCamera()
            }
        }

        startCamera()
        setupCameraToZoom()
        setupListener()
    }

    private fun setupListener() {
        binding.apply {
            btnCapture.setOnClickListener { takePhoto() }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .setTargetResolution(Size(1440, 2560))
                .build()
                .also {
                    it.surfaceProvider = binding.cameraAddTransaction.surfaceProvider
                }

            imageCapture = ImageCapture.Builder()
                .setFlashMode(ImageCapture.FLASH_MODE_ON)
                .setTargetResolution(Size(1440, 2560))
                .build()

            try {
                cameraProvider?.let { processCameraProvider ->
                    processCameraProvider.unbindAll() // ketika pertama unibnd semua

                    camera = processCameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } ?: run {
                    // cameraProvider ternyata null
                    Log.e(TAG, "Camera provider is null")
                }
            } catch (exc: Exception) {
                Toast.makeText(
                    this,
                    "Gagal membuka kamera",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun stopCamera() {
        cameraProvider?.unbindAll()
    }

    private fun switchCamera() {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
            CameraSelector.DEFAULT_FRONT_CAMERA
        else
            CameraSelector.DEFAULT_BACK_CAMERA

        startCamera() // replay
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupCameraToZoom() {
        val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val currentZoomRatio = camera.cameraInfo.zoomState.value?.zoomRatio ?: 1f
                val delta = detector.scaleFactor
                camera.cameraControl.setZoomRatio(currentZoomRatio * delta)
                return true
            }
        }

        val scaleGestureDetector = ScaleGestureDetector(this, listener)

        binding.cameraAddTransaction.setOnTouchListener { _, motionEvent ->
            scaleGestureDetector.onTouchEvent(motionEvent)
            true
        }
    }

    private fun takePhoto() {
        val imageCapture = this.imageCapture ?: return

        val photoFile = FileUtils.createCustomTempFile(this)

        val outputOptins = ImageCapture.OutputFileOptions
            .Builder(photoFile)
            .build()

        imageCapture.takePicture(
            outputOptins,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(
                        this@AddPreviewActivity,
                        "success_take_picture",
                        Toast.LENGTH_SHORT
                    ).show()

                    val uri = outputFileResults.savedUri
                    uri?.let {
                        // take foto
                        val fragment = PreviewFragment.newInstance(uri.toString())

                        stopCamera()
                        supportFragmentManager.beginTransaction()
                            .replace(binding.fragmentCountainer.id, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        this@AddPreviewActivity,
                        "error_take_picture",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        )
    }



    companion object {
        const val ARG_URI = "image_uri"

        private val TAG = AddPreviewActivity::class.java.simpleName

    }
}