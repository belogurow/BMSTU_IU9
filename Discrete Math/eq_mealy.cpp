#include <iostream>
#include <vector>

using namespace std;

class Automat {
public:
    int n, m, q;
    vector<vector<int>> one;
    vector<vector<string>> two;
    vector<vector<int>> one_new;
    vector<vector<string>> two_new;

    Automat(int n, int m, int q) {
        this->n = n;
        this->m = m;
        this->q = q;
        vector<vector<int>> a(n, vector<int>(m));
        this->one = a;
        this->one_new = a;
        vector<vector<string>> b(n, vector<string>(m));
        this->two = b;
        this->two_new = b;
        Scan();
    }

    void DFS(vector<int> &used, int begin, int &index) {
        used[begin] = index;
        index++;
        for(int i = 0; i < m; i++)
            if (used[one[begin][i]] == -1)
                DFS(used, one[begin][i], index);
    }

    void Canon() {
        vector<int> used(n, -1);
        int index = 0;
        DFS(used, q, index);
        for(int i = 0; i < n; i++)
            if (used[i] != -1) {
                two_new[used[i]] = two[i];
                for(int j = 0; j < m; j++)
                    one_new[used[i]][j] = used[one[i][j]];
            }
        /*cout << index << endl << m << endl << 0 << endl;
        for(int i = 0; i < index; i++) {
            for(int j = 0; j < m; j++)
                cout << one_new[i][j] << " ";
            cout << endl;
        }
        for(int i = 0; i < index; i++) {
            for(int j = 0; j < m; j++)
                cout << two_new[i][j] << " ";
            cout << endl;
        }*/
        q = 0;
        n = index;
        one = one_new;
        two = two_new;
    }

    int Find(vector<int> &a, int x) {
        if (a[x] == x)
            return x;
        else
            return a[x] = Find(a, a[x]);
    }

    void Union(vector<int> &a, int x, int y) {
        int x_new = Find(a, x), y_new = Find(a, y);
        if (x_new == y_new)
            return;
        if (rand() % 2)
            swap(x_new, y_new);
        a[x_new] = y_new;
    }

    void Split(int &m, vector<int> &help) {
        m = n;
        vector<int> a;
        for(int i = 0; i < m; i++)
            a.push_back(i);
        for(int i = 0; i < n; i++)
            for(int j = i + 1; j < n; j++)
                if (help[i] == help[j] && Find(a, i) != Find(a, j)) {
                    bool eq = true;
                    for(int k = 0; k < this->m; k++) {
                        if (help[one[i][k]] != help[one[j][k]]) {
                            eq = false;
                            break;
                        }
                    }
                    if (eq) {
                        Union(a, i, j);
                        m--;
                    }
                }
        for(int i = 0; i < n; i++)
            help[i] = Find(a, i);
    }

    void Split1(int &q, vector<int> &help) {
        q = this->n;
        vector<int> a;
        for(int i = 0; i < q; i++)
            a.push_back(i);
        for(int i = 0; i < n; i++)
            for(int j = i + 1; j < n; j++)
                if (Find(a, i) != Find(a, j)) {
                    bool eq = true;
                    for(int k = 0; k < m; k++) {
                        if (two[i][k] != two[j][k]) {
                            eq = false;
                            break;
                        }
                    }
                    if (eq) {
                        Union(a, i, j);
                        q--;
                    }
                }
        for(int i = 0; i < n; i++)
            help[i] = Find(a, i);
    }

    void AufenkampHohn() {
        vector<int> help(n);
        int m1, m2 = -1;
        Split1(m1, help);
        while(m1 != m2) {
            m2 = m1;
            Split(m1, help);
        }
        vector<int> help1(n), help2(n);
        for(int i = 0, a = 0; i < n; i++)
            if (help[i] == i) {
                help2[a] = i;
                help1[i] = a++;
            }
        this->n = m1;
        this->q = help1[help[this->q]];
        vector<vector<string>> p(this->n, vector<string>(this->m));
        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++) {
                one[i][j] = help1[help[one[help2[i]][j]]];
                p[i][j] = two[help2[i]][j];
            }
        two = p;
    }

    void Print() {
        cout << "digraph {\n\trankdir = LR\n\tdummy [label = \"\", shape = none]\n";
        for(int i = 0; i < n; i++)
            cout << "\t" << i << " [shape = circle]\n";
        cout << "\tdummy -> " << 0 << endl;
        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++)
                cout << "\t"  << i << " -> " << one[i][j] << " [label = \"" << char(97+j) << "(" << two[i][j] << ")\"]\n";
        cout << "}";
    }

    void Scan() {
        for(int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                cin >> one[i][j];
        for(int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                cin >> two[i][j];
    }

    bool Eq(Automat mealy) {
        if (n != mealy.n || m != mealy.m || q != mealy.q)
            return false;
        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++)
                if (one[i][j] != mealy.one[i][j] || two[i][j] != mealy.two[i][j])
                    return false;
        return true;
    }
};

int main() {
    int n, m, q;
    cin >> n >> m >> q;
    Automat mealy1(n, m, q);
    mealy1.AufenkampHohn();
    mealy1.Canon();
    cin >> n >> m >> q;
    Automat mealy2(n, m ,q);
    mealy2.AufenkampHohn();
    mealy2.Canon();
    if (mealy1.Eq(mealy2))
        cout << "EQUAL" << endl;
    else
        cout << "NOT EQUAL" << endl;
    return 0;
}