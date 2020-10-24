package com.krystofmacek.marketviz.ui.fragments.overview

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.workers.IndicesDataUpdateWorker
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration

@AndroidEntryPoint
class OverviewFragment : Fragment(R.layout.fragment_overview) {

    private val viewModel: OverviewViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.marketIndices.observe(viewLifecycleOwner, Observer {
            // TODO: update list in adapter to display qotes in recycler
            Log.i("Observing Indices", it.size.toString())
        })
    }
}