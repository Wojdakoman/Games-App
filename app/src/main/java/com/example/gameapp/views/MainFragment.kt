package com.example.gameapp.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gameapp.R
import com.example.gameapp.viewmodels.DiscoverViewModel
import com.example.gameapp.viewmodels.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    private lateinit var viewModel: DiscoverViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var preferences: SharedPreferences

    private lateinit var newestAdapter: SliderAdapter
    private lateinit var bestAdapter: SliderAdapter
    private lateinit var comingAdapter: SliderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        auth = Firebase.auth

        if(auth.currentUser == null){
            startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(DiscoverViewModel::class.java)
        preferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        sharedViewModel.loadAccessToken(preferences?.getString("access_token", ""))

        viewModel.repository = sharedViewModel.repository
        viewModel.showProgress.postValue(true)

        newestAdapter = SliderAdapter()
        bestAdapter = SliderAdapter()
        comingAdapter = SliderAdapter()

        sharedViewModel.token.observe(viewLifecycleOwner, Observer {
            if(preferences != null){
                Log.d("[SAVE]", "token: $it")
                with(preferences.edit()){
                    putString("access_token", it)
                    apply()
                }
            }
        })

        viewModel.loadNewest().observe(viewLifecycleOwner, Observer {
            newestAdapter.list = it
            newestAdapter.notifyDataSetChanged()
        })

        viewModel.loadBestGames().observe(viewLifecycleOwner, Observer {
            bestAdapter.list = it
            bestAdapter.notifyDataSetChanged()
        })

        viewModel.loadComingGames().observe(viewLifecycleOwner, Observer {
            comingAdapter.list = it
            comingAdapter.notifyDataSetChanged()
        })

        //show progress circle or not
        viewModel.showProgress.observe(viewLifecycleOwner, Observer {
            if(it)
                progressCircle.visibility = View.VISIBLE
            else progressCircle.visibility = View.GONE
        })

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //viewModel.loadNewest()

        newestRecycler.apply {
            adapter = newestAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        bestRecycler.apply {
            adapter = bestAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        comingRecycler.apply {
            adapter = comingAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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
                searchItem.collapseActionView()
                val action = MainFragmentDirections.actionMainFragmentToSearchResultsFragment(query!!)
                view?.findNavController()?.navigate(action)
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
                auth.signOut()
                startActivity(Intent(context, LoginActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}