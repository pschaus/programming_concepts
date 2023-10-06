package basics;
import java.util.ArrayList;

public class ASCIIDecoder {

    /*
     * The 2D array "sentences" contain a set of decimal ASCII code we want you
     * to translate. Each sub-element of this array is a different sentence.
     * Ex : if we pass this array : [ ["72", "101", "108", "108", "111"], ["87", "111", "114", "108", "100"]]
     * to your decode method, you should return : [ "Hello", "World" ].
     * 
     * Forbidden characters are passed as an array of int.
     * Each element of this array correspond to the decimal ASCII code
     * of a forbidden character OR null if there's no forbidden character
     * If you encounter one of these forbidden character
     * you must ignore it when you translate your sentence.
     *
     * Use the StringBuilder class and its method appendCodePoint(int) to translate the ASCII code.
     *
     * You should NEVER return null or an array containing null.
     */
    public static String [] decode(int[] forbidden, String[][] sentences){
        // STUDENT return null;
        // BEGIN STRIP
        ArrayList<Integer> f = new ArrayList<>();
        if (forbidden != null){
            for (int fb : forbidden){
                f.add(fb);
            }
        }

        String[] decoded = new String[sentences.length];
        int i = 0;
        for (String[] s: sentences){
            StringBuilder sb = new StringBuilder();
            for (String w : s){
                if (!f.contains(Integer.parseInt(w))){
                    sb.appendCodePoint(Integer.parseInt(w));
                }
            }
            decoded[i] = sb.toString();
            i++;
        }
        return decoded;
        // END STRIP
    }

}