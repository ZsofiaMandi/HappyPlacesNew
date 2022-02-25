package zsofi.applications.happyplaces

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import zsofi.applications.happyplaces.databinding.ActivityAddHappyPlaceBinding
import java.util.*

class AddHappyPlaceActivity : AppCompatActivity(),View.OnClickListener {

    private var binding: ActivityAddHappyPlaceBinding? = null
    private var savedDate : IntArray = intArrayOf(0, 0, 0)

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
                Toast.makeText(this@AddHappyPlaceActivity,
                    "Storage read and write permissions are granted. " +
                            "Now you can select an image from Gallery", Toast.LENGTH_SHORT).show()
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
                        1 -> Toast.makeText(this@AddHappyPlaceActivity,
                            "Camera selection coming soon...",Toast.LENGTH_SHORT).show()
                    }
                }
                pictureDialog.show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if(binding != null){
            binding = null
        }
    }

}