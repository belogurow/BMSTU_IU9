import java.util.*;


class Graph {
    ArrayList<Integer> GR = new ArrayList<>();
    int par;
    int comp;
    String mark;
}

public class BridgeNum {

    public static void DFS1(Graph[] graph, LinkedList<Integer> queue, int a, int component) {
        graph[a].par = -1;
        VisitVertex1(graph, queue, a);

    }

    public static void VisitVertex1(Graph[] graph, LinkedList<Integer> queue, int a) {
        graph[a].mark = "gray";
        queue.add(a);
        for(int i = 0; i < graph[a].GR.size(); i++) {
            if(graph[graph[a].GR.get(i)].mark == "white") {
                graph[graph[a].GR.get(i)].par = a;
                VisitVertex1(graph, queue, graph[a].GR.get(i));
            }
        }
    }

    public static int DFS2(Graph[] graph, LinkedList<Integer> queue, int component) {
        while(queue.size() > 0) {
            int a = queue.remove();
            if(graph[a].comp == -1) {
                VisitVertex2(graph, a, component);
                component++;
            }
        }
        return component;
    }

    public static void VisitVertex2(Graph[] graph, int a, int component) {
        graph[a].comp = component;
        for(int i = 0; i < graph[a].GR.size(); i++) {
            if (graph[graph[a].GR.get(i)].comp == -1 && graph[graph[a].GR.get(i)].par != a)
                VisitVertex2(graph, graph[a].GR.get(i), component);
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n ,m;
        n = in.nextInt();
        LinkedList<Integer> queue = new LinkedList<>();
        Graph[] G = new Graph[n];
        for(int i = 0; i < n; i++) {
            G[i] = new Graph();
        }
        m = in.nextInt();
        int u, v;
        for(int i = 0; i < m; i++) {
            u = in.nextInt();
            v = in.nextInt();
            G[v].GR.add(u);
            G[u].GR.add(v);
        }
        for(int i = 0; i < n; i++) {
            G[i].comp = -1;
            G[i].mark = "white";
        }
        int component = 1;
        for(int i = 0; i < n; i++) {
            if(G[i].mark == "white") {
                component--;
                DFS1(G, queue, i, component);
            }
            component = DFS2(G, queue, component);
        }
        System.out.println(component-1);
    }
}