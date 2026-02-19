package io.github.chafficui.CrucialLib.Utils.localization;

public abstract class Localized {
    public abstract String getLocalizedString(String key);

    public Localized(String identifier) {
        Localizer.localizedElements.put(identifier, this);
    }
}
