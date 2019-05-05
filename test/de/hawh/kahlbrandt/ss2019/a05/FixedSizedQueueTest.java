package de.hawh.kahlbrandt.ss2019.a05;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.hawh.kahlbrandt.ss2019.a05.FixedSizeQueue;
import de.hawh.kahlbrandt.ss2019.a05.QueueFullException;
import de.hawh.kahlbrandt.ss2019.a05.QueueEmptyException;
import de.hawh.kahlbrandt.ss2019.a05.interfaces.Queue;

/**
 * Testcases for a class to be written as a lab task.
 *
 * @author Bernd Kahlbrandt
 *
 */
class FixedSizedQueueTest {
	private Queue<String> emptyQueue = new FixedSizeQueue<>();
	private List<String> stringTestData = List.of("42", "11", "08/15");
	private Queue<String> smallQueue01;
	private Queue<String> smallQueue02;

	/**
	 * Builds some {@link FixedSizeQueue}s from test data. May throw exceptions, if
	 * methods are not correctly implemented.
	 */
	@BeforeEach
	public void setUp() {
		try {
			smallQueue01 = new FixedSizeQueue<>(3);
			smallQueue02 = new FixedSizeQueue<>(3);
			for (String s : stringTestData) {
				smallQueue01.enqueue(s);
				smallQueue02.enqueue(s);
			}
		} catch (Exception e) {
			Assertions.fail(e.toString() + " thrown. May be due to errors concerning constructors in setUp.");
		}
	}

	@Test
	void testDefaultConstructor() {
		Assertions.assertTrue(emptyQueue.isEmpty());
	}
	@Test
	void testConstructor() {
		Assertions.assertFalse(smallQueue01.isEmpty());
	}

	@Test
	void testEnqueue() throws QueueEmptyException {
		Assertions.assertTrue(emptyQueue.isEmpty());
		Assertions.assertFalse(emptyQueue.isFull());
		Assertions.assertEquals(stringTestData.get(0), smallQueue01.peek());
		Assertions.assertDoesNotThrow(() -> smallQueue01.dequeue());
		Assertions.assertDoesNotThrow(() -> smallQueue01.enqueue("42"));
		Assertions.assertTrue(smallQueue01.isFull());

	}

	@Test
	void testEnqueueException() {
		Assertions.assertThrows(QueueFullException.class, () -> smallQueue01.enqueue("One too many"));
	}

	@Test
	void testDequeue() throws QueueEmptyException {
		for (String s : stringTestData) {
			Assertions.assertEquals(s, smallQueue01.peek());
			smallQueue01.dequeue();
		}
		Assertions.assertTrue(smallQueue01.isEmpty());
	}

	@Test
	void testDequeueException() throws QueueEmptyException {
		Assertions.assertThrows(QueueEmptyException.class, () -> emptyQueue.dequeue());
		/**
		 * Uncritical: Here it's the simplest loop. Looking for a better solution.
		 */
		for (@SuppressWarnings("unused")
		String s : stringTestData) {
			smallQueue01.dequeue();
		}
		Assertions.assertThrows(QueueEmptyException.class, () -> smallQueue01.dequeue());

	}

	@Test
	void testIsEmpty() {
		Assertions.assertTrue(emptyQueue.isEmpty());
		Assertions.assertFalse(smallQueue01.isEmpty());
		Assertions.assertDoesNotThrow(() -> smallQueue01.dequeue());
	}

	@Test
	void testIsFull() {
		Assertions.assertTrue(smallQueue01.isFull());
		Assertions.assertFalse(emptyQueue.isFull());
		Assertions.assertDoesNotThrow(() -> smallQueue01.dequeue());
	}

	@Test
	void testPeek() throws QueueEmptyException {
		Assertions.assertEquals(stringTestData.get(0), smallQueue01.peek());
	}

	@Test
	void testPeekException() {
		Assertions.assertThrows(QueueEmptyException.class, () -> emptyQueue.peek());
	}

	@Test
	void testEqualsHashcode() {
		if (smallQueue01.equals(smallQueue02)) {
			Assertions.assertEquals(smallQueue01.hashCode(), smallQueue02.hashCode(), "equals and hashCode may be inconsistent!");
		}
	}

	@Test
	void testOverwrites() {
		List<String> missing = new ArrayList<>();
		try {
			FixedSizeQueue.class.getDeclaredMethod("toString", new Class<?>[0]);
		} catch (NoSuchMethodException | SecurityException e) {
			missing.add("Have you overwritten 'toString'?");
		}
		try {
			FixedSizeQueue.class.getDeclaredMethod("equals", new Class<?>[0]);
		} catch (NoSuchMethodException | SecurityException e) {
			System.out.println("Ex: "+e.getMessage());
			missing.add("Have you overwritten 'equals'?");
		}
		try {
			FixedSizeQueue.class.getDeclaredMethod("hashCode", new Class<?>[0]);
		} catch (NoSuchMethodException | SecurityException e) {
			missing.add("Have you overwritten 'hashCode'?");
		}
		if(!missing.isEmpty()) {
			String msg = "";
			for(String s : missing) {
				msg += s;
			}
			Assertions.fail(msg);
		}

	}

	@Test
	void testCustomSerializedForm() {

		List<String> missing = new ArrayList<>();
		List<Method> declaredMethods = Arrays.asList(FixedSizeQueue.class.getDeclaredMethods());
		try {
			Method writeObj = FixedSizeQueue.class.getDeclaredMethod("writeObject", ObjectOutputStream.class);
			if(!declaredMethods.contains(writeObj)) {
				missing.add(writeObj.toString() + " not written!");
			}
			Assertions.assertTrue(Modifier.isPrivate(writeObj.getModifiers()), "writeObject is not private!");

		} catch (NoSuchMethodException | SecurityException e) {
			missing.add("writeObject not defined!");
		}
		try {
			Method readObj = FixedSizeQueue.class.getDeclaredMethod("readObject", ObjectInputStream.class);
			if(!declaredMethods.contains(readObj)) {
				missing.add(readObj.toString() + " not written!");
			}
			Assertions.assertTrue(Modifier.isPrivate(readObj.getModifiers()), "readObject is not private!");
		} catch (NoSuchMethodException | SecurityException e) {
			missing.add("readObject not defined!");
		}
		if(!missing.isEmpty()) {
			String msg = "";
			for(String s : missing) {
				msg += s;
			}
			Assertions.fail(msg);

		}
	}

}
