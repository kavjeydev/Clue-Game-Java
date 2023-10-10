/*
    hangman.cpp
        
    Method implementations for the hangman class.
    
    assignment: CSCI 262 Project - Evil Hangman        

    author:

    last modified: 3/7/2019
*/

#include "hangman.h"
#include <set>
#include <fstream>
#include <vector>
#include <iostream>
#include<bits/stdc++.h>
#include <map>

using namespace std;

// constructor
int GUESSES;
int WORD_LENGTH;
string DISPLAY_WORD;
string ALREADY_GUESSED;
set<string> words_set = {};
vector<string> chosen_set = {};
string chosen_word;

map<string, int> families;

int count1;
string global;

hangman::hangman() {



}

int hangman::get_setsize(){
    return chosen_set.size();
}

// start_new_game()
//
// Setup a new game of hangman.
void hangman::start_new_game(int num_guesses, int word_length) {
    GUESSES = num_guesses;
    WORD_LENGTH = word_length;

    ifstream fin("dictionary.txt");
    string word;


    while (!fin.eof()) {
        getline(fin, word);
        words_set.insert(word);
    }

    fin.close();

    for(int i = 0; i < WORD_LENGTH; i++){
        DISPLAY_WORD += "-";
    }

}

void removeFrom(){

}

// process_guess()
//
// Process a player's guess - should return true/false depending on whether
// or not the guess was in the hidden word.  If the guess is incorrect, the
// remaining guess count is decreased.
bool hangman::process_guess(char c, string current) {
    string biggest;
    ALREADY_GUESSED += c;
    bool end = false;
    sort(ALREADY_GUESSED.begin(), ALREADY_GUESSED.end());
    int sizeFam = 0;
    find(c, DISPLAY_WORD);
    for(auto fam : families){
        //find(c, DISPLAY_WORD);
        string name = fam.first;
        int size = fam.second;
       // cout << "size: " << size << " sizeFam: " << sizeFam << endl;
        if(size > sizeFam){
           // cout << "name: " << fam.first << endl;
            biggest = name;
            sizeFam = size;

            //cout << endl;
            end = true;
        }
       // fam.second = 0;

        //sizeFam = 0;

    }
   // for(int i = 0; i < count1; i++){
        //string fam = global;
       // families[global] = 0;

    //}

    for(string guessed : chosen_set){

        string fam = assign(current, c, guessed);
        //cout << fam << endl;
        global = fam;
        families[fam]--;
        //count1++;
        //cout << families[fam] << endl;
    }

    /*count1 = 0;
    global = "";*/
    if(!(DISPLAY_WORD != biggest)){
        GUESSES--;
        end = false;
    }

    DISPLAY_WORD = biggest;


    //cout << chosen_set[0] << endl;
    // << biggest << endl;



    removeFromCurrent([=](string word) {
        for (size_t i = 0; i < word.length(); i++) {
            if (DISPLAY_WORD[i] == '-' && word[i] == c) {
                return true;
            }
            if (DISPLAY_WORD[i] != '-' && DISPLAY_WORD[i] != word[i]) {
                return true;
            }
        }
        return false;
    });


  //  cout << "getting here" << endl;


    return end;
}





string hangman::assign(string current, char c, string word){
    string fam = current;
    for(size_t i = 0; i < word.length(); i++){
        if(word[i] == c){
            fam[i] = c;
        }
    }
    //cout << fam << endl;
    return fam;
}


void hangman::find(char c, string current){
    //families[global] = 0;
    for(string guessed : chosen_set){

        string fam = assign(current, c, guessed);
        //cout << fam << endl;
        global = fam;
        families[fam]++;
        //count1++;
        //cout << families[fam] << endl;
    }
}
void resetMap(){
    for(int i = 0; i < count1; i++){
        string fam = global;
        families[fam]--;
    }
}


void hangman::removeFromCurrent(function<bool(string)> ok){
    chosen_set.erase(remove_if(chosen_set.begin(), chosen_set.end(), ok), chosen_set.end());
}


void hangman::choose_word() {
    for(auto itr = words_set.begin(); itr != words_set.end(); itr++){
        if(itr->length() == WORD_LENGTH){
            chosen_set.push_back(*itr);
        }
    }

}


// get_display_word()
//
// Return a representation of the hidden word, with unguessed letters
// masked by '-' characters.
string hangman::get_display_word() {
    return DISPLAY_WORD;
}


// get_guesses_remaining()
//
// Return the number of guesses remaining for the player.
int hangman::get_guesses_remaining() {
    return GUESSES;
}


// get_guessed_chars()
//
// What letters has the player already guessed?  Return in alphabetic order.
string hangman::get_guessed_chars() {
    return ALREADY_GUESSED;
}


// was_char_guessed()
//
// Return true if letter was already guessed.
bool hangman::was_char_guessed(char c) {
    for(int i = 0; i < ALREADY_GUESSED.size(); i++){
        if(ALREADY_GUESSED[i] == c){
            return true;
        }
    }
    return false;
}


// is_won()
//
// Return true if the game has been won by the player.
bool hangman::is_won() {
    int count = 0;
    for(int i = 0; i < DISPLAY_WORD.size(); i++){
        if(DISPLAY_WORD[i] == '-'){
            count++;
        }
    }
    if(count > 0){
        return false;
    }
    if(DISPLAY_WORD.find('-') == string::npos){
        DISPLAY_WORD = "";
        GUESSES = 0;
        WORD_LENGTH = 0;
        ALREADY_GUESSED = "";
        words_set = {};
        chosen_set = {};
        return true;
    }
    //return true;
}


// is_lost()
//
// Return true if the game has been lost.
bool hangman::is_lost() {
    if(GUESSES == 0 && DISPLAY_WORD.find('-') != string::npos){
        DISPLAY_WORD = "";
        GUESSES = 0;
        WORD_LENGTH = 0;
        ALREADY_GUESSED = "";
        words_set = {};
        chosen_set = {};
        return true;
    }
    return false;
}


// get_hidden_word
//
// Return the true hidden word to show the player.
string hangman::get_hidden_word() {
    int random = rand() % chosen_set.size();
    return chosen_set[0];
}
string hangman::get_setFirst() {

    return chosen_set[0];
}


