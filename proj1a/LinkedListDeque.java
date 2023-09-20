public class LinkedListDeque<T> {
    private class Node {
        private Node prev;
        private T item;
        private Node next;

        public Node() {
            prev = null;
            item = null;
            next = null;
        }

        public Node(Node p, T i, Node n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    private Node sentiNode;
    private Node first;
    private Node last;
    private Node trav;
    private int size;

    public LinkedListDeque() {
        sentiNode = new Node();
        sentiNode.prev = sentiNode;
        sentiNode.next = sentiNode;
        first = sentiNode;
        last = sentiNode;
        size = 0;
    }

    public void addFirst(T item) {
        if (item == null) {
            return;
        }

        first.prev = new Node(first.prev, item, first);
        first = first.prev;
        if (size == 0) {
            last = first;
        }
        size++;

    }

    public void addLast(T item) {
        if (item == null) {
            return;
        }

        last.next = new Node(last, item, last.next);
        last = last.next;
        if (size == 0) {
            first = last;
        }
        size++;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        T firstItem = first.item;
        first.next.prev = first.prev;
        first = first.next;
        size--;

        return firstItem;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }

        T lastItem = last.item;
        last.prev.next = last.next;
        last = last.prev;
        size--;

        return lastItem;
    }

    public T get(int index) {
        if (index >= size) {
            return null;
        }

        trav = first;
        for (int i = 0; i < index; i++) {
            trav = trav.next;
        }

        return trav.item;
    }

    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }

        return getRecursiveHelper(first, index).item;
    }

    private Node getRecursiveHelper(Node head, int index) {
        if (index == 0) {
            return head;
        }

        return getRecursiveHelper(head.next, index - 1);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        trav = first;

        while (trav.item != null) {
            System.out.print(trav.item);
            System.out.print(' ');
            trav = trav.next;
        }
    }
}
