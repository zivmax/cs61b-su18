public class ArrayDeque<T> implements Deque<T> {
    private final int REFACTOR;
    private final double LEAST_USAGE_RATIO;
    private int size;
    private int head;
    private int tail;

    private T[] container;

    public ArrayDeque() {
        REFACTOR = 2;
        LEAST_USAGE_RATIO = 0.25;
        size = 0;
        container = (T[]) new Object[8];
        updateHeaders();
    }

    @Override
    public void addFirst(T item) {
        if (item == null) {
            return;
        }

        if (head == 0) {
            resize(container.length * REFACTOR);
        }

        head--;
        size++;
        container[head] = item;
    }

    @Override
    public void addLast(T item) {
        if (item == null) {
            return;
        }

        if (tail == container.length - 1) {
            resize(container.length * REFACTOR);
        }

        tail++;
        size++;
        container[tail] = item;
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        if (size / (double) container.length < LEAST_USAGE_RATIO && container.length >= 16) {
            resize(container.length / 2);
        }

        head++;
        size--;
        return get(-1);
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }

        if (size / (double) container.length < LEAST_USAGE_RATIO && container.length >= 16) {
            resize(container.length / 2);
        }

        tail--;
        size--;
        return get(size);
    }

    @Override
    public T get(int index) {

        return container[head + index];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (T item : container) {
            System.out.print(item);
            System.out.print(' ');
        }
    }

    private void resize(int newSize) {
        int oldHeader = head;
        T[] tmp = container;
        container = (T[]) new Object[newSize];
        updateHeaders();

        for (int i = 0, j = head, k = oldHeader; i < Math.min(newSize, size); i++) {
            container[j] = tmp[k];
            j++;
            k++;
        }
    }

    private void updateHeaders() {
        head = (container.length - size) / 2;
        tail = head + size - 1;
    }
}
