package algorithms;


/**
 * Question:
 *
 * You are asked to clean a increasing sorted linked List (see the TODO below)
 * Cleaning the linkedList means keeping only one occurrence of each value.
 *
 * For instance cleaning: 3,3,3,4,5,5,6,6,6,7,9,9,9,9,10,10
 * Gives: 3,4,5,6,7,9,10
 *
 * Your algorithm should execute in Theta(n)
 * where n are the number of elements in the original list
 *
 */
public class CleanLinkedList {

    Node first = null;
    Node last = null;

    public void add(int v) {
        // TODO
        // STUDENT
        // BEGIN STRIP
        if (first == null) {
            first = new Node(v,null);
            last = first;
        } else {
            last.next = new Node(v,null);
            last = last.next;
        }
        // END STRIP
    }

    public void add(int ... values) {
        for (int v: values) {
            add(v);
        }
    }


    /**
     * Given the increasingly sorted list, it removes the duplicates
     * @return an increasingly sorted list containing the same set
     *         of elements as list but without duplicates.
     */
    public CleanLinkedList clean() {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        CleanLinkedList result = new CleanLinkedList();
        Node current = first;
        result.add(current.v);
        while (current != null) {
            if (current.v != result.last.v) {
                result.add(current.v);
            }
            current = current.next;
        }
        return result;
        // END STRIP
    }


    class Node {
        int v;
        Node next;
        Node(int v, Node next) {
            this.v = v;
            this.next = next;
        }
    }


}

