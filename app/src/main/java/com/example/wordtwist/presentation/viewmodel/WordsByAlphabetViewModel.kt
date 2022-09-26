package com.example.wordtwist.presentation.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wordtwist.data.model.words.MAX_NUMBER_OF_WORDS
import com.example.wordtwist.data.model.words.Words

private const val TAG = "WordsByAlphabetViewModel"
private const val SCORE_INCREASE = 10
private const val HINT_USE_VALUE = 5

class WordsByAlphabetViewModel : ViewModel(){

    private lateinit var currentWord: String
    private var _currentWordMeaning: String? = null
    val currentWordMeaning get() = _currentWordMeaning
    private var wordList = mutableListOf<String>()
    private var _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String> get() = _currentScrambledWord
    private var _currentWordCount = MutableLiveData(0)
    val currentWordCount get() = _currentWordCount
    private var _score = MutableLiveData(0)
    val score get() = _score

    private fun getNextWordByAlphabet(alphabet: String) {
//        var currentWordSelection: Pair<String,String>? = null
//        for (i in Words.words.indices) {
//            if (alphabet.equals(Words.words[i].first().first.first().toString(), true))
//                currentWordSelection = Words.words[i].random()
           val currentWordSelection = Words.getWordsByStartingLetter(alphabet).random()
            Log.d(TAG, "getNextWordByAlphabet: $currentWordSelection")
                currentWord = currentWordSelection.first
            _currentWordMeaning= currentWordSelection.second
            Log.d(TAG, "getNextWordByAlphabet: $currentWord ")
//        }

        //        breakdown current word to characters
        val tempWord = currentWord.toCharArray()

//        ensure that scrambled word displayed is not equal the word required
        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }
//        check if the next word being selected has been unscrambled
        if (wordList.contains(currentWord)) {
            getNextWordByAlphabet(alphabet)
        } else {
            _currentScrambledWord.value = String(tempWord)
            currentWordCount.value = _currentWordCount.value?.inc()
            wordList.add(currentWord)
        }
    }

    private fun increaseScore(usedHint: Boolean) {
        if (!usedHint) {
            _score.value = _score.value?.plus(SCORE_INCREASE)
        }else {
            _score.value = _score.value?.plus(HINT_USE_VALUE)
        }

    }

    fun isUserWordCorrect(playerWord: String, usedHint: Boolean): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore(usedHint)
            return true
        }
        return false
    }

    fun nextWordByAlphabet(alphabet: String): Boolean {
        return if (currentWordCount.value!! < MAX_NUMBER_OF_WORDS) {
            getNextWordByAlphabet(alphabet)
            false
        }else {
            true
        }

    }

    //Reinitialize game data to start game

    fun restartGame(alphabet: String) {
        _score.value = 0
        _currentWordCount.value = 0
        wordList.clear()
        getNextWordByAlphabet(alphabet)
    }

    fun onLaunch() {
        _score.value = 0
        _currentWordCount.value = 0
        wordList.clear()
    }
}