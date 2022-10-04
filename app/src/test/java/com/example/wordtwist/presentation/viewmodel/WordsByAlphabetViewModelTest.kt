package com.example.wordtwist.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class WordsByAlphabetViewModelTest {

    @get:Rule

    val instantExecutorRule = InstantTaskExecutorRule()

    val viewModel = WordsByAlphabetViewModel()

    val arrayOfAlphabets = ('A').rangeTo('Z').toList()
    val alphabet = arrayOfAlphabets.random().toString()


    @Test
    fun returns_true_when_max_word_count_is_reached() {
        viewModel.currentWordCount.value = 10
        val result = viewModel.nextWordByAlphabet(alphabet)
        assertEquals(true, result)
    }

    @Test
    fun word_begins_with_given_alphabet() {
//        viewModel.currentWord.observeForever {  }
//        viewModel.currentScrambledWord.observeForever {  }
        assertEquals(viewModel.currentWord.value?.length, viewModel.currentScrambledWord.value?.length)
    }

    @Test
    fun word_value_matches_key() {
        assertEquals(viewModel.currentWord.value, viewModel.currentWordMeaning)
    }
}