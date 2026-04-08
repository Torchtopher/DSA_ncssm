package edu.ncssm.briansea.pset4;

import java.util.ArrayList;
import java.util.Arrays;
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


    public String toString() {
        String out = new String();
        ArrayList<T> heap_arr_copy = new ArrayList<T>(heap_arr);
        if (heap_arr_copy.size() == 0) {
            return "empty";
        }
        //System.out.println(heap_arr_copy);
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

    public void add(T elem) {
        heap_arr.add(elem);

        int cur_idx = heap_arr.size() - 1;
        // parent bigger than child, need to swap
        while (heap_arr.get(parentOf(cur_idx)).compareTo(heap_arr.get(cur_idx)) > 0) {
            swap(cur_idx, parentOf(cur_idx));
            cur_idx = parentOf(cur_idx);
            
        }

    }

    public T pop() {
        if (heap_arr.size() == 0) { return null; }

        System.out.println("Heap arr size " + heap_arr.size());

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
        System.out.println("Swapping " + from + " to " + to);
        System.out.println(heap_arr);
        heap_arr.set(from, to_org);
        heap_arr.set(to, from_org);
        System.out.println(heap_arr);
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
