package fp;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


/**
 * ObservableCounter is a counter that can be
 * incremented with the increment() method.
 * The mehtod value() returns the number of calls
 * to increment() since the creation of the counter.
 *
 * The counter can be observed with a functional programming
 * interface.
 *
 * Complete and modify the class if necessary
 */
public class ObservableCounter {

    // TODO add instance variables
    // BEGIN STRIP
    private int c = 0;
    private List<Consumer<Integer>> observers = new ArrayList<>();
    // END STRIP

    /**
     * Return the current value of the counter
     *
     * @return the current value of the counter
     */
    public synchronized int value() {
        // TODO
        // STUDENT return -1;
        // BEGIN STRIP
        return c;
        // END STRIP
    }

    /**
     * Increments the counter and notifies all the registered observers
     *
     * @return the new value of the counter
     */
    public synchronized int increment() {
        // TODO
        // STUDENT return -1;
        // BEGIN STRIP
        c += 1;
        for (Consumer<Integer> o: observers) {
            o.accept(c);
        }
        return c;
        // END STRIP
    }


    /**
     * Adds an observer that will listen and be notified
     * with the new value when the counter value has changed.
     *
     * @param o the observer that is added to the list of observers
     */
    public synchronized void onChange(Consumer<Integer> o) {
        // TODO
        // BEGIN STRIP
        observers.add(o);
        // END STRIP
    }

    public static void main(String[] args) {

        // initialize a counter at 0
        ObservableCounter c = new ObservableCounter();

        // augment the counter value
        c.increment();

        // the counter is now at 1
        assert(c.value() == 1);

        // register a code that will prints the value of the counter each time it modified with increment()
        c.onChange(v -> System.out.println("observer:"+v));

        // increment the counter, since one observer listen to the change, he should be notified
        // as a consequence, the message "observer:2" should be printed
        c.increment();

    }


}
