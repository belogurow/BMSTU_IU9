import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by alex on 05.04.15.
 */

class Graph {
    ArrayList<Road> GR = new ArrayList<Road>();
    int index, key;
}

class Road {
    int len;
    Graph link;

    public Road(int len, Graph link) {
        this.link = link;
        this.len = len;
    }
}

class PriQueue {
    int cap, count;
    Graph[] heap;

    public PriQueue(int n) {
        count = 0;
        cap = n;
        heap = new Graph[n];
    }

    public void Insert(PriQueue q, Graph v) {
        int i = q.count;
        q.heap[q.count++] = v;
        while (i > 0 && q.heap[(i - 1)/2].key > q.heap[i].key) {
            Graph swap = q.heap[(i - 1)/2];
            q.heap[(i - 1)/2] = q.heap[i];
            q.heap[i] = swap;
            q.heap[i].index = i;
            i = (i - 1)/2;
        }
        q.heap[i].index = i;
    }

    public Graph ExtractMin(PriQueue q) {
        Graph a = q.heap[0];
        q.count--;
        if (q.count > 0) {
            q.heap[0] = q.heap[q.count];
            q.heap[0].index = 0;
            SortHeap(q.heap, q.count, 0);
        }
        return a;
    }

    public void SortHeap(Graph[] graph, int count, int n) {
        int l, r, j, i = n;
        for(;;) {
            l = i*2 + 1;
            r = l + 1;
            j = i;
            if ((l < count) && (graph[i].key > graph[l].key))
                i = l;
            if ((r < count) && (graph[i].key > graph[r].key))
                i = r;
            if (i == j)
                break;
            Graph swap = graph[i];
            graph[i] = graph[j];
            graph[j] = swap;
            graph[i].index = i;
            graph[j].index = j;
        }
    }

    public void DecreaseKey(PriQueue q, Graph v, int k) {
        int i = v.index;
        v.key = k;
        while (i > 0 && q.heap[(i - 1)/2].key > k) {
            Graph swap = q.heap[(i - 1)/2];
            q.heap[(i - 1)/2] = q.heap[i];
            q.heap[i] = swap;
            q.heap[i].index = i;
            i = (i - 1)/2;
        }
        v.index = i;
    }

    public boolean Empty(PriQueue q) {
        return (q.count == 0);
    }


}

public class Prim {
    public static void MST_Prim(Graph[] graph, int n) {
        int a = 0;
        PriQueue PrQ = new PriQueue(n);
        Graph v = graph[0];
        for(;; v = PrQ.ExtractMin(PrQ), a += v.key) {
            v.index = -2;
            for(int i = 0; i < v.GR.size(); i++) {
                Road p = v.GR.get(i);
                Graph u = p.link;
                int len = p.len;
                if (u.index == -1) {
                    u.key = len;
                    PrQ.Insert(PrQ, u);
                }
                else if (u.index != -2 && len < u.key) {
                    PrQ.DecreaseKey(PrQ, u, len);
                }
            }
            if (PrQ.Empty(PrQ))
                break;
        }
        System.out.println(a);
    }
    
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n, m, u, v, len;
        n = in.nextInt();
        m = in.nextInt();
        Graph[] graph = new Graph[n];
        for(int i = 0; i < n; i++) {
            graph[i] = new Graph();
            graph[i].key = -1;
            graph[i].index = -1;
        }
        for(int i = 0; i < m; i++) {
            u = in.nextInt();
            v = in.nextInt();
            len = in.nextInt();
            graph[v].GR.add(new Road(len, graph[u]));
            graph[u].GR.add(new Road(len, graph[v]));
        }
        MST_Prim(graph, n);
    }
}
