import static org.junit.Assert.*;

import java.util.stream.Stream;

import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testArrayDequeGold() {
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> sad2 = new ArrayDequeSolution<>();
        String msg = "";

        for (int i = 0; i < 1000; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.3) {
                sad1.addLast(i);
                sad2.addLast(i);
                msg = msg + "addLast(" + i + ")\n";
            } else if (numberBetweenZeroAndOne <= 0.6) {
                sad1.addFirst(i);
                sad2.addFirst(i);
                msg = msg + "addFirst(" + i + ")\n";
            } else if (numberBetweenZeroAndOne > 0.6) {
                msg = msg + "removeFirst()\n";
                assertEquals(msg, sad1.removeFirst(), sad2.removeFirst());
            } else if (numberBetweenZeroAndOne > 0.8) {
                msg = msg + "removeFirst()\n";
                assertEquals(msg, sad1.removeLast(), sad2.removeLast());
            }
        }
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(TestArrayDequeGold.class);
    }
}
