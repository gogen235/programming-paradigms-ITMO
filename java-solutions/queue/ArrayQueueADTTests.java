package queue;

public class ArrayQueueADTTests {
    private static final int COUNT = 10;
    public static void fillTail(ArrayQueueADT queue) {
        for (int i = 0; i <= COUNT; i++) {
            ArrayQueueADT.enqueue(queue, i);
        }
    }

    public static void fillHead(ArrayQueueADT queue) {
        for (int i = COUNT; i >= 0; i--) {
            ArrayQueueADT.push(queue, i);
        }
    }

    public static boolean dumpHead(ArrayQueueADT queue) {
        System.out.println("dumpHead");
        int i = 0;
        while (!ArrayQueueADT.isEmpty(queue)) {
            if (!ArrayQueueADT.element(queue).equals(i) || !ArrayQueueADT.dequeue(queue).equals(i) ) {
                return false;
            }
            i++;
        }
        return true;
    }

    public static boolean dumpTail(ArrayQueueADT queue) {
        System.out.println("dumpTail");
        int i = COUNT;
        while (!ArrayQueueADT.isEmpty(queue)) {
            if (!ArrayQueueADT.peek(queue).equals(i) || !ArrayQueueADT.remove(queue).equals(i)) {
                return false;
            }
            i--;
        }
        return true;
    }


    public static void main(String[] args) {
        ArrayQueueADT queue = new ArrayQueueADT();
        fillTail(queue);
        System.out.println(dumpHead(queue));
        fillHead(queue);
        System.out.println(dumpTail(queue));
    }
}
