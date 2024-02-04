package queue;

import java.util.Arrays;
import java.util.Objects;

public class ArrayQueue extends AbstractQueue {
    private Object[] elements = new Object[5]; // a
    private int head, tail; // h, t

    @Override
    protected void enqueueImpl(Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(size + 1);
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
    }

    // Pre: true
    // Post: n' = n && h' = h &&
    // forall i h <= i < h + n : elements'[i % elements'.length] = elements[i % elements.length]
    // (capacity > elements.length && elements'.length = 2 * capacity && t' = h + n ||
    // capacity <= elements.length && elements'.length = elements.length && t' = t)
    private void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            elements = Arrays.copyOf(elements, 2 * capacity);
            System.arraycopy(elements, 0, elements, size, tail);
            tail = size + head;
        }
    }
    @Override
    public void dequeueImpl() {
        head = (head + 1) % elements.length;
    }
    @Override
    protected Object elementImpl() {
        return elements[head];
    }
    @Override
    public void clearImpl() {
        head = 0;
        tail = 0;
    }

    // Pre: element != null
    // Post: n' = n + 1 && immutableReverse(1, n) && a'[0] = element
    public void push(Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(size + 1);
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    // Pre: n > 0
    // Post: n' = n - 1 && immutableReverse(0, n - 1) && ret = a[n]
    public Object remove() {
        assert size > 0;
        tail = (tail - 1 + elements.length) % elements.length;
        size--;
        return elements[tail];
    }

    // Pre: n > 0
    // Post: n' = n && immutableReverse(0, n) && ret = a[n]
    public Object peek() {
        assert size > 0;
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    // Pre: idx < size && idx >= 0
    // Post: n' = n && immutableReverse(0, n) && ret = a[idx]
    public Object get(int idx) {
        assert idx < size && idx >= 0;
        return elements[(idx + head) % elements.length];
    }

    // Pre: idx < size && idx >= 0 && value != null
    // Post: n' = n && immutableReverse(0, n) && a[idx] = value
    public void set(int idx, Object value) {
        assert idx < size && idx >= 0 && value != null;
        elements[(idx + head) % elements.length] = value;
    }

    protected ArrayQueue create() {
        return new ArrayQueue();
    }

}
