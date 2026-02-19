package io.github.chafficui.CrucialLib.Utils.localization;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocalizerTest {

    @BeforeEach
    void setUp() {
        Localizer.localizedElements.clear();
    }

    @AfterEach
    void tearDown() {
        Localizer.localizedElements.clear();
    }

    @Test
    void getLocalizedStringReturnsEmptyForUnknownIdentifier() {
        String result = Localizer.getLocalizedString("unknown_key");
        assertEquals("", result);
    }

    @Test
    void getLocalizedStringResolvesFromRegisteredLocalized() {
        Localizer.localizedElements.put("lang", new Localized("lang") {
            @Override
            public String getLocalizedString(String key) {
                if ("greeting".equals(key)) return "Hello";
                return "";
            }
        });

        String result = Localizer.getLocalizedString("lang_greeting");
        assertEquals("Hello", result);
    }

    @Test
    void getLocalizedStringReplacesPlaceholders() {
        Localizer.localizedElements.put("msg", new Localized("msg") {
            @Override
            public String getLocalizedString(String key) {
                if ("welcome".equals(key)) return "Hello {0}, you have {1} items!";
                return "";
            }
        });

        String result = Localizer.getLocalizedString("msg_welcome", "Alice", "5");
        assertEquals("Hello Alice, you have 5 items!", result);
    }

    @Test
    void getLocalizedStringWithCompoundKey() {
        Localizer.localizedElements.put("ui", new Localized("ui") {
            @Override
            public String getLocalizedString(String key) {
                if ("button_click".equals(key)) return "Click me";
                return "";
            }
        });

        String result = Localizer.getLocalizedString("ui_button_click");
        assertEquals("Click me", result);
    }

    @Test
    void localizedConstructorRegistersItself() {
        Localized loc = new Localized("test") {
            @Override
            public String getLocalizedString(String key) {
                return "value";
            }
        };

        assertSame(loc, Localizer.localizedElements.get("test"));
    }
}
