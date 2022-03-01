package zsofi.applications.happyplaces.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import zsofi.applications.happyplaces.databinding.ActivityHappyPlaceDetailBinding

class HappyPlaceDetailActivity : AppCompatActivity() {

    private var binding : ActivityHappyPlaceDetailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHappyPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (binding != null) {
            binding = null
        }
    }

}