package edu.ncssm.chrisholley.pset2;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class LList<T> implements List<T>, Iterable<T> {

    private LLNode head = null;
    private LLNode tail = null;
    private int size = 0;

    public LList() {

    }

    class LLiterator implements Iterator<T> {

        private LLNode cur_node = null;
        public LLiterator(LList<T> llist) {
            if (llist.head != null) {
                cur_node = llist.head;
            }
        }

        @Override
        public boolean hasNext() {
            if (cur_node != null) {
                return true;
            }
            return false;
        }

        @Override
        public T next() {
            T data = this.cur_node.getData();
            this.cur_node = this.cur_node.next;
            return data;
        }
    }

    class LLNode {
        private LLNode next;
        private LLNode prev;
        private T data;

        public LLNode(T d, LLNode nxt, LLNode prv) {
            this.next = nxt;
            this.prev = prv;
            this.data = d;
        }

        public T getData() {
            return data;
        }

        public LLNode getNext() {
            return next;
        }

        public LLNode getPrev() {
            return prev;
        }

        public void setNext(LLNode next) {
            this.next = next;
        }

        public void setPrev(LLNode prev) {
            this.prev = prev;
        }
    }


    /***
     *
     * @return number of elements in the linked list, 0 is empty
     */
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("");
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public Iterator<T> iterator() {
        return new LLiterator(this);
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("");
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public boolean add(T t) {
        System.out.println("Add called");

        this.size += 1;

        LLNode node = new LLNode(t, null, null);
        // first element
        if (this.size == 1) {
            this.head = node;
            this.tail = node;
            return true;
        }
        LLNode prev_end = this.tail;
        this.tail = node;
        node.setPrev(prev_end);
        prev_end.setNext(this.tail);
        return true;
    }


    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public void clear() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    private boolean invalidIdx(int index) {
        if (index < 0 || index >= this.size) {
            return true;
        }
        return false;
    }
    @Override
    public T get(int index) {
        System.out.println("Get called");
        if (invalidIdx(index)) {
            throw new IndexOutOfBoundsException("");
        }

        LLNode cur_node = this.head;
        for (int i = 0; i<index; i++) {
            cur_node = cur_node.next;
        }
        System.out.println(cur_node.getData());
        return cur_node.getData();
    }

    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public void add(int index, T element) {
        if (invalidIdx(index)) {
            throw new IndexOutOfBoundsException("");
        }

        // adding at the end, can delegate to add() that adds at the end
        if( this.size == 0  || index == this.size() - 1) {
            this.add(element);
            return;
        }

        LLNode cur_node = this.head;
        LLNode new_node = new LLNode(element, null, null);
        this.size++;

        // adding at the start, setting head
        if (index == 0) {
            new_node.next = cur_node;
            this.head.prev = new_node;
            this.head = new_node;
            return;
        }

        // can assume there is a left and right since we handled both other cases above
        for (int i = 0; i<index; i++) {
            cur_node = cur_node.next;
        }
        LLNode left = cur_node.prev;
        left.next = new_node;
        cur_node.prev = new_node;
    }

    @Override
    public T remove(int index) {
        if (invalidIdx(index)) {
            throw new IndexOutOfBoundsException("");
        }
        
        if (index == 0) {
            
        } else if (index == this.size - 1) {
            
        }

        LLNode cur_node = this.head;
        for (int i = 0; i<index; i++) {
            cur_node = cur_node.next;
        }
        
        
        
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException("");
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("");
    }

}
