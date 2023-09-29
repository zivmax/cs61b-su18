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

    @Test
    public void iterTest() {
        ArrayRingBuffer<ArrayRingBuffer<Integer>> arb = new ArrayRingBuffer<>(3);
        ArrayRingBuffer<Integer> a1 = new ArrayRingBuffer<>(3);
        ArrayRingBuffer<Integer> a2 = new ArrayRingBuffer<>(3);
        ArrayRingBuffer<Integer> a3 = new ArrayRingBuffer<>(3);
        for (int i = 0; i < 3; i++) {
            a1.enqueue(i);
            a2.enqueue(i);
            a3.enqueue(i);
        }

        arb.enqueue(a1);
        arb.enqueue(a2);
        arb.enqueue(a3);

        for (ArrayRingBuffer<Integer> a : arb) {
            for (Integer i : a) {
                
            }
        }

        assertEquals(a1.fillCount(), 0);
        assertEquals(a2.fillCount(), 0);
        assertEquals(a3.fillCount(), 0);
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
}
