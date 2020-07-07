package keyboy.helper;

import java.util.prefs.Preferences;

import static keyboy.KeyBoy.PREF_ROOT_NODE;


public class PrefHelper {

    private static Preferences getPref() {
        return Preferences.userRoot().node(PREF_ROOT_NODE);
    }

    public static Boolean isPrefSet(String key) {
        return getPref().get(key, null) != null;
    }

    public static String getString(String key, String defaultValue) {
        return getPref().get(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        return Integer.parseInt(getPref().get(key, String.valueOf(defaultValue)));
    }

    public static Boolean getBoolean(String key, Boolean defaultValue) {
        return getPref().getBoolean(key, defaultValue);
    }

    public static void putBoolean(String key, Boolean value) {
        getPref().putBoolean(key, value);
    }

    public static void putString(String key, String value) {
        getPref().put(key, value);
    }

    public static void putString(String key, int value) {
        getPref().put(key, String.valueOf(value));
    }
}
