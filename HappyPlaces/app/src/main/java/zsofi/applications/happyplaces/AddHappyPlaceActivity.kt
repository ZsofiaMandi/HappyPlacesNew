package zsofi.applications.happyplaces

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import zsofi.applications.happyplaces.databinding.ActivityAddHappyPlaceBinding

class AddHappyPlaceActivity : AppCompatActivity() {

    var binding: ActivityAddHappyPlaceBinding? = null

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
    }
}