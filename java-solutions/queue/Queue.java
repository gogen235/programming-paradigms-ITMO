package queue;

public interface Queue {
    // Model:
    // a - массив с объектами
    // Элементы записываются в конец очереди, достаются из начала

    // Ivn:
    // n >= 0, forall i 0 <= i < n : a[i] != null

    // immutable(j, k):
    // forall i j <= i < k : a'[i - j] = a[i]

    // Pre: element != null
    // Post: n' = n + 1 && immutable(0, n) && a'[n] = element
    void enqueue(Object element);

    // Pre: n > 0
    // Post: n' = n - 1 && immutable(1, n) && ret = a[0]
    Object dequeue();

    // Pre: n > 0
    // Post: n' = n && immutable(0, n) && ret = a[0]
    Object element();

    // Pre: true
    // Post: n' = n && immutable(0, n) && ret = n
    int size();

    // Pre: true
    // Post: n' = n && immutable(0, n) && ret = (n > 0)
    boolean isEmpty();

    // Pre: true
    // Post: n' = 0 && a is empty
    void clear();

    // k := n
    // Pre: k > 0
    // Post: n' = n && immutable(0, n) &&
    // ret: forall 0 <= i < n && (i + 1) % k == 0: a_ret[i - i // k] = a[i] && n_ret = n // k
    Queue getNth(int n);

    // Pre: k > 0
    // Post: n' = n - n // k && forall 0 <= i < n && (i + 1) % k != 0: a'[i - i // k] = a[i]

    void dropNth(int n);

    // Pre: k > 0
    // Post: n' = n - n // k && forall 0 <= i < n && (i + 1) % k == 0: a'[i] = a[i + i // k] &&
    // ret: forall 0 <= i < n && (i + 1) % k == 0: a_ret[i] = a[i + i // k] && n_ret = n // k
    Queue removeNth(int n);
}
