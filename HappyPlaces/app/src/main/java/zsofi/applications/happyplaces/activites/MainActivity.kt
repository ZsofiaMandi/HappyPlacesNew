package zsofi.applications.happyplaces.activites

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import zsofi.applications.happyplaces.adapters.HappyPlacesAdapter
import zsofi.applications.happyplaces.database.DatabaseHandler
import zsofi.applications.happyplaces.databinding.ActivityMainBinding
import zsofi.applications.happyplaces.models.HappyPlaceModel
import androidx.activity.result.ActivityResultCallback

import androidx.activity.result.contract.ActivityResultContracts

import androidx.activity.result.ActivityResultLauncher




class MainActivity : AppCompatActivity() {

    var binding : ActivityMainBinding? = null

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            getHappyPlacesListFromLocalDB()
        }else{
           Log.e("Activity", "Cancelled or Back pressed")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.fabHappyPlace?.setOnClickListener {
            openHappyPlaceActivityForResult()
        }
        getHappyPlacesListFromLocalDB()
    }

    private fun setupHappyPlacesRecyclerView(happyPlaceList: ArrayList<HappyPlaceModel>){
        binding?.rvHappyPlacesList?.layoutManager = LinearLayoutManager(this)
        binding?.rvHappyPlacesList?.setHasFixedSize(true)

        val placesAdapter = HappyPlacesAdapter(happyPlaceList)
        binding?.rvHappyPlacesList?.adapter = placesAdapter

        placesAdapter.setOnClickListener(object : HappyPlacesAdapter.OnCLickListener{
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val intent = Intent(this@MainActivity, HappyPlaceDetailActivity::class.java)
                startActivity(intent)
            }
        })

    }

    private  fun getHappyPlacesListFromLocalDB(){
        val dbHandler = DatabaseHandler(this)
        val getHappyPlaceList : ArrayList<HappyPlaceModel> =dbHandler.getHappyPlacesList()

        if (getHappyPlaceList.size > 0){
            binding?.rvHappyPlacesList?.visibility = View.VISIBLE
            binding?.tvNoRecordsAvailable?.visibility = View.GONE

            setupHappyPlacesRecyclerView(getHappyPlaceList)
        }else{
            binding?.rvHappyPlacesList?.visibility = View.GONE
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
        }
    }


    private fun openHappyPlaceActivityForResult() {
        val intent = Intent(this, AddHappyPlaceActivity::class.java)
        resultLauncher.launch(intent)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (binding != null) {
            binding = null
        }
    }
}