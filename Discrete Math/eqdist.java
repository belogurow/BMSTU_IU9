import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

class graph {
    ArrayList<Integer> GR = new ArrayList<>();
}
class dist {
    ArrayList<Integer> D = new ArrayList<>();
}

public class EqDist {
    public static void findTheDist(graph[] g, dist[] d, int v, int m, int n) {
        LinkedList<Integer> queue = new LinkedList<>();
        int[] used = new int[n];
        for(int i = 0; i < n; i++) {
            used[i] = 0;
        }
        queue.add(v);
        while (!queue.isEmpty()) {
            int a = queue.remove();
            for(int i = 0; i < g[a].GR.size(); i++)
                if (used[g[a].GR.get(i)] == 0) {
                    used[g[a].GR.get(i)] = 1;
                    queue.add(g[a].GR.get(i));
                    d[m].D.set(g[a].GR.get(i), d[m].D.get(a) + 1);
                    //System.out.println("i = " + i + ", " + used[g[a].GR.get(i)]);
                }
        }
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n, m, k, test = -999999;
        n = in.nextInt();
        m = in.nextInt();
        graph[] g = new graph[n];
        for(int i = 0; i < n; i++) {
            g[i] = new graph();
        }
        int u, v;
        for(int i = 0; i < m; i++) {
            u = in.nextInt();
            v = in.nextInt();
            g[v].GR.add(u);
            g[u].GR.add(v);
        }
        k = in.nextInt();
        dist[] d = new dist[k];
        for(int i = 0; i < k; i++) {
            d[i] = new dist();
        }
        for(int i = 0; i < k; i++) {
            v = in.nextInt();
            for (u = 0; u < n; u++) {
                d[i].D.add(test);
                //int q = d[i].D.get(u);
                //System.out.println(q);
            }
            d[i].D.set(v, 0);
            findTheDist(g, d, v, i, n);
            d[i].D.set(v, 0);
            /*for (u = 0; u < n; u++) {
                int q = d[i].D.get(u);
                System.out.println(q);
            }
            System.out.println(); */
        }
        boolean pr = false;
        for(int i = 0; i < n; i++) {
            boolean a = true;
            for(int j = 0; j < k-1; j++) {
                if (d[j].D.get(i) == -999999 || d[j].D.get(i) != d[j + 1].D.get(i)) {
                    a = false;
                    break;
                }
            }
            if (a) {
                pr = true;
                System.out.print(i + " ");
            }
        }
        if (!pr)
            System.out.print("-");
    }
}
