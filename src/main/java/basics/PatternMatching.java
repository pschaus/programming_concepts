package basics;

public class PatternMatching {

    /**
     * Look for one sequence of characters (the pattern) in an input
     * string, and return the position of the pattern in the string
     * (if present). If the pattern is present multiple times in the
     * string, the function must return the leftmost occurrence of the
     * pattern (i.e. the occurrence whose index is the lowest). The
     * function must be case-sensitive (i.e. <code>Hello</code> is not
     * the same as <code>hello</code>).
     * @param pattern The pattern to look for.
     * @param value The string to look in.
     * @result The index of the leftmost occurrence of the pattern in
     * the string. Must be <code>-1</code> if the pattern is absent
     * from the string.
     **/
    public static int find(String pattern,
                           String value) {
        // STUDENT return -1;
        // BEGIN STRIP
        for (int pos = 0; pos < value.length(); pos++) {
            boolean isMatch = true;
            
            int i;            
            for (i = 0; (isMatch &&
                         i < pattern.length() &&
                         pos + i < value.length()); i++) {
                if (value.charAt(pos + i) != pattern.charAt(i)) {
                    isMatch = false;
                }
            }
            
            if (i == pattern.length()) {
                return pos;
            }
        }

        return -1;  // Not found
        // END STRIP
    }

}
