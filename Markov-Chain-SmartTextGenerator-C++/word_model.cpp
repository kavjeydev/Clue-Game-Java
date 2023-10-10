/*
   CSCI 262 Data Structures, Fall 2019, Project 4 - Markov

   Author: Lucas Henke

   Modified: 10/24/2019
*/

#include "word_model.h"
#include <map>
#include <vector>
#include <set>
#include <iostream>
#include <algorithm>

map<string, vector<string>> hold;
vector<string> temp;
string tempo;
int globalOrder1;

void word_model::initialize(string text, int order) {
    globalOrder1 = order;

    for(int i = 0; i < text.size(); i++){
        if(text[i] != ' '){
            tempo += text[i];
        }
        else if(text[i] == ' '){
            tempo += ' ';
            temp.push_back(tempo);
            tempo = "";
        }
    }

    for(int j = 0; j < temp.size(); j++){
        string part = temp[j];
        int tempOrder = order;
        vector<string> temporary;
        //int start = text.find(part);
        auto it = find(temp.begin(), temp.end(), part);

        if (it != temp.end())
        {
            int index = it - temp.begin();
            while(tempOrder > 0) {
                temporary.push_back(temp[index + tempOrder]);
                tempOrder--;
            }
            hold[part] = temporary;
        }

        //hold.emplace(part, temporary);
    }

    //cout << text << endl;
//    for(auto fam : hold){
//        cout << "first: " << fam.first << " ";
//        cout << "second: "; //<< fam.second.size() << " " << endl;
//        for(int k = 0; k < fam.second.size(); k++){
//            // cout << "here" << endl;
//            cout << fam.second.size() << " ";
//        }
//        cout << endl;
//    }

}


string word_model::generate(int size) {
    int randChoose = rand() % temp.size();
    string seed = temp[randChoose];
    string output = seed;
// - globalOrder1
     for (int i = 0; i < size-1; i++) {
        auto possibleChar = hold[seed];
        int random = rand() % hold[seed].size();
        output += hold[seed][random];

        seed = hold[seed][random];

    }

    return output;
}
