package com.example.dishlist.presentation.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.dishlist.R
import com.example.dishlist.databinding.FragmentDishListBinding
import com.example.dishlist.presentation.state.DishListScreenState
import com.example.dishlist.presentation.utils.DishAdapter
import com.example.dishlist.presentation.utils.addDishItemDecoration
import com.example.dishlist.presentation.viewmodel.DishListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DishListFragment : Fragment() {

    private var _binding: FragmentDishListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DishListViewModel by viewModels()
    private val dishAdapter by lazy { createDishAdapter() }

    private fun createDishAdapter(): DishAdapter {
        return DishAdapter(
            checkDishAction = { id, isChecked -> viewModel.checkDish(id, isChecked) },
            transitionAction = { model, transitionView ->
                val action = DishListFragmentDirections.actionDishListFragmentToDishDetailsFragment(model)
                val extras = FragmentNavigatorExtras(transitionView to transitionView.transitionName)
                findNavController().navigate(action, extras)
            }
        )
    }

    private val separatorDrawable by lazy {
        ResourcesCompat.getDrawable(resources, R.drawable.grid_divider, null)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDishListBinding.inflate(layoutInflater, container, false)
        setNavigationTransitionAnimation()
        return binding.root
    }

    private fun setNavigationTransitionAnimation() {
        // todo: fix enter animation, return works well, enter - very fast
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.grid_exit_transition)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewModelObserver()
        initRecyclerView()
        setButtonClickListener()
        setupRefreshLayout()
        postponeEnterTransition()
        binding.dishListRecyclerView.post { startPostponedEnterTransition() }
    }

    private fun setupRefreshLayout() {
        // todo: add scroll to top on refresh
        binding.dishListRefreshLayout.apply {
            scrollUpChild = binding.dishListRecyclerView
            setOnRefreshListener { viewModel.refresh() }
        }
    }

    private fun setButtonClickListener() {
        binding.dishListButton.setOnClickListener {
            viewModel.removeCheckedDishes()
        }
    }

    private fun setViewModelObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.screenState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { state ->
                    showLoader(state.shouldShowProgress)
                    hideEmptyView()

                    when (state) {
                        is DishListScreenState.SuccessScreenLoad -> {
                            dishAdapter.populateData(state.items)
                            binding.dishListButton.isEnabled = state.shouldEnableButton
                        }
                        is DishListScreenState.EmptyScreen -> {
                            showEmptyView()
                        }
                        else -> {}
                    }
                }
        }
    }

    private fun showEmptyView() {
        dishAdapter.populateData(emptyList())
        binding.dishListContainer.visibility = View.GONE
        binding.dishListEmptyView.visibility = View.VISIBLE
    }

    private fun hideEmptyView() {
        binding.dishListContainer.visibility = View.VISIBLE
        binding.dishListEmptyView.visibility = View.GONE
    }

    private fun showLoader(shouldShowProgress: Boolean) {
        binding.dishListRefreshLayout.isRefreshing = shouldShowProgress
    }

    private fun initRecyclerView() {
        binding.dishListRecyclerView.apply {
            adapter = dishAdapter
            addDishItemDecoration(separatorDrawable)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}