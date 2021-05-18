package com.example.gameapp.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.gameapp.R
import com.example.gameapp.models.bindImage
import com.example.gameapp.models.entities.SearchResult
import com.example.gameapp.viewmodels.GameViewModel
import kotlinx.android.synthetic.main.search_result_row.view.*

class SearchResultsAdapter(private val viewModel: GameViewModel): RecyclerView.Adapter<SearchResultsAdapter.Holder>() {
    inner class Holder(view: View): RecyclerView.ViewHolder(view)

    private var list = listOf<SearchResult>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_result_row, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        bindImage(holder.itemView.gameCover, viewModel.getCoverUrl(list[position].cover))
        holder.itemView.gameName.text = list[position].name
        if(list[position].creators == "null")
            holder.itemView.gameCreators.visibility = View.GONE
        else holder.itemView.gameCreators.text = list[position].creators

        holder.itemView.setOnClickListener {
            val action = when(viewModel.listMode){
                2 -> SearchResultsFragmentDirections.actionSearchResultsFragmentToGameFragment(list[position].id, list[position].name)
                else -> GamesListFragmentDirections.actionGamesListFragmentToGameFragment(list[position].id, list[position].name)
            }
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount() = list.size

    fun setData(data: List<SearchResult>){
        list = data
    }
}