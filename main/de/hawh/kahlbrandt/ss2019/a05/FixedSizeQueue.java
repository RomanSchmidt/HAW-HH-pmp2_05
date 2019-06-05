package de.hawh.kahlbrandt.ss2019.a05;

import de.hawh.kahlbrandt.ss2019.a05.interfaces.Queue;
import org.jetbrains.annotations.NotNull;

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
    /**
     * @serial
     */
    private int _maxLength;

    /**
     * @serial
     */
    private int _firstElement = 0;

    private transient K[] _queue;

    /**
     * @serial
     */
    private int _currentSize = 0;

    public FixedSizeQueue() {
        this(FixedSizeQueue.DEFAULT_CAPACITY);
    }

    /**
     * type erasure tho cast object[] to object[]
     */
    @SuppressWarnings("unchecked")
    public FixedSizeQueue(int maxLength) {
        this._maxLength = Math.max(FixedSizeQueue._minLength, maxLength);
        this._queue = (K[]) new Object[this._maxLength];
    }

    @Override
    @SuppressWarnings("unchecked")
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
        if(this == obj) {
            return true;
        }
        if (!(obj instanceof FixedSizeQueue)) {
            return false;
        }
        return Arrays.equals(this._queue, ((FixedSizeQueue) obj)._queue);
    }

    @Override
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

    /**
     * _queue just to make it custom somehow
     * @serialData
     */
    private void writeObject(@NotNull ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(this._queue);
    }

    /**
     * _queue just to make it custom somehow
     */
    @SuppressWarnings("unchecked")
    private void readObject(@NotNull ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this._queue = (K[]) stream.readObject();
    }
}
