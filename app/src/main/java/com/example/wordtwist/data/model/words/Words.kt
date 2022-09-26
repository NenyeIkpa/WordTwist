package com.example.wordtwist.data.model.words

import com.example.wordtwist.data.model.words.atozwords.*

const val MAX_NUMBER_OF_WORDS = 10
object Words {
    val words = listOf(A, B, C)



    fun getWordsByStartingLetter(alphabet: String): List<Pair<String,String>> {
      return  when(alphabet)
      {
          "A" -> A
          "B" -> B
          "C" -> C
          "D" -> D
          "E" -> E
          "F" -> F
          "G" -> G
          "H" -> H
          "I" -> I
          "J" -> J
          "K" -> K
          "L" -> L
          "M" -> M
          "N" -> N
          "O" -> O
          "P" -> P
          "Q" -> Q
          "R" -> R
          "S" -> S
          "T" -> T
          "U" -> U
          "V" -> V
          "W" -> W
          "X" -> X
          "Y" -> Y
          else -> Z

        }
    }
}