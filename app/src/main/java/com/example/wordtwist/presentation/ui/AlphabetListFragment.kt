package com.example.wordtwist.presentation.ui

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wordtwist.R
import com.example.wordtwist.presentation.adapter.AlphabetListAdapter
import com.example.wordtwist.databinding.FragmentListAlphabetBinding


class AlphabetListFragment : Fragment(), MenuProvider {

    private var _binding: FragmentListAlphabetBinding? = null

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

        chooseLayout()

    }

//    set the layout manager based on the desired orientation
    private fun chooseLayout() {
        if (isGridLayoutManager) {
            binding.alphabetListRv.layoutManager =  GridLayoutManager(requireContext(), 3)
        }else {
            binding.alphabetListRv.layoutManager = LinearLayoutManager(requireContext())
        }
        binding.alphabetListRv.adapter = AlphabetListAdapter()
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



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}