package zsofi.applications.happyplaces

import android.app.AlertDialog
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import zsofi.applications.happyplaces.databinding.ActivityAddHappyPlaceBinding
import java.text.SimpleDateFormat
import java.util.*

class AddHappyPlaceActivity : AppCompatActivity(),View.OnClickListener {

    var binding: ActivityAddHappyPlaceBinding? = null
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
                    dialog, which->
                    when(which){
                        0 -> choosePhotoFromGallery()
                        1 -> Toast.makeText(this@AddHappyPlaceActivity,
                            "Camera selection coming soon...",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}