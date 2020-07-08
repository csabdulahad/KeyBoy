package keyboy.data;

import java.util.HashMap;

public class Note {

    private static final HashMap<String, Integer> noteIndex;

    static {

        noteIndex = new HashMap<>();

        noteIndex.put("A", 1);
        noteIndex.put("Bb", 2);
        noteIndex.put("A#", 2);

        noteIndex.put("B", 3);

        noteIndex.put("C", 4);
        noteIndex.put("C#", 5);
        noteIndex.put("Db", 5);

        noteIndex.put("D", 6);
        noteIndex.put("D#", 7);
        noteIndex.put("Eb", 7);

        noteIndex.put("E", 8);

        noteIndex.put("F", 9);
        noteIndex.put("F#", 10);
        noteIndex.put("Gb", 10);

        noteIndex.put("G", 11);
        noteIndex.put("G#", 12);
        noteIndex.put("Ab", 12);
    }

    public static int getIndex(String note) {
        return noteIndex.get(note);
    }

    public static String getNote(int index, boolean flat) {
        switch (index) {

            case 1:
                return "A";

            case 2:
                return flatOrSharp("A#", "Bb", flat);

            case 3:
                return "B";

            case 4:
                return "C";

            case 5:
                return flatOrSharp("C#", "Db", flat);

            case 6:
                return "D";

            case 7:
                return flatOrSharp("D#", "Eb", flat);

            case 8:
                return "E";

            case 9:
                return "F";

            case 10:
                return flatOrSharp("F#", "Gb", flat);

            case 11:
                return "G";

            default:
                return flatOrSharp("G#", "Ab", flat);
        }
    }

    private static String flatOrSharp(String s, String b, boolean flat) {
        return flat ? b : s;
    }

}
