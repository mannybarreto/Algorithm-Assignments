package huffman;

import static org.junit.Assert.*;
import org.junit.Test;

public class HuffmanTests {
    
    // Compression Tests
    // -----------------------------------------------
    @Test
    public void comp_t0() {
        Huffman h = new Huffman("AB");
        // byte 0: 0000 0010 = 2 (message length = 2)
        // byte 1: 0100 0000 = 64 (0 = "A", 1 = "B")
        // [!] Only first 2 bits of byte 1 are meaningful
        byte[] compressed = {2, 64};
        assertArrayEquals(compressed, h.compress("AB"));
    }
    
    @Test
    public void comp_t1() {
        Huffman h = new Huffman("AB");
        // byte 0: 0000 0010 = 2 (message length = 2)
        // byte 1: 1000 0000 = -128 (0 = "A", 1 = "B")
        // [!] Only first 2 bits of byte 1 are meaningful
        byte[] compressed = {2, -128};
        assertArrayEquals(compressed, h.compress("BA"));
    }
    
    @Test
    public void comp_t2() {
        Huffman h = new Huffman("ABBBCC");
        // byte 0: 0000 0110 = 6 (message length = 6)
        // byte 1: 1000 0111 = -121 (10 = "A", 0 = "B", C = 11)
        // byte 2: 1000 0000 = -128
        // [!] Only first bit of byte 2 is meaningful
        byte[] compressed = {6, -121, -128};
        assertArrayEquals(compressed, h.compress("ABBBCC"));
    }
    
    @Test
    public void comp_t3() {
        Huffman h = new Huffman("ABBBCC");
        // byte 0: 0000 0110 = 6 (message length = 6)
        // byte 1: 0100 1101 = 77 (10 = "A", 0 = "B", C = 11)
        // byte 2: 1000 0000 = -128
        byte[] compressed = {6, 77, -128};
        assertArrayEquals(compressed, h.compress("BABCBC"));
    }
    
    
    // Decompression Tests
    // -----------------------------------------------
    @Test
    public void decomp_t0() {
        Huffman h = new Huffman("AB");
        // byte 0: 0000 0010 = 2 (message length = 2)
        // byte 1: 0100 0000 = 64 (0 = "A", 1 = "B")
        byte[] compressed = {2, 64};
        assertEquals("AB", h.decompress(compressed));
    }
    
    @Test
    public void decomp_t1() {
        Huffman h = new Huffman("AB");
        // byte 0: 0000 0010 = 2 (message length = 2)
        // byte 1: 1000 0000 = -128 (0 = "A", 1 = "B")
        byte[] compressed = {2, -128};
        assertEquals("BA", h.decompress(compressed));
    }
    
    @Test
    public void decom_t3() {
        Huffman h = new Huffman("ABBBCC");
        // byte 0: 0000 0110 = 6 (message length = 6)
        // byte 1: 1000 0111 = -121 (10 = "A", 0 = "B", C = 11)
        // byte 2: 1000 0000 = -128
        byte[] compressed = {6, -121, -128};
        assertEquals("ABBBCC", h.decompress(compressed));
    }
    
    @Test
    public void decom_t4() {
        Huffman h = new Huffman("ABBBCC");
        // byte 0: 0000 0110 = 6 (message length = 6)
        // byte 1: 0100 1101 = 77 (10 = "A", 0 = "B", C = 11)
        // byte 2: 1000 0000 = -128
        byte[] compressed = {6, 77, -128};
        assertEquals("BABCBC", h.decompress(compressed));
    }
    
}
