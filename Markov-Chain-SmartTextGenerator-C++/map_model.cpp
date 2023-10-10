/*
   CSCI 262 Data Structures, Fall 2019, Project 4 - Markov

   Author: Lucas Henke

   Modified: 10/24/2019
*/

#include "map_model.h"
#include <map>

#include <set>

map<string, set<char>> chain;
int globalOrder;
set<string> ngram;
set<char> v;
set<char> temp;

void map_model::initialize(string text, int order) {

    text += " ";
    for(int i = 0; i < order; i++){
        text += text[i];
    }


    globalOrder = order;
    string part;
    for (int i = 0; i <= (text.length() - order); i++) {
        v.clear();
        part = text.substr(i, (order));
        ngram.insert(part);

        int start = text.find(part);
        while (start != string::npos) {
            if(start+order < text.size()) {
                v.insert(text[start+order]);
            }
            start = text.find(part, start + 1);
        }
        chain[part] = v;

    }
    /*cout << test << endl;
    for(auto fam : chain){

        cout << "first: " << fam.first << " ";
        cout << "second: "; //<< fam.second.size() << " " << endl;
        for(int k = 0; k < fam.second.size(); k++){
            // cout << "here" << endl;
            cout << fam.second.size() << " ";
        }
        cout << endl;
    }*/
}

string map_model::generate(int size) {
    int randChoose = rand() % ngram.size();
    auto it = ngram.begin();
    for (int i = 0; i < randChoose; i++)
    {
        it++;
    }
    string ok1 = *it;

    string seed = ok1;
    string output = seed;


    //cout << size << endl;
    int random = rand() % chain[seed].size();
    for (int i = 0; i < size - globalOrder; i++) {
        auto possibleChar = chain[seed];
        //cout << "size: " << chain[seed].size() << endl;
        if (possibleChar.empty()) {
            break;
        }

        random = rand() % chain[seed].size();
        auto it = chain[seed].begin();
        for (int i = 0; i < random; i++)
        {
            it++;
        }
        char ok = *it;
        output += ok;

        seed = output.substr(output.length() - globalOrder, output.length());

    }

    return output;


}
