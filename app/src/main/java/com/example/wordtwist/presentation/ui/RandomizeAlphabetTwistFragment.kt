package com.example.wordtwist.presentation.ui

import android.app.AlertDialog
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
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.wordtwist.R
import com.example.wordtwist.databinding.FragmentRandomizeAlphabetTwistBinding
import com.example.wordtwist.presentation.viewmodel.WordsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val TAG = "RandomizeAlphabetTwistFragment"
class RandomizeAlphabetTwistFragment : Fragment() {

    private var _binding: FragmentRandomizeAlphabetTwistBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: WordsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRandomizeAlphabetTwistBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showAlertDialog()
            }

        })

        viewModel = ViewModelProvider(requireActivity())[WordsViewModel::class.java]
        viewModel.nextWord()


        binding.showHintIv.setOnClickListener {
                    MaterialAlertDialogBuilder(requireContext())
                        .setMessage(R.string.choose_hint_options)
                        .setPositiveButton(getString(R.string.definition)) { _, _ ->
                            showWordDefinition()

                        }
                        .setNegativeButton(getString(R.string.first_alphabet)) {_,_ ->
                            showFirstAlphabet()
                        }
                        .show()


        }

        //trigger submit button when user clicks enter
        binding.answerEtField.setOnEditorActionListener { _, actionId, keyEvent ->
            if ((keyEvent != null && (keyEvent.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                binding.buttonSubmit.performClick()
            }
           false

        }
        binding.buttonSubmit.setOnClickListener {
            hideKeyboard(requireView())
            onSubmitWord() }
        binding.buttonSkip.setOnClickListener {
            hideKeyboard(requireView())
            onSkipWord() }

        viewModel.currentScrambledWord.observe(viewLifecycleOwner) {newWord ->
            binding.unscrambledWord.text = newWord
        }

        viewModel.score.observe(viewLifecycleOwner) {currentScore ->
            binding.score.text = getString(R.string.score, currentScore)
        }

        viewModel.currentWordCount.observe(viewLifecycleOwner) {currentWordCount ->
            binding.wordCount.text = getString(R.string.no_of_words_unscrambled, currentWordCount)
        }

        //trigger submit button when user clicks enter
        binding.answerEtField.setOnEditorActionListener { _, actionId, keyEvent ->
            if ((keyEvent != null && (keyEvent.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                binding.buttonSubmit.performClick()
            }
            false

        }
    }

    private fun showFirstAlphabet() {
        val firstLetter = viewModel.word.first().uppercase()
        binding.answerEtField.setText(firstLetter)
        viewModel.isHintUsed(true)
//        MaterialAlertDialogBuilder(requireContext())
//            .setMessage(firstLetter)
//            .setPositiveButton(getString(R.string.positive)) { _, _ ->
//            }
//            .show()


    }

    private fun showWordDefinition(){
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(viewModel.currentWordMeaning)
            .setPositiveButton(getString(R.string.positive)) { _, _ ->
            }
            .show()
        viewModel.isHintUsed(true)
    }

    private fun onSkipWord() {
        if (!viewModel.nextWord()) {
            setErrorTextField(false)
        }else {
            showFinalScoreDialog()
        }

    }

    private fun setErrorTextField(error: Boolean) {
        if ( error) {
            binding.answerEtLayout.isErrorEnabled = true
            binding.answerEtLayout.error = "Try again"
        }else {
            binding.answerEtLayout.isErrorEnabled = false
            binding.answerEtField.text = null
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun onSubmitWord() {
        val playerWord = binding.answerEtField.text.toString()
        Log.d(TAG, "onSubmitWord: $playerWord")
        val result = viewModel.isUserWordCorrect(playerWord)
        if (playerWord.isNotEmpty()) {
            if (result) {
                setErrorTextField(false)
                if (viewModel.nextWord()) {
                    showFinalScoreDialog()
                }
            } else {
                setErrorTextField(true)
            }
        }else {
            setErrorTextField(true)
        }
    }

    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, viewModel.score.value))
            .setCancelable(false)  //sets dialog as uncancellable when back button is pressed
            .setNegativeButton(getString(R.string.exit)) { _, _ ->   //this is shorthand for setNegativeButton(getString(R.string.exit), { _, _ -> exitGame()})
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                restartGame()
            }
            .show()

        //setNegativeButton() and setPositiveButton() takes in two parameters, a string and a function, DialogInterface.OnClickListener which can be expressed as a lambda.
        //when last argument being passed is a function,the lambda expression can be placed outside of the parentheses.
        //this is known as trailing lambda syntax.
    }

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.confirm_exit))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.negative)) {_,_ ->
                //game continues
            }
            .setPositiveButton(getString(R.string.positive)) {_,_ ->
                findNavController().navigate(R.id.action_randomizeAlphabetTwistFragment_to_OptionsFragment)
            }
            .show()
    }

    private fun restartGame() {
        viewModel.restartGame()
        setErrorTextField(false)
    }

    private fun exitGame() {
        findNavController().navigate(R.id.action_randomizeAlphabetTwistFragment_to_OptionsFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}