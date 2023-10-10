#ifndef _HANGMAN_H
#define _HANGMAN_H

/*
    hangman.h
        
    Class definition for the hangman class.

    assignment: CSCI 262 Project - Evil Hangman        

    author: 

    last modified: 9/24/2017
*/

#include <string>
#include <vector>
#include <bits/stdc++.h>


using namespace std;

/******************************************************************************
   class hangman

   Maintains game state for a game of hangman.

******************************************************************************/

class hangman {
public:
    hangman();

    // start a new game where player gets num_guesses unsuccessful tries
	void start_new_game(int num_guesses, int word_length);

    // player guesses letter c; return whether or not char is in word
    bool process_guess(char c, string current);

    string assign(string current, char c, string word);

    void find(char c, string current);

    // display current state of word - guessed characters or '-'
    string get_display_word();

    void removeFromCurrent(function<bool(string)> test);

    int get_setsize();

    // How many guesses remain?
	int get_guesses_remaining();

    void choose_word();

    // What characters have already been guessed (for display)?
    string get_guessed_chars();

    // Has this character already been guessed?
    bool was_char_guessed(char c);

    // Has the game been won/lost?  (Else, it continues.)
    bool is_won();
    bool is_lost();

    string updateDisplay();

    // Return the true hidden word.
    string get_hidden_word();

    string get_setFirst();

private:
    
};

#endif
