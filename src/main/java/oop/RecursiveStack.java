package oop;

import java.util.EmptyStackException;
import java.util.Iterator;

/**
 * A Recursive Stack is a stack (LIFO)
 * that is immutable.
 * @param <E>
 */
public class RecursiveStack<E>  implements Iterable<E> {

    final E e;
    final RecursiveStack<E> next;

    /**
     * Creates an empty stack
     */
    public RecursiveStack() {
        // TODO
        e = null;
        next = null;
    }

    /**
     * Create a stack with e on top and next as the next stack.
     * The next is unchanged.
     *
     * @param e the element to put on top
     * @param next the following stack
     */
    private RecursiveStack(E e, RecursiveStack<E> next) {
        // TODO
        this.e = e;
        this.next = next;
    }

    /**
     * Creates and return a new stack with e on top and the
     * current stack at the bottom.
     * The current stack is unchanged.
     *
     * @param e the element to place on top
     * @return the new stack
     */
    public RecursiveStack<E> add(E e) {
		// STUDENT return null;
        // TODO
        // BEGIN STRIP
        return new RecursiveStack<>(e,this);
        // END STRIP
    }

    /**
     * Returns the element on top of the stack
     *
     * @throws EmptyStackException if the stack is empty
     * @return the element on top of the stack
     */
    public E top() {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        if (size() == 0) throw new EmptyStackException();
        return e;
        // END STRIP
    }

    /**
     * Return the stack with element on top removed.
     * The current stack is unchanged.
     *
     * @return the stack with the top element removed
     */
    public RecursiveStack<E> removeTop() {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        if (size() == 0) throw new EmptyStackException();
        return next;
        // END STRIP
    }

    /**
     * Computes the number of elements in the stack
     *
     * @return the number of element in the stack
     */
    public int size() {
        // TODO
        // STUDENT return -1;
        // BEGIN STRIP
        return next == null ? 0 : 1+next.size();
        // END STRIP
    }

    /**
     * Reverse the stack.
     * The current stack is unchanged.
     *
     * @return a reversed version of the current stack (the top element becomes the bottom one)
     */
    public RecursiveStack<E> reverse() {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        if (next == null) return this;
        RecursiveStack<E> s_ = new RecursiveStack<>();
        for (E e: this) {
            s_ = s_.add(e);
        }
        return s_;
        // END STRIP;
    }

    /**
     * Creates a top-down iterator on the stack
     * The delete is not implemented
     *
     * @return the top-down iterator.
     */
    @Override
    public Iterator<E> iterator() {
        // TODO: think about implementing an inner class
        // STUDENT return null;
        // BEGIN STRIP
        return new StackIterator();
        // END STRIP
    }

    // BEGIN STRIP
    private class StackIterator implements Iterator<E> {

        RecursiveStack<E> s = RecursiveStack.this;

        @Override
        public boolean hasNext() {
            return s.next != null;
        }

        @Override
        public E next() {
            E e = s.e;
            s = s.next;
            return e;
        }
    }
    // END STRIP


}
