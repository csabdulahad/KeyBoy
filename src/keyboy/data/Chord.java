package keyboy.data;

import java.util.ArrayList;

public class Chord {

    public static final ArrayList<String> CHORD_MAJOR;
    public static final ArrayList<String> CHORD_MINOR;

    static {
        CHORD_MAJOR = getChordList(true);
        CHORD_MINOR = getChordList(false);
    }

    private static ArrayList<String> getChordList(boolean major) {
        ArrayList<String> list = new ArrayList<>();

        String a = major ? "A" : "Am";
        list.add(a);

        String aShp = major ? "A#" : "A#m";
        list.add(aShp);

        String b = major ? "B" : "Bm";
        list.add(b);

        String c = major ? "C" : "Cm";
        list.add(c);

        String cShp = major ? "C#" : "C#m";
        list.add(cShp);

        String d = major ? "D" : "Dm";
        list.add(d);

        String dShp = major ? "D#" : "D#m";
        list.add(dShp);

        String e = major ? "E" : "Em";
        list.add(e);

        String f = major ? "F" : "Fm";
        list.add(f);

        String fShp = major ? "F#" : "F#m";
        list.add(fShp);

        String g = major ? "G" : "Gm";
        list.add(g);

        String gShp = major ? "G#" : "G#m";
        list.add(gShp);

        return list;
    }

}
