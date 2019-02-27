package nim;

import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class NimPlayerTests {
    
    // Global timeout to prevent infinite loops from
    // crashing the test suite + to test that your
    // alpha-beta pruning and memoization are working;
    // If they are, 3 seconds should be more than enough
    @Rule
    public Timeout globalTimeout = Timeout.seconds(3);
    
    /**
     * Basic test to make sure the nimesis knows the
     * base cases: how to win when presented with the
     * opportunity
     */
    @Test
    public void NimPlayerTest_t0() {
        NimPlayer nimesis = new NimPlayer(2);
        assertEquals(1, nimesis.choose(1));
        assertEquals(2, nimesis.choose(2));
    }
    
    /**
     * One-ply above winning condition to see it
     * the nimesis can put itself into a sure win
     */
    @Test
    public void NimPlayerTest_t1() {
        NimPlayer nimesis = new NimPlayer(2);
        assertEquals(1, nimesis.choose(4));
        assertEquals(2, nimesis.choose(5));
    }
    
    /**
     * OK, two-ply now, just to make sure!
     */
    @Test
    public void NimPlayerTest_t2() {
        NimPlayer nimesis = new NimPlayer(2);
        assertEquals(1, nimesis.choose(6));
        assertEquals(1, nimesis.choose(7));
    }
    
    /**
     * OK, can now take between 1 - 3 stones
     */
    @Test
    public void NimPlayerTest_t3() {
        NimPlayer nimesis = new NimPlayer(3);
        assertEquals(1, nimesis.choose(1));
        assertEquals(2, nimesis.choose(2));
        assertEquals(3, nimesis.choose(3));
    }
    
    /**
     * Same, but 1-ply from victory
     */
    @Test
    public void NimPlayerTest_t4() {
        NimPlayer nimesis = new NimPlayer(3);
        assertEquals(1, nimesis.choose(5));
        assertEquals(2, nimesis.choose(6));
        assertEquals(3, nimesis.choose(7));
    }
    
    /**
     * Same, but 2-ply from victory
     */
    @Test
    public void NimPlayerTest_t5() {
        NimPlayer nimesis = new NimPlayer(3);
        assertEquals(2, nimesis.choose(10));
        assertEquals(1, nimesis.choose(9));
        assertEquals(1, nimesis.choose(8));
    }
    
    /**
     * OK, welcome to flavor country
     */
    @Test
    public void NimPlayerTest_t6() {
        NimPlayer nimesis = new NimPlayer(3);
        assertEquals(1, nimesis.choose(40));
        assertEquals(3, nimesis.choose(39));
        assertEquals(2, nimesis.choose(38));
    }
    
    /**
     * OK, *REALLY* welcome to flavor country
     */
    @Test
    public void NimPlayerTest_t7() {
        NimPlayer nimesis = new NimPlayer(3);
        assertEquals(1, nimesis.choose(1000));
    }

}
