import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque<Character> d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        assertTrue(palindrome.isPalindrome(""));
        assertTrue(palindrome.isPalindrome("a"));
        assertTrue(palindrome.isPalindrome("racecar"));
        assertFalse(palindrome.isPalindrome("caC"));
        assertFalse(palindrome.isPalindrome("cat"));

        assertTrue(palindrome.isPalindrome("", new OffByOne()));
        assertTrue(palindrome.isPalindrome("a", new OffByOne()));
        assertTrue(palindrome.isPalindrome("abab", new OffByOne()));
        assertTrue(palindrome.isPalindrome("chid", new OffByOne()));
        assertTrue(palindrome.isPalindrome("hopi", new OffByOne()));
        assertTrue(palindrome.isPalindrome("oban", new OffByOne()));
        assertFalse(palindrome.isPalindrome("caac", new OffByOne()));
        assertFalse(palindrome.isPalindrome("racecar", new OffByOne()));

        assertTrue(palindrome.isPalindrome("", new OffByN(5)));
        assertTrue(palindrome.isPalindrome("a", new OffByN(5)));
        assertTrue(palindrome.isPalindrome("biding", new OffByN(5)));
        assertTrue(palindrome.isPalindrome("moth", new OffByN(5)));
        assertTrue(palindrome.isPalindrome("oint", new OffByN(5)));
        assertFalse(palindrome.isPalindrome("stut", new OffByN(5)));
        assertFalse(palindrome.isPalindrome("caac", new OffByN(5)));
        assertFalse(palindrome.isPalindrome("racecar", new OffByN(5)));

    }
}
