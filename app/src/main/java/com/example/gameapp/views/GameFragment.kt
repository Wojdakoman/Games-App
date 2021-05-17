package com.example.gameapp.views

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gameapp.R
import com.example.gameapp.models.bindImage
import com.example.gameapp.viewmodels.GameViewModel
import com.example.gameapp.viewmodels.SharedViewModel
import kotlinx.android.synthetic.main.fragment_game.*

class GameFragment : Fragment() {
    private lateinit var viewModel: GameViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val args: GameFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        viewModel.repository = sharedViewModel.repository

        viewModel.setDefaultState(args.gameID)

        viewModel.game.observe(viewLifecycleOwner, Observer {
            viewModel.getGameDetails()
            gameTitle.text = it.name
        })

        viewModel.cover.observe(viewLifecycleOwner, Observer {
            bindImage(coverImage, viewModel.getCoverUrl(it))
        })
        
        viewModel.background.observe(viewLifecycleOwner, Observer {
            bindImage(backgroundImage, viewModel.getCoverUrl(it))
        })

        viewModel.isPlayed.observe(viewLifecycleOwner, Observer {
            if(it){
                playedButton.setImageResource(R.drawable.ic_verified)
            } else {
                playedButton.setImageResource(R.drawable.ic_done)
            }
        })

        viewModel.isFav.observe(viewLifecycleOwner, Observer {
            if(it){
                favButton.setImageResource(R.drawable.ic_favorite)
            } else {
                favButton.setImageResource(R.drawable.ic_favorite_border)
            }
        })

        //show progress circle or not
        viewModel.showProgress.observe(viewLifecycleOwner, Observer {
            if(it)
                progressCircle.visibility = View.VISIBLE
            else progressCircle.visibility = View.GONE
        })

        viewModel.changeGame(args.gameID)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playedButton.setOnClickListener {
            viewModel.playedClick()
        }
        favButton.setOnClickListener {
            viewModel.favClick()
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
                val action = GameFragmentDirections.actionGameFragmentToSearchResultsFragment(query!!)
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
                viewModel.firebase.logOut()
                startActivity(Intent(context, LoginActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}