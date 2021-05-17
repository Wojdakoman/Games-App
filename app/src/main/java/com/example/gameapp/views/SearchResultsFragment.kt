package com.example.gameapp.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gameapp.R
import com.example.gameapp.viewmodels.GameViewModel
import com.example.gameapp.viewmodels.SharedViewModel
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_search_results.*
import kotlinx.android.synthetic.main.fragment_search_results.progressCircle

class SearchResultsFragment : Fragment() {
    private lateinit var viewModel: GameViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var fragmentLayoutManager: LinearLayoutManager
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private val args: SearchResultsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        viewModel.repository = sharedViewModel.repository

        viewModel.listMode = 2

        fragmentLayoutManager = LinearLayoutManager(context)
        searchResultsAdapter = SearchResultsAdapter(viewModel)

        viewModel.search(args.searchQuery)

        viewModel.searchResults.observe(viewLifecycleOwner, Observer {
            searchResultsAdapter.setData(it)
            searchResultsAdapter.notifyDataSetChanged()
        })

        //show progress circle or not
        viewModel.showProgress.observe(viewLifecycleOwner, Observer {
            if(it)
                progressCircle.visibility = View.VISIBLE
            else progressCircle.visibility = View.GONE
        })

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchResultsList.apply {
            adapter = searchResultsAdapter
            layoutManager = fragmentLayoutManager
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.topbar_menu, menu)

        val searchItem = menu.findItem(R.id.btnSearch)
        val searchView: SearchView = searchItem.actionView as SearchView
        //search view handler
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.search(query!!)
                searchItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //Log.d("[STYPE]", "$newText")
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.btnLogOut -> {
                viewModel.firebase.logOut()
                startActivity(Intent(context, LoginActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}