package com.example.gameapp.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.gameapp.R
import com.example.gameapp.models.bindImage
import com.example.gameapp.models.entities.SearchResult
import kotlinx.android.synthetic.main.slider_item.view.*
import kotlinx.android.synthetic.main.slider_item.view.gameCover

class SliderAdapter(): RecyclerView.Adapter<SliderAdapter.Holder>() {
    inner class Holder(view: View): RecyclerView.ViewHolder(view)

    var list = listOf<SearchResult>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slider_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        bindImage(holder.itemView.gameCover, "https://images.igdb.com/igdb/image/upload/t_720p/${list[position].cover}.jpg")
        holder.itemView.gameTitle.text = list[position].name

        holder.itemView.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToGameFragment(list[position].id, list[position].name)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount() = list.size
}