package com.example.wordtwist.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wordtwist.data.model.words.MAX_NUMBER_OF_WORDS
import com.example.wordtwist.data.model.words.Words
import com.example.wordtwist.data.model.words.Words.words
private const val TAG = "WordsViewModel"
private const val SCORE_INCREASE = 20

class WordsViewModel: ViewModel() {
   private lateinit var currentWord: String
    private var wordList = mutableListOf<String>()
    private var _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord get() = _currentScrambledWord
    private var _currentWordCount = MutableLiveData<Int>(0)
    val currentWordCount get() = _currentWordCount
    private var _score = MutableLiveData<Int>(0)
    val score get() = _score


    private fun getNextWord() {
        //get a random list from the list of words and a random word from that random list.
        currentWord = Words.words.random().random().first
//        breakdown current word to characters
        val tempWord = currentWord.toCharArray()

//        ensure that scrambled word displayed is not equal the word required
        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }
//        check if the next word being selected has been unscrambled
        if (wordList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord.value = String(tempWord)
            _currentWordCount.value = _currentWordCount.value?.inc()
            wordList.add(currentWord)
        }
    }

    private fun increaseScore() {
        _score.value = _score.value?.plus(SCORE_INCREASE)
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
     if (playerWord.equals(currentWord, true)) {
         increaseScore()
         return true
     }
        return false
    }

    fun nextWord(): Boolean {
        return if (currentWordCount.value!! < MAX_NUMBER_OF_WORDS) {
            getNextWord()
            false
        }else {
            true

        }

    }


    //Reinitialize game data to start game
    fun restartGame() {
        _score.value = 0
        _currentWordCount.value = 0
        wordList.clear()
        getNextWord()
    }
}