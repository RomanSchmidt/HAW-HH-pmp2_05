package de.hawh.kahlbrandt.ss2019.a05;

import de.hawh.kahlbrandt.ss2019.a05.interfaces.Queue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Stanislaw Brug, Roman Schmidt
 */
public class FixedSizeQueue<K> implements Queue, Serializable {

    private static final long serialVersionUID = 13245;
    private transient static final int _minLength = 1;
    private int _maxLength;
    private int _firstElement = 0;
    private K[] _queue;
    private int _currentSize = 0;

    public FixedSizeQueue() {
        this(FixedSizeQueue.DEFAULT_CAPACITY);
    }

    public FixedSizeQueue(int maxLength) {
        this._maxLength = Math.max(FixedSizeQueue._minLength, maxLength);
        this._queue = (K[]) new Object[this._maxLength];
    }

    @Override
    public void enqueue(Object element) throws QueueFullException {
        if (this.isFull()) {
            throw new QueueFullException();
        }
        this._queue[this._currentSize++ % this._maxLength] = (K) element;
    }

    @Override
    public void dequeue() throws QueueEmptyException {
        if (this._currentSize == 0) {
            throw new QueueEmptyException();
        }
        this._queue[this._firstElement] = null;
        --this._currentSize;
        this._firstElement = (this._firstElement + 1) % this._maxLength;
    }

    @Override
    public Object peek() throws QueueEmptyException {
        if (this.isEmpty()) {
            throw new QueueEmptyException();
        }
        return this._queue[this._firstElement];
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

    @org.jetbrains.annotations.Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FixedSizeQueue)) {
            return false;
        }
        return Arrays.equals(this._queue, ((FixedSizeQueue) obj)._queue);
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < this._maxLength; ++i) {
            K element = this._queue[(this._firstElement + i) % this._maxLength];
            output.append(element.toString());
            output.append(',');
        }
        if (output.length() > 0) {
            output.deleteCharAt(output.length() - 1);
        }
        return output.toString();
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(stream);
        out.writeObject(this._queue);
        out.writeObject(this._maxLength);
        out.writeObject(this._firstElement);
        out.writeObject(this._currentSize);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException, QueueFullException {
        ObjectInputStream in = new ObjectInputStream(stream);

        this._queue = (K[]) in.readObject();
        this._maxLength = (int) in.readObject();
        this._firstElement = (int) in.readObject();
        this._currentSize = (int) in.readObject();
    }
}
