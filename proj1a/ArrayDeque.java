public class ArrayDeque<Data> {
    private int size;
    private final int REFACTOR;
    private final double LEAST_USAGE_RATIO;

    private Data[] container;

    public ArrayDeque() {
        REFACTOR = 2;
        LEAST_USAGE_RATIO = 0.25;
        size = 0;
        container = (Data[]) new Object[8];
    }

    public void addFirst(Data item) {
        if (item == null) {
            return;
        }

        if (size == container.length) {
            resize(size * REFACTOR);
        }

        for (int i = size; i >= 1; i--) {
            container[i] = container[i - 1];
        }
        container[0] = item;
    }

    public void addLast(Data item) {
        if (item == null) {
            return;
        }

        if (size == container.length) {
            resize(size * REFACTOR);
        }

        container[size] = item;
    }

    public Data removeFirst() {
        Data firstItem = container[0];
        if (size / container.length <= LEAST_USAGE_RATIO) {
            resize(size / 2);
        }

        for (int i = 0; i < size - 1; i++) {
            container[i] = container[i + 1];
        }

        size--;
        return firstItem;
    }

    public Data removeLast() {
        Data lastItem = container[size - 1];

        if (size / container.length <= LEAST_USAGE_RATIO) {
            resize(size / 2);
        }

        size--;
        return lastItem;
    }

    public Data get(int index) {
        return container[index];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (Data data : container) {
            System.out.print(data);
            System.out.print(' ');
        }
    }

    private void resize(int new_size) {
        Data[] tmp = (Data[]) new Object[new_size];
        for (int i = 0; i < container.length; i++) {
            tmp[i] = container[i];
        }

        container = tmp;
    }
}
