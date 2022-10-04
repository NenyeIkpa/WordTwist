package com.example.wordtwist.presentation.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.wordtwist.R
import com.example.wordtwist.databinding.FragmentOptionsBinding
import com.example.wordtwist.presentation.viewmodel.WordsByAlphabetViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class OptionsFragment : Fragment() {

    private var _binding: FragmentOptionsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOptionsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectAlphabetButton.setOnClickListener {
            findNavController().navigate(R.id.action_OptionsFragment_AlphabetListFragment)
        }

        binding.exploreButton.setOnClickListener {
            findNavController().navigate(R.id.action_OptionsFragment_to_randomizeAlphabetTwistFragment)

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}