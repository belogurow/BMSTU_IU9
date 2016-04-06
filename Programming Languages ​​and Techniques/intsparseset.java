import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by alex on 12.04.15.
 */
public class IntSparseSet extends AbstractSet<Integer> {
    private int count;
    private final int low, high;
    private int[] sparse, dense;

    public IntSparseSet(int low, int high) {
        this.low = low;
        this.high = high;
        this.count = 0;
        this.sparse = new int[high-low];
        this.dense = new int[high-low];
    }

    @Override
    public Iterator<Integer> iterator() {
        return new IntIterator();
    }

    private class IntIterator implements Iterator<Integer> {
        int count1;

        public IntIterator() {
            count1 = 0;
        }

        public boolean hasNext() {
            return (count1 < count);
        }

        public Integer next() {
            return dense[count1++] + low;
        }
        public void remove() {
            IntSparseSet.this.remove(dense[count1-1] + low);
        }
    }

    @Override
    public int size() {
        return count;
    }

    public void clear() {
        count = 0;
    }

    @Override
    public boolean remove(Object x) {
        int a = (Integer) x;
        a = a - low;
        if (a >= high - low || a < 0)
            return false;
        if (sparse[a] <= count-1 && dense[sparse[a]] == a) {
            count--;
            dense[sparse[a]] = dense[count];
            sparse[dense[count]] = sparse[a];
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean add(Integer x) {
        int a = (Integer) x;
        a = a - low;
        if (a >= high - low || a < 0)
            return false;
        if  (sparse[a] >= count || dense[sparse[a]] != a) {
            sparse[a] = count;
            dense[count] = a;
            count++;
            return true;
        }
        else
            return false;
    }

    public boolean contains(Object x) {
        int a = (Integer) x;
        a = a - low;
        if (a >= high - low || a < 0)
            return false;
        else
            return (sparse[a] >= 0 && sparse[a] < high && dense[sparse[a]] == a);
    }
}
