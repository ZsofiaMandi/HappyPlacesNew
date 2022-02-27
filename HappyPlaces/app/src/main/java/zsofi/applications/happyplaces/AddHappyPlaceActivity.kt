package zsofi.applications.happyplaces

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import zsofi.applications.happyplaces.databinding.ActivityAddHappyPlaceBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddHappyPlaceActivity : AppCompatActivity(),View.OnClickListener {

    private var binding: ActivityAddHappyPlaceBinding? = null
    private var savedDate : IntArray = intArrayOf(0, 0, 0)

    val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result ->
            if(result.resultCode == RESULT_OK && result.data!=null){
                try {
                    binding?.ivImageUpload?.setPadding(0,0,0,0)
                    binding?.ivImageUpload?.setImageURI(result.data?.data)
                    val drawable = binding?.ivImageUpload?.drawable
                    val bitmap = drawable!!.toBitmap()
                    val saveImageToInternalStorage = saveImageToInternalStorage(bitmap)
                    Log.e("Saved image: ", "Path :: $saveImageToInternalStorage")
                    // TODO to check why did the border from the image disappear
                }catch (e: IOException){
                    e.printStackTrace()
                    Toast.makeText(this@AddHappyPlaceActivity,
                        "Failed to load image from gallery!", Toast.LENGTH_SHORT).show()
                }

            }
        }

    private val cameraLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result ->
            if (result.resultCode == RESULT_OK && result.data!=null){
                val thumbNail : Bitmap = result.data?.extras!!.get("data") as Bitmap
                binding?.ivImageUpload?.setPadding(0,0,0,0)
                binding?.ivImageUpload?.setImageBitmap(thumbNail)
                val saveImageToInternalStorage = saveImageToInternalStorage(thumbNail)
                Log.e("Saved image: ", "Path :: $saveImageToInternalStorage")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddHappyPlaceBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarAddPlace)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Add Happy Place"
        }

        binding?.toolbarAddPlace?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.etDate?.setOnClickListener(this)
        binding?.tvAddImage?.setOnClickListener(this)

    }

    override fun onClick(v: View?){
        when(v!!.id){
            R.id.etDate ->{
                val myCalendar = Calendar.getInstance()
                var year = myCalendar.get(Calendar.YEAR)
                var month = myCalendar.get(Calendar.MONTH)
                var day = myCalendar.get(Calendar.DAY_OF_MONTH)

                if(savedDate[0] != 0)
                {
                    year = savedDate[0]
                    month = savedDate[1]
                    day = savedDate[2]
                }

                clickDatePicker(year, month, day)
            }
            R.id.tvAddImage ->{
                val pictureDialog = AlertDialog.Builder(this)
                pictureDialog.setTitle("Select Action")

                val pictureDialogItems = arrayOf("Select photo from Gallery",
                    "Capture photo from camera")
                pictureDialog.setItems(pictureDialogItems){
                        _, which->
                    when(which){
                        0 -> choosePhotoFromGallery()
                        1 -> takePhotoFromCamera()
                    }
                }
                pictureDialog.show()
            }
        }
    }

    private fun clickDatePicker(year: Int, month: Int, day: Int) {

        val dpd = DatePickerDialog(
            this,
            R.style.myDialogTheme,
            { _, selectedYear, selectedMonth, selectedDay ->

                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"

                binding?.etDate?.setText(selectedDate)

                savedDate[0] = selectedYear
                savedDate[1] = selectedMonth
                savedDate[2] = selectedDay

            },
            year,
            month,
            day
        )
        dpd.show()
    }

    private fun choosePhotoFromGallery(){
        Dexter.withContext(this).withPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object: PermissionListener{
            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                val pickIntent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                openGalleryLauncher.launch(pickIntent)

            }
            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                Toast.makeText(this@AddHappyPlaceActivity,
                    "Storage read and write permissions are denied. " +
                            "You cannot upload photo from your Gallery", Toast.LENGTH_SHORT).show()
            }
            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                showRationalDialogForPermissions()
            }
        }).check()
    }

    private fun takePhotoFromCamera(){
        Dexter.withContext(this).withPermission(
            Manifest.permission.CAMERA
        ).withListener(object: PermissionListener{
            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraLauncher.launch(cameraIntent)

            }
            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                Toast.makeText(this@AddHappyPlaceActivity,
                    "Camera permission is denied, you cannot take photo.", Toast.LENGTH_SHORT).show()
            }
            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                showRationalDialogForPermissions()
            }
        }).check()
    }

    private fun showRationalDialogForPermissions(){
        AlertDialog.Builder(this, R.style.myDialogTheme).setMessage("It looks like you have turned off permission" +
                " required for this feature. It can be enabled under the Applications Settings")
            .setPositiveButton("GO TO SETTINGS"){
                _, _ ->
                try{
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch(e: ActivityNotFoundException){
                    e.printStackTrace()
                }

            }.setNegativeButton("Cancel"){dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri{
        val wrapper = ContextWrapper(applicationContext)
        // Mode private - Other apps wont be able to access this directory
        var file = wrapper.getDir(IMAGE_DIRECTORY,Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg") // file, filename

        try{
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        }catch (e: IOException){
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }

    companion object{
        private const val IMAGE_DIRECTORY = "HappyPlacesImages"
    }

    override fun onDestroy() {
        super.onDestroy()

        if(binding != null){
            binding = null
        }
    }

}