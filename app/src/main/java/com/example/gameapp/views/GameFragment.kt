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
import com.example.gameapp.R
import com.example.gameapp.models.bindImage
import com.example.gameapp.viewmodels.GameViewModel
import com.example.gameapp.viewmodels.GamesViewModel
import com.example.gameapp.viewmodels.SharedViewModel
import kotlinx.android.synthetic.main.fragment_game.*

class GameFragment : Fragment() {
    private lateinit var viewModel: GameViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        viewModel.repository = sharedViewModel.repository

        viewModel.getCover().observe(viewLifecycleOwner, Observer {
            Log.d("[COVER]", "cover: $it")
            bindImage(coverImage, viewModel.getCoverUrl(it))
        })

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}