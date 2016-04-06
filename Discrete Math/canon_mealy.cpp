#include <iostream>
#include <vector>

using namespace std;

void DFS(vector<int> &used, vector<vector<int>> one, int begin, int &index, int m) {
    used[begin] = index;
    index++;
    for(int i = 0; i < m; i++)
        if (used[one[begin][i]] == -1)
            DFS(used, one, one[begin][i], index, m);
}

int main() {
    int n, m, q, index = 0;
    cin >> n >> m >> q;
    vector<vector<int>> one(n, vector<int>(m));
    for(int i = 0; i < n; i++)
        for (int j = 0; j < m; j++)
            cin >> one[i][j];
    vector<vector<string>> two(n, vector<string>(m));
    for(int i = 0; i < n; i++)
        for (int j = 0; j < m; j++)
            cin >> two[i][j];
    vector<int> used(n, -1);
    DFS(used, one, q, index, m);
    vector<vector<int>> one_new(n, vector<int>(m));
    vector<vector<string>> two_new(n, vector<string>(m));
    for(int i = 0; i < n; i++)
        if (used[i] != -1) {
            two_new[used[i]] = two[i];
            for(int j = 0; j < m; j++)
                one_new[used[i]][j] = used[one[i][j]];
        }
    cout << index << endl << m << endl << 0 << endl;
    for(int i = 0; i < index; i++) {
        for(int j = 0; j < m; j++)
            cout << one_new[i][j] << " ";
        cout << endl;
    }
    for(int i = 0; i < index; i++) {
        for(int j = 0; j < m; j++)
            cout << two_new[i][j] << " ";
        cout << endl;
    }
    return 0;
}