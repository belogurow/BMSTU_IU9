#include <iostream>
#include <vector>

using namespace std;

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
    cout << "digraph {\n\trankdir = LR\n\tdummy [label = \"\", shape = none]\n";
    for(int i = 0; i < n; i++)
        cout << "\t" << i << " [shape = circle]\n";
    cout << "\tdummy -> " << q << endl;
    for(int i = 0; i < n; i++)
        for(int j = 0; j < m; j++)
            cout << "\t"  << i << " -> " << one[i][j] << " [label = \"" << char(97+j) << "(" << two[i][j] << ")\"]\n";
    cout << "}";
    return 0;
}