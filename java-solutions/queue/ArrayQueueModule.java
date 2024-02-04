package queue;

import java.util.Arrays;
import java.util.Objects;

public class ArrayQueueModule {
    private static int size; // n
    private static Object[] elements = new Object[5]; // a
    private static int head, tail; // h, t

    // Model:
    // a - массив с объектами
    // Элементы записываются в конец очереди, достаются из начала

    // Ivn:
    // n >= 0, forall i 0 <= i < n : a[i] != null

    // immutable(j, k):
    // forall i j <= i < k : a'[i - j] = a[i]

    // Pre: element != null
    // Post: n' = n + 1 && immutable(0, n) && a'[n] = element
    public static void enqueue(Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(size + 1);
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    // Pre: true
    // Post: n' = n && h' = h &&
    // forall i h <= i < h + n : elements'[i % elements'.length] = elements[i % elements.length]
    // (capacity > elements.length && elements'.length = 2 * capacity && t' = h + n ||
    // capacity <= elements.length && elements'.length = elements.length && t' = t)
    private static void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            elements = Arrays.copyOf(elements, 2 * capacity);
            System.arraycopy(elements, 0, elements, size, tail);
            tail = size + head;
        }
    }

    // Pre: n > 0
    // Post: n' = n - 1 && immutable(1, n) && ret = a[0]
    public static Object dequeue() {
        assert size > 0;
        Object element = elements[head];
        head = (head + 1) % elements.length;
        size--;
        return element;
    }

    // Pre: n > 0
    // Post: n' = n && immutable(0, n) && ret = a[0]
    public static Object element() {
        assert size > 0;
        return elements[head];
    }

    // Pre: true
    // Post: n' = n && immutable(0, n) && ret = n
    public static int size() {
        return size;
    }

    // Pre: true
    // Post: n' = n && immutable(0, n) && ret = (n > 0)
    public static boolean isEmpty() {
        return size == 0;
    }

    // Pre: true
    // Post: n' = 0 && a is empty
    public static void clear() {
        head = 0;
        tail = 0;
        size = 0;
    }

    // Pre: element != null
    // Post: n' = n + 1 && immutableReverse(1, n) && a'[0] = element
    public static void push(Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(size + 1);
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    // Pre: n > 0
    // Post: n' = n - 1 && immutableReverse(0, n - 1) && ret = a[n]
    public static Object remove() {
        assert size > 0;
        tail = (tail - 1 + elements.length) % elements.length;
        size--;
        return elements[tail];
    }

    // Pre: n > 0
    // Post: n' = n && immutableReverse(0, n) && ret = a[n]
    public static Object peek() {
        assert size > 0;
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    // Pre: idx < size && idx >= 0
    // Post: n' = n && immutableReverse(0, n) && ret = a[idx]
    public static Object get(int idx) {
        assert idx < size && idx >= 0;
        return elements[(idx + head) % elements.length];
    }

    // Pre: idx < size && idx >= 0 && value != null
    // Post: n' = n && immutableReverse(0, n) && a[idx] = value
    public static void set(int idx, Object value) {
        assert idx < size && idx >= 0 && value != null;
        elements[(idx + head) % elements.length] = value;
    }

}
