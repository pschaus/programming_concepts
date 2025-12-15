package algorithms;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Author Pierre Schaus
 *
 * We are interested in the implementation of a circular simply linked list,
 * i.e. a list for which the last position of the list refers, as the next position,
 * to the first position of the list.
 *
 * The addition of a new element (enqueue method) is done at the end of the list and
 * the removal (remove method) is done at a particular index of the list.
 *
 * A (single) reference to the end of the list (last) is necessary to perform all operations on this queue.
 *
 * You are therefore asked to implement this circular simply linked list by completing the class see (TODO's)
 * Most important methods are:
 *
 * - the enqueue to add an element;
 * - the remove method [The exception IndexOutOfBoundsException is thrown when the index value is not between 0 and size()-1];
 * - the iterator (ListIterator) used to browse the list in FIFO.
 *
 * @param <Item>
 */
public class CircularLinkedList<Item> implements Iterable<Item> {

    private long nOp = 0; // count the number of operations
    private int n;          // size of the stack
    private Node last;   // trailer of the list

    // helper linked list class
    private class Node {
        private Item item;
        private Node next;
    }

    public CircularLinkedList() {
        // TODO initialize instance variables
        // BEGIN STRIP
        // we use trick with a dummy first node to avoid dealing with special cases
        last = new Node();
        last.next = last;
        n = 1;
        // END STRIP
    }

    public boolean isEmpty() {
        // TODO
        // STUDENT return false;
        // BEGIN STRIP
        return n == 1;
        // END STRIP
    }

    public int size() {
        // TODO
        // STUDENT return -1;
        // BEGIN STRIP
        return n-1;
        // END STRIP
    }

    private long nOp() {
        return nOp;
    }



    /**
     * Append an item at the end of the list
     * @param item the item to append
     */
    public void enqueue(Item item) {
        // TODO
        // BEGIN STRIP
        nOp++;
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = oldLast.next;
        oldLast.next = last;
        n++;
        // END STRIP

    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their indices).
     * Returns the element that was removed from the list.
     */
    public Item remove(int index) {
        // STUDENT return null;
        // BEGIN STRIP
        nOp++;
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        Node prev = last.next;
        for (int i = 0; i < index; i++) {
            prev = prev.next;
        }
        if (prev.next == last) {
            last = prev;
        }
        Item v = prev.next.item;
        prev.next = prev.next.next;
        n--;
        return v;
        // END STRIP
    }


    /**
     * Returns an iterator that iterates through the items in FIFO order.
     * @return an iterator that iterates through the items in FIFO order.
     */
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    /**
     * Implementation of an iterator that iterates through the items in FIFO order.
     * The iterator should implement a fail-fast strategy, that is ConcurrentModificationException
     * is thrown whenever the list is modified while iterating on it.
     * This can be achieved by counting the number of operations (nOp) in the list and
     * updating it everytime a method modifying the list is called.
     * Whenever it gets the next value (i.e. using next() method), and if it finds that the
     * nOp has been modified after this iterator has been created, it throws ConcurrentModificationException.
     */
    private class ListIterator implements Iterator<Item> {

        // TODO You probably need a constructor here and some instance variables

        // BEGIN STRIP
        private Node current;
        private long nOp;


        private ListIterator() {
            nOp = nOp();
            current = last.next.next;
        }
        // END STRIP

        @Override
        public boolean hasNext() {
            // STUDENT return false;
            // BEGIN STRIP
            return current != last.next;
            // END STRIP
        }

        @Override
        public Item next() {
            // STUDENT return null;
            // BEGIN STRIP
            if (nOp() != nOp) throw new ConcurrentModificationException();
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
            // END STRIP
        }

    }

}


