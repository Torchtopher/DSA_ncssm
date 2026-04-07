package edu.ncssm.briansea.pset4;

/**
 * Handles compression and decompression of data
 *
 * @author Brian Sea
 * @since Problem Set 4
 * @version 0.0.1
 */
public class Compressor {
    /**
     * Converts compressed bytes to a String
     * Uses standard Huffman Compression
     * @param bytes the bytes to decompress
     * @return the string representation
     */
    public String decompress(byte[] bytes){


        return new String(bytes);
    }

    /**
     * Compresses a string to bytes
     * Uses standard Huffman Compression
     * @param str the string to compress
     * @return the compress data
     */
    public byte[] compress(String str){
        return str.toString().getBytes();
    }
}