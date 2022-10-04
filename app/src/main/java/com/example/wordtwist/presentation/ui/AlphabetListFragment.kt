package com.example.wordtwist.presentation.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.example.wordtwist.R
import com.example.wordtwist.presentation.adapter.AlphabetListAdapter
import com.example.wordtwist.databinding.FragmentListAlphabetBinding
import com.example.wordtwist.presentation.viewmodel.WordsByAlphabetViewModel
import com.example.wordtwist.utils.OnAlphabetClickListener


class AlphabetListFragment : Fragment(), MenuProvider, OnAlphabetClickListener {

    private var _binding: FragmentListAlphabetBinding? = null
    private val viewModel: WordsByAlphabetViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var isGridLayoutManager = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListAlphabetBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        //disable swipeability of sliding layout
        binding.slidingPaneLayout.lockMode = SlidingPaneLayout.LOCK_MODE_LOCKED

        //add custom callback for sliding layout on press of back button
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            AlphabetListOnBackPressedCallBack(binding.slidingPaneLayout))

        chooseLayout()

        getScrambledWord()

    }

//    set the layout manager based on the desired orientation
    private fun chooseLayout() {
        if (isGridLayoutManager) {
            binding.alphabetListRv.layoutManager =  GridLayoutManager(requireContext(), 3)
        }else {
            binding.alphabetListRv.layoutManager = LinearLayoutManager(requireContext())
        }
        binding.alphabetListRv.adapter = AlphabetListAdapter(this)
    }

    private fun setMenuIcon(menuItem: MenuItem?) {
        if (menuItem == null) {
            return
        }
        menuItem.icon = if(isGridLayoutManager)
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_grid_layout)
        else ContextCompat.getDrawable(requireContext(), R.drawable.ic_linear_layout)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.alphabet_list_layout_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId) {
            R.id.switch_layout -> {
//                sets isLinearLayoutManager to the opposite value
                isGridLayoutManager = !isGridLayoutManager
                chooseLayout()
                setMenuIcon(menuItem)
                 true
            }else -> {
                false
            }
        }
    }

    //set scrambled word when the sliding pane layout is opened initially
    private fun getScrambledWord() {
        val currentAlphabet = viewModel.currentAlphabet.value.toString()
        if (binding.slidingPaneLayout.isOpen && currentAlphabet.isEmpty()) {
            viewModel.nextWordByAlphabet("A")
        }
    }

    override fun onAlphabetClicked(alphabet: String) {
        viewModel.getAlphabet(alphabet)
        viewModel.nextWordByAlphabet(alphabet)
        binding.slidingPaneLayout.openPane()
    }







    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


class AlphabetListOnBackPressedCallBack(
    private val slidingPaneLayout: SlidingPaneLayout
    ): OnBackPressedCallback(slidingPaneLayout.isSlideable && slidingPaneLayout.isOpen),
SlidingPaneLayout.PanelSlideListener{
    override fun handleOnBackPressed() {
        slidingPaneLayout.closePane()
    }

    override fun onPanelSlide(panel: View, slideOffset: Float) {
    }

    override fun onPanelOpened(panel: View) {
        isEnabled = true
    }

    override fun onPanelClosed(panel: View) {
        isEnabled = false
    }

    init {
        slidingPaneLayout.addPanelSlideListener(this)
    }
}