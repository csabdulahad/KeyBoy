package keyboy.musimath;

public class MusicMath {

    /*
     * this method assumed that char a, b are in lowercase latter and in the valid range.
     * otherwise it will stack forever. it can tell how much step it is needed to get from
     * one char point to another within a loop of a to g of music notes.
     * */
    public static int diff(char a, char b) {
        int diff = 0;
        while (a != b) {
            a++;
            diff++;
            if (a > 'g') a = 'a';
            if (a < 'a') a = 'g';
        }
        return diff;
    }

    /*
     * like previous method in this class, this method assumes that latter in valid range. this
     * method moves along a to g from one char point as per give step.
     * */
    public static char charMath(int latter, int step) {
        int inc = (step < 0) ? -1 : 1;

        int start = 0, end = Math.abs(step);
        while (start != end) {
            latter += inc;
            if (latter > 103) latter = 97;
            if (latter < 97) latter = 103;
            start++;
        }

        return (char) latter;
    }

}
