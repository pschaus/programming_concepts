package oop;
import java.util.Optional;

/**
 * In this exercise, you will implement some methods for a circular linked-list.
 * A circular linked-list is a linked-list for which the last element has a successor: the
 * first element.
 * For example, if the list is 4 -> 5 -> 2, then 4 is the first element, and 2 points towards 4.
 *                             ^         |
 *                             |         |
 *                             -----------
 * 
 * We ask you to implement two methods; enqueue and remove which, respectively, add an element at the end of the queue, and
 * removes an element at a given index. The time complexity of each method is note in their specifications.
 */
public class CircularLinkedList {

    public static class Node {
        public int value;
        public Optional<Node> next;

        public Node(int value) {
            this.value = value;
            this.next = Optional.empty();
        }
        
        public void setNext(Node next) {
            this.next = Optional.of(next);
        }
        
        public boolean hasNext() {
            return this.next.isPresent();
        }
    }
    
    public Optional<Node> first;
    public Optional<Node> last;
    public int size;

    public CircularLinkedList() {
        this.first = Optional.empty();
        this.last = Optional.empty();
        this.size = 0;
    }
    
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public Optional<Node> getFirst() {
        return this.first;
    }

    public Optional<Node> getLast() {
        return this.last;
    }
    
    public void enqueue(int value) {
        // STUDENT
        // BEGIN STRIP
        Node node = new Node(value);
        this.size += 1;
        if (this.first.isPresent()) {
            Node f = this.first.get();
            Node l = this.last.get();
            node.setNext(f);
            l.setNext(node);

            this.last = Optional.of(node);
        } else {
            this.first = Optional.of(node);
            this.last = Optional.of(node);
        }
        // END STRIP
    }
    
    public int remove(int index) {
        // STUDENT return -1;
        // BEGIN STRIP
        if (index >= this.size) {

        }
        if (index == 0) {
            int value = this.first.get().value;
        }
        Node first = this.first.get();
        
        return 0;
        // END STRIP
    }
}
