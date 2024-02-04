package queue;

public class ArrayQueueTests {
    private static final int COUNT = 10;
    public static void fillTail(ArrayQueue queue) {
        for (int i = 0; i <= COUNT; i++) {
            queue.enqueue(i);
        }
    }

    public static void fillHead(ArrayQueue queue) {
        for (int i = COUNT; i >= 0; i--) {
            queue.push(i);
        }
    }

    public static boolean dumpHead(ArrayQueue queue) {
        System.out.println("dumpHead");
        int i = 0;
        while (!queue.isEmpty()) {
            if (!queue.element().equals(i) || !queue.dequeue().equals(i) ) {
                return false;
            }
            i++;
        }
        return true;
    }

    public static boolean dumpTail(ArrayQueue queue) {
        System.out.println("dumpTail");
        int i = COUNT;
        while (!queue.isEmpty()) {
            if (!queue.peek().equals(i) || !queue.remove().equals(i)) {
                return false;
            }
            i--;
        }
        return true;
    }

    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue();
        fillTail(queue);
        System.out.println(dumpHead(queue));
        fillHead(queue);
        System.out.println(dumpTail(queue));
    }
}
