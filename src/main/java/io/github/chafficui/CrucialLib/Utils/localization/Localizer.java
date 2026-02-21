package io.github.chafficui.CrucialLib.Utils.localization;

import java.util.HashMap;

/**
 * Global localization resolver. Keys use the format {@code "identifier_key"} where
 * the part before the first underscore selects the {@link Localized} source and the
 * remainder is passed to that source as the lookup key. Supports {@code {0}},
 * {@code {1}}, ... placeholder substitution with the supplied values.
 *
 * @see Localized
 * @see LocalizedFromYaml
 */
public class Localizer {
    public static HashMap<String, Localized> localizedElements = new HashMap<>();

    /**
     * Resolves a localized string by splitting the composite key, looking up the
     * appropriate {@link Localized} source, and substituting any placeholder values.
     *
     * @param key    the composite localization key in {@code "identifier_key"} format,
     *               where the part before the first underscore identifies the
     *               {@link Localized} source and the rest is the translation key
     * @param values the replacement values for {@code {0}}, {@code {1}}, etc.
     *               placeholders in the translated string
     * @return the fully resolved and substituted localized string, or an empty
     *         string if the identifier or key is not found
     */
    public static String getLocalizedString(String key, String... values) {
        String[] splitKey = key.split("_");
        Localized localized = localizedElements.get(splitKey[0]);
        if (localized == null) {
            return "";
        }
        //set all splitKey together to a string except the first one
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < splitKey.length; i++) {
            sb.append(splitKey[i]).append("_");
        }
        String localizedString = localized.getLocalizedString(sb.substring(0, sb.length() - 1));
        for (int i = 0; i < values.length; i++) {
            localizedString = localizedString.replace("{" + i + "}", values[i]);
        }
        return localizedString == null ? "" : localizedString;
    }
}
