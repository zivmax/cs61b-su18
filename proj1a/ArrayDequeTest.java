import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    public void testResize() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < 16; i++) {
            deque.addLast(i);
        }
        assertEquals(16, deque.size());

        deque.addLast(16);
        assertEquals(17, deque.size());
        for (int i = 0; i < 16; i++) {
            deque.addLast(i);
        }
        assertEquals(33, deque.size());
    }

    @Test
    public void testAdd() {
        ArrayDeque<String> deque = new ArrayDeque<>();
        deque.addLast("a");
        deque.addLast("b");

        assertEquals(2, deque.size());
        assertEquals("a", deque.get(0));
        assertEquals("b", deque.get(1));
    }

    @Test
    public void testRemove() {
        ArrayDeque<String> deque = new ArrayDeque<>();
        deque.addLast("a");
        deque.addLast("b");
        deque.addLast("c");

        assertEquals("a", deque.removeFirst());
        assertEquals(2, deque.size());
        assertEquals("b", deque.get(0));

        assertEquals("c", deque.removeLast());
        assertEquals(1, deque.size());
        assertEquals("b", deque.get(0));
    }

    @Test
    public void testIsEmpty() {
        ArrayDeque<String> deque = new ArrayDeque<>();
        assertTrue(deque.isEmpty());

        deque.addLast("a");
        assertFalse(deque.isEmpty());
    }
}