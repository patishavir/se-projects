package oz.infra.preferences;

import java.util.prefs.Preferences;

public class PreferencesUtils {
	public static String get(final PreferencesNodeType nodeType, final Class kclass,
			final String key) {
		return getPreferences(nodeType, kclass).get(key, null);
	}

	public static String get(final Class kclass, final String key) {
		return getPreferences(PreferencesNodeType.USER, kclass).get(key, null);
	}

	public static void put(final PreferencesNodeType nodeType, final Class kclass,
			final String key, final String value) {
		getPreferences(nodeType, kclass).put(key, value);
	}

	public static void put(final Class kclass, final String key, final String value) {
		getPreferences(PreferencesNodeType.USER, kclass).put(key, value);
	}

	public static void putOrRemove(final PreferencesNodeType nodeType, final Class kclass,
			final String key, final String value) {
		Preferences preferences = getPreferences(nodeType, kclass);
		if (value != null && value.length() > 0) {
			preferences.put(key, value);
		} else {
			preferences.remove(key);
		}
	}

	public static void putOrRemove(final Class kclass, final String key, final String value) {
		Preferences preferences = getPreferences(PreferencesNodeType.USER, kclass);
		if (value != null && value.length() > 0) {
			preferences.put(key, value);
		} else {
			preferences.remove(key);
		}
	}

	public static void remove(final PreferencesNodeType nodeType, final Class kclass,
			final String key) {
		getPreferences(nodeType, kclass).remove(key);
	}

	public static void remove(final Class kclass, final String key) {
		getPreferences(PreferencesNodeType.USER, kclass).remove(key);
	}

	private static Preferences getPreferences(final PreferencesNodeType nodeType, final Class kclass) {
		Preferences preferences = null;
		switch (nodeType) {
		case SYSTEM: {
			preferences = Preferences.systemNodeForPackage(kclass);
			break;
		}
		case USER:
			preferences = Preferences.userNodeForPackage(kclass);
			break;
		}
		return preferences;
	}
}
