package zsofi.applications.happyplaces.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import zsofi.applications.happyplaces.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity() {

    private var binding: ActivityMapBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)

        setContentView(binding?.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(binding != null){
            binding = null
        }
    }
}