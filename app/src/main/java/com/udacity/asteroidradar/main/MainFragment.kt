package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

/***
 * Simple Fragment: Hold's Image of the day for Asteroid along with list of other asteroids.
 * */

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
//        ViewModelProvider(this).get(MainViewModel::class.java)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(
            this,
            MainViewModel.Factory(activity.application)
        ).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        setObservers(binding, viewModel)

        binding.asteroidRecycler.adapter =
            AsteroidsAdapter(AsteroidsAdapter.OnClickListener { asteroid ->
                //navigate to details fragment with required arguments
                findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
            })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        viewModel.getAsteroids(
            when (item.itemId) {
                R.id.show_all_menu -> {
                    Constants.GetListFor.WEEK
                }
                R.id.show_rent_menu -> {
                    Constants.GetListFor.TODAY
                }
                else -> {
                    Constants.GetListFor.SAVED
                }
            }
        )
        return true
    }

    private fun setObservers(binding: FragmentMainBinding, viewModel: MainViewModel) {
        with(viewModel) {
            asteroids.observe(viewLifecycleOwner, Observer {
                showToast("Updated")
            })
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

}