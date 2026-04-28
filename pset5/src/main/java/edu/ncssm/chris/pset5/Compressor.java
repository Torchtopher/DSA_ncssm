package edu.ncssm.chris.pset5;

import java.util.*;

/**
 * Handles compression and decompression of data
 *
 * @author Brian Sea
 * @since Problem Set 4 (Answer)
 * @version 0.0.1
 */
public class Compressor {

    // Symbolizes the end of the compressed message
    private final char END_OF_FILE = (char)3;

    /**
     * Compresses a string to bytes
     * Uses standard Huffman Compression
     * @param str the string to compress
     * @return the compressed data
     */
    public byte[] compress(String str){
        return compress(str, false);
    }

    /**
     * Compresses a string to bytes
     * Uses standard Huffman Compression
     * @param str the string to compress
     * @param stopAtTable whether to stop compression after table generation
     * @return the compressed data
     */
    public byte[] compress(String str, boolean stopAtTable){
        StringBuilder sb = new StringBuilder();

        HashMap<Character, Integer> freqTable = createFreqTable(str);

        stopAtTable = true;
        // Stopping at the table shouldn't include the EOF
        if( !stopAtTable ) {
            freqTable.put(END_OF_FILE, 1);
            str += END_OF_FILE;
        }

        HuffNode root = createHuffmanTree(freqTable);
        HuffNode cur = root;
        while (cur.left != null) {
            cur = cur.left;
        }
        System.out.println("Max left, is " + cur.symbol);
        HashMap<Character,String> table = createHuffmanTable(root);

        for( Character c : table.keySet() ){
            String symbol = ""+c;
            if( c == ' ' ){
                symbol = "(space)";
            }
            else if( c == '\t' ){
                symbol = "(tab)";
            }
            else if( c == '\n'){
                symbol = "(enter)";
            }
            else if( c == END_OF_FILE){
                symbol = "(EOF)";
            }

            sb.append(symbol).append(":").append(table.get(c)).append("\n");
        }


        // Stop compression at table generation
        if( stopAtTable ){
            return sb.toString().getBytes();
        }

        BitSet encodedTree = new BitSet();
        int tsize = encodeTree(root, encodedTree, 0);

        BitSet encodedMessage = new BitSet();
        int msize = encodeMessage(str, table, encodedMessage);

        // TODO: Combine the BitSets into encodedTree

        return encodedTree.toByteArray();
    }

    /**
     * Converts compressed bytes to a String
     * Uses standard Huffman Compression
     * @param bytes the bytes to decompress
     * @return the string representation
     */
    public String decompress(byte[] bytes){

        BitSet encoded = BitSet.valueOf(bytes);
        HuffNode root = new HuffNode();
        int idx = decodeTree(encoded, 0, root);
        String message = decodeMessage(encoded, idx, root);

        return message;
    }

    private void CLR(HuffNode n) {
        if (n == null) {
            return;
        }
        
        process();

        left();

        right();


    }

    private int decodeTree(BitSet encoded, int idx, HuffNode node){
        HuffNode cur = node;
        // so each 1 means to go left in tree, stored in
        int counter = 0;
        boolean add_to_cur = false;
        StringBuilder sb = new StringBuilder();
        System.out.println(encoded.toString());
        
        for (int i = 0; i<=encoded.length(); i++) {
            boolean bit = encoded.get(i);
            String bitstr = bit ? "1" : "0";
            System.out.println("Bit is " + bit);

            if (counter != 0) {
                sb.append(bitstr);
                counter--;
                if (counter == 0) { add_to_cur = true; }
                System.out.println(sb);
                continue;
            }

            if (add_to_cur) {
                cur.symbol = (char)Integer.parseInt(sb.reverse().toString(), 2);
                add_to_cur = false;
                System.out.println("Symbol for first root node " + cur.symbol);
                // now need to go up one
            }

            if (i > 15) {
                break;
            }
            if (!bit) {
                cur.left = new HuffNode();
                cur = cur.left;
            } else {
                counter = 7; // next 8 bits are zero
            }
        }
        return 0;
    }

    private String decodeMessage(BitSet encoded, int idx, HuffNode root){
        StringBuilder rtn = new StringBuilder();

        return rtn.toString();
    }

    private int encodeTree(HuffNode node, BitSet bits, int idx){
        return idx;
    }
    private int encodeMessage(String s, Map<Character, String> table, BitSet encoded ){
        return 0;
    }

    private HashMap<Character, Integer> createFreqTable(String str){
        HashMap<Character, Integer> table = new HashMap<>();

        for(Character c : str.toCharArray()){
            if(table.containsKey(c)){
                table.put(c, table.get(c)+1);
            }
            else {
                table.put(c, 1);
            }
        }
        return table;
    }

    private HuffNode createHuffmanTree( HashMap<Character, Integer> table ){
        Heap<HuffNode> heap = new Heap<>();
        Heap<Character> sort = new Heap<>();
        for( Character c : table.keySet() ){
            sort.add(c);
        }

        List<Character> sorted = new ArrayList<>();
        while(!sort.isEmpty()){
            sorted.add(sort.remove());
        }

        for(Character c: sorted){
            HuffNode node = new HuffNode();
            node.symbol = c;
            node.frequency = table.get(c);
            heap.add(node);
        }

        HuffNode root = null;
        while( !heap.isEmpty() ){
            root = heap.remove();
            if( !heap.isEmpty()){
                HuffNode right = heap.remove();
                HuffNode parent = new HuffNode();
                parent.frequency = root.frequency + right.frequency;
                parent.left = root;
                parent.right = right;
                heap.add(parent);
            }
        }
        return root;
    }

    private HashMap<Character, String> createHuffmanTable(HuffNode root){
        HashMap<Character, String> huffTable = new HashMap<>();
        createHuffmanTableHelp(root, "", huffTable);
        return huffTable;
    }
    private void createHuffmanTableHelp(HuffNode node, String str, HashMap<Character, String> table){
        if( node.symbol != null ){
            table.put(node.symbol, str);
            return;
        }
        createHuffmanTableHelp(node.left, str+"0", table);
        createHuffmanTableHelp(node.right, str+"1", table);
    }

    private static class HuffNode implements Comparable<HuffNode> {
        public Character symbol;
        public int frequency;
        public HuffNode left, right;

        public int compareTo(HuffNode other){
            return this.frequency - other.frequency;
        }
    }
}