package io.github.chafficui.CrucialLib.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonTest {

    @TempDir
    Path tempDir;

    @Test
    void toJsonConvertsObjectToJson() {
        String json = Json.toJson(new TestObject("hello", 42));
        assertNotNull(json);
        assertTrue(json.contains("hello"));
        assertTrue(json.contains("42"));
    }

    @Test
    void saveAndLoadFileRoundTrip() throws IOException {
        String filename = tempDir.resolve("test.json").toString();
        String content = "{\"name\":\"test\",\"value\":123}";
        Json.saveFile(content, filename);

        String loaded = Json.loadFile(filename);
        assertNotNull(loaded);
        assertTrue(loaded.contains("test"));
        assertTrue(loaded.contains("123"));
    }

    @Test
    void fromJsonDeserializesObject() throws IOException {
        String filename = tempDir.resolve("obj.json").toString();
        TestObject original = new TestObject("world", 99);
        Json.saveFile(Json.toJson(original), filename);

        TestObject deserialized = Json.fromJson(filename, TestObject.class);
        assertEquals("world", deserialized.name);
        assertEquals(99, deserialized.value);
    }

    @Test
    void loadFileThrowsOnMissingFile() {
        assertThrows(IOException.class, () -> Json.loadFile("/nonexistent/file.json"));
    }

    @Test
    void toJsonHandlesNull() {
        assertEquals("null", Json.toJson(null));
    }

    @Test
    void toJsonHandlesList() {
        String json = Json.toJson(List.of("a", "b", "c"));
        assertNotNull(json);
        assertTrue(json.contains("a"));
        assertTrue(json.contains("b"));
        assertTrue(json.contains("c"));
    }

    static class TestObject {
        String name;
        int value;

        TestObject() {}

        TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }
}
