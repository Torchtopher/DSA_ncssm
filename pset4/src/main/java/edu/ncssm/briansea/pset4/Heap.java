package edu.ncssm.briansea.pset4;

import java.util.ArrayList;
import java.util.Comparator;

public class Heap<T extends Comparable<T>> {

    private ArrayList<T> heap_arr = new ArrayList<>();
    private Comparator<T> comp = new NaturalOrdering<T>();

    // default constructor does nothing since default comparator is natural ordering of T
    public Heap() {}

    public Heap(Comparator<T> c) {
        this.comp = c;
    }

    /**
     *
     * @return The element at the root of the heap, null if empty
     */
    public T peek() {
        if (heap_arr.size() == 0) { return null; }
        return heap_arr.getFirst();
    }

    /**
     *
     * @return String representaion of the heap, do not rely on this method for anything
     */
    public String toString() {
        String out = new String();
        ArrayList<T> heap_arr_copy = new ArrayList<T>(heap_arr);
        if (heap_arr_copy.size() == 0) {
            return "empty";
        }
        out += heap_arr_copy.get(0) + "\n";
        ArrayList<Integer> children = new ArrayList<>();
        ArrayList<Integer> new_children = new ArrayList<>();
        for (int i : childOf(0)) {
            children.add(i);
        }

        while (!children.isEmpty()) {
            for (Integer c : children) {
                if (c >= heap_arr_copy.size()) {
                    break;
                }
                // should use stringbuilder
                out += " " + heap_arr_copy.get(c);
                for (int i : childOf(c)) {
                    new_children.add(i);
                }
            }
            children = new_children;
            new_children.clear();
            out += "\n";
        }
        return out;
    }

    /**
     *
     * @param elem, adds elem to the MinHeap
     */
    public void add(T elem) {
        heap_arr.add(elem);

        int cur_idx = heap_arr.size() - 1;
        // parent bigger than child, need to swap
        while (heap_arr.get(parentOf(cur_idx)).compareTo(heap_arr.get(cur_idx)) > 0) {
            swap(cur_idx, parentOf(cur_idx));
            cur_idx = parentOf(cur_idx);
            
        }

    }

    /***
     *
     * @return the smallest element in the MinHeap, null if empty
     */
    public T remove() {
        if (heap_arr.size() == 0) { return null; }

        swap(0, heap_arr.size()-1); // move it to the end
        T removed = heap_arr.remove(heap_arr.size()-1);
        int cur_idx = 0; // heapify down

        int left_child = childOf(cur_idx)[0];
        int right_child = childOf(cur_idx)[1];
        boolean has_left = left_child < heap_arr.size();
        boolean has_right = right_child < heap_arr.size();

        boolean bigger_than_left = has_left && heap_arr.get(cur_idx).compareTo(heap_arr.get(left_child)) > 0;
        boolean bigger_than_right = has_right && heap_arr.get(cur_idx).compareTo(heap_arr.get(right_child)) > 0;
        while (bigger_than_left || bigger_than_right) {

            if (bigger_than_right && !bigger_than_left) {
                swap(cur_idx, right_child);
                cur_idx = right_child;
            } else if (bigger_than_left && !bigger_than_right) {
                swap(cur_idx, left_child);
                cur_idx = left_child;
            }
            else {
                // left is smaller or equal to right
                if (heap_arr.get(left_child).compareTo(heap_arr.get(right_child)) <= 0) {
                    swap(cur_idx, left_child);
                    cur_idx = left_child;
                }
                else {
                    swap(cur_idx, right_child);
                    cur_idx = right_child;
                }
            }

            left_child = childOf(cur_idx)[0];
            right_child = childOf(cur_idx)[1];
            has_left = left_child < heap_arr.size();
            has_right = right_child < heap_arr.size();
            bigger_than_left = has_left && heap_arr.get(cur_idx).compareTo(heap_arr.get(left_child)) > 0;
            bigger_than_right = has_right && heap_arr.get(cur_idx).compareTo(heap_arr.get(right_child)) > 0;
        }

        return removed;
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

    /**
     *
     * @param parent, parent index
     * @return int[2] array, notably will contain both possible child indcies even if one or both are out of bounds
     */
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
