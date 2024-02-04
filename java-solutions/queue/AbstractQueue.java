package queue;

public abstract class AbstractQueue implements Queue{
    protected int size;
    public void enqueue(Object element) {
        assert element != null;
        enqueueImpl(element);
        size++;
    }
    protected abstract void enqueueImpl(Object element);

    @Override
    public Object dequeue() {
        assert size > 0;
        Object result = element();
        size--;
        dequeueImpl();
        return result;
    }
    protected abstract void dequeueImpl();

    @Override
    public Object element() {
        assert size > 0;
        return elementImpl();
    }
    protected abstract Object elementImpl();

    public int size() {
        return size;
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public void clear() {
        size = 0;
        clearImpl();
    }
    protected abstract void clearImpl();
    protected abstract Queue create();

    public Queue getNth(int n) {
        assert n > 0;
        Queue newQueue = create();
        int oldSize = size;
        for (int i = 0; i < oldSize; i++) {
            Object curr = dequeue();
            enqueue(curr);
            if ((i + 1) % n == 0) {
                newQueue.enqueue(curr);
            }
        }
        return newQueue;
    }

    public void dropNth(int n) {
        assert n > 0;
        int oldSize = size;
        for (int i = 0; i < oldSize; i++) {
            Object curr = dequeue();
            if ((i + 1) % n != 0) {
                enqueue(curr);
            }
        }
    }

    public Queue removeNth(int n) {
        assert n > 0;
        Queue result = getNth(n);
        dropNth(n);
        return result;
    }
}
