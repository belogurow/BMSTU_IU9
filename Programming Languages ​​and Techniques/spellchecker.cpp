#include <iostream>
#include <vector>
#include <algorithm>
#include <fstream>
#include <map>

using namespace std;

vector<string> FindBigramm(string str) {
    vector<string> bi;
    char a[3] = { 0 };
    int size = str.size();
    if (size <= 2) {
        bi.push_back(str);
    }
    else
        for(int i = 0; i < size - 1; i++) {
            a[0] = str[i];
            a[1] = str[i+1];
            bi.push_back(a);
        }
    //for(string s : bi)
    //    cout << s << " ";
    //cout << endl;
    sort(bi.begin(), bi.end());
    return bi;
}

double MeasureOfSimilarity(vector<string> bi1, vector<string> bi2) {
    /*for(string s : bi1)
        cout << s << " ";
    cout << " : ";
    for(string s : bi2)
        cout << s << " ";
    cout << endl; */
    vector<string> bi3;
    set_intersection(bi1.begin(), bi1.end(), bi2.begin(), bi2.end(), inserter(bi3, bi3.begin()));
    /*for(string s : bi1)
        for(string q : bi2)
            if (s == q) {
                bi3.push_back(s);
                break;
            } 
    for(string s : bi3)
        cout << s << " ";
    cout << endl; */
    double x = bi3.size();
    if (x == 0)
        return 0;
    bi3.clear();
    set_union(bi1.begin(), bi1.end(), bi2.begin(), bi2.end(), inserter(bi3, bi3.begin()));
    /*bi3 = bi1;
    for(string s : bi2) {
        bool add = true;
        for(string q : bi3) {
            if (s == q) {
                add = false;
                break;
            }
        }
        if (add)
            bi3.push_back(s);
    }

    for(string s : bi3)
        cout << s << " ";
    cout << endl; */
    return x/bi3.size();
}



int main() {
    map<string, pair<vector<string>, int>> dictionary;
    ifstream dict("count_big.txt");
    while(dict) {
        string str;
        int x;
        dict >> str >> x;
        dictionary.emplace(str, make_pair(FindBigramm(str), x));
    }
    do {
        string str, out;
        cin >> str;
        out = str;
        vector<string> bi = FindBigramm(str);
        double measure = -1.0;
        int n = 0;
        for(auto i = dictionary.begin(); i != dictionary.end(); i++) {
            //cout << str << " : " << *(i->second.first) << endl;
            double measure_in = MeasureOfSimilarity(bi, i->second.first);
            auto help = i->second.second;
            if (measure_in > measure || (measure_in == measure && i->second.second > n) || (measure_in == measure && help == n && i->first < out)) {
                measure = measure_in;
                n = help;
                out = i->first;

            }
        }
        if (cin)
            cout << out << endl;
    } while (cin);
    dict.close();
    return 0;
}