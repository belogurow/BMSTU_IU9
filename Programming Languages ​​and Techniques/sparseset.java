import java.util.*;
/**
 * Created by alex on 19.04.15.
 */
public class SparseSet<T extends Hintable> extends AbstractSet<T> {
    private ArrayList<T> dense;
    private int count;

    public SparseSet() {
        this.dense = new ArrayList<>();
        this.count = 0;
    }


    public void clear() {
        this.count = 0;
    }

    public int size() {
        return count;
    }

    public Iterator<T> iterator() {
        return new ObjIterator();
    }

    private class ObjIterator implements Iterator<T> {
        int count1;

        public ObjIterator() {
            count1 = 0;
        }

        public boolean hasNext() {
            return (count1 < count);
        }

        public T next() {
            return dense.get(count1++);
        }

        public void remove() {
            SparseSet.this.remove(dense.get(count1 - 1));
        }


    }

    public boolean add(T x) {
        if (x.hint() >= count || dense.get(x.hint()) != x) {
            dense.add(x);
            x.setHint(count);
            count++;
            return true;
        }
        else
            return false;
    }

    public boolean remove(Object x) {
        T a = (T) x;
        if (dense.get(a.hint()) == a && a.hint() <= count-1) {
            count--;
            dense.get(count).setHint(a.hint());
            dense.set(a.hint(), dense.get(count));
            return true;
        }
        else
            return false;
    }

    public boolean contains(Object x) {
        T a = (T) x;
        return (dense.get(a.hint()) == a && a.hint() < count);
    }
}