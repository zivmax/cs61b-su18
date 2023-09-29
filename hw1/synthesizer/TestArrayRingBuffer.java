package synthesizer;

import static org.junit.Assert.*;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import org.junit.Test;

/**
 * Tests the ArrayRingBuffer class.
 * 
 * @author Felix
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(100000);
        ArrayBlockingQueue<Integer> bq = new ArrayBlockingQueue<>(100000);

        Random random = new Random();

        for (int i = 0; i < 100000; i++) {
            int coin = random.nextInt(100);
            if (coin < 85) {
                arb.enqueue(i);
                bq.add(i);
                assertEquals(arb.fillCount(), bq.size());
                assertEquals(arb.peek(), bq.peek());
            } else {
                assertEquals(arb.fillCount(), bq.size());
                assertEquals(bq.poll(), arb.dequeue());
            }
        }

    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
}
