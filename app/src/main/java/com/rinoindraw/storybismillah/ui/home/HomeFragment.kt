package com.rinoindraw.storybismillah.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rinoindraw.storybismillah.R
import com.rinoindraw.storybismillah.database.model.Story
import com.rinoindraw.storybismillah.databinding.FragmentHomeBinding
import com.rinoindraw.storybismillah.ui.story.StoryAdapter
import com.rinoindraw.storybismillah.utils.SessionManager
import com.rinoindraw.storybismillah.utils.ext.animateVisibility
import dagger.hilt.android.AndroidEntryPoint

@Suppress("SameParameterValue")
@AndroidEntryPoint
@ExperimentalPagingApi
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private lateinit var storyRecyclerView: RecyclerView
    private lateinit var storyAdapter: StoryAdapter

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var pref: SessionManager
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(LayoutInflater.from(requireActivity()))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = SessionManager(requireContext())
        token = pref.fetchAuthToken().toString()

        initAction()
        initSwipeRefreshLayout()
        initStoryRecyclerView()
        getAllStories()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAction() {
        binding.apply {
            binding?.btnAccount?.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_profileFragment)
            )
        }
    }

    private fun getAllStories() {
        homeViewModel.getAllStories(token).observe(viewLifecycleOwner) { result ->
            setRecyclerViewData(result)
        }
    }

    private fun initSwipeRefreshLayout() {
        binding?.swipeRefresh?.setOnRefreshListener {
            getAllStories()
        }
    }

    private fun initStoryRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        storyAdapter = StoryAdapter()

        storyAdapter.addLoadStateListener { loadState ->
            if ((loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && storyAdapter.itemCount < 1) || loadState.source.refresh is LoadState.Error) {
                binding?.apply {
                    tvNotFoundError.animateVisibility(true)
                    ivNotFoundError.animateVisibility(true)
                    rvStories.animateVisibility(false)
                }
            } else {
                binding?.apply {
                    tvNotFoundError.animateVisibility(false)
                    ivNotFoundError.animateVisibility(false)
                    isLoading(false)
                    rvStories.animateVisibility(true)
                }
            }
            binding?.swipeRefresh?.isRefreshing = loadState.source.refresh is LoadState.Loading
        }

        try {
            storyRecyclerView = binding?.rvStories!!
            storyRecyclerView.apply {
                adapter =  storyAdapter
                layoutManager = linearLayoutManager
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    private fun setRecyclerViewData(stories: PagingData<Story>) {
        val rvState = storyRecyclerView.layoutManager?.onSaveInstanceState()
        storyAdapter.submitData(lifecycle, stories)
        storyRecyclerView.layoutManager?.onRestoreInstanceState(rvState)
    }

    private fun isLoading(loading: Boolean) {
        if (loading) {
            binding.apply {
                this!!.shimmerLoading.visibility = View.VISIBLE
                shimmerLoading.startShimmer()
                rvStories.visibility = View.INVISIBLE
            }
        } else {
            binding.apply {
                this!!.rvStories.visibility = View.VISIBLE
                shimmerLoading.stopShimmer()
                shimmerLoading.visibility = View.INVISIBLE
            }
        }
    }

}