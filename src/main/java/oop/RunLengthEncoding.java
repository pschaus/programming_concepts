package oop;

import java.util.ArrayList;
import java.util.List;


/**
 * (Proposed at the exam of August 2025)
 *  Imagine a string that contains a lot of characters that are repeated,
 *  for example like this:
 *     "dddddaaazzzzzzdddz"
 *
 *  To be more space efficient, we could represent the string as a list
 *  where the elements of the list tell us how often a character is repeated.
 *  For our example, this would look like this:
 *     [  (5,'d'), (3,'a'), (6,'z'), (3,'d') , (1,'z') ]
 *  An element like (6,'z') means that there are 6 consecutive 'z' at this
 *  position in the string.
 *
 *  This kind of compression is called Run Length Encoding.
 *
 *  The class RunLengthEncoding below is an implementation of such a
 *  compressed representation. It uses a List containing objects of the
 *  class RepeatingValue.
 *
 *  Your must implement the constructors and methods marked as "TODO".
 */
public class RunLengthEncoding {

    /**
     * Class that represents one repeating character.
     */
    public static class RepeatingValue {
        private char value;            // the character
        private int numRepetitions;    // the number of repetitions

        /**
         * Constructs a RepeatingValue for the given character and number of repetitions.
         */
        public RepeatingValue(char value, int numRepetitions) {
            this.value = value;
            this.numRepetitions = numRepetitions;
        }

        public char getValue() {
            return value;
        }

        public int getNumRepetitions() {
            return numRepetitions;
        }
    }


    /**
     * You should use only the following member variable to store the Run Length Encoding.
     * There's no need to create any additional member variables.
     **/
    private final List<RepeatingValue> repeatingValues;


    /**
     * Constructs a new RunLengthEncoding object representing an empty string.
     */
    public RunLengthEncoding() {
        // TODO
        // STUDENT repeatingValues = null;
        // BEGIN STRIP
        this("");
        // END STRIP
    }

    /**
     * Constructs a new RunLengthEncoding object representing a string.
     * You can assume that the string is NOT null.
     *
     * @param content the string to be encoded using Run Length Encoding
     */
    public RunLengthEncoding(String content) {
        // TODO
        // STUDENT repeatingValues = null;
        // BEGIN STRIP
        repeatingValues = new ArrayList<RepeatingValue>();
        for (int i = 0; i < content.length(); i++) {
            append(content.charAt(i));
        }
        // END STRIP
    }

    /**
     * Appends a character to the end of the string.
     *
     * @param value the character to be appended
     */
    public void append(char value) {
        // TODO
        // BEGIN STRIP
        if (repeatingValues.isEmpty()) {
            repeatingValues.add(new RepeatingValue(value, 1));
        } else {
            // List::getLast() only exists since Java 21
            RepeatingValue last = repeatingValues.get(repeatingValues.size() - 1);
            if (last.getValue() == value) {
                repeatingValues.set(repeatingValues.size() - 1, new RepeatingValue(last.getValue(), last.getNumRepetitions() + 1));
            } else {
                repeatingValues.add(new RepeatingValue(value, 1));
            }
        }
        // END STRIP
    }

    /**
     * Returns the compressed representation.
     */
    public List<RepeatingValue> getCompressed() {
        return repeatingValues;
    }

    /**
     * Returns the uncompressed string represented by this RunLengthEncoding object.
     * Example:
     *   If the object currently stores the information that 'd' is repeated five times
     *   and 'a' is repeated three times, this method returns "dddddaaa".
     *
     * @return the uncompressed string
     */
    public String getUncompressed() {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        String s = "";
        for (RepeatingValue rv: repeatingValues) {
            // String::repeat() only exists since Java 11
            // We use a for-loop instead:
            for (int i = 0; i < rv.getNumRepetitions(); i++) {
                // in theory, using strings like this is not efficient,
                // but the Java compiler will replace this by a StringBuilder object.
                s = s + rv.getValue();
            }
        }
        return s;
        // END STRIP
    }

    // Run this main method to see an example:
    public static void main(String[] args) {
        RunLengthEncoding rle = new RunLengthEncoding("dddddaaazzzzzzddd");

        // let's look what is inside the object:
        for (RepeatingValue rv: rle.getCompressed()) {
            System.out.println(rv.getNumRepetitions() + " times the character " + rv.getValue());
        }

        System.out.println("Here is the uncompressed string: " + rle.getUncompressed());
    }
}
