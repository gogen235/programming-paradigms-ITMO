package queue;

import java.util.Arrays;
import java.util.Objects;

public class ArrayQueueADT {
    private int size; // n
    private Object[] elements;
    private int head, tail; // h, t

    public ArrayQueueADT() {
        elements = new Object[10];
        size = 0;
        tail = 0;
        head = 0;
    }


    // Model:
    // a - массив с объектами
    // Элементы записываются в конец очереди, достаются из начала

    // Ivn:
    // n >= 0, forall i 0 <= i < n : a[i] != null

    // immutable(j, k):
    // forall i j <= i < k : a'[i - j] = a[i]

    // Pre: element != null
    // Post: n' = n + 1 && immutable(0, n) && a'[n] = element
    public static void enqueue(ArrayQueueADT queue, Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(queue, queue.size + 1);
        queue.elements[queue.tail] = element;
        queue.tail = (queue.tail + 1) % queue.elements.length;
        queue.size++;
    }

    // Pre: true
    // Post: n' = n && h' = h &&
    // forall i h <= i < h + n : elements'[i % elements'.length] = elements[i % elements.length]
    // (capacity > elements.length && elements'.length = 2 * capacity && t' = h + n ||
    // capacity <= elements.length && elements'.length = elements.length && t' = t)
    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        if (capacity > queue.elements.length) {
            // A B . . . . C D
            // A B . . . . C D . . . . . . . .
            // A B . . . . C D . . . . A B . .

            // A B . . . . C D A B . . . . C D
            queue.elements = Arrays.copyOf(queue.elements, 2 * capacity);
            System.arraycopy(queue.elements, 0, queue.elements, queue.size, queue.tail);
            queue.tail = queue.size + queue.head;
        }
    }

    // Pre: n > 0
    // Post: n' = n - 1 && immutable(1, n) && ret = a[0]
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.size > 0;
        Object element = queue.elements[queue.head];
        queue.head = (queue.head + 1) % queue.elements.length;
        queue.size--;
        return element;
    }

    // Pre: n > 0
    // Post: n' = n && immutable(0, n) && ret = a[0]
    public static Object element(ArrayQueueADT queue) {
        assert queue.size > 0;
        return queue.elements[queue.head];
    }

    // Pre: true
    // Post: n' = n && immutable(0, n) && ret = n
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    // Pre: true
    // Post: n' = n && immutable(0, n) && ret = (n > 0)
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // Pre: true
    // Post: n' = 0 && a is empty
    public static void clear(ArrayQueueADT queue) {
        queue.head = 0;
        queue.tail = 0;
        queue.size = 0;
    }

    // Pre: element != null
    // Post: n' = n + 1 && immutableReverse(1, n) && a'[0] = element
    public static void push(ArrayQueueADT queue, Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(queue, queue.size + 1);
        queue.head = (queue.head - 1 + queue.elements.length) % queue.elements.length;
        queue.elements[queue.head] = element;
        queue.size++;
    }

    // Pre: n > 0
    // Post: n' = n - 1 && immutableReverse(0, n - 1) && ret = a[n]
    public static Object remove(ArrayQueueADT queue) {
        assert queue.size > 0;
        queue.tail = (queue.tail - 1 + queue.elements.length) % queue.elements.length;
        queue.size--;
        return queue.elements[queue.tail];
    }

    // Pre: n > 0
    // Post: n' = n && immutableReverse(0, n) && ret = a[n]
    public static Object peek(ArrayQueueADT queue) {
        assert queue.size > 0;
        return queue.elements[(queue.tail - 1 + queue.elements.length) % queue.elements.length];
    }

    // Pre: idx < size && idx >= 0
    // Post: n' = n && immutableReverse(0, n) && ret = a[idx]
    public static Object get(ArrayQueueADT queue, int idx) {
        assert idx < queue.size && idx >= 0;
        return queue.elements[(idx + queue.head) % queue.elements.length];
    }

    // Pre: idx < size && idx >= 0 && value != null
    // Post: n' = n && immutableReverse(0, n) && a[idx] = value
    public static void set(ArrayQueueADT queue, int idx, Object value) {
        assert idx < queue.size && idx >= 0 && value != null;
        queue.elements[(idx + queue.head) % queue.elements.length] = value;
    }

}
