package io.github.sainak.trackmysleepquality.sleeptracker

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import io.github.sainak.trackmysleepquality.R
import io.github.sainak.trackmysleepquality.database.SleepDatabase
import io.github.sainak.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import io.github.sainak.trackmysleepquality.util.addSystemWindowInsetToPadding


/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 * The Clear button will clear all data from the database.
 */
class SleepTrackerFragment : Fragment() {

    private lateinit var sleepTrackerViewModel: SleepTrackerViewModel
    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     *
     * It is also responsible for passing the [SleepTrackerViewModel] to the
     * [FragmentSleepTrackerBinding] generated by DataBinding. This will allow DataBinding
     * to use the [LiveData] on our ViewModel.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sleep_tracker, container, false
        )

        // add insets to views
        binding.fabTrackerContainer.addSystemWindowInsetToPadding(bottom = true)
        binding.sleepList.addSystemWindowInsetToPadding(bottom = true)

        // Report that this fragment would like to participate in populating
        setHasOptionsMenu(true)

        /**
         * Set a hint for whether this fragment's menu should be visible.  This
         * is useful if you know that a fragment has been placed in your view
         * hierarchy so that the user can not currently seen it, so any menu items
         * it has should also not be shown.
         */
        //setMenuVisibility(true)

        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = this

        // Get a reference to the ViewModel associated with this fragment.
        sleepTrackerViewModel =
            ViewModelProvider(this, viewModelFactory)[SleepTrackerViewModel::class.java]

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.viewModel = sleepTrackerViewModel

        val adapter = SleepNightAdapter(SleepNightListener { nightId ->
            sleepTrackerViewModel.onSleepNightClicked(nightId)
        })
        binding.sleepList.adapter = adapter

        sleepTrackerViewModel.nights.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        // Add an Observer on the state variable for showing a Snackbar message
        // when the CLEAR button is pressed.
        sleepTrackerViewModel.showSnackBarEvent.observe(viewLifecycleOwner, {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    binding.fabTrackerContainer,
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                // Reset state to make sure the toast is only shown once, even if the device
                // has a configuration change.
                sleepTrackerViewModel.doneShowingSnackbar()
            }
        })

        // Add an Observer on the state variable for Navigating when STOP button is pressed.
        sleepTrackerViewModel.navigateToSleepQuality.observe(viewLifecycleOwner, { night ->
            night?.let {
                // We need to get the navController from this, because button is not ready, and it
                // just has to be a view. For some reason, this only matters if we hit stop again
                // after using the back button, not if we hit stop and choose a quality.
                // Also, in the Navigation Editor, for Quality -> Tracker, check "Inclusive" for
                // popping the stack to get the correct behavior if we press stop multiple times
                // followed by back.
                // Also: https://stackoverflow.com/questions/28929637/difference-and-uses-of-oncreate-oncreateview-and-onactivitycreated-in-fra
                this.findNavController().navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId)
                )
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                sleepTrackerViewModel.doneNavigating()
            }
        })

        // Add an Observer on the state variable for Navigating when and item is clicked.
        sleepTrackerViewModel.navigateToSleepDetail.observe(viewLifecycleOwner, { night ->
            night?.let {
                this.findNavController().navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepDetailFragment(night)
                )
                sleepTrackerViewModel.onSleepDetailNavigated()
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_sleep_tracker, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_clear) {
            sleepTrackerViewModel.onClear()
        }

        return super.onOptionsItemSelected(item)
    }
}
