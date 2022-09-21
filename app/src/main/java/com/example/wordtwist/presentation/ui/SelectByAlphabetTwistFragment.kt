package com.example.wordtwist.presentation.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.wordtwist.R
import com.example.wordtwist.data.model.words.Words
import com.example.wordtwist.databinding.FragmentSelectByAlphabetTwistBinding
import com.example.wordtwist.presentation.viewmodel.WordsByAlphabetViewModel
import com.example.wordtwist.presentation.viewmodel.WordsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val TAG = "SelectByAlphabetTwistFragment"

class SelectByAlphabetTwistFragment : Fragment() {
    private var _binding: FragmentSelectByAlphabetTwistBinding?= null
    private val binding get()= _binding!!
    private lateinit var alphabet: String
    private lateinit var viewModel: WordsByAlphabetViewModel

    companion object {
        val ALPHABET = "alphabet"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //retrieve the alphabet from the fragment arguments
        arguments?.let {
            alphabet = it.getString(ALPHABET).toString()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSelectByAlphabetTwistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showAlertDialog()
            }
        })

        binding.byAlphabetShowHintIv.setOnHoverListener { _, _ ->
            MaterialAlertDialogBuilder(requireContext())
                .setMessage("Click to get a hint(meaning of word)")
                .setCancelable(false)
                .show()
            true
        }

        binding.byAlphabetShowHintIv.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage(viewModel.currentWordMeaning)
                .setPositiveButton(getString(R.string.positive)) {_,_ ->
                    //return
                }
                .show()
        }

        displaySelectedBeginningAlphabet()

       viewModel = ViewModelProvider(requireActivity())[WordsByAlphabetViewModel::class.java]
        Log.d(TAG, "onViewCreated: ")
        viewModel.nextWordByAlphabet(alphabet)

        //trigger submit button when user clicks enter
        binding.byAlphabetAnswerEtField.setOnEditorActionListener { _, actionId, keyEvent ->
            if ((keyEvent != null && (keyEvent.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                binding.byAlphabetButtonSubmit.performClick()
            }
            false

        }
        binding.byAlphabetButtonSubmit.setOnClickListener {
            hideKeyboard(requireView())
            onSubmitWord()
        }
        binding.byAlphabetButtonSkip.setOnClickListener { onSkipWord() }

        //observe current scrambled word livedata
        viewModel.currentScrambledWord.observe(viewLifecycleOwner){newWord ->
            binding.byAlphabetUnscrambledWord.text = newWord
        }

        viewModel.score.observe(viewLifecycleOwner) {currentScore ->
            binding.byAlphabetScore.text = getString(R.string.score, currentScore)
        }

      viewModel.currentWordCount.observe(viewLifecycleOwner) {currentWordCount ->
          binding.byAlphabetWordCount.text = getString(R.string.no_of_words_unscrambled, currentWordCount)
      }
    }



    private fun showAlertDialog() {
       MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.confirm_exit))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.negative)) {_,_ ->
                //game continues
            }
            .setPositiveButton(getString(R.string.positive)) {_,_ ->
                findNavController().navigate(R.id.action_selectByAlphabetTwistFragment_to_OptionsFragment)
            }
           .show()
    }

    private fun displaySelectedBeginningAlphabet() {
        binding.byAlphabetWordsThatBeginWith.text = getString(R.string.words_that_begin_with, alphabet)
    }

    private fun onSkipWord() {
        if (!viewModel.nextWordByAlphabet(alphabet)) {
            setErrorTextField(false)
        }else {
            showFinalScoreDialog()
        }

    }

    private fun hideKeyboard(view: View) {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setErrorTextField(error: Boolean) {
        if ( error) {
            binding.byAlphabetAnswerEtLayout.isErrorEnabled = true
            binding.byAlphabetAnswerEtLayout.error = "Try again"
        }else {
            binding.byAlphabetAnswerEtLayout.isErrorEnabled = false
            binding.byAlphabetAnswerEtField.text = null
        }
    }

    private fun onSubmitWord() {
        val playerWord = binding.byAlphabetAnswerEtField.text.toString()
        val usedHint = binding.byAlphabetShowHintIv.isSelected
        if (viewModel.isUserWordCorrect(playerWord,usedHint)){
            setErrorTextField(false)
            if (viewModel.nextWordByAlphabet(alphabet)) {
                showFinalScoreDialog()
            }
        }else{
            setErrorTextField(true)
        }
    }

    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, viewModel.score.value))
            .setCancelable(false)  //sets dialog as uncancellable when back button is pressed
            .setNegativeButton(getString(R.string.exit)) {_,_ ->   //this is shorthand for setNegativeButton(getString(R.string.exit), { _, _ -> exitGame()})
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)) {_,_ ->
                restartGame()
            }
            .show()

        //setNegativeButton() and setPositiveButton() takes in two parameters, a string and a function, DialogInterface.OnClickListener which can be expressed as a lambda.
        //when last argument being passed is a function,the lambda expression can be placed outside of the parentheses.
        //this is known as trailing lambda syntax.
    }

    private fun restartGame() {
        viewModel.restartGame(alphabet)
        setErrorTextField(false)
    }

    private fun exitGame() {
        findNavController().navigate(R.id.AlphabetListFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}