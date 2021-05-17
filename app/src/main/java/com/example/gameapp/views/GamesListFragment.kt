package com.example.gameapp.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.gameapp.R
import com.example.gameapp.viewmodels.GameViewModel
import com.example.gameapp.viewmodels.GamesViewModel
import com.example.gameapp.viewmodels.SharedViewModel

class GamesListFragment : Fragment() {
    private lateinit var viewModel: GamesViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val args: GamesListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(GamesViewModel::class.java)
        viewModel.repository = sharedViewModel.repository

        viewModel.getGames()
        Log.d("[MODE]", "i ${args.view}")

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_games_list, container, false)
    }
}