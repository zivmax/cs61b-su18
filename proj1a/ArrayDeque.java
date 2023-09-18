public class ArrayDeque<T> {
    private int size;
    private final int REFACTOR;
    private final double LEAST_USAGE_RATIO;

    private T[] container;

    public ArrayDeque() {
        REFACTOR = 2;
        LEAST_USAGE_RATIO = 0.25;
        size = 0;
        container = (T[]) new Object[8];
    }

    public void addFirst(T item) {
        if (item == null) {
            return;
        }

        if (size == container.length) {
            resize(container.length * REFACTOR);
        }

        for (int i = size; i >= 1; i--) {
            container[i] = container[i - 1];
        }

        size++;
        container[0] = item;
    }

    public void addLast(T item) {
        if (item == null) {
            return;
        }

        if (size == container.length) {
            resize(container.length * REFACTOR);
        }

        size++;
        container[size - 1] = item;
    }

    public T removeFirst() {
        T firstItem = container[0];
        if (size / (double) container.length < LEAST_USAGE_RATIO && container.length >= 16) {
            resize(container.length / 2);
        }

        if (size != 1) {
            for (int i = 0; i < size - 1; i++) {
                container[i] = container[i + 1];
            }
        }

        size--;
        return firstItem;
    }

    public T removeLast() {
        T lastItem = container[size - 1];

        if (size / (double) container.length < LEAST_USAGE_RATIO && container.length >= 16) {
            resize(container.length / 2);
        }

        size--;
        return lastItem;
    }

    public T get(int index) {

        return container[index];
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

    private void resize(int new_size) {
        T[] tmp = (T[]) new Object[new_size];
        for (int i = 0; i < Math.min(new_size, container.length); i++) {
            tmp[i] = container[i];
        }

        container = tmp;
    }
}
