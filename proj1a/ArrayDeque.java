public class ArrayDeque<T> {
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

    public T get(int index) {

        return container[head + index];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (T item : container) {
            System.out.print(item);
            System.out.print(' ');
        }
    }

    private void resize(int NewSize) {
        int OldHeader = head;
        T[] tmp = container;
        container = (T[]) new Object[NewSize];
        updateHeaders();

        for (int i = 0, j = head, k = OldHeader; i < Math.min(NewSize, size); i++) {
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
