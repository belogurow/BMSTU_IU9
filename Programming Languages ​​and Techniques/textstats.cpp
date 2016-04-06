
#ifndef TEXTSTATS_HPP_INCLUDED
#define TEXTSTATS_HPP_INCLUDED

#include <string>
#include <vector>
#include <unordered_set>
#include <map>

using namespace std;

// get_tokens ��������� �����, �������������� � ASCII, �� �����,
// � ��������� ���� ���� � ������� ��������.
// ����� -- ��� ������������������ ��������, �� ���������� 
// �������������.
void get_tokens(const string &s, const unordered_set<char> &delimiters, vector<string> &tokens) {
        string a = "";
        	int size = s.size();
        for (int i = 0; i < size; i++) {
                if (delimiters.find(s[i]) == delimiters.end()) {
                        a.push_back(tolower(s[i]));
                }
                else {
                        if (a.size() != 0)
                                tokens.push_back(a);
                        a = "";
                }
        }
        if (a.size() != 0)
			tokens.push_back(a);
}

// get_type_freq ���������� ��������� ������� ������, � �������
// ��� ������� ����� ������� ���������� ��� ��������� � �����.
void get_type_freq(const vector<string> &tokens, map<string, int> &freqdi) {
        int size = tokens.size();
        for (int i = 0; i < size; i++) {
                if (freqdi.find(tokens[i]) == freqdi.end()) {
                        freqdi.insert(pair<string, int>(tokens[i], 1));
                }
                else {
                        freqdi[tokens[i]]++;
                }
        }
}

// get_types ���������� ������ ���������� ����, ������������� �
// ������. ������ ������ ���� ������������ �����������������.
void get_types(const vector<string> &tokens, vector<string> &wtypes) {
        int size = tokens.size();
        map<string, int> test;
        for (int i = 0; i < size; i++) {
                if (test.find(tokens[i]) == test.end())
                        test.insert(pair<string, int>(tokens[i], 0));
        }
        for (auto i = test.begin(); i != test.end(); i++)
                wtypes.push_back(i->first);
}

// get_x_length_words ��������� �� ������ ���������� ���� �����
// ������, � ������� �������� ������ �� �����, ����� �������
// �� ������ x ��������.
void get_x_length_words(const vector<string> &wtypes, int x, vector<string> &words) {
        int size = wtypes.size();
        for (int i = 0; i < size; i++) {
                if (wtypes[i].size() >= x)
                        words.push_back(wtypes[i]);
        }

}


void get_x_freq_words(const map<string, int> &freqdi, int x, vector<string> &words) {
        for (auto i = freqdi.begin(); i != freqdi.end(); i++) {
                if ((i->second) >= x)
                        words.push_back(i->first);
        }


}

// get_words_by_length_dict ���������� �������, � ������� ������
// ���� -- ��� ����� �����, � �������� -- ��� ������ ���� ��������
// �����.
void get_words_by_length_dict(const vector<string> &wtypes, map<int, vector<string> > &lengthdi) {
        int size = wtypes.size();
        for (int i = 0; i < size; i++) {
                lengthdi[wtypes[i].size()].push_back(wtypes[i]);
        }
}

#endif