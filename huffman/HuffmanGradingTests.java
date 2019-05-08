package huffman;

import static org.junit.Assert.*;
import org.junit.Test;

public class HuffmanGradingTests {
    
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
    
    @Test
    public void comp_t4() {
        Huffman h = new Huffman("ACADACBABE");
        // Encoding Map:
        // {A=0, B=110, C=10, D=1110, E=1111}
        byte[] compressed = {10, 78, 89, -68};
        assertArrayEquals(compressed, h.compress("ACADACBABE"));
    }
    
    @Test
    public void comp_t5() {
        Huffman h = new Huffman("ABCDEFGHIJ");
        // Encoding Map:
        // {A=1100, B=001, C=011, D=1110, E=101, F=010, G=100, H=1111, I=000, J=1101}
        byte[] compressed = {10, -62, -6, -87, -29, 64};
        assertArrayEquals(compressed, h.compress("ABCDEFGHIJ"));
    }
    
    @Test
    public void comp_t6() {
        Huffman h = new Huffman("ABCDEFGHIJ");
        // Encoding Map:
        // {A=1100, B=001, C=011, D=1110, E=101, F=010, G=100, H=1111, I=000, J=1101}
        byte[] compressed = {6, -52, -62, 72};
        // Not that we would always want to use this encoding map for this corpus, but
        // we *could*
        assertArrayEquals(compressed, h.compress("AAABBB"));
    }
    
    @Test
    public void comp_t7() {
        Huffman h = new Huffman("ABABBABA");
        // Encoding Map:
        // {A=0, B=1}
        byte[] compressed = {4, 96};
        assertArrayEquals(compressed, h.compress("ABBA"));
    }
    
    @Test
    public void comp_t8() {
        Huffman h = new Huffman("1223334444555556666667777777");
        // Encoding Map:
        // {1=0010, 2=0011, 3=000, 4=110, 5=111, 6=01, 7=10}
        byte[] compressed = {7, 35, 27, -80};
        assertArrayEquals(compressed, h.compress("1234567"));
    }
    
    @Test
    public void comp_t9() {
        Huffman h = new Huffman("This is a full sentence. How odd to see it in a test case! Punctuation and all. Wow.");
        // Encoding Map:
        // { 
        //   ' '=00, !=1011010, a=1101, c=10011, d=01101, e=1110, f=010011, H=010000, h=1011011, i=0111, l=0101, .=10010, 
        //   n=1100, o=1000, P=101100, s=1010, T=010001, t=1111, u=10111, W=010010, w=01100
        // }
        byte[] compressed = {
            84, 70, -37, -48, -12, 104, -99, -43, 74, -20, -2, -55, -12, -124, 33, 
            -124, 53, -89, -63, 93, -61, -8, -8, 105, -3, 94, 79, 107, -83, 22, 95, 
            39, -9, -33, 120, -61, 113, -90, -86, -56, 74, 25, 32
        };
        assertArrayEquals(compressed, h.compress("This is a full sentence. How odd to see it in a test case! Punctuation and all. Wow."));
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
    public void decomp_t2() {
        Huffman h = new Huffman("ABBBCC");
        // byte 0: 0000 0110 = 6 (message length = 6)
        // byte 1: 1000 0111 = -121 (10 = "A", 0 = "B", C = 11)
        // byte 2: 1000 0000 = -128
        byte[] compressed = {6, -121, -128};
        assertEquals("ABBBCC", h.decompress(compressed));
    }
    
    @Test
    public void decomp_t3() {
        Huffman h = new Huffman("ABBBCC");
        // byte 0: 0000 0110 = 6 (message length = 6)
        // byte 1: 0100 1101 = 77 (10 = "A", 0 = "B", C = 11)
        // byte 2: 1000 0000 = -128
        byte[] compressed = {6, 77, -128};
        assertEquals("BABCBC", h.decompress(compressed));
    }
    
    @Test
    public void decomp_t4() {
        Huffman h = new Huffman("ACADACBABE");
        // Encoding Map:
        // {A=0, B=110, C=10, D=1110, E=1111}
        byte[] compressed = {10, 78, 89, -68};
        assertEquals("ACADACBABE", h.decompress(compressed));
    }
    
    @Test
    public void decomp_t5() {
        Huffman h = new Huffman("ABCDEFGHIJ");
        // Encoding Map:
        // {A=1100, B=001, C=011, D=1110, E=101, F=010, G=100, H=1111, I=000, J=1101}
        byte[] compressed = {10, -62, -6, -87, -29, 64};
        assertEquals("ABCDEFGHIJ", h.decompress(compressed));
    }
    
    @Test
    public void decomp_t6() {
        Huffman h = new Huffman("ABCDEFGHIJ");
        // Encoding Map:
        // {A=1100, B=001, C=011, D=1110, E=101, F=010, G=100, H=1111, I=000, J=1101}
        byte[] compressed = {6, -52, -62, 72};
        assertEquals("AAABBB", h.decompress(compressed));
    }
    
    @Test
    public void decomp_t7() {
        Huffman h = new Huffman("ABABBABA");
        // Encoding Map:
        // {A=0, B=1}
        byte[] compressed = {4, 96};
        assertEquals("ABBA", h.decompress(compressed));
    }
    
    @Test
    public void decomp_t8() {
        Huffman h = new Huffman("1223334444555556666667777777");
        // Encoding Map:
        // {1=0010, 2=0011, 3=000, 4=110, 5=111, 6=01, 7=10}
        byte[] compressed = {7, 35, 27, -80};
        assertEquals("1234567", h.decompress(compressed));
    }
    
    @Test
    public void decomp_t9() {
        Huffman h = new Huffman("This is a full sentence. How odd to see it in a test case! Punctuation and all. Wow.");
        // Encoding Map:
        // { 
        //   ' '=00, !=1011010, a=1101, c=10011, d=01101, e=1110, f=010011, H=010000, h=1011011, i=0111, l=0101, .=10010, 
        //   n=1100, o=1000, P=101100, s=1010, T=010001, t=1111, u=10111, W=010010, w=01100
        // }
        byte[] compressed = {
            84, 70, -37, -48, -12, 104, -99, -43, 74, -20, -2, -55, -12, -124, 33, 
            -124, 53, -89, -63, 93, -61, -8, -8, 105, -3, 94, 79, 107, -83, 22, 95, 
            39, -9, -33, 120, -61, 113, -90, -86, -56, 74, 25, 32
        };
        assertEquals("This is a full sentence. How odd to see it in a test case! Punctuation and all. Wow.", h.decompress(compressed));
    }
    
}
