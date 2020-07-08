package keyboy.musimath;

import keyboy.data.Note;

public class MusicMath {

    /*
     * this method finds the step difference between two notes(index defined in Note class). for
     * forward step it returns negative value as difference, positive otherwise.
     * */
    public static int diff(String noteA, String noteB) {
        int indexA = Note.getIndex(noteA);
        int indexB = Note.getIndex(noteB);
        if (indexA == indexB) return 0;

        int stepDiff = indexA - indexB;

        if (indexA > indexB) return -stepDiff;
        else return Math.abs(stepDiff);
    }

    /*
     * this method can move the notes(defined by index as in Note class) for given number of
     * step and return the index of the note.
     * */
    public static int moveOnFretBoard(int from, int numOfStep) {
        int stepInc = numOfStep < 0 ? -1 : 1;
        int stepCount = 0;
        int end = Math.abs(numOfStep);

        while (stepCount != end) {
            from += stepInc;
            stepCount++;
            if (from > 12) from = 1;
            if (from < 1) from = 12;
        }
        return from;
    }

}
