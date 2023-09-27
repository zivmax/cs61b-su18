import static org.junit.Assert.*;

import java.util.stream.Stream;

import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testArrayDequeGold() {
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> sad2 = new ArrayDequeSolution<>();
        String msg = "";
        
        for (int i = 0; i < 30000; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.5) {
                sad1.addLast(i);
                sad2.addLast(i);
                msg = msg + "addLast(" + i + ")\n"; 
            } else if (numberBetweenZeroAndOne > 0.9) {
                sad1.removeFirst();
                sad2.removeFirst();
                msg = msg + "removeFirst()\n"; 
            } else if (numberBetweenZeroAndOne > 0.8) {
                sad1.removeLast();
                sad2.removeLast();
                msg = msg + "removeFirst()\n"; 
            } else {
                sad1.addFirst(i);
                sad2.addFirst(i);
                msg = msg + "addFirst(" + i + ")\n"; 
            }
            for (int j = 0; j < Math.min(sad1.size(), sad2.size()); j++) {
                assertEquals(msg, sad1.get(j), sad2.get(j));
            }
        }
    }
    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(TestArrayDequeGold.class);
    }
}
