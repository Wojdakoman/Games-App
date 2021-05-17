package com.example.gameapp.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.gameapp.R
import com.example.gameapp.models.bindImage
import com.example.gameapp.viewmodels.GameViewModel
import com.example.gameapp.viewmodels.GamesViewModel
import com.example.gameapp.viewmodels.SharedViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_game.*

class GameFragment : Fragment() {
    private lateinit var viewModel: GameViewModel
    private lateinit var database: DatabaseReference
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val args: GameFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Firebase.database.reference
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        viewModel.repository = sharedViewModel.repository

        viewModel.setDefaultState()

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
}