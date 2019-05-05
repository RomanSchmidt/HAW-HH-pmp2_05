package de.hawh.kahlbrandt.ss2019.a05.interfaces;

import de.hawh.kahlbrandt.ss2019.a05.QueueEmptyException;
import de.hawh.kahlbrandt.ss2019.a05.QueueFullException;

/**
 * An Interface to be used for a specialized Queue in a lab task.
 * @author Bernd Kahlbrandt
 *
 */
public interface Queue<E> {
	/**
	 * Default capacity to be used by a fixed sized Queue, akna.&nbsp;
	 * input restricted Queue.
	 */
	static int DEFAULT_CAPACITY = 42;
	/**
	 * Inserts an element into the Queue.
	 * @param element The element to be inserted
	 */
	void enqueue(E element) throws QueueFullException;
	/**
	 * Removes the first (oldest) element in the Queue.
	 */
	void dequeue() throws QueueEmptyException;
	/**
	 * Returns the first element of the Queue but leaves it in the Queue.
	 * @return The first element.
	 */
	E peek() throws QueueEmptyException;
	/**
	 * Checks if the Queue is empty.
	 * @return <b>true</b> if the Queue is empty, <b>false</b> otherwise.
	 */
	boolean isEmpty();
	/**
	 * Checks if the Queue is full.
	 * @return <b>true</b> if the Queue is full, <b>false</b> otherwise.
	 */
	boolean isFull();
}
