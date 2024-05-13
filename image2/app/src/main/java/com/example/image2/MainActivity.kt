package com.example.image2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.annotations.SerializedName
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import android.util.Base64
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.MultipartBody

import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part



class MainActivity : AppCompatActivity() {

    private lateinit var selectImageButton: Button
    private lateinit var imgView: ImageView
    private lateinit var img_after_process: ImageView
    private lateinit var cameraButton: Button
    private lateinit var bitmap: Bitmap



    private val apiService = ApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectImageButton = findViewById(R.id.button)
        imgView = findViewById(R.id.imageView2)
        img_after_process = findViewById(R.id.imageView3)
        cameraButton = findViewById(R.id.camerabtn)

        // Handling permissions
        checkAndGetPermissions()

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 250)
        }

        cameraButton.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, 200)
        }
    }

    private fun checkAndGetPermissions() {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
        } else {
            Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                250 -> {
                    // Handling image selected from gallery
                    data?.data?.let { uri ->
                        val selectedBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                        imgView.setImageBitmap(selectedBitmap)
                        preprocessImage(selectedBitmap)
                    }
                }
                200 -> {
                    // Handling image captured from camera
                    val imageBitmap = data?.extras?.get("data") as Bitmap?
                    imageBitmap?.let {
                        imgView.setImageBitmap(it)
                        preprocessImage(it)
                    }
                }
            }
        }
    }

    private fun preprocessImage(bitmap: Bitmap) {
        Log.d("MainActivity", "preprocessImage called")
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val imageBytes = byteArrayOutputStream.toByteArray()

                val requestFile = imageBytes.toRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile)

                apiService.preprocessImage(body).enqueue(object : Callback<PreprocessedImageResponse> {
                    override fun onResponse(call: Call<PreprocessedImageResponse>, response: Response<PreprocessedImageResponse>) {
                        if (response.isSuccessful) {
                            val processedImageURL = response.body()?.processedImageURL
                            Log.d("MainActivity", "calledloadimg")
                            displayImageFromBase64(processedImageURL)
                        } else {
                            // Handle unsuccessful response
                        }
                    }

                    override fun onFailure(call: Call<PreprocessedImageResponse>, t: Throwable) {
                        // Handle failure
                    }
                })

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun displayImageFromBase64(base64Image: String?) {
        // Decode the base64-encoded string into a byte array
        val imageData = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT)

        // Convert the byte array to a Bitmap
        val bitmap = android.graphics.BitmapFactory.decodeByteArray(imageData, 0, imageData.size)

        // Set the Bitmap to your ImageView
        img_after_process.setImageBitmap(bitmap)
    }





}



//data class PreprocessedImageResponse(
//    @SerializedName("processedImageURL")
//    val processedImageURL: String // Change this to match the actual field name in your JSON response
//)


