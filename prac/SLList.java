public class SLList {
    public static class IntNode {
        public int item;
        public IntNode next;

        public IntNode(int i, IntNode n) {
            item = i;
            next = n;
        }

        /** Returns the size of the list starting at IntNode p. */
        private static int size(IntNode p) {
            if (p.next == null) {
                return 1;
            }

            return 1 + size(p.next);
        }
    }

    private IntNode first;

    public SLList(int x) {
        first = new IntNode(x, null);
    }

    /** Adds an item to the front of the list. */
    public void addFirst(int x) {
        first = new IntNode(x, first);
    }

    /** Retrieves the front item from the list. */
    public int getFirst() {
        return first.item;
    }

    /** Adds an item to the end of the list. */
    public void addLast(int x) {
        IntNode trav = first;
        while (trav.next != null) {
            trav = trav.next;
        }

        trav.next = new IntNode(x, null);

        return;
    }

    /** Returns the number of items in the list using recursion. */
    public int size() {
        return IntNode.size(first);
    }

}