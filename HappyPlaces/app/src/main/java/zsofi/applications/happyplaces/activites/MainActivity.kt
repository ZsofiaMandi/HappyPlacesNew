package zsofi.applications.happyplaces.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import zsofi.applications.happyplaces.database.DatabaseHandler
import zsofi.applications.happyplaces.databinding.ActivityMainBinding
import zsofi.applications.happyplaces.models.HappyPlaceModel

class MainActivity : AppCompatActivity() {

    var binding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.fabHappyPlace?.setOnClickListener {
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivity(intent)
        }
        getHappyPlacesListFromLocalDB()
    }

    private  fun getHappyPlacesListFromLocalDB(){
        val dbHandler = DatabaseHandler(this)
        val getHappyPlaceList : ArrayList<HappyPlaceModel> =dbHandler.getHappyPlacesList()

        if (getHappyPlaceList.size > 0){
            for(i in getHappyPlaceList){
                Log.e("Title", i.title)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (binding != null) {
            binding = null
        }
    }
}