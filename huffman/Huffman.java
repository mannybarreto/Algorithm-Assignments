package huffman;

import java.util.HashMap;
import java.util.Iterator;
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
        encodingMap = retrieveEncoding(new HashMap<Character, String>() , trieRoot, "");
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
        
        for (int i = 0; i < message.length(); i++) {
            encodedString = encodedString + encodingMap.get(message.charAt(i));
        }
        
        // TODO: Switch to bitshift <<
        while (encodedString.length() % 8 != 0) {
            encodedString = encodedString + "0";
        }
                
        ByteArrayOutputStream bout = new ByteArrayOutputStream(); 
        bout.write(Integer.parseInt(encodedString, 2));
        
        byte[] encoded = bout.toByteArray();
        
        byte[] result = new byte[1 + encoded.length];
        result[0] = (byte) message.length();
        
        for (int i = 0; i < encoded.length; i++) {
            result[1 + i] = encoded[i];
        }
        
        return result;
    }

    private HashMap<Character, String> retrieveEncoding(HashMap<Character, String> encoding, HuffNode node, String path) {
        if (node.isLeaf()) {
            encoding.put(node.character, path);
        }

        if (node.left != null) {
            encoding.putAll(retrieveEncoding(encoding, node.left, path + "0"));
        }

        if (node.right != null) {
            encoding.putAll(retrieveEncoding(encoding, node.right, path + "1"));
        }

        return encoding;
    }

    private void constructTrie(PriorityQueue<HuffNode> queue) {
        while (queue.size() >= 2) {
            HuffNode newNode = new HuffNode((char) 0, 0);

            newNode.count += queue.peek().count;
            newNode.left = queue.poll();
            newNode.count += queue.peek().count;
            newNode.right = queue.poll();

            queue.add(newNode);
        }
        trieRoot = queue.poll();
    }

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
        throw new UnsupportedOperationException();
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
