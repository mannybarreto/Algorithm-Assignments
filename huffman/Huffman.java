package huffman;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.io.ByteArrayOutputStream;

/**
 * Huffman instances provide reusable Huffman Encoding Maps for
 * compressing and decompressing text corpi with comparable
 * distributions of characters.
 */
public class Huffman {
    
    // -----------------------------------------------
    // Construction
    // -----------------------------------------------

    private HuffNode trieRoot;
    private Map<Character, String> encodingMap;
    
    /**
     * Creates the Huffman Trie and Encoding Map using the character
     * distributions in the given text corpus
     * @param corpus A String representing a message / document corpus
     *        with distributions over characters that are implicitly used
     *        throughout the methods that follow. Note: this corpus ONLY
     *        establishes the Encoding Map; later compressed corpi may
     *        differ.
     */
    Huffman (String corpus) {
        PriorityQueue<HuffNode> queue = createPriorityQueue(corpus);
        constructTrie(queue);
        encodingMap = new HashMap<Character, String>();
        retrieveEncoding(trieRoot, "");
    }
    
    
    /*
     * Core methods for Huffman creation, compression, and decompression.
     */
    
    /**
     * Takes the inputed String and counts the appearances of characters to create a PriorityQueue of
     * HuffNodes and their # of appearances. Queue generally used for constructing the tree used for
     * finding encodingMap.
     * @param message String to be used 
     * @return PriorityQueue<HuffNode> containing all characters and their number of appearances in message.
     */
    private PriorityQueue<HuffNode> createPriorityQueue(String message) {
        HashMap<Character, Integer> distribution = new HashMap<>();
        
        for (int i = 0; i < message.length(); i++) {
            char currentChar = message.charAt(i);
            if (distribution.containsKey(currentChar)) {
                distribution.put(currentChar, distribution.get(currentChar) + 1);
            } else {
                distribution.put(currentChar, 1);
            }
        }
        
        PriorityQueue<HuffNode> result = new PriorityQueue<HuffNode>();
        
        for (Entry<Character, Integer> entry : distribution.entrySet()) {
            result.add(new HuffNode(entry.getKey(), entry.getValue()));
        }
        
        return result;
    }
    
    /**
     * Takes queue formed from createPriorityQueue and creates Huffman Trie, 
     * setting trieRoot to last element in queue.
     * @param queue PriorityQueue formed by createPriorityQueue with HuffmanNodes of each character in message.
     */
    private void constructTrie(PriorityQueue<HuffNode> queue) {
        if (queue.size() == 1) {
            HuffNode newNode = new HuffNode((char) 0, queue.peek().count);
            newNode.left = queue.poll();
            queue.add(newNode);
        }
        
        while (queue.size() > 1) {
            HuffNode newNode = new HuffNode((char) 0, 0);

            newNode.left = queue.poll();
            newNode.right = queue.poll();
            newNode.count = newNode.left.count + newNode.right.count;

            queue.add(newNode);
        }
        trieRoot = queue.poll();
    }

    private void retrieveEncoding(HuffNode node, String path) {
        if (node.isLeaf()) {
            encodingMap.put(node.character, path);
        }

        if (node.left != null) {
            retrieveEncoding(node.left, path + "0");
        }

        if (node.right != null) {
            retrieveEncoding(node.right, path + "1");
        }
    }
    
    // -----------------------------------------------
    // Compression
    // -----------------------------------------------
    
    /**
     * Compresses the given String message / text corpus into its Huffman coded
     * bitstring, as represented by an array of bytes. Uses the encodingMap
     * field generated during construction for this purpose.
     * @param message String representing the corpus to compress.
     * @return {@code byte[]} representing the compressed corpus with the
     *         Huffman coded bytecode. Formatted as 3 components: (1) the
     *         first byte contains the number of characters in the message,
     *         (2) the bitstring containing the message itself, (3) possible
     *         0-padding on the final byte.
     */
    public byte[] compress (String message) {
        String encodedString = "";
        ByteArrayOutputStream bout = new ByteArrayOutputStream(); 
        
        for (int i = 0; i < message.length(); i++) {
            encodedString = encodedString + encodingMap.get(message.charAt(i));
        }
        
        while (encodedString.length() % 8 != 0) {
            encodedString = encodedString + "0";
        }
        
        for (int i = 0; i < encodedString.length() / 8; i++) {
            String currentByte = encodedString.substring(8 * i, 8 * (i + 1));
            bout.write(Integer.parseInt(currentByte, 2));
        }     
                
        byte[] encoded = bout.toByteArray();
        byte[] result = new byte[1 + encoded.length];
        result[0] = (byte) message.length();
        
        for (int i = 0; i < encoded.length; i++) {
            result[1 + i] = encoded[i];
        }
        
        return result;
    }
     
    
    // -----------------------------------------------
    // Decompression
    // -----------------------------------------------
    
    /**
     * Decompresses the given compressed array of bytes into their original,
     * String representation. Uses the trieRoot field (the Huffman Trie) that
     * generated the compressed message during decoding.
     * @param compressedMsg {@code byte[]} representing the compressed corpus with the
     *        Huffman coded bytecode. Formatted as 3 components: (1) the
     *        first byte contains the number of characters in the message,
     *        (2) the bitstring containing the message itself, (3) possible
     *        0-padding on the final byte.
     * @return Decompressed String representation of the compressed bytecode message.
     */
    public String decompress (byte[] compressedMsg) {
        String decodedString = "";
        HuffNode currentNode = trieRoot;
        String strCompressedMsg = "";
        
        for (int k = 1; k < compressedMsg.length; k++) {
            String currentByte = "";
            if (compressedMsg[k] < 0) {
                currentByte = Integer.toBinaryString(compressedMsg[k]).substring(24);
            } else {
                currentByte = Integer.toBinaryString(compressedMsg[k]);
            }
   
            while (currentByte.length() < 8) {
                currentByte = "0" + currentByte;
            }
            
            strCompressedMsg += currentByte;
        }
        
        for (int i = 0; decodedString.length() < compressedMsg[0]; i++) {
            if (currentNode.isLeaf() == true) {
                decodedString += currentNode.character;
                currentNode = trieRoot;
            }
            if (strCompressedMsg.charAt(i) == '1') {
                currentNode = currentNode.right;
            } else {
                currentNode = currentNode.left;
            }
        }
        return decodedString;
    }
    
    // -----------------------------------------------
    // Huffman Trie
    // -----------------------------------------------
    
    /**
     * Huffman Trie Node class used in construction of the Huffman Trie.
     * Each node is a binary (having at most a left and right child), contains
     * a character field that it represents (in the case of a leaf, otherwise
     * the null character \0), and a count field that holds the number of times
     * the node's character (or those in its subtrees) appear in the corpus.
     */
    private static class HuffNode implements Comparable<HuffNode> {
        
        HuffNode left, right;
        char character;
        int count;
        
        HuffNode (char character, int count) {
            this.count = count;
            this.character = character;
        }
        
        public boolean isLeaf () {
            return left == null && right == null;
        }
        
        public int compareTo (HuffNode other) {
            return this.count - other.count;
        }
        
    }

}
