package de.hawh.kahlbrandt.ss2019.a05;

import de.hawh.kahlbrandt.ss2019.a05.interfaces.Queue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testcases for a class to be written as a lab task.
 *
 * @author Bernd Kahlbrandt
 */
class FixedSizedQueueTest {
    private final String _serializedFile = System.getProperty("user.dir") + "/src/serializedFile.ser";
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
            fail(e.toString() + " thrown. May be due to errors concerning constructors in setUp.");
        }
    }

    @Test
    void testDefaultConstructor() {
        assertTrue(emptyQueue.isEmpty());
    }

    @Test
    void testConstructor() {
        assertFalse(smallQueue01.isEmpty());
    }

    @Test
    void testEnqueue() throws QueueEmptyException {
        assertTrue(emptyQueue.isEmpty());
        assertFalse(emptyQueue.isFull());
        assertEquals(stringTestData.get(0), smallQueue01.peek());
        assertDoesNotThrow(() -> smallQueue01.dequeue());
        assertDoesNotThrow(() -> smallQueue01.enqueue("42"));
        assertTrue(smallQueue01.isFull());

    }

    @Test
    void testEnqueueException() {
        assertThrows(QueueFullException.class, () -> smallQueue01.enqueue("One too many"));
    }

    @Test
    void testDequeue() throws QueueEmptyException {
        for (String s : stringTestData) {
            assertEquals(s, smallQueue01.peek());
            smallQueue01.dequeue();
        }
        assertTrue(smallQueue01.isEmpty());
    }

    @Test
    void testDequeueException() throws QueueEmptyException {
        assertThrows(QueueEmptyException.class, () -> emptyQueue.dequeue());
        /**
         * Uncritical: Here it's the simplest loop. Looking for a better solution.
         */
        for (@SuppressWarnings("unused")
                String s : stringTestData) {
            smallQueue01.dequeue();
        }
        assertThrows(QueueEmptyException.class, () -> smallQueue01.dequeue());

    }

    @Test
    void testIsEmpty() {
        assertTrue(emptyQueue.isEmpty());
        assertFalse(smallQueue01.isEmpty());
        assertDoesNotThrow(() -> smallQueue01.dequeue());
    }

    @Test
    void testIsFull() {
        assertTrue(smallQueue01.isFull());
        assertFalse(emptyQueue.isFull());
        assertDoesNotThrow(() -> smallQueue01.dequeue());
    }

    @Test
    void testPeek() throws QueueEmptyException {
        assertEquals(stringTestData.get(0), smallQueue01.peek());
    }

    @Test
    void testPeekException() {
        assertThrows(QueueEmptyException.class, () -> emptyQueue.peek());
    }

    @Test
    void testEqualsHashcode() {
        if (smallQueue01.equals(smallQueue02)) {
            assertEquals(smallQueue01.hashCode(), smallQueue02.hashCode(), "equals and hashCode may be inconsistent!");
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
            missing.add("Have you overwritten 'equals'?");
        }
        try {
            FixedSizeQueue.class.getDeclaredMethod("hashCode", new Class<?>[0]);
        } catch (NoSuchMethodException | SecurityException e) {
            missing.add("Have you overwritten 'hashCode'?");
        }
        if (!missing.isEmpty()) {
            String msg = "";
            for (String s : missing) {
                msg += s;
            }
            fail(msg);
        }

    }

    @Test
    void testCustomSerializedForm() {

        List<String> missing = new ArrayList<>();
        List<Method> declaredMethods = Arrays.asList(FixedSizeQueue.class.getDeclaredMethods());
        try {
            Method writeObj = FixedSizeQueue.class.getDeclaredMethod("writeObject", ObjectOutputStream.class);
            if (!declaredMethods.contains(writeObj)) {
                missing.add(writeObj.toString() + " not written!");
            }
            assertTrue(Modifier.isPrivate(writeObj.getModifiers()), "writeObject is not private!");

        } catch (NoSuchMethodException | SecurityException e) {
            missing.add("writeObject not defined!");
        }
        try {
            Method readObj = FixedSizeQueue.class.getDeclaredMethod("readObject", ObjectInputStream.class);
            if (!declaredMethods.contains(readObj)) {
                missing.add(readObj.toString() + " not written!");
            }
            assertTrue(Modifier.isPrivate(readObj.getModifiers()), "readObject is not private!");
        } catch (NoSuchMethodException | SecurityException e) {
            missing.add("readObject not defined!");
        }
        if (!missing.isEmpty()) {
            String msg = "";
            for (String s : missing) {
                msg += s;
            }
            fail(msg);
        }
    }

    @Test
    void serializeQueue() {
        try {
            FileOutputStream fileOut = new FileOutputStream(this._serializedFile);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.smallQueue01);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in " + this._serializedFile);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    @Test
    void deserializeQueue() throws QueueEmptyException {
        Queue<String> e = null;
        try {
            FileInputStream fileIn = new FileInputStream(this._serializedFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            e = (Queue<String>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("FixedSizeQueue class not found");
            c.printStackTrace();
            return;
        }

        assertEquals(e.toString(), this.smallQueue01.toString());
        assertEquals(e.hashCode(), this.smallQueue01.hashCode());
        assertTrue(e.equals(this.smallQueue01));
    }
}