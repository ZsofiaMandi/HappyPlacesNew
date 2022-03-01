package zsofi.applications.happyplaces.adapters

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import zsofi.applications.happyplaces.activites.AddHappyPlaceActivity
import zsofi.applications.happyplaces.activites.MainActivity
import zsofi.applications.happyplaces.databinding.ItemHappyPlaceBinding
import zsofi.applications.happyplaces.models.HappyPlaceModel



class HappyPlacesAdapter(private val happyPlaceList: List<HappyPlaceModel>)
    : RecyclerView.Adapter<HappyPlacesAdapter.MainViewHolder>() {

    private var onClickListener: OnCLickListener? = null




   inner class  MainViewHolder(private val itemBinding: ItemHappyPlaceBinding)
       : RecyclerView.ViewHolder(itemBinding.root){
           fun bindItem(place: HappyPlaceModel){
               itemBinding.tvTitle.text = place.title
               itemBinding.tvDescription.text = place.description
               itemBinding.ivPlaceImage.setImageURI(Uri.parse(place.image))

           }

            fun bindOnClickListener(position: Int, model: HappyPlaceModel){
               itemBinding.itemView.setOnClickListener {
                   if (onClickListener != null){
                       onClickListener!!.onClick(position, model)
                   }
               }
            }


       }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(ItemHappyPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val place = happyPlaceList[position]
        holder.bindItem(place)
        holder.bindOnClickListener(position, place)
    }

    override fun getItemCount(): Int {
        return happyPlaceList.size
    }

    interface  OnCLickListener{
        fun onClick(position: Int, model: HappyPlaceModel){
        }
    }

    fun setOnClickListener(onCLickListener: OnCLickListener){
        this.onClickListener = onCLickListener
    }

    fun notifyEditItem(activity: Activity, position: Int, resultLauncher: ActivityResultLauncher<Intent>){
        val intentEdit = Intent(activity, AddHappyPlaceActivity::class.java)
        intentEdit.putExtra(MainActivity.EXTRA_PLACE_DETAILS, happyPlaceList[position])
        resultLauncher.launch(intentEdit)
        notifyItemChanged(position)
    }


}