package zsofi.applications.happyplaces.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import zsofi.applications.happyplaces.databinding.ItemHappyPlaceBinding
import zsofi.applications.happyplaces.models.HappyPlaceModel

class HappyPlacesAdapter(private val happyPlaceList: List<HappyPlaceModel>)
    : RecyclerView.Adapter<HappyPlacesAdapter.MainViewHolder>() {

   inner class  MainViewHolder(private val itemBinding: ItemHappyPlaceBinding)
       : RecyclerView.ViewHolder(itemBinding.root){
           fun bindItem(place: HappyPlaceModel){
               itemBinding.tvTitle.text = place.title
               itemBinding.tvDescription.text = place.description
               itemBinding.ivPlaceImage.setImageURI(Uri.parse(place.image))
           }
       }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(ItemHappyPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val place = happyPlaceList[position]
        holder.bindItem(place)
    }

    override fun getItemCount(): Int {
        return happyPlaceList.size
    }


}