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
        assertFalse(OffByN.equalChars('f', 'h'));
    }

}
