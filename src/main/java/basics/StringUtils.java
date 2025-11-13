package basics;

public class StringUtils {


    /**
     * Split a string according to a delimiter
     *
     * @param str The string to split
     * @param delimiter The delimiter
     * @return An array containing the substring which fall
     *          between two consecutive occurrence of the delimiter.
     *          If there is no occurrence of the delimiter, it should
     *          return an array of size 1 with the string at element 0
     */
    public static String [] split(String str, char delimiter){
        // STUDENT return null;
        // BEGIN STRIP
        int cnt = 1;
        for (int i=0; i<str.length(); i++){
            if (str.charAt(i) == delimiter) cnt++;
        }
        String[] l = new String[cnt];

        int current = 0;
        int j = 0;
        String subs = "";
        while (current<str.length()){
            if (str.charAt(current) == delimiter){
                l[j]=subs;
                subs = "";
                j++;
            }
            else {
                subs += str.charAt(current);
            }
            current ++;
        }
        l[j] = subs;
        return l;
        // END STRIP
    }


    /**
     * Find the first occurrence of a substring in a string
     *
     * @param str The string to look in
     * @param sub The string to look for
     * @return The index of the start of the first appearance of
     *          the substring in str or -1 if sub does not appear
     *          in str
     */
    public static int indexOf(String str, String sub){
        // STUDENT return 0;
        // BEGIN STRIP
        int l = sub.length();
        for (int i=0; i<str.length()-l+1; i++){
            boolean same = true;
            for (int j=0; j<l; j++){
                if (str.charAt(i+j) != sub.charAt(j)) same = false;
            }
            if (same){
                return i;
            }
        }
        return -1;
        // END STRIP
    }


    /**
     * Convert a string to lowercase
     *
     * @param str The string to convert
     * @return A new string, same as str but with every
     *          character put to lower case.
     */
    public static String toLowerCase(String str){
        // STUDENT return null;
        // BEGIN STRIP
        String low = "";
        for(int i=0; i<str.length(); i++){
            low += Character.toLowerCase(str.charAt(i));
        }
        return low;
        // END STRIP
    }


    /**
     * Check if a string is a palindrome
     *
     * A palindrome is a sequence of character that is the
     * same when read from left to right and from right to
     * left.
     *
     * @param str The string to check
     * @return true if str is a palindrome, false otherwise
     */
    public static boolean palindrome(String str){
        // STUDENT return false;
        // BEGIN STRIP
        int i = 0;
        int j = str.length()-1;
        boolean same = true;
        while (i<j && same){
            if (str.charAt(i) != str.charAt(j)) same = false;
            i++;
            j--;
        }
        return same;
        // END STRIP
    }


}