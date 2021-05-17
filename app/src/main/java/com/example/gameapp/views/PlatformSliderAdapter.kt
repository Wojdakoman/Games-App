package com.example.gameapp.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gameapp.R
import com.example.gameapp.models.bindImage
import kotlinx.android.synthetic.main.platform_slider.view.*

class PlatformSliderAdapter(): RecyclerView.Adapter<PlatformSliderAdapter.Holder>() {
    inner class Holder(view: View): RecyclerView.ViewHolder(view)

    var list = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.platform_slider, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        bindImage(holder.itemView.platformLogo, "https://images.igdb.com/igdb/image/upload/t_thumb/${list[position]}.jpg")
    }

    override fun getItemCount() = list.size
}