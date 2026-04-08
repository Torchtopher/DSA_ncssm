package edu.ncssm.briansea.pset4;

import edu.ncssm.briansea.pset4.MinHeap;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
/**
 * Handles compression and decompression of data
 *
 * @author Brian Sea
 * @since Problem Set 4
 * @version 0.0.1
 */


public class Compressor {

    private class TreeMapPretty<K, V> extends TreeMap<K,V> {

        @Override
        public String toString() {
            String s = super.toString();
            // regex finds all =[1|0] and replaces with SEP so we don't get confused by ==101010 
            s = s.replaceAll("=(?=[01])", "SEP")
                                    .replace("\t", "(tab)")
                                    .replace("\n", "(enter)")
                                    .replace("  SEP", " (space)SEP")
                                    .replace(", ", "\n")
                                    .replace("SEP", ":");
            s = s.substring(1, s.length()-1); // remove first and last {, } so it looks identical to large_text-Table.txt
            return s;
        }
    }


    private class HuffmanNode implements Comparable<HuffmanNode> {
        public String representation = "";
        public Integer count = 0;
        public HuffmanNode left = null;
        public HuffmanNode right = null;

        @Override
        public int compareTo(HuffmanNode o) {
            return this.count.compareTo(o.count);
        }

        public String toString() {
            return representation + " size: " + count + "\n left: " + left + "\n\nright " + right;
        }
    }

    /**
     * Converts compressed bytes to a String
     * Uses standard Huffman Compression
     * @param bytes the bytes to decompress
     * @return the string representation
     */
    public String decompress(byte[] bytes){


        return new String(bytes);
    }

    // - Create the frequency
    //    table from the input.
    private TreeMap<Character, Integer> createFreqTable(String str) {
        TreeMap<Character, Integer> map = new TreeMap<>(); // uses natrual ordering of Character
        for (char c : str.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        System.out.println(map);
        return map;
    }

    // - Create the Huffman
    //    Tree from the frequency table. For the initial
    //    insertion, make sure to do so in lexigraphical
    //    order!
    private HuffmanNode createHuffmanTree(TreeMap<Character, Integer> freq_map) {
        MinHeap<HuffmanNode> huffTree = new MinHeap<>();
        for (Map.Entry<Character, Integer> pair : freq_map.entrySet()) {
            System.out.println(pair.getKey() + " " + pair.getValue());
            HuffmanNode n = new HuffmanNode();
            n.count = pair.getValue();
            n.representation = String.valueOf(pair.getKey());
            huffTree.add(n);
        }
        System.out.println(huffTree);

        while (true) {
            if (huffTree.peek() == null) {
                break;
            }
            HuffmanNode n1 = huffTree.pop();
            if (huffTree.peek() == null) {
                return n1; // if only 1 node
            }
            HuffmanNode n2 = huffTree.pop();
            HuffmanNode merged_node = new HuffmanNode();
            merged_node.representation = n2.representation + n1.representation;
            merged_node.count = n2.count + n1.count;
            merged_node.left = n1;
            merged_node.right = n2;
            huffTree.add(merged_node);
        }
        System.out.println(huffTree);
        assert false;
        return null;
    }

    private void LRC(HuffmanNode n, String bit_str, TreeMap<Character, String> map) {
        if (n == null) { return; }
        if (n.left == null && n.right == null) {
            assert n.representation.length() == 1;
            map.put(n.representation.toCharArray()[0], bit_str);
        }
        System.out.println(bit_str);
        if (n.left != null) {
            String n_string = new String(bit_str);
            n_string += "0";
            LRC(n.left, n_string, map);
        }
        if (n.right != null) {
            String n_string = new String(bit_str);
            n_string += "1";
            LRC(n.right, n_string, map);
        }

    }

    private TreeMapPretty<Character, String> createHuffmanTable(HuffmanNode root) {
        TreeMapPretty<Character, String > map = new TreeMapPretty<>();

        LRC(root, new String(), map);
        System.out.println(map);
        return map;
    }

    // CLR
    private void preOrder(HuffmanNode n, BitSet bits) {
        if (n == null) { return; }

        if (n.left == null && n.right == null) {
            bits.set(bits.length(), true); // add a 1
            assert n.representation.length() == 1;
            char c = n.representation.toCharArray()[0];
            // 
            for (int i=0; i<8; i++) {

            }
        }
    }
    /**
     * Compresses a string to bytes
     * Uses standard Huffman Compression
     * @param str the string to compress
     * @return the compress data
     */
    public byte[] compress(String str){
        HuffmanNode huffTree = createHuffmanTree(createFreqTable(str));
        TreeMapPretty<Character, String> huffTable = createHuffmanTable(huffTree);
        BitSet bits = new BitSet();
        preOrder(huffTree, bits);

        return bits.toByteArray();
    }
}