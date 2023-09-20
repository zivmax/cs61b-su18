import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator OffByN = new OffByN(5);

    // Your tests go here.
    @Test
    public void testEqualChars() {
        assertTrue(OffByN.equalChars('a', 'f'));
        assertTrue(OffByN.equalChars('f', 'a'));
        assertTrue(OffByN.equalChars('1', '6'));
        assertFalse(OffByN.equalChars('f', 'h'));
        assertFalse(OffByN.equalChars('0', '4'));
        assertFalse(OffByN.equalChars('a', 'b'));
    }

}
