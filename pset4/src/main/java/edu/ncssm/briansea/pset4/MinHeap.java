package edu.ncssm.briansea.pset4;

import java.util.ArrayList;
import java.util.Comparator;

public class MinHeap<T extends Comparable<T>> {

    private ArrayList<T> heap_arr = new ArrayList<>();
    private Comparator<T> comp = new NaturalOrdering<T>();

    public MinHeap() {
    }

    public MinHeap(Comparator<T> c) {
        this.comp = c;
    }

    public T peek() {
        if (heap_arr.size() == 0) { return null; }
        return heap_arr.getFirst();
    }



    public void add(T elem) {
        heap_arr.add(elem);

        int cur_idx = heap_arr.size() - 1;
        // parent bigger than child, need to swap
        while (heap_arr.get(parentOf(cur_idx)).compareTo(heap_arr.get(cur_idx)) > 0) {
            swap(cur_idx, parentOf(cur_idx));
            cur_idx = parentOf(cur_idx);
            
        }

    }

    public T remove() {

    }

    private void swap(int from, int to) {
        T from_org = heap_arr.get(from);
        T to_org = heap_arr.get(to);
        heap_arr.set(from, to_org);
        heap_arr.set(to, from_org);
    }

    private int parentOf(int child) {
        // don't go out of bounds
        return Math.max(0, ((child - 1) / 2));
    }

    private int[] childOf(int parent) {
        return new int[]{parent*2 + 1, parent*2 + 2};
    }


    private static class NaturalOrdering<T extends Comparable<T>> implements Comparator<T>{
        @Override
        public int compare(T left, T right) {
            return left.compareTo(right);
        }
    }

}
