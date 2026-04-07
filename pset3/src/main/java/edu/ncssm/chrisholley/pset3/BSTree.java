package edu.ncssm.chrisholley.pset3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class BSTree<T extends Comparable<T>> {
    private BSTNode<T> root = null;
    private Comparator<? super T> comp;

    private class BSTNode<T extends Comparable<T>> {
        public BSTNode<T> left;
        public BSTNode<T> right;
        public T data;

        public BSTNode(T d) {
            this.data = d;
        }

    }

    /***
     * Constructs an empty BST, and uses natural ordering for comaprisions
     */
    public BSTree() {
        this(null);
    }

    /***
     *
     * @param comp, constructs an empty BST, and uses the provided comparator for comparisions
     */
    public BSTree( Comparator<? super T> comp ){
        this.comp = Objects.requireNonNullElseGet(comp, () -> new NaturalOrdering<>());
    }

    /***
     *
     * @param data, inserts data into the tree, going left when ties occur
     */
    public void add( T data ){
        if (root == null) {
            root = new BSTNode<>(data);
            return;
        }

        BSTNode<T>[] res = find(data, false);
        BSTNode<T> parent = res[0];
        // BSTNode<T> child = res[1];

        BSTNode<T> to_add = new BSTNode<>(data);
        if (comp.compare(data, parent.data) <= 0) { parent.left = to_add; }
        else { parent.right = to_add; }
    }

    /***
     * Sets the root node to null, gives same state as creating a new tree
     */
    public void clear() {
        // GC got the cleanup
        this.root = null;
    }

    /***
     *
     * @param data, removes all nodes with value equal to data
     */
    public void removeAll( T data ){
        while (remove(data));
    }

    // parent first, then child
    private BSTNode<T>[] find(T data, boolean take_first) {
        BSTNode<T> parent = null;
        BSTNode<T> cur = root;

        while (cur != null) {

            // found first instance, good for remove
            // otherwise want to find the last place where we could insert this number
            // a little awkward because now parent can be null or be == root and they mean the same thing
            if (comp.compare(data, cur.data) == 0 && take_first) {break;}

            if (parent == null) { parent = root;}
            else { parent = cur; }

            // need to go left if equals or smaller than the current node
            if (comp.compare(data, cur.data) <= 0) {
                cur = cur.left;
            }
            else { cur = cur.right;}
        }
        return new BSTNode[]{parent, cur};
    }

    /**
     * Removes the first match elements from this object
     * @param data the elements to remove
     * @return true if this object is changed, false otherwise
     */
    public boolean remove( T data ) {
        if (root == null) { return false; }

        BSTNode<T>[] res = find(data, true);
        BSTNode<T> parent = res[0];
        BSTNode<T> child = res[1];

//        System.out.printf("Parent data %d, child data %d", (Integer)parent.data, (Integer)child.data);
        // find didn't actually find the right value
        if (child == null || (child.data.compareTo(data) != 0)) {return false;}
        return removeNode(child, parent);
    }

    private boolean removeNode( BSTNode<T> remove, BSTNode<T> parent ){
        BSTNode<T> to_add = remove.left != null ? remove.left : remove.right;

        // has 0 or 1 children
        if (remove.left == null || remove.right == null) {
            // trying to remove root, parent is encoded as root from find
            if ((remove == root && parent == root) || parent == null) {
                root = to_add;
                return true;
            }
            if (parent.left == remove) {
                parent.left = to_add;
            }
            else {
                parent.right = to_add;
            }
            return true;
        }

        // 2 children
        BSTNode<T> mlr_par = remove;
        BSTNode<T> mlr = remove.left;
        while (mlr.right != null) {
            mlr_par = mlr;
            mlr = mlr.right;
        }

        // if only these were ints and A ^ B ^ A could work for variable swapping
        T tmp = mlr.data;
        mlr.data = remove.data;
        remove.data = tmp;
        return removeNode(mlr, mlr_par);
    }

    /***
     *
     * @return a List<T> where the elements have been inserted the preorder (CLR)
     */
    public List<T> preOrder(){
        ArrayList<T> ret = new ArrayList<>();
        preOrderWalk(root, ret);
        return ret;
    }
    private void preOrderWalk( BSTNode<T> curr, List<T> list ){
        // clr
        if (curr == null) return;
        list.add(curr.data);
        preOrderWalk(curr.left, list);
        preOrderWalk(curr.right, list);
    }


    /***
     *
     * @return a List<T> where the elements have been inserted the inorder (LCR)
     */
    public List<T> inOrder() {
        ArrayList<T> ret = new ArrayList<>();
        inOrderWalk(root, ret);
        return ret;
    }

    private void inOrderWalk( BSTNode<T> curr, List<T> list ){
        // lcr
        if (curr == null) return;
        inOrderWalk(curr.left, list);
        list.add(curr.data);
        inOrderWalk(curr.right, list);
    }


    /***
     *
     * @return a List<T> where the elements have been inserted the postorder (LRC)
     */
    public List<T> postOrder() {
        ArrayList<T> ret = new ArrayList<>();
        postOrderWalk(root, ret);
        return ret;
    }

    private void postOrderWalk( BSTNode<T> curr, List<T> list){
        // lrc
        if (curr == null) return;
        postOrderWalk(curr.left, list);
        postOrderWalk(curr.right, list);
        list.add(curr.data);
    }

    private class NaturalOrdering<T extends Comparable<T>> implements Comparator<T>{
        /***
         * 0 if equal
         * +Z if left > right
         * -X if right > left
         */
        @Override
        public int compare(T left, T right) {
            return left.compareTo(right);
        }
    }

}