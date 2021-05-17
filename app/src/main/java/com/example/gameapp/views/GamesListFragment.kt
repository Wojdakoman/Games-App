package com.example.gameapp.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gameapp.R
import com.example.gameapp.viewmodels.GameViewModel
import com.example.gameapp.viewmodels.SharedViewModel
import kotlinx.android.synthetic.main.fragment_games_list.*
import kotlinx.android.synthetic.main.fragment_search_results.*
import kotlinx.android.synthetic.main.fragment_search_results.progressCircle

class GamesListFragment : Fragment() {
    private lateinit var viewModel: GameViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var fragmentLayoutManager: LinearLayoutManager
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private val args: GamesListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        viewModel.repository = sharedViewModel.repository

        fragmentLayoutManager = LinearLayoutManager(context)
        searchResultsAdapter = SearchResultsAdapter(viewModel)

        viewModel.listMode = args.view
        viewModel.getGames()

        viewModel.searchResults.observe(viewLifecycleOwner, Observer {
            searchResultsAdapter.setData(it)
            searchResultsAdapter.notifyDataSetChanged()
            Log.d("[RES]", "d: $it")
        })

        //show progress circle or not
        viewModel.showProgress.observe(viewLifecycleOwner, Observer {
            if(it)
                progressCircle.visibility = View.VISIBLE
            else progressCircle.visibility = View.GONE
        })

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_games_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gamesList.apply {
            adapter = searchResultsAdapter
            layoutManager = fragmentLayoutManager
        }
    }
}