package queue;

public class ArrayQueueModuleTests {
    private static final int COUNT = 10;
    public static void fillTail() {
        for (int i = 0; i <= COUNT; i++) {
            ArrayQueueModule.enqueue(i);
        }
    }

    public static void fillHead() {
        for (int i = COUNT; i >= 0; i--) {
            ArrayQueueModule.push(i);
        }
    }

    public static boolean dumpHead() {
        System.out.println("dumpHead");
        int i = 0;
        while (!ArrayQueueModule.isEmpty()) {
            if (!ArrayQueueModule.element().equals(i) || !ArrayQueueModule.dequeue().equals(i) ) {
                return false;
            }
            i++;
        }
        return true;
    }

    public static boolean dumpTail() {
        System.out.println("dumpTail");
        int i = COUNT;
        while (!ArrayQueueModule.isEmpty()) {
            if (!ArrayQueueModule.peek().equals(i) || !ArrayQueueModule.remove().equals(i)) {
                return false;
            }
            i--;
        }
        return true;
    }

    public static void main(String[] args) {
        fillTail();
        System.out.println(dumpHead());
        fillHead();
        System.out.println(dumpTail());
    }
}
