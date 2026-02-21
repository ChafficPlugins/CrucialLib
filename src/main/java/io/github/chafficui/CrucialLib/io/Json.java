package io.github.chafficui.CrucialLib.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Utility class for JSON file I/O using Gson with pretty printing.
 * Provides methods to save and load JSON strings from files, as well as
 * serialize and deserialize Java objects to and from JSON.
 */
public class Json {
    static Gson g = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Writes a JSON string to a file, overwriting any existing content.
     *
     * @param json     the JSON string to write
     * @param filename the path of the file to write to
     * @throws IOException if the file cannot be written
     */
    public static void saveFile(String json, String filename) throws IOException {
        FileWriter file = new FileWriter(filename);
        file.write(json);
        file.flush();
    }

    /**
     * Reads the entire contents of a file and returns it as a string.
     *
     * @param filename the path of the file to read
     * @return the full content of the file as a string
     * @throws IOException if the file cannot be read
     */
    public static String loadFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }
        String everything = sb.toString();
        br.close();
        return everything;
    }

    /**
     * Serializes an object to its JSON representation using pretty printing.
     *
     * @param object the object to serialize
     * @return the pretty-printed JSON string representing the object
     */
    public static String toJson(Object object){
        return g.toJson(object);
    }

    /**
     * Loads a JSON file and deserializes its content into an object of the
     * specified class.
     *
     * @param <T>          the type of the desired object
     * @param jsonFilePath the path of the JSON file to load
     * @param objectType   the class of the object to deserialize into
     * @return the deserialized object
     * @throws IOException if the file cannot be read
     */
    public static <T> T fromJson(String jsonFilePath, Class<T> objectType) throws IOException {
        return g.fromJson(loadFile(jsonFilePath), objectType);
    }

    /**
     * Loads a JSON file and deserializes its content into an object of the
     * specified type. This overload supports generic types via {@link Type}.
     *
     * @param <T>          the type of the desired object
     * @param jsonFilePath the path of the JSON file to load
     * @param type         the type of the object to deserialize into (supports generics)
     * @return the deserialized object
     * @throws IOException if the file cannot be read
     */
    public static <T> T fromJson(String jsonFilePath, Type type) throws IOException {
        return g.fromJson(loadFile(jsonFilePath), type);
    }
}