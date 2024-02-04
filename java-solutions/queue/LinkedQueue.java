package queue;

public class LinkedQueue extends AbstractQueue {
    private Node head;
    private Node tail;
    @Override
    protected void enqueueImpl(Object element) {
        Node curr = tail;
        tail = new Node(element, null);
        if (size == 0) {
            head = tail;
        } else {
            curr.next = tail;
        }
    }

    @Override
    public void dequeueImpl() {
        head = head.next;
    }

    @Override
    protected Object elementImpl() {
        return head.value;
    }

    @Override
    public void clearImpl() {
        head = tail;
    }
    protected LinkedQueue create() {
        return new LinkedQueue();
    }


    private static class Node {
        private final Object value;
        private Node next;

        public Node(Object value, Node next) {
            assert value != null;

            this.value = value;
            this.next = next;
        }
    }
}
