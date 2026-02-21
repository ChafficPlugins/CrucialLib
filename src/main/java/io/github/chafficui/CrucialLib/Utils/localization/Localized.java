package io.github.chafficui.CrucialLib.Utils.localization;

/**
 * Abstract base for a localization source. Subclasses provide translated strings
 * by key. Each {@code Localized} instance registers itself under an identifier
 * in the {@link Localizer}, so that localized strings can later be resolved
 * through the global {@link Localizer#getLocalizedString(String, String...)} method.
 *
 * @see Localizer
 */
public abstract class Localized {

    /**
     * Returns the translated string associated with the given key.
     *
     * @param key the localization key to look up
     * @return the translated string for the key, or an empty/fallback string
     *         if no translation is found
     */
    public abstract String getLocalizedString(String key);

    /**
     * Creates a new {@code Localized} source and registers it in the
     * {@link Localizer} under the specified identifier.
     *
     * @param identifier the unique identifier used to reference this localization
     *                   source in composite keys (the part before the first
     *                   underscore in {@code "identifier_key"} format)
     */
    public Localized(String identifier) {
        Localizer.localizedElements.put(identifier, this);
    }
}
