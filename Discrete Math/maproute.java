import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by alex on 18.05.15.
 */

class Graph implements Comparable<Graph> {
    ArrayList<Graph> INTO, FROM, BUCKET;
    Graph ancestor, label, sdom, dom, parent;
    int n, mark;
    boolean used, operand;

    Graph(int n) {
        INTO = new ArrayList<Graph>();
        FROM = new ArrayList<Graph>();
        BUCKET = new ArrayList<Graph>(10);
        this.n = n;
        sdom = label = this;
        ancestor = null;
        operand = false;
    }

    @Override
    public int compareTo(Graph graph) {
        return (this.n - graph.n);
    }
}
public class Loops {
    public static void add_INTO_FROM(ArrayList<Graph> graph, int i, int n) {
        if (i < n - 1) {
            graph.get(i).FROM.add(graph.get(i + 1));
            graph.get(i+1).INTO.add(graph.get(i));
        }
    }

    public static void add_INTO_FROM_for_op(ArrayList<Graph> graph, ArrayList<Integer> mark, ArrayList<Integer> op, int n) {
        int j = 0;
        for(Graph gr : graph) {
            if (gr.operand) {
                gr.FROM.add(graph.get(mark.indexOf(op.get(j))));
                graph.get(mark.indexOf(op.get(j))).INTO.add(gr);
                j++;
            }
        }
        /*for(int i = 0, j = 0; i < n; i++) {
            if (graph.get(i).operand) {
                graph.get(i).FROM.add(graph.get(mark.indexOf(op.get(j))));
                graph.get(mark.indexOf(op.get(j))).INTO.add(graph.get(i));
                j++;
            }

        } */
    }

    public static void DFS_with_SORT(ArrayList<Graph> graph) {
        DFS(graph.get(0));
        for(int i = 0; i < graph.size(); i++) {
            if (!graph.get(i).used) {
                graph.remove(i);
                i--;
            }
            else {
                for(int j = 0; j < graph.get(i).INTO.size(); j++) {
                    if (!graph.get(i).INTO.get(j).used) {
                        graph.get(i).INTO.remove(j);
                        j--;
                    }
                }
            }
        }
        Collections.sort(graph);
    }

    public static int N = 0;
    public static void DFS(Graph graph) {
        graph.n = N;
        graph.used = true;
        N++;
        for(int i = 0; i < graph.FROM.size(); i++) {
            if (!graph.FROM.get(i).used) {
                graph.FROM.get(i).parent = graph;
                DFS(graph.FROM.get(i));
            }
        }
    }

    public static void Dominators(ArrayList<Graph> graph) {
        int n = graph.size() - 1;
        for(int i = n; i > 0; i--) {
            Graph w = graph.get(i);
            for (Graph v : w.INTO) {
                Graph u = FindMin(v);
                if (u.sdom.n < w.sdom.n)
                    w.sdom = u.sdom;
            }
            //System.out.println(0);
            w.ancestor = w.parent;
            w.sdom.BUCKET.add(w);
            for (Graph v : w.parent.BUCKET) {
                Graph u = FindMin(v);
                if (u.sdom == v.sdom)
                    v.dom = w.parent;
                else
                    v.dom = u;
            }
            //System.out.println(1);
            w.parent.BUCKET.clear();
        }
        n++;
        for(int i = 1; i < n; i++) {
            if (graph.get(i).dom != graph.get(i).sdom)
                graph.get(i).dom = graph.get(i).dom.dom;
        }
        //System.out.println(2);
        graph.get(0).dom = null;
    }

    public static Graph FindMin(Graph v) {
        Graph min;
        if (v.ancestor == null)
            min = v;
        else {
            Stack<Graph> stack = new Stack<Graph>();
            Graph u = v;
            while (u.ancestor.ancestor != null) {
                stack.push(u);
                u = u.ancestor;
            }
            while (!stack.empty()) {
                v = stack.pop();
                if (v.ancestor.label.sdom.n < v.label.sdom.n)
                    v.label = v.ancestor.label;
                v.ancestor = u.ancestor;
            }
            min = v.label;
        }
        return min;
    }

    public static int FindLoops(ArrayList<Graph> graph) {
        int loops = 0;
        for(Graph gr : graph) {
            for (Graph gr_into : gr.INTO) {
                while (gr_into != gr && gr_into != null)
                    gr_into = gr_into.dom;
                if (gr_into == gr) {
                    loops++;
                    break;
                }
            }
        }
        return loops;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt(), N = 0;
        ArrayList<Graph> graph = new ArrayList<Graph>();
        ArrayList<Integer> op = new ArrayList<>();
        ArrayList<Integer> mark = new ArrayList<>();
        for(int i = 0; i < n; i++)
            graph.add(new Graph(i));
        in.nextLine();
        /*for(int i = 0; i < n; i++) {
            String s = in.nextLine();
            int size = s.length();
            boolean operand = false;
            String number = "";
            for(int j = 0; j < size; j++) {
                number = "";
                if (Character.isDigit(s.charAt(j))) {
                    while (j < s.length() && Character.isDigit(s.charAt(j))) {
                        number += s.charAt(j) + "";
                        j++;
                    }
                    int num = new Integer(number);
                    if (operand) {
                        op.add(num);
                        graph.get(i).operand = true;
                    }
                    else
                        mark.add(num);
                    continue;
                }
                if (Character.isAlphabetic(s.charAt(j))) {
                    switch (s.charAt(j)) {
                        case 'A':
                            j += 5;
                            add_INTO_FROM(graph, i, n);
                            continue;
                        case 'B':
                            operand = true;
                            j += 6;
                            add_INTO_FROM(graph, i, n);
                            continue;
                        case 'J':
                            operand = true;
                            j += 4;
                            continue;
                    }
                }
            }
        } */
        for(int i = 0; i < n; i++) {
            String str[] = in.nextLine().split(" ");
            mark.add(new Integer(str[0]));
            switch (str[1].charAt(0)) {
                case 'A':
                    add_INTO_FROM(graph, i, n);
                    break;
                case 'B':
                    op.add(new Integer(str[2]));
                    graph.get(i).operand = true;
                    add_INTO_FROM(graph, i, n);
                    break;
                case 'J':
                    op.add(new Integer(str[2]));
                    graph.get(i).operand = true;
                    break;
            }

        }


        add_INTO_FROM_for_op(graph, mark, op, n);
        DFS_with_SORT(graph);
        Dominators(graph);
        System.out.println(FindLoops(graph));


    }
}
