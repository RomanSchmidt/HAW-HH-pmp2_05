package de.hawh.kahlbrandt.ss2019.a05;

import de.hawh.kahlbrandt.ss2019.a05.interfaces.Queue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

public class FixedSizeQueue<K> implements Queue, Serializable {

    private static final long serialVersionUID = 13245;
    private transient static final int _minLength = 1;
    private int _maxLength;
    private K[] _queue;
    private transient int _currentSize = 0;

    public FixedSizeQueue() {
        this(FixedSizeQueue.DEFAULT_CAPACITY);
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
        this._queue[this._currentSize] = (K) element;
        this._currentSize++;
    }

    @Override
    public void dequeue() throws QueueEmptyException {
        if (this._currentSize == 0) {
            throw new QueueEmptyException();
        }
        this._queue = Arrays.copyOfRange(this._queue, 1, this._queue.length + 1);
        --this._currentSize;
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

    private void writeObject(ObjectOutputStream stream) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(stream);
        out.writeObject(this._queue);
        out.writeObject(this._maxLength);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException, QueueFullException {
        ObjectInputStream in = new ObjectInputStream(stream);

        K[] queue = (K[]) in.readObject();
        this._maxLength = (int) in.readObject();
        this._queue = (K[]) new Object[Math.max(FixedSizeQueue._minLength, this._maxLength)];

        for (K k : queue) {
            this.enqueue(k);
        }
    }
}
