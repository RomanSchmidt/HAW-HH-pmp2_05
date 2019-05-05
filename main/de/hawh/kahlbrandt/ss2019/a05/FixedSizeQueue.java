package de.hawh.kahlbrandt.ss2019.a05;

import de.hawh.kahlbrandt.ss2019.a05.interfaces.Queue;

import java.io.Serializable;
import java.util.Arrays;

public class FixedSizeQueue<K> implements Queue, Serializable {

    private transient static final int _minLength = 1;
    private final int _maxLength;
    private K[] _queue;
    private int _currentSize = 0;

    public FixedSizeQueue() {
        this(FixedSizeQueue._minLength);
    }

    public FixedSizeQueue(int maxLength) {
        this._maxLength = maxLength;
        this._queue = (K[]) new Object[Math.max(FixedSizeQueue._minLength, maxLength)];
    }

    @Override
    public void enqueue(Object element) throws QueueFullException {
        if (this.isFull()) {
            throw new QueueFullException();
        }
        System.out.println("current size before enqueue: " + this._currentSize);
        System.out.println("_queue before enqueue: " + this._queue.length);
        this._queue[this._currentSize] = (K) element;
        this._currentSize++;
        System.out.println("after enqueue: " + this._currentSize);
        System.out.println("_queue after enqueue: " + this._queue.length);
    }

    @Override
    public void dequeue() throws QueueEmptyException {
        if (this._currentSize == 0) {
            throw new QueueEmptyException();
        }
        System.out.println("this._currentSize before dequeue: " + this._currentSize);
        System.out.println("size of array before dequeue: " + this._queue.length);

        this._queue = Arrays.copyOfRange(this._queue, 1, this._queue.length + 1);
        --this._currentSize;
        System.out.println("this._currentSize after dequeue: " + this._currentSize);
        System.out.println("size of array after dequeue: " + this._queue.length);
    }

    @Override
    public Object peek() throws QueueEmptyException {
        if (this.isEmpty()) {
            throw new QueueEmptyException();
        }
        return this._queue[0];
    }

    @Override
    public boolean isEmpty() {
        return this._currentSize == 0;
    }

    @Override
    public boolean isFull() {
        return this._currentSize == this._maxLength;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this._queue);
    }

    /**
     * ernsthaft?
     */
    public boolean equals() {
        return false;
    }

    @org.jetbrains.annotations.Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FixedSizeQueue)) {
            return false;
        }
        return this.toString().equals(obj.toString());
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        for (Object element : this._queue) {
            output.append(element.toString());
            output.append(',');
        }
        output.deleteCharAt(output.length() - 1);
        return output.toString();
    }
}
